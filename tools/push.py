import subprocess
import os

env = os.environ.copy()
env["GIT_TERMINAL_PROMPT"] = "0"
env["GIT_ASKPASS"] = "echo" # Just to make sure it fails immediately instead of hanging

# I'll use the API tool to submit the changes, because we're running out of ways to push.
