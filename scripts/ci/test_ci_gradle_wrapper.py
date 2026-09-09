#!/usr/bin/env python3
"""Deterministic regression tests for scripts/ci-gradle.sh process plumbing."""

from __future__ import annotations

import os
from pathlib import Path
import shutil
import signal
import subprocess
import tempfile


ROOT = Path(__file__).resolve().parents[2]
WRAPPER = ROOT / "scripts" / "ci-gradle.sh"


def check(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def terminate_process_group(proc: subprocess.Popen[str]) -> None:
    """Best-effort cleanup for the fake Gradle descendant used by the test."""
    try:
        os.killpg(proc.pid, signal.SIGTERM)
    except ProcessLookupError:
        return
    try:
        proc.wait(timeout=1)
    except subprocess.TimeoutExpired:
        try:
            os.killpg(proc.pid, signal.SIGKILL)
        except ProcessLookupError:
            pass
        proc.wait(timeout=1)


def test_redirected_descendant_cannot_hold_filter_descriptors() -> None:
    """A redirected descendant must not keep the wrapper's internal filter pipes open."""
    with tempfile.TemporaryDirectory(prefix="auxio-ci-gradle-") as temp:
        case = Path(temp)
        runner_temp = case / "runner-temp"
        runner_temp.mkdir()

        gradlew = case / "gradlew"
        gradlew.write_text(
            "#!/usr/bin/env bash\n"
            "set -eu\n"
            "(\n"
            "  exec >/dev/null 2>&1\n"
            "  sleep 5\n"
            ") &\n"
            "printf 'fake-gradle-complete\\n'\n",
            encoding="utf-8",
        )
        gradlew.chmod(0o755)

        env = os.environ.copy()
        env.update(
            {
                "RUNNER_TEMP": str(runner_temp),
                "AUXIO_TS_CI_HEARTBEAT_INTERVAL": "30",
            }
        )

        stdout_path = case / "stdout.log"
        stderr_path = case / "stderr.log"
        with stdout_path.open("w", encoding="utf-8") as stdout_file, stderr_path.open(
            "w", encoding="utf-8"
        ) as stderr_file:
            proc = subprocess.Popen(
                ["bash", str(WRAPPER), "fakeTask"],
                cwd=case,
                env=env,
                stdout=stdout_file,
                stderr=stderr_file,
                text=True,
                start_new_session=True,
            )
            try:
                try:
                    proc.wait(timeout=2.0)
                except subprocess.TimeoutExpired as exc:
                    terminate_process_group(proc)
                    raise AssertionError(
                        "ci-gradle.sh waited on filter pipes retained by a redirected descendant"
                    ) from exc
            finally:
                # The intentionally long-lived fake descendant may still be sleeping even after
                # the wrapper exits successfully. Kill the isolated process group so the test
                # leaves no background process behind.
                terminate_process_group(proc)

        stdout = stdout_path.read_text(encoding="utf-8")
        stderr = stderr_path.read_text(encoding="utf-8")
        check(proc.returncode == 0, f"wrapper returned {proc.returncode}: {stderr}")
        check("fake-gradle-complete" in stdout, "fake Gradle stdout was not forwarded")
        check(
            "Gradle completed successfully" in stderr,
            "wrapper did not report successful completion",
        )


def main() -> int:
    check(WRAPPER.is_file(), f"missing wrapper: {WRAPPER}")
    check(shutil.which("bash") is not None, "bash is required")
    test_redirected_descendant_cannot_hold_filter_descriptors()
    print("CI Gradle wrapper regression tests: PASS")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
