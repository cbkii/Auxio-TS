#!/usr/bin/env python3
from __future__ import annotations

import re
import subprocess
from pathlib import Path
from textwrap import dedent


def replace_once(text: str, old: str, new: str, label: str) -> str:
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{label}: expected exactly one match, found {count}")
    return text.replace(old, new, 1)


def replace_regex(text: str, pattern: str, replacement: str, label: str) -> str:
    updated, count = re.subn(pattern, replacement, text, count=1, flags=re.DOTALL)
    if count != 1:
        raise SystemExit(f"{label}: expected exactly one regex match, found {count}")
    return updated


def update(path: str, transform) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    updated = transform(text)
    if updated == text:
        raise SystemExit(f"{path}: transform produced no change")
    file.write_text(updated, encoding="utf-8")


def patch_workflow(text: str) -> str:
    text = replace_once(
        text,
        "    if: github.actor == github.repository_owner\n",
        "    if: github.actor == github.repository_owner && github.triggering_actor == github.repository_owner\n",
        "owner and rerun gate",
    )
    text = replace_once(
        text,
        '            echo "::error::Missing RELEASE_PUSH_TOKEN. Configure a fine-grained PAT owned by ${EXPECTED_PUSH_ACTOR} with Contents read/write before running a release."\n',
        '            echo "::error::Missing RELEASE_PUSH_TOKEN. Configure a fine-grained PAT owned by ${EXPECTED_PUSH_ACTOR} with Contents read/write and Administration read before running a release."\n',
        "token permission diagnostic",
    )

    old_permission = """          permission_file="${RUNNER_TEMP}/release-push-permission.txt"
          permission_error="${RUNNER_TEMP}/release-push-permission.error"
          if ! GH_TOKEN="${RELEASE_PUSH_TOKEN}" timeout --foreground 30s \\
            gh api "repos/${GITHUB_REPOSITORY}" --jq '.permissions.push // false' \\
              > "${permission_file}" 2> "${permission_error}"; then
            echo "::error::Unable to verify RELEASE_PUSH_TOKEN access to ${GITHUB_REPOSITORY}; the API request failed or timed out."
            while IFS= read -r line; do
              [[ -n "${line}" ]] && echo "::error::gh: ${line}"
            done < "${permission_error}"
            exit 1
          fi
          can_push="$(tr -d '\\r\\n' < "${permission_file}")"
          [[ "${can_push}" == true ]] || {
            echo "::error::RELEASE_PUSH_TOKEN does not report push access to ${GITHUB_REPOSITORY}."
            exit 1
          }
"""
    new_permission = """          permission_file="${RUNNER_TEMP}/release-push-permission.json"
          permission_error="${RUNNER_TEMP}/release-push-permission.error"
          if ! GH_TOKEN="${RELEASE_PUSH_TOKEN}" timeout --foreground 30s \\
            gh api "repos/${GITHUB_REPOSITORY}" \\
              > "${permission_file}" 2> "${permission_error}"; then
            echo "::error::Unable to verify RELEASE_PUSH_TOKEN access to ${GITHUB_REPOSITORY}; the API request failed or timed out."
            while IFS= read -r line; do
              [[ -n "${line}" ]] && echo "::error::gh: ${line}"
            done < "${permission_error}"
            exit 1
          fi
          can_push="$(jq -er '.permissions.push == true' "${permission_file}")" || {
            echo "::error::RELEASE_PUSH_TOKEN does not report push access to ${GITHUB_REPOSITORY}."
            exit 1
          }
          repository_owner_id="$(jq -er '.owner.id | select(type == "number" and . > 0)' "${permission_file}")" || {
            echo "::error::Repository metadata did not expose a valid owner id."
            exit 1
          }
          [[ "${can_push}" == true ]] || {
            echo "::error::RELEASE_PUSH_TOKEN does not report push access to ${GITHUB_REPOSITORY}."
            exit 1
          }
          [[ "${push_actor_id}" == "${repository_owner_id}" ]] || {
            echo "::error::RELEASE_PUSH_TOKEN user id ${push_actor_id} does not match repository owner id ${repository_owner_id}."
            exit 1
          }
"""
    text = replace_once(text, old_permission, new_permission, "repository authority check")

    text = replace_once(
        text,
        '          classic_json="${RUNNER_TEMP}/classic-dev-protection.json"\n'
        '          classic_error="${RUNNER_TEMP}/classic-dev-protection.error"\n',
        "",
        "classic workflow variables",
    )

    classic_block = """          if GH_TOKEN="${RELEASE_PUSH_TOKEN}" timeout --foreground 30s \\
            gh api "repos/${GITHUB_REPOSITORY}/branches/dev/protection" \\
              > "${classic_json}" 2> "${classic_error}"; then
            :
          elif grep -Eq 'HTTP 404|Branch not protected' "${classic_error}"; then
            printf '{}\\n' > "${classic_json}"
          else
            echo "::error::Unable to inspect classic dev branch protection; protected push authority cannot be proven."
            while IFS= read -r line; do
              [[ -n "${line}" ]] && echo "::error::gh: ${line}"
            done < "${classic_error}"
            exit 1
          fi

"""
    text = replace_once(text, classic_block, "", "classic protection workflow fetch")
    text = replace_once(
        text,
        '            --release-tag "${RELEASE_TAG}" \\\n'
        '            --classic-protection-json "${classic_json}"\n',
        '            --release-tag "${RELEASE_TAG}"\n',
        "classic protection checker argument",
    )
    text = replace_once(
        text,
        "          DISPATCHER: ${{ github.actor }}\n",
        "          DISPATCHER: ${{ github.actor }}\n"
        "          TRIGGERING_ACTOR: ${{ github.triggering_actor }}\n",
        "summary triggering actor env",
    )
    text = replace_once(
        text,
        '            echo "- Dispatcher: ${DISPATCHER}"\n',
        '            echo "- Original dispatcher: ${DISPATCHER}"\n'
        '            echo "- Current triggering actor: ${TRIGGERING_ACTOR}"\n',
        "summary triggering actor output",
    )
    return text


def patch_ruleset_checker(text: str) -> str:
    text = replace_regex(
        text,
        r'"""Fail-closed validation for Manual Release protected-ref authority\..*?"""\n',
        dedent("""\
        \"\"\"Fail-closed validation for Manual Release protected-ref authority.

        The workflow downloads complete branch/tag ruleset details with the owner PAT
        and passes them to this checker. The checker independently reads live
        push-target rulesets and classic ``dev`` branch protection with the same
        credential.

        A release is authorised only when:

        * every active branch ruleset applying to ``dev`` grants the owner user an
          ``always`` bypass;
        * for a new release, every active tag ruleset applying to the exact release tag
          grants the same bypass and at least one applicable tag ruleset restricts
          creation;
        * every active push-target ruleset grants the same bypass; and
        * classic branch protection enforced for administrators cannot reject the
          release metadata fast-forward.
        \"\"\"
        """),
        "module docstring",
    )

    new_runner = dedent("""\
    def _run_gh_api_result(
        args: list[str], *, token: str, description: str
    ) -> subprocess.CompletedProcess[str]:
        env = os.environ.copy()
        env["GH_TOKEN"] = token
        try:
            return subprocess.run(
                ["gh", "api", *args],
                check=False,
                capture_output=True,
                text=True,
                encoding="utf-8",
                errors="replace",
                timeout=30,
                env=env,
            )
        except FileNotFoundError as exc:
            raise AuthorityError("gh CLI is unavailable during authority validation") from exc
        except subprocess.TimeoutExpired as exc:
            raise AuthorityError(f"timed out while {description}") from exc


    def _run_gh_api(args: list[str], *, token: str, description: str) -> str:
        completed = _run_gh_api_result(args, token=token, description=description)
        if completed.returncode != 0:
            detail = completed.stderr.strip() or f"exit status {completed.returncode}"
            raise AuthorityError(f"failed while {description}: {detail}")
        return completed.stdout
    """)
    text = replace_regex(
        text,
        r'def _run_gh_api\(.*?\n\ndef _fetch_live_push_rulesets',
        new_runner + "\n\ndef _fetch_live_push_rulesets",
        "GitHub API runner",
    )

    classic_fetch = dedent("""\


    def _parse_json_object(text: str, *, description: str) -> dict[str, Any]:
        try:
            value = json.loads(text)
        except json.JSONDecodeError as exc:
            raise AuthorityError(
                f"{description} returned invalid JSON at line {exc.lineno}, "
                f"column {exc.colno}"
            ) from exc
        if not isinstance(value, dict):
            raise AuthorityError(f"{description} is not a JSON object")
        return value


    def _fetch_live_classic_protection(default_branch: str) -> dict[str, Any]:
        token = os.environ.get("RELEASE_PUSH_TOKEN", "")
        repository = os.environ.get("GITHUB_REPOSITORY", "")
        if not token:
            raise AuthorityError(
                "RELEASE_PUSH_TOKEN is unavailable for classic-protection inspection"
            )
        if not repository or "/" not in repository:
            raise AuthorityError(
                "GITHUB_REPOSITORY is unavailable for classic-protection inspection"
            )

        endpoint = f"repos/{repository}/branches/{default_branch}/protection"
        completed = _run_gh_api_result(
            [endpoint],
            token=token,
            description=f"reading classic protection for {default_branch}",
        )
        if completed.returncode != 0:
            detail = completed.stderr.strip() or f"exit status {completed.returncode}"
            if "Branch not protected" in detail:
                return {}
            raise AuthorityError(
                "failed while reading classic branch protection; generic 404, "
                "authorization failures and inaccessible resources are not treated as "
                f"absence: {detail}"
            )

        protection = _parse_json_object(
            completed.stdout,
            description=f"classic protection for {default_branch}",
        )

        if not isinstance(protection.get("required_signatures"), dict):
            signature_endpoint = f"{endpoint}/required_signatures"
            signatures = _run_gh_api_result(
                [signature_endpoint],
                token=token,
                description=f"reading required signatures for {default_branch}",
            )
            if signatures.returncode == 0:
                protection["required_signatures"] = _parse_json_object(
                    signatures.stdout,
                    description=f"required signatures for {default_branch}",
                )
            else:
                detail = signatures.stderr.strip() or (
                    f"exit status {signatures.returncode}"
                )
                # The parent protection read already proved repository authority.
                # GitHub returns 404 when this optional protection is not enabled.
                if "HTTP 404" in detail or "Not Found" in detail:
                    protection["required_signatures"] = {"enabled": False}
                else:
                    raise AuthorityError(
                        f"failed while reading required signatures for "
                        f"{default_branch}: {detail}"
                    )

        return protection
    """)

    text = replace_regex(
        text,
        r'(def _fetch_live_push_rulesets\(\).*?\n    return rulesets\n)',
        r'\1' + classic_fetch,
        "live classic-protection fetch",
    )

    new_classic_verify = dedent("""\
    def _verify_classic_protection(
        protection: dict[str, Any], *, actor_login: str
    ) -> None:
        if not protection:
            return

        locked = protection.get("lock_branch")
        if isinstance(locked, dict) and locked.get("enabled") is True:
            raise AuthorityError("classic branch protection locks dev against updates")

        enforce_admins = protection.get("enforce_admins")
        admins_enforced = isinstance(enforce_admins, dict) and (
            enforce_admins.get("enabled") is True
        )
        if not admins_enforced:
            return

        blocking: list[str] = []
        if protection.get("required_pull_request_reviews") is not None:
            blocking.append("required pull request")
        if protection.get("required_status_checks") is not None:
            blocking.append("required status checks")
        signatures = protection.get("required_signatures")
        if isinstance(signatures, dict) and signatures.get("enabled") is True:
            blocking.append("required signed commits")

        restrictions = protection.get("restrictions")
        if isinstance(restrictions, dict):
            users = [
                user for user in restrictions.get("users", []) if isinstance(user, dict)
            ]
            teams = [
                team for team in restrictions.get("teams", []) if isinstance(team, dict)
            ]
            apps = [
                app for app in restrictions.get("apps", []) if isinstance(app, dict)
            ]
            allowed_users = {
                str(user.get("login", "")).lower() for user in users
            }
            if actor_login.lower() not in allowed_users:
                represented = []
                if teams:
                    represented.append("teams")
                if apps:
                    represented.append("GitHub Apps")
                suffix = (
                    f"; restrictions also name {', '.join(represented)}, but user "
                    "membership/identity cannot be inferred safely"
                    if represented
                    else ""
                )
                blocking.append(
                    f"push restrictions do not explicitly allow user {actor_login}{suffix}"
                )

        if blocking:
            raise AuthorityError(
                "classic dev branch protection is enforced for administrators and "
                "can reject the release metadata fast-forward: " + ", ".join(blocking)
            )
    """)
    text = replace_regex(
        text,
        r'def _verify_classic_protection\(.*?\n\ndef verify',
        new_classic_verify + "\n\ndef verify",
        "classic protection verifier",
    )

    creation_helper = dedent("""\


    def _require_tag_creation_rule(
        tag_rulesets: Iterable[dict[str, Any]], *, tag_ref: str
    ) -> None:
        has_creation_rule = any(
            any(
                isinstance(rule, dict) and rule.get("type") == "creation"
                for rule in ruleset.get("rules", [])
            )
            for ruleset in tag_rulesets
        )
        if not has_creation_rule:
            raise AuthorityError(
                f"no active ruleset applying to {tag_ref} contains the required "
                "creation restriction"
            )
    """)
    text = replace_once(
        text,
        "\ndef verify(args: argparse.Namespace) -> None:\n",
        creation_helper + "\n\ndef verify(args: argparse.Namespace) -> None:\n",
        "tag creation helper",
    )

    text = replace_once(
        text,
        '    push_rulesets = [] if args.skip_live_push_rulesets else _fetch_live_push_rulesets()\n',
        '    push_rulesets = _fetch_live_push_rulesets()\n',
        "unskippable live push rulesets",
    )

    text = replace_regex(
        text,
        r'        has_creation_rule = any\(.*?            \)\n\n    classic = _read_json\(args\.classic_protection_json\)\n    if not isinstance\(classic, dict\):\n        raise AuthorityError\("classic branch-protection input is not a JSON object"\)\n',
        '        _require_tag_creation_rule(tag_rulesets, tag_ref=tag_ref)\n\n'
        '    classic = _fetch_live_classic_protection(args.default_branch)\n',
        "tag creation and classic live verification",
    )

    new_self_test = dedent("""\
    def self_test() -> None:
        actor_id = 150587541
        branch, tag, push = _fixture_rulesets(actor_id)

        assert not _github_pathname_match("refs/tags/v6.4.9", "refs/*")
        assert _github_pathname_match("refs/tags/v6.4.9", "refs/**")
        assert _github_pathname_match("refs/tags/v6.4.9", "refs/tags/v*")
        assert not _github_pathname_match("release/a/b", "release/*")
        assert _github_pathname_match("release/a/b", "release/**/*")
        assert _github_pathname_match("release/a", "release/**/*")
        assert not _github_pathname_match("refs/tags/v6.4.9", "refs/tags/?")

        assert _pattern_matches(
            "v*",
            full_ref="refs/tags/v6.4.9",
            target="tag",
            default_branch="dev",
        )
        assert not _pattern_matches(
            "v*",
            full_ref="refs/tags/nightly",
            target="tag",
            default_branch="dev",
        )
        assert _pattern_matches(
            "~ALL",
            full_ref="refs/heads/dev",
            target="branch",
            default_branch="dev",
        )

        assert _ruleset_applies(
            branch,
            target="branch",
            full_ref="refs/heads/dev",
            default_branch="dev",
        )
        assert _ruleset_applies(
            tag,
            target="tag",
            full_ref="refs/tags/v6.4.9",
            default_branch="dev",
        )
        assert len(
            _verify_ref(
                [branch, tag, push],
                target="branch",
                full_ref="refs/heads/dev",
                default_branch="dev",
                actor_id=actor_id,
            )
        ) == 1
        verified_tags = _verify_ref(
            [branch, tag, push],
            target="tag",
            full_ref="refs/tags/v6.4.9",
            default_branch="dev",
            actor_id=actor_id,
        )
        assert len(verified_tags) == 1
        _require_tag_creation_rule(verified_tags, tag_ref="refs/tags/v6.4.9")
        assert len(_verify_push_rulesets([branch, tag, push], actor_id=actor_id)) == 1

        missing_branch = dict(branch)
        missing_branch["bypass_actors"] = []
        try:
            _verify_ref(
                [missing_branch],
                target="branch",
                full_ref="refs/heads/dev",
                default_branch="dev",
                actor_id=actor_id,
            )
        except AuthorityError:
            pass
        else:
            raise AssertionError("missing branch bypass was accepted")

        missing_push = dict(push)
        missing_push["bypass_actors"] = []
        try:
            _verify_push_rulesets([missing_push], actor_id=actor_id)
        except AuthorityError:
            pass
        else:
            raise AssertionError("missing push-ruleset bypass was accepted")

        missing_creation = dict(tag)
        missing_creation["rules"] = [{"type": "deletion"}]
        try:
            _require_tag_creation_rule(
                [missing_creation], tag_ref="refs/tags/v6.4.9"
            )
        except AuthorityError:
            pass
        else:
            raise AssertionError("tag ruleset without creation restriction was accepted")

        for protection in (
            {
                "enforce_admins": {"enabled": True},
                "required_status_checks": {
                    "strict": True,
                    "contexts": ["quality"],
                },
            },
            {
                "enforce_admins": {"enabled": True},
                "required_signatures": {"enabled": True},
            },
            {
                "enforce_admins": {"enabled": True},
                "restrictions": {
                    "users": [],
                    "teams": [{"slug": "release-team"}],
                    "apps": [{"slug": "release-app"}],
                },
            },
        ):
            try:
                _verify_classic_protection(protection, actor_login="cbkii")
            except AuthorityError:
                pass
            else:
                raise AssertionError("blocking classic protection was accepted")

        _verify_classic_protection(
            {
                "enforce_admins": {"enabled": True},
                "restrictions": {
                    "users": [{"login": "cbkii"}],
                    "teams": [],
                    "apps": [],
                },
            },
            actor_login="cbkii",
        )

        print("OK release ruleset authority self-test")


    def build_parser() -> argparse.ArgumentParser:
        parser = argparse.ArgumentParser()
        parser.add_argument("--self-test", action="store_true")
        parser.add_argument("--ruleset-dir", type=Path)
        parser.add_argument("--actor-id", type=int)
        parser.add_argument("--actor-login")
        parser.add_argument("--default-branch", default="dev")
        parser.add_argument(
            "--mode",
            choices=("create_new_release", "repair_existing_release"),
        )
        parser.add_argument("--release-tag", default="")
        return parser
    """)
    text = replace_regex(
        text,
        r'def self_test\(\) -> None:.*?\ndef main\(argv: list\[str\] \| None = None\) -> int:',
        new_self_test + "\n\ndef main(argv: list[str] | None = None) -> int:",
        "self-test and parser",
    )

    text = replace_once(
        text,
        '        "--mode": args.mode,\n'
        '        "--classic-protection-json": args.classic_protection_json,\n',
        '        "--mode": args.mode,\n',
        "required argument list",
    )
    return text


def patch_checker(text: str) -> str:
    text = replace_once(
        text,
        'bash -n "$0" "${bridge_checker}" "${app_checker}" "${signer_parser}" "${variant_checker}"\n',
        'for script in "$0" "${bridge_checker}" "${app_checker}" "${signer_parser}" "${variant_checker}"; do\n'
        '  bash -n "${script}" || fail "Shell syntax check failed for ${script}"\n'
        'done\n',
        "per-script bash syntax",
    )
    text = replace_once(
        text,
        "    'git push origin \":refs/tags/',\n",
        "    'git push origin \":refs/tags/',\n"
        "    '--skip-live-push-rulesets',\n"
        "    '--classic-protection-json',\n",
        "forbidden authority bypass switches",
    )
    text = replace_once(
        text,
        "    'if: github.actor == github.repository_owner',\n",
        "    'if: github.actor == github.repository_owner && github.triggering_actor == github.repository_owner',\n",
        "owner rerun gate contract",
    )
    text = replace_once(
        text,
        '    "gh api \\"repos/${GITHUB_REPOSITORY}\\" --jq \'.permissions.push // false\'",\n',
        '    "gh api \\"repos/${GITHUB_REPOSITORY}\\"",\n'
        "    'repository_owner_id=',\n",
        "repository permission contract",
    )
    text = replace_once(
        text,
        "    'branches/dev/protection',\n",
        "",
        "workflow classic endpoint contract",
    )
    text = replace_once(
        text,
        "if ruleset_step.count('timeout --foreground 30s') != 3:\n"
        "    raise SystemExit('Ruleset preflight must bound list, detail and classic API calls.')\n",
        "if ruleset_step.count('timeout --foreground 30s') != 2:\n"
        "    raise SystemExit('Ruleset preflight must bound list and detail API calls.')\n",
        "ruleset timeout contract",
    )
    text = replace_once(
        text,
        "    'classic_error=',\n",
        "",
        "classic shell error contract",
    )
    text = replace_once(
        text,
        "    'timeout=30',\n",
        "    'timeout=30',\n"
        "    '_fetch_live_classic_protection',\n"
        "    'required_signatures',\n",
        "classic Python contract",
    )
    text = replace_once(
        text,
        "    'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c',\n"
        "    'actions/upload-artifact@043fb46d1a93c77aae656e7c1c64a875d1fc6a0a',\n",
        "    'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c',\n"
        "    'android-actions/setup-android@40fd30fb8d7440372e1316f5d1809ec01dcd3699',\n"
        "    'actions/upload-artifact@043fb46d1a93c77aae656e7c1c64a875d1fc6a0a',\n",
        "setup-android immutable pin",
    )
    return text


def patch_docs(text: str) -> str:
    text = replace_once(
        text,
        "- repository permission: **Contents: Read and write**;\n",
        "- repository permissions:\n"
        "  - **Contents: Read and write** for the immutable tag and `dev` fast-forward;\n"
        "  - **Administration: Read** so the fail-closed preflight can inspect ruleset bypass actors and classic branch protection;\n",
        "PAT permissions",
    )

    new_audit = dedent("""\
    ### Read-only settings audit

    The following bounded, read-only audit fails closed on GitHub API errors, malformed
    JSON and incomplete pagination. It supports GNU `timeout` on Linux/Termux and
    `gtimeout` from Homebrew coreutils on macOS.

    ```bash
    #!/usr/bin/env bash
    set -uo pipefail

    repo='cbkii/Auxio-TS'

    for required_command in gh jq mktemp; do
      command -v "$required_command" >/dev/null 2>&1 || {
        printf 'STOP: required command is unavailable: %s\\n' "$required_command" >&2
        exit 1
      }
    done

    if command -v timeout >/dev/null 2>&1; then
      timeout_command=(timeout --foreground 30s)
    elif command -v gtimeout >/dev/null 2>&1; then
      timeout_command=(gtimeout 30s)
    else
      printf 'STOP: install GNU timeout (coreutils) before running this audit.\\n' >&2
      exit 1
    fi

    api_get() {
      local endpoint=$1
      local errors
      local output
      errors="$(mktemp)"
      if ! output="$("${timeout_command[@]}" gh api "$endpoint" 2>"$errors")"; then
        printf 'STOP: GitHub API request failed: %s\\n' "$endpoint" >&2
        cat "$errors" >&2
        rm -f "$errors"
        return 1
      fi
      rm -f "$errors"
      printf '%s\\n' "$output"
    }

    api_pages() {
      local endpoint=$1
      local errors
      local output
      errors="$(mktemp)"
      if ! output="$(
        "${timeout_command[@]}" gh api --paginate --slurp "$endpoint" 2>"$errors"
      )"; then
        printf 'STOP: paginated GitHub API request failed: %s\\n' "$endpoint" >&2
        cat "$errors" >&2
        rm -f "$errors"
        return 1
      fi
      rm -f "$errors"
      printf '%s\\n' "$output"
    }

    printf '\\n== Workflow permission ==\\n'
    workflow_permission="$(api_get "repos/${repo}/actions/permissions/workflow")" ||
      exit 1
    printf '%s\\n' "$workflow_permission" | jq -e . || {
      printf 'STOP: workflow-permission response is not valid JSON.\\n' >&2
      exit 1
    }

    printf '\\n== Actions secret names ==\\n'
    secret_pages="$(api_pages "repos/${repo}/actions/secrets?per_page=100")" ||
      exit 1
    printf '%s\\n' "$secret_pages" |
      jq -er '[.[].secrets[]?.name] | unique | .[]' |
      sort || {
        printf 'STOP: Actions-secret response is incomplete or invalid.\\n' >&2
        exit 1
      }

    printf '\\n== Rulesets ==\\n'
    ruleset_pages="$(api_pages "repos/${repo}/rulesets?per_page=100&includes_parents=true")" ||
      exit 1
    ruleset_rows="$(
      printf '%s\\n' "$ruleset_pages" |
        jq -er 'add | unique_by(.id) | .[] | [.id, .name, .target, .enforcement] | @tsv'
    )" || {
      printf 'STOP: ruleset index is incomplete or invalid.\\n' >&2
      exit 1
    }
    [[ -n "$ruleset_rows" ]] || {
      printf 'STOP: no repository rulesets were returned.\\n' >&2
      exit 1
    }
    printf '%s\\n' "$ruleset_rows"

    while IFS=$'\\t' read -r id name target enforcement; do
      [[ "$id" =~ ^[0-9]+$ ]] || {
        printf 'STOP: invalid ruleset id: %s\\n' "$id" >&2
        exit 1
      }
      printf '\\n--- ruleset %s: %s (%s, %s) ---\\n' \\
        "$id" "$name" "$target" "$enforcement"
      detail="$(api_get "repos/${repo}/rulesets/${id}?includes_parents=true")" ||
        exit 1
      printf '%s\\n' "$detail" | jq -e '{
        id,
        name,
        target,
        enforcement,
        bypass_actors,
        conditions,
        rules
      }' || {
        printf 'STOP: ruleset %s response is incomplete or invalid.\\n' "$id" >&2
        exit 1
      }
    done <<< "$ruleset_rows"

    printf '\\n== Classic dev protection, when present ==\\n'
    classic_errors="$(mktemp)"
    if classic="$(
      "${timeout_command[@]}" gh api \\
        "repos/${repo}/branches/dev/protection" 2>"$classic_errors"
    )"; then
      rm -f "$classic_errors"
      printf '%s\\n' "$classic" | jq -e . || {
        printf 'STOP: classic-protection response is not valid JSON.\\n' >&2
        exit 1
      }
    elif grep -Fq 'Branch not protected' "$classic_errors"; then
      rm -f "$classic_errors"
      printf 'No classic branch-protection rule applies to dev.\\n'
    else
      printf 'STOP: classic branch-protection inspection failed.\\n' >&2
      cat "$classic_errors" >&2
      rm -f "$classic_errors"
      exit 1
    fi
    ```

    **STOP:** do not dispatch another release until the audit shows
    `RELEASE_PUSH_TOKEN`, the token owner is on the bypass list for every applicable
    branch, tag and push ruleset, and overlapping classic protection has been
    reconciled.
    """)

    text = replace_regex(
        text,
        r'### Read-only settings audit\n.*?\n\*\*STOP:\*\*.*?\n\n## Required secrets',
        new_audit + "\n\n## Required secrets",
        "read-only audit section",
    )

    references = dedent("""\
    ## GitHub authority references

    The release authority model follows GitHub's official documentation:

    - workflow reruns retain the original actor's privileges, while
      `github.triggering_actor` identifies who initiated a rerun:
      <https://docs.github.com/en/actions/how-tos/manage-workflow-runs/re-run-workflows-and-jobs>
    - repository rulesets support `User` bypass actors and `always` bypass mode, and
      ruleset details only expose `bypass_actors` to callers with ruleset write
      authority:
      <https://docs.github.com/en/rest/repos/rules>
    - reading classic branch protection and required-signature protection requires
      repository **Administration: Read**:
      <https://docs.github.com/en/rest/branches/branch-protection>
    - `GITHUB_TOKEN` permissions are set per workflow; the repository default remains
      read-only:
      <https://docs.github.com/en/actions/security-for-github-actions/security-guides/automatic-token-authentication>

    """)
    text = replace_once(
        text,
        "## Required secrets\n",
        references + "## Required secrets\n",
        "official references",
    )
    return text


update(".github/workflows/manual-release.yml", patch_workflow)
update("scripts/check-release-ruleset-authority.py", patch_ruleset_checker)
update("scripts/check-manual-release-workflow.sh", patch_checker)
update("docs/RELEASE_WORKFLOW.md", patch_docs)

subprocess.run(
    ["python3", "-m", "py_compile", "scripts/check-release-ruleset-authority.py"],
    check=True,
)
for script in (
    "scripts/check-manual-release-workflow.sh",
    "scripts/check-lsposed-bridge-contracts.sh",
    "scripts/check-app-release-contracts.sh",
    "scripts/lib/apksigner-certificate.sh",
    "scripts/check-ci-variant-contracts.sh",
):
    subprocess.run(["bash", "-n", script], check=True)

subprocess.run(
    ["python3", "scripts/check-release-ruleset-authority.py", "--self-test"],
    check=True,
)
subprocess.run(["bash", "scripts/check-manual-release-workflow.sh"], check=True)
print("PR #220 finalization patch applied and validated")
