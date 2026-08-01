#!/usr/bin/env python3
"""Fail-closed validation for Manual Release protected-ref authority.

The workflow downloads complete branch/tag ruleset details with the owner PAT
and passes them to this checker. The checker independently lists and downloads
push-target rulesets because those rules are repository-wide and have no branch
or tag ref-name targeting.

A release is authorised only when:

* every active branch ruleset applying to ``dev`` grants the owner user an
  ``always`` bypass;
* for a new release, every active tag ruleset applying to the exact release tag
  grants the same bypass and at least one applicable tag ruleset restricts
  creation;
* every active push-target ruleset grants the same bypass; and
* classic branch protection enforced for administrators cannot reject the
  release metadata fast-forward.
"""

from __future__ import annotations

import argparse
import fnmatch
import json
import os
import subprocess
import sys
import tempfile
from functools import lru_cache
from pathlib import Path
from typing import Any, Iterable


class AuthorityError(RuntimeError):
    """Expected protected-ref authority validation failure."""


def _read_json(path: Path) -> Any:
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except FileNotFoundError as exc:
        raise AuthorityError(f"missing JSON input: {path}") from exc
    except json.JSONDecodeError as exc:
        raise AuthorityError(
            f"invalid JSON in {path} at line {exc.lineno}, column {exc.colno}"
        ) from exc


def _is_active(ruleset: dict[str, Any]) -> bool:
    # GitHub currently returns `active`. Retaining `enabled` keeps old exported
    # fixtures readable without silently accepting any other unknown state.
    return str(ruleset.get("enforcement", "")).lower() in {"active", "enabled"}


def _github_pathname_match(value: str, pattern: str) -> bool:
    """Match GitHub ref patterns using pathname-aware wildcard semantics.

    GitHub rulesets use ``File::FNM_PATHNAME`` semantics: ``*``, ``?`` and a
    character class do not cross ``/``. A complete ``**`` path segment may
    consume zero or more slash-delimited components. Matching segment-by-
    segment gives the required behaviour while still using Python's mature
    wildcard handling inside each component.
    """

    value_parts = tuple(value.split("/"))
    pattern_parts = tuple(pattern.split("/"))

    @lru_cache(maxsize=None)
    def match(value_index: int, pattern_index: int) -> bool:
        if pattern_index == len(pattern_parts):
            return value_index == len(value_parts)

        current_pattern = pattern_parts[pattern_index]
        if current_pattern == "**":
            # Zero components, or consume one component and remain on **.
            return match(value_index, pattern_index + 1) or (
                value_index < len(value_parts)
                and match(value_index + 1, pattern_index)
            )

        if value_index == len(value_parts):
            return False
        if not fnmatch.fnmatchcase(value_parts[value_index], current_pattern):
            return False
        return match(value_index + 1, pattern_index + 1)

    return match(0, 0)


def _pattern_matches(
    pattern: str,
    *,
    full_ref: str,
    target: str,
    default_branch: str,
) -> bool:
    if pattern == "~ALL":
        return True
    if pattern == "~DEFAULT_BRANCH":
        return target == "branch" and full_ref == f"refs/heads/{default_branch}"

    prefix = "refs/heads/" if target == "branch" else "refs/tags/"
    short_ref = full_ref.removeprefix(prefix)

    if pattern.startswith("refs/"):
        return _github_pathname_match(full_ref, pattern)

    # Support both API-form full refs and UI/export forms that omit the prefix.
    return _github_pathname_match(short_ref, pattern) or _github_pathname_match(
        full_ref, f"{prefix}{pattern}"
    )


def _ruleset_applies(
    ruleset: dict[str, Any],
    *,
    target: str,
    full_ref: str,
    default_branch: str,
) -> bool:
    if str(ruleset.get("target", "")).lower() != target or not _is_active(ruleset):
        return False

    conditions = ruleset.get("conditions")
    if not isinstance(conditions, dict):
        raise AuthorityError(
            f"ruleset {ruleset.get('id', '?')} has no readable conditions object"
        )
    ref_name = conditions.get("ref_name")
    if not isinstance(ref_name, dict):
        raise AuthorityError(
            f"ruleset {ruleset.get('id', '?')} has no readable ref_name condition"
        )

    include = ref_name.get("include")
    exclude = ref_name.get("exclude", [])
    if not isinstance(include, list) or not include:
        raise AuthorityError(
            f"ruleset {ruleset.get('id', '?')} has no non-empty ref include list"
        )
    if not isinstance(exclude, list):
        raise AuthorityError(
            f"ruleset {ruleset.get('id', '?')} has a malformed ref exclude list"
        )

    def matches(pattern: object) -> bool:
        return isinstance(pattern, str) and _pattern_matches(
            pattern,
            full_ref=full_ref,
            target=target,
            default_branch=default_branch,
        )

    if any(matches(pattern) for pattern in exclude):
        return False
    return any(matches(pattern) for pattern in include)


def _has_owner_always_bypass(ruleset: dict[str, Any], actor_id: int) -> bool:
    actors = ruleset.get("bypass_actors")
    if not isinstance(actors, list):
        return False

    for actor in actors:
        if not isinstance(actor, dict):
            continue
        try:
            current_id = int(actor.get("actor_id"))
        except (TypeError, ValueError):
            continue
        if (
            current_id == actor_id
            and str(actor.get("actor_type", "")).lower() == "user"
            and str(actor.get("bypass_mode", "")).lower() == "always"
        ):
            return True
    return False


def _describe(ruleset: dict[str, Any]) -> str:
    return f"{ruleset.get('name', '<unnamed>')} (id={ruleset.get('id', '?')})"


def _verify_ref(
    rulesets: Iterable[dict[str, Any]],
    *,
    target: str,
    full_ref: str,
    default_branch: str,
    actor_id: int,
) -> list[dict[str, Any]]:
    applicable = [
        ruleset
        for ruleset in rulesets
        if _ruleset_applies(
            ruleset,
            target=target,
            full_ref=full_ref,
            default_branch=default_branch,
        )
    ]
    if not applicable:
        raise AuthorityError(f"no active {target} ruleset applies to {full_ref}")

    missing = [
        ruleset
        for ruleset in applicable
        if not _has_owner_always_bypass(ruleset, actor_id)
    ]
    if missing:
        names = ", ".join(_describe(ruleset) for ruleset in missing)
        raise AuthorityError(
            f"owner user id {actor_id} lacks User/always bypass on rulesets "
            f"applying to {full_ref}: {names}"
        )
    return applicable


def _verify_push_rulesets(
    rulesets: Iterable[dict[str, Any]], *, actor_id: int
) -> list[dict[str, Any]]:
    # Push rulesets have repository/fork-network scope rather than a ref-name
    # condition, so every active one can govern both protected ref mutations.
    applicable = [
        ruleset
        for ruleset in rulesets
        if str(ruleset.get("target", "")).lower() == "push" and _is_active(ruleset)
    ]
    missing = [
        ruleset
        for ruleset in applicable
        if not _has_owner_always_bypass(ruleset, actor_id)
    ]
    if missing:
        names = ", ".join(_describe(ruleset) for ruleset in missing)
        raise AuthorityError(
            f"owner user id {actor_id} lacks User/always bypass on active "
            f"repository-wide push rulesets: {names}"
        )
    return applicable


def _run_gh_api(args: list[str], *, token: str, description: str) -> str:
    env = os.environ.copy()
    env["GH_TOKEN"] = token
    try:
        completed = subprocess.run(
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
        raise AuthorityError("gh CLI is unavailable during push-ruleset validation") from exc
    except subprocess.TimeoutExpired as exc:
        raise AuthorityError(f"timed out while {description}") from exc

    if completed.returncode != 0:
        detail = completed.stderr.strip() or f"exit status {completed.returncode}"
        raise AuthorityError(f"failed while {description}: {detail}")
    return completed.stdout


def _fetch_live_push_rulesets() -> list[dict[str, Any]]:
    token = os.environ.get("RELEASE_PUSH_TOKEN", "")
    repository = os.environ.get("GITHUB_REPOSITORY", "")
    if not token:
        raise AuthorityError("RELEASE_PUSH_TOKEN is unavailable for push-ruleset inspection")
    if not repository or "/" not in repository:
        raise AuthorityError("GITHUB_REPOSITORY is unavailable for push-ruleset inspection")

    ids_output = _run_gh_api(
        [
            "--paginate",
            f"repos/{repository}/rulesets?per_page=100&includes_parents=true&targets=push",
            "--jq",
            ".[].id",
        ],
        token=token,
        description="listing active push-target rulesets",
    )

    ids: list[int] = []
    for line in ids_output.splitlines():
        value = line.strip()
        if not value:
            continue
        if not value.isdecimal():
            raise AuthorityError(f"push-ruleset API returned an invalid id: {value}")
        ids.append(int(value))

    rulesets: list[dict[str, Any]] = []
    for ruleset_id in sorted(set(ids)):
        detail_text = _run_gh_api(
            [f"repos/{repository}/rulesets/{ruleset_id}?includes_parents=true"],
            token=token,
            description=f"reading push ruleset {ruleset_id}",
        )
        try:
            detail = json.loads(detail_text)
        except json.JSONDecodeError as exc:
            raise AuthorityError(
                f"push ruleset {ruleset_id} returned invalid JSON at "
                f"line {exc.lineno}, column {exc.colno}"
            ) from exc
        if not isinstance(detail, dict):
            raise AuthorityError(f"push ruleset {ruleset_id} is not a JSON object")
        rulesets.append(detail)
    return rulesets


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
        allowed_users = {
            str(user.get("login", "")).lower()
            for user in restrictions.get("users", [])
            if isinstance(user, dict)
        }
        if actor_login.lower() not in allowed_users:
            blocking.append("push restrictions")

    if blocking:
        raise AuthorityError(
            "classic dev branch protection is enforced for administrators and "
            "can reject the release metadata fast-forward: " + ", ".join(blocking)
        )


def verify(args: argparse.Namespace) -> None:
    ruleset_paths = sorted(args.ruleset_dir.glob("*.json"))
    if not ruleset_paths:
        raise AuthorityError(f"no ruleset detail files found in {args.ruleset_dir}")

    downloaded: list[dict[str, Any]] = []
    for path in ruleset_paths:
        value = _read_json(path)
        if not isinstance(value, dict):
            raise AuthorityError(f"ruleset detail is not a JSON object: {path}")
        downloaded.append(value)

    push_rulesets = [] if args.skip_live_push_rulesets else _fetch_live_push_rulesets()
    by_id: dict[str, dict[str, Any]] = {}
    for ruleset in [*downloaded, *push_rulesets]:
        key = str(ruleset.get("id", f"anonymous-{len(by_id)}"))
        by_id[key] = ruleset
    rulesets = list(by_id.values())

    branch_rulesets = _verify_ref(
        rulesets,
        target="branch",
        full_ref=f"refs/heads/{args.default_branch}",
        default_branch=args.default_branch,
        actor_id=args.actor_id,
    )
    active_push_rulesets = _verify_push_rulesets(rulesets, actor_id=args.actor_id)

    tag_rulesets: list[dict[str, Any]] = []
    if args.mode == "create_new_release":
        if not args.release_tag:
            raise AuthorityError("create_new_release requires --release-tag")
        tag_ref = f"refs/tags/{args.release_tag}"
        tag_rulesets = _verify_ref(
            rulesets,
            target="tag",
            full_ref=tag_ref,
            default_branch=args.default_branch,
            actor_id=args.actor_id,
        )
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

    classic = _read_json(args.classic_protection_json)
    if not isinstance(classic, dict):
        raise AuthorityError("classic branch-protection input is not a JSON object")
    _verify_classic_protection(classic, actor_login=args.actor_login)

    print(
        "OK protected-ref authority: "
        f"branch_rulesets={len(branch_rulesets)} "
        f"tag_rulesets={len(tag_rulesets)} "
        f"push_rulesets={len(active_push_rulesets)} "
        f"actor={args.actor_login}({args.actor_id})"
    )


def _fixture_rulesets(actor_id: int) -> tuple[dict[str, Any], ...]:
    bypass = [
        {"actor_id": actor_id, "actor_type": "User", "bypass_mode": "always"}
    ]
    return (
        {
            "id": 1,
            "name": "dev protection",
            "target": "branch",
            "enforcement": "active",
            "bypass_actors": bypass,
            "conditions": {
                "ref_name": {"include": ["~DEFAULT_BRANCH"], "exclude": []}
            },
            "rules": [{"type": "pull_request"}],
        },
        {
            "id": 2,
            "name": "release tags",
            "target": "tag",
            "enforcement": "active",
            "bypass_actors": bypass,
            "conditions": {
                "ref_name": {"include": ["refs/tags/v*"], "exclude": []}
            },
            "rules": [{"type": "creation"}, {"type": "deletion"}],
        },
        {
            "id": 3,
            "name": "repository push protections",
            "target": "push",
            "enforcement": "active",
            "bypass_actors": bypass,
            "conditions": {},
            "rules": [
                {
                    "type": "file_path_restriction",
                    "parameters": {"restricted_file_paths": ["secrets/**"]},
                }
            ],
        },
    )


def self_test() -> None:
    actor_id = 150587541
    branch, tag, push = _fixture_rulesets(actor_id)

    # File::FNM_PATHNAME contract: ordinary wildcards never cross `/`, while a
    # complete `**` component may cross zero or more components.
    assert not _github_pathname_match("refs/tags/v6.4.9", "refs/*")
    assert _github_pathname_match("refs/tags/v6.4.9", "refs/**")
    assert _github_pathname_match("refs/tags/v6.4.9", "refs/tags/v*")
    assert not _github_pathname_match("release/a/b", "release/*")
    assert _github_pathname_match("release/a/b", "release/**/*")
    assert _github_pathname_match("release/a", "release/**/*")
    assert not _github_pathname_match("refs/tags/v6.4.9", "refs/tags/?")

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
    assert len(
        _verify_ref(
            [branch, tag, push],
            target="tag",
            full_ref="refs/tags/v6.4.9",
            default_branch="dev",
            actor_id=actor_id,
        )
    ) == 1
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

    try:
        _verify_classic_protection(
            {
                "enforce_admins": {"enabled": True},
                "required_status_checks": {"strict": True, "contexts": ["quality"]},
            },
            actor_login="cbkii",
        )
    except AuthorityError:
        pass
    else:
        raise AssertionError("blocking classic protection was accepted")

    with tempfile.TemporaryDirectory() as directory:
        root = Path(directory)
        for ruleset in (branch, tag, push):
            (root / f"{ruleset['id']}.json").write_text(
                json.dumps(ruleset), encoding="utf-8"
            )
        classic = root / "classic.json"
        classic.write_text("{}\n", encoding="utf-8")
        verify(
            argparse.Namespace(
                ruleset_dir=root,
                actor_id=actor_id,
                actor_login="cbkii",
                default_branch="dev",
                mode="create_new_release",
                release_tag="v6.4.9",
                classic_protection_json=classic,
                skip_live_push_rulesets=True,
            )
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
    parser.add_argument("--classic-protection-json", type=Path)
    parser.add_argument(
        "--skip-live-push-rulesets",
        action="store_true",
        help=argparse.SUPPRESS,
    )
    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    if args.self_test:
        self_test()
        return 0

    required = {
        "--ruleset-dir": args.ruleset_dir,
        "--actor-id": args.actor_id,
        "--actor-login": args.actor_login,
        "--mode": args.mode,
        "--classic-protection-json": args.classic_protection_json,
    }
    missing = [name for name, value in required.items() if value is None]
    if missing:
        print(f"ERROR: missing required arguments: {', '.join(missing)}", file=sys.stderr)
        return 2

    try:
        verify(args)
    except AuthorityError as exc:
        print(f"STOP: {exc}", file=sys.stderr)
        return 3
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
