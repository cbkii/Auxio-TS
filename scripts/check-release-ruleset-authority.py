#!/usr/bin/env python3
"""Fail-closed validation for Manual Release protected-ref authority.

The workflow fetches complete active repository ruleset JSON with the owner PAT,
then this script verifies that every active ruleset applying to the protected
`dev` branch (and, for new releases, the exact release tag) grants the token
owner a User/always bypass. It also rejects blocking classic branch protection
that is explicitly enforced for administrators.
"""

from __future__ import annotations

import argparse
import fnmatch
import json
import sys
import tempfile
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
    # GitHub's current value is `active`; tolerate the historical response label
    # `enabled` so an API spelling change cannot silently skip protection.
    return str(ruleset.get("enforcement", "")).lower() in {"active", "enabled"}


def _candidate_patterns(pattern: str, target: str) -> tuple[str, ...]:
    if pattern.startswith("refs/"):
        return (pattern,)
    prefix = "refs/heads/" if target == "branch" else "refs/tags/"
    return (pattern, f"{prefix}{pattern}")


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

    short_ref = full_ref.removeprefix("refs/heads/").removeprefix("refs/tags/")
    for candidate_pattern in _candidate_patterns(pattern, target):
        if fnmatch.fnmatchcase(full_ref, candidate_pattern):
            return True
        if fnmatch.fnmatchcase(short_ref, candidate_pattern):
            return True
    return False


def _ruleset_applies(
    ruleset: dict[str, Any],
    *,
    target: str,
    full_ref: str,
    default_branch: str,
) -> bool:
    if str(ruleset.get("target", "")).lower() != target:
        return False
    if not _is_active(ruleset):
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

    if any(
        isinstance(pattern, str)
        and _pattern_matches(
            pattern,
            full_ref=full_ref,
            target=target,
            default_branch=default_branch,
        )
        for pattern in exclude
    ):
        return False

    return any(
        isinstance(pattern, str)
        and _pattern_matches(
            pattern,
            full_ref=full_ref,
            target=target,
            default_branch=default_branch,
        )
        for pattern in include
    )


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

    rulesets: list[dict[str, Any]] = []
    for path in ruleset_paths:
        value = _read_json(path)
        if not isinstance(value, dict):
            raise AuthorityError(f"ruleset detail is not a JSON object: {path}")
        rulesets.append(value)

    branch_ref = f"refs/heads/{args.default_branch}"
    branch_rulesets = _verify_ref(
        rulesets,
        target="branch",
        full_ref=branch_ref,
        default_branch=args.default_branch,
        actor_id=args.actor_id,
    )

    tag_rulesets: list[dict[str, Any]] = []
    if args.mode == "create_new_release":
        if not args.release_tag:
            raise AuthorityError("create_new_release requires --release-tag")
        tag_rulesets = _verify_ref(
            rulesets,
            target="tag",
            full_ref=f"refs/tags/{args.release_tag}",
            default_branch=args.default_branch,
            actor_id=args.actor_id,
        )
        if not any(
            any(
                isinstance(rule, dict) and rule.get("type") == "creation"
                for rule in ruleset.get("rules", [])
            )
            for ruleset in tag_rulesets
        ):
            raise AuthorityError(
                f"no active ruleset applying to refs/tags/{args.release_tag} "
                "contains the required creation restriction"
            )

    classic = _read_json(args.classic_protection_json)
    if not isinstance(classic, dict):
        raise AuthorityError("classic branch-protection input is not a JSON object")
    _verify_classic_protection(classic, actor_login=args.actor_login)

    print(
        "OK protected-ref authority: "
        f"branch_rulesets={len(branch_rulesets)} "
        f"tag_rulesets={len(tag_rulesets)} "
        f"actor={args.actor_login}({args.actor_id})"
    )


def self_test() -> None:
    actor_id = 150587541
    branch = {
        "id": 1,
        "name": "dev protection",
        "target": "branch",
        "enforcement": "active",
        "bypass_actors": [
            {"actor_id": actor_id, "actor_type": "User", "bypass_mode": "always"}
        ],
        "conditions": {"ref_name": {"include": ["~DEFAULT_BRANCH"], "exclude": []}},
        "rules": [{"type": "pull_request"}],
    }
    tag = {
        "id": 2,
        "name": "release tags",
        "target": "tag",
        "enforcement": "active",
        "bypass_actors": [
            {"actor_id": actor_id, "actor_type": "User", "bypass_mode": "always"}
        ],
        "conditions": {"ref_name": {"include": ["refs/tags/v*"], "exclude": []}},
        "rules": [{"type": "creation"}, {"type": "deletion"}],
    }

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
    _verify_ref(
        [branch, tag],
        target="branch",
        full_ref="refs/heads/dev",
        default_branch="dev",
        actor_id=actor_id,
    )
    _verify_ref(
        [branch, tag],
        target="tag",
        full_ref="refs/tags/v6.4.9",
        default_branch="dev",
        actor_id=actor_id,
    )

    missing = dict(branch)
    missing["bypass_actors"] = []
    try:
        _verify_ref(
            [missing],
            target="branch",
            full_ref="refs/heads/dev",
            default_branch="dev",
            actor_id=actor_id,
        )
    except AuthorityError:
        pass
    else:
        raise AssertionError("missing bypass was accepted")

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
        (root / "1.json").write_text(json.dumps(branch), encoding="utf-8")
        (root / "2.json").write_text(json.dumps(tag), encoding="utf-8")
        classic = root / "classic.json"
        classic.write_text("{}\n", encoding="utf-8")
        args = argparse.Namespace(
            ruleset_dir=root,
            actor_id=actor_id,
            actor_login="cbkii",
            default_branch="dev",
            mode="create_new_release",
            release_tag="v6.4.9",
            classic_protection_json=classic,
        )
        verify(args)

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
