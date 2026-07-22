1. **Fix missing core_settings.gradle:**
   - The error says: `Could not read script '/home/runner/work/Auxio-TS/Auxio-TS/media/core_settings.gradle' as it does not exist.`
   - This happens because the `media` submodule wasn't fetched correctly or was lost when checking out the branch. The `bootstrap-dependencies.sh` script does `git submodule update --init --recursive`. However, in the provided job log, the git status shows `media/libraries/decoder_ffmpeg/src/main/jni/ffmpeg` and other submodules being updated.
   - Ah, actually, I noticed in `git status` output earlier: `modified:   musikr/src/main/cpp/taglib (untracked content)`. Maybe I need to run `git submodule update --init --recursive` to ensure submodules are set up properly?
   - Wait! The failed CI job is `format-and-remove` running `spotlessApply` and `git diff`. The CI log shows: `Could not read script '/home/runner/work/Auxio-TS/Auxio-TS/media/core_settings.gradle' as it does not exist.`
   - This indicates that the Github Action `format-and-remove` does not initialize the submodules before running `spotlessApply`. But the `bootstrap-dependencies.sh` *was* run! Look at the logs:
     ```
     --- Updating submodule media ---
     Cloning into '/home/runner/work/Auxio-TS/Auxio-TS/media'...
     Submodule path 'media': checked out '71d3c18df8dc96d984c2a5d788dce56289eb588b'
     ```
   - If `media/core_settings.gradle` does not exist after `bootstrap-dependencies.sh` was run, maybe the commit `71d3c18d` in the `media` submodule doesn't have it? Or maybe the CI run checked out the main repo *again*?
   - Look at the `format-and-remove` job logs:
     `Run bash ./scripts/ci-gradle.sh spotlessCheck` -> Fails.
     Then it runs `actions/checkout` AGAIN on `fix/queue-and-source-authority...` in the `format-and-remove` job:
     ```
     Run actions/checkout
     Run set -euo pipefail
     ./gradlew --no-daemon --console=plain spotlessApply
     ```
     The `actions/checkout` in `format-and-remove` does `submodules: false`. It doesn't run `bootstrap-dependencies.sh`. So `media/core_settings.gradle` is missing.

   - How to fix the `format-and-remove` job? I need to edit `.github/workflows/temporary-wp13-format.yml` or whatever workflow file is running `format-and-remove` and ensure it either initializes submodules or runs `bash ./scripts/bootstrap-dependencies.sh --profile full-build` before `spotlessApply`.
   - Actually, wait, the `format-and-remove` job runs ONLY IF the first job fails. It tries to run `spotlessApply` and commit it. The reason the first job failed is because there was a spotless failure: `app/src/main/java/org/oxycblt/auxio/music/ConfiguredSourcePolicy.kt` had violations!
   - We already fixed the spotless failure by running `./gradlew spotlessApply` and committing it!
   - So, the CI check failed *before* my last commit. My last commit was `Fix queue authority and configured source implementation` and then `Acknowledged user request to stop work`. The CI failure happened on the *previous* commit.

2. **Verify current CI status:**
   - Since I already ran `spotlessApply` and committed the changes, the next CI run should pass! I just need to verify that spotless is clean.
   - Let's check `./gradlew spotlessCheck`.
