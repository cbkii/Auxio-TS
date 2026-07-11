import subprocess
import os

def check_branch():
    result = subprocess.run(["git", "branch", "--show-current"], capture_output=True, text=True)
    return result.stdout.strip()

print("Current branch:", check_branch())
