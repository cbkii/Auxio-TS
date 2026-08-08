#!/usr/bin/env python3
"""Exercise replacement when an asset DELETE commits but the client sees failure."""
from __future__ import annotations

import json
import os
import subprocess
import tempfile
from pathlib import Path

ROOT = Path.cwd()
SCRIPT = ROOT / "scripts/manual-release/16-upload-or-replace-planned-release-assets.sh"


def run(command: list[str], env: dict[str, str]) -> str:
    result = subprocess.run(
        command,
        cwd=ROOT,
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
    return result.stdout


def executable(path: Path, text: str) -> None:
    path.write_text(text, encoding="utf-8")
    path.chmod(0o755)


def main() -> int:
    if not SCRIPT.is_file():
        print("FAILED: Manual Release upload script is unavailable")
        return 2

    try:
        with tempfile.TemporaryDirectory(prefix="auxio-release-delete-") as temp:
            root = Path(temp)
            fakebin = root / "fakebin"
            fakebin.mkdir()
            state_path = root / "state.json"
            state_path.write_text(
                json.dumps(
                    {
                        "delete_failed_once": False,
                        "release": {
                            "id": 101,
                            "tag_name": "v6.6.0",
                            "upload_url": "https://uploads.invalid/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}",
                            "assets": [
                                {"id": 777, "name": "artifact.apk", "state": "uploaded"}
                            ],
                        },
                    }
                )
                + "\n",
                encoding="utf-8",
            )

            executable(
                fakebin / "gh",
                r'''#!/usr/bin/env python3
import json
import os
import sys
from pathlib import Path

path = Path(os.environ["FAKE_RELEASE_STATE"])
state = json.loads(path.read_text(encoding="utf-8"))
args = sys.argv[1:]
method = "GET"
endpoint = ""
i = 1
while i < len(args):
    if args[i] == "--method":
        method = args[i + 1]
        i += 2
    elif args[i].startswith("repos/"):
        endpoint = args[i]
        i += 1
    else:
        i += 1
if method == "GET" and endpoint.endswith("/releases/101"):
    print(json.dumps(state["release"]))
    raise SystemExit(0)
if method == "DELETE" and endpoint.endswith("/releases/assets/777"):
    state["release"]["assets"] = [
        asset for asset in state["release"]["assets"] if asset["id"] != 777
    ]
    first = not state["delete_failed_once"]
    state["delete_failed_once"] = True
    path.write_text(json.dumps(state) + "\n", encoding="utf-8")
    raise SystemExit(1 if first else 0)
raise SystemExit(2)
''',
            )
            executable(
                fakebin / "curl",
                r'''#!/usr/bin/env python3
import json
import os
import sys
import urllib.parse
from pathlib import Path

path = Path(os.environ["FAKE_RELEASE_STATE"])
state = json.loads(path.read_text(encoding="utf-8"))
url = next(value for value in reversed(sys.argv[1:]) if value.startswith("https://"))
name = urllib.parse.parse_qs(urllib.parse.urlparse(url).query)["name"][0]
asset = {"id": 888, "name": name, "state": "uploaded"}
state["release"]["assets"].append(asset)
path.write_text(json.dumps(state) + "\n", encoding="utf-8")
print(json.dumps(asset))
''',
            )
            executable(fakebin / "sleep", "#!/usr/bin/env bash\nexit 0\n")

            staged = root / "artifact.apk"
            staged.write_bytes(b"replacement")
            upload = root / "upload.tsv"
            upload.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
            replace_names = root / "replace.txt"
            replace_names.write_text("artifact.apk\n", encoding="utf-8")

            env = os.environ.copy()
            env.update(
                {
                    "PATH": f"{fakebin}:{env.get('PATH', '')}",
                    "RUNNER_TEMP": str(root),
                    "GITHUB_REPOSITORY": "cbkii/Auxio-TS",
                    "GH_TOKEN": "mock-token",
                    "FAKE_RELEASE_STATE": str(state_path),
                    "RELEASE_TAG": "v6.6.0",
                    "RELEASE_ID": "101",
                    "UPLOAD_TSV": str(upload),
                    "REPLACE_NAMES_FILE": str(replace_names),
                    "REPLACE": "false",
                }
            )
            run(["bash", str(SCRIPT)], env)
            assets = json.loads(state_path.read_text(encoding="utf-8"))["release"]["assets"]
            if assets != [{"id": 888, "name": "artifact.apk", "state": "uploaded"}]:
                raise RuntimeError(f"unexpected remote asset state after recovery: {assets}")
    except (OSError, RuntimeError, StopIteration, KeyError, json.JSONDecodeError) as error:
        print(f"FAILED: Manual Release ambiguous asset delete: {error}")
        return 1

    print("SUCCESS: Manual Release ambiguous asset delete recovered without duplicate assets")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
