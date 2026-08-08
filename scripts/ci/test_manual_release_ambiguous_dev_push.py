#!/usr/bin/env python3
"""Exercise ambiguous dev metadata push recovery against a local Git remote."""
from __future__ import annotations

import os
import shutil
import subprocess
import tempfile
from pathlib import Path

ROOT = Path.cwd()
SCRIPT = ROOT / "scripts/manual-release/19-synchronise-released-source-metadata-to-dev.sh"


def run(command: list[str], cwd: Path, env: dict[str, str] | None = None) -> str:
    result = subprocess.run(
        command,
        cwd=cwd,
        env=env,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        timeout=30,
        check=False,
    )
    if result.returncode != 0:
        raise RuntimeError(
            f"command failed: {' '.join(command)}\nstdout:\n{result.stdout}\nstderr:\n{result.stderr}"
        )
    return result.stdout.strip()


def main() -> int:
    real_git = shutil.which("git")
    if not real_git or not SCRIPT.is_file():
        print("FAILED: git or Manual Release sync script is unavailable")
        return 2

    try:
        with tempfile.TemporaryDirectory(prefix="auxio-release-dev-push-") as temp:
            root = Path(temp)
            bare = root / "origin.git"
            work = root / "work"
            fakebin = root / "fakebin"
            fakebin.mkdir()

            run([real_git, "init", "--bare", str(bare)], root)
            run([real_git, "init", str(work)], root)
            run([real_git, "switch", "-c", "dev"], work)
            run([real_git, "config", "user.name", "Release Smoke"], work)
            run([real_git, "config", "user.email", "release-smoke@example.invalid"], work)
            (work / "version.txt").write_text("base\n", encoding="utf-8")
            run([real_git, "add", "version.txt"], work)
            run([real_git, "commit", "-m", "base"], work)
            run([real_git, "remote", "add", "origin", str(bare)], work)
            run([real_git, "push", "-u", "origin", "HEAD:refs/heads/dev"], work)
            run([real_git, "symbolic-ref", "HEAD", "refs/heads/dev"], bare)

            (work / "version.txt").write_text("release\n", encoding="utf-8")
            run([real_git, "add", "version.txt"], work)
            run([real_git, "commit", "-m", "release metadata"], work)
            release_sha = run([real_git, "rev-parse", "HEAD"], work)

            marker = root / "failed-once"
            git_wrapper = fakebin / "git"
            git_wrapper.write_text(
                "#!/usr/bin/env bash\n"
                "set -u\n"
                f"real_git={real_git!r}\n"
                "if [[ \"${1:-}\" == push && ! -e \"${FAKE_GIT_MARKER}\" ]]; then\n"
                "  if \"$real_git\" \"$@\"; then\n"
                "    : > \"${FAKE_GIT_MARKER}\"\n"
                "    echo 'simulated transport failure after committed push' >&2\n"
                "    exit 1\n"
                "  fi\n"
                "fi\n"
                "exec \"$real_git\" \"$@\"\n",
                encoding="utf-8",
            )
            git_wrapper.chmod(0o755)
            gh = fakebin / "gh"
            gh.write_text("#!/usr/bin/env bash\n[[ \"$1 $2\" == 'auth setup-git' ]]\n", encoding="utf-8")
            gh.chmod(0o755)

            output = root / "output.txt"
            output.write_text("", encoding="utf-8")
            env = os.environ.copy()
            env.update(
                {
                    "PATH": f"{fakebin}:{env.get('PATH', '')}",
                    "FAKE_GIT_MARKER": str(marker),
                    "GITHUB_OUTPUT": str(output),
                    "RELEASE_SHA": release_sha,
                }
            )
            run(["bash", str(SCRIPT)], work, env)

            if not marker.is_file():
                raise RuntimeError("simulated push-failure wrapper did not intercept the dev push")

            remote_sha = run([real_git, "ls-remote", "origin", "refs/heads/dev"], work).split()[0]
            status = next(
                (line.split("=", 1)[1] for line in output.read_text(encoding="utf-8").splitlines() if line.startswith("status=")),
                "",
            )
            if remote_sha != release_sha or status != "fast_forwarded":
                raise RuntimeError(
                    f"ambiguous push postcondition mismatch: remote={remote_sha} status={status}"
                )
    except (OSError, RuntimeError, subprocess.SubprocessError) as error:
        print(f"FAILED: Manual Release ambiguous dev push: {error}")
        return 1

    print("SUCCESS: Manual Release ambiguous dev push resolved by remote postcondition")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
