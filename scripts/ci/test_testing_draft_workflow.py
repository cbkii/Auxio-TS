import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
DRAFT = (ROOT / ".github/workflows/testing-apk-draft.yml").read_text()
PUBLISH = (ROOT / ".github/workflows/testing-apk-publish.yml").read_text()
REPAIR = (ROOT / ".github/workflows/testing-apk-reconcile-notes.yml").read_text()
HELPER = (ROOT / "scripts/ci/build-auxio-testing-input.sh").read_text()
GRADLE = (ROOT / "app/build.gradle").read_text()
NOTES = (ROOT / "scripts/ci/reconcile_testing_release.py").read_text()


class TestingPublisherContracts(unittest.TestCase):
    def test_explicit_build_requests_and_trusted_source(self):
        self.assertIn("workflow_dispatch:", DRAFT)
        self.assertIn("pull_request_target:", DRAFT)
        self.assertIn("issue_comment:", DRAFT)
        self.assertIn("source_to_build:", DRAFT)
        self.assertIn("No explicit TESTING APK request", DRAFT)
        self.assertIn("OWNER|MEMBER|COLLABORATOR", DRAFT)
        self.assertIn("requested == 'true'", DRAFT)
        self.assertIn("needs.resolve-request.outputs.source_sha", DRAFT)
        self.assertIn("default: dev", DRAFT)
        self.assertNotIn("workflow_run:", DRAFT)

    def test_trusted_comment_not_blocked_by_optional_label_write(self):
        self.assertIn("issue_comment:", DRAFT)
        self.assertIn('trusted_commenter "$AUTHOR_ASSOCIATION"', DRAFT)
        self.assertIn('if ! ensure_testing_label "$pr_number"; then', DRAFT)
        self.assertIn('continuing the explicit trusted request', DRAFT)

    def test_signing_and_install_identity(self):
        self.assertIn('System.getenv("AUXIO_TS_TESTING_APK") == "1"', GRADLE)
        self.assertIn('setVersionName("0.0.0-TS18-TESTING")', GRADLE)
        self.assertIn("setVersionCode(2100000000)", GRADLE)
        self.assertIn('versionName "26.9.8"', GRADLE)
        self.assertIn("versionCode 26090800", GRADLE)
        self.assertIn('testing_package com.tw.media', HELPER)
        self.assertIn('testing_version_code 2100000000', HELPER)
        self.assertIn("testDebugUnitTest", HELPER)
        self.assertIn("apksigner", HELPER)
        self.assertIn('KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}', PUBLISH)
        self.assertIn("package: name='com.tw.media'", PUBLISH)
        self.assertIn("arm64-v8a", PUBLISH)
        self.assertIn("SIGNER", PUBLISH)

    def test_handoff_and_two_snapshot_commit_marker(self):
        self.assertIn("auxio-ts-testing-input", DRAFT)
        self.assertIn("auxio-debug.apk", PUBLISH)
        self.assertIn("auxio-release.apk", PUBLISH)
        self.assertIn("group_complete", PUBLISH)
        self.assertIn('for entry in "${groups[@]:2}"', PUBLISH)
        self.assertIn("BUILD_INFO-", PUBLISH)
        self.assertIn("SHA256SUMS-", PUBLISH)
        self.assertIn('upload_asset "$release_id" "outgoing/$INFO_NAME"', PUBLISH)
        self.assertLess(PUBLISH.index('upload_asset "$release_id" "outgoing/$SUMS_NAME"'),
                        PUBLISH.index('upload_asset "$release_id" "outgoing/$INFO_NAME"'))
        self.assertIn("cancel-in-progress: false", PUBLISH)
        self.assertIn("retention-days: 7", DRAFT)

    def test_anchored_repair_readback_and_draft_only(self):
        self.assertIn("testing-apk-draft-managed", PUBLISH)
        self.assertIn("testing-apk-notes:begin", NOTES)
        self.assertIn("testing-apk-notes:end", NOTES)
        self.assertIn("BUILD_INFO checksum mismatch", NOTES)
        self.assertIn("PATCH/read-back mismatch", NOTES)
        self.assertIn("unexpected APK package/version/ABI contract", NOTES)
        self.assertIn("workflow_dispatch:", REPAIR)
        self.assertIn("reconcile_testing_release.py", REPAIR)
        self.assertIn("group: testing-only-draft-release", REPAIR)
        self.assertIn("contents: write", PUBLISH)
        self.assertNotIn("release-orchestrator.py", PUBLISH)


if __name__ == "__main__":
    unittest.main()
