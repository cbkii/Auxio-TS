#!/usr/bin/env python3
"""Network-free tests for Auxio metadata-driven release notes."""
import copy
import hashlib
import importlib.util
import unittest
from pathlib import Path
from unittest.mock import patch

PATH = Path(__file__).resolve().parents[1] / "reconcile-release-notes.py"
spec = importlib.util.spec_from_file_location("auxio_release_notes", PATH)
notes = importlib.util.module_from_spec(spec)
spec.loader.exec_module(notes)
REPO = "cbkii/Auxio-TS"
TAG = "v26.9.8"
SHA = "a" * 40


def fixture(variants=("app-release",), draft=True):
    assets, blobs = [], {}
    for variant in variants:
        apk = f"Auxio-TS-{TAG}-{variant}.apk"
        digest = hashlib.sha256(("APK" + variant).encode()).hexdigest()
        payload = (
            f"asset={apk}\nasset_kind=signed-apk\nasset_sha256={digest}\n"
            f"source_commit={SHA}\nrelease_tag={TAG}\n"
            f"application_id={'com.tw.media' if variant == 'app-release' else 'example.debug'}\n"
            "version_code=2609080\nversion_name=26.9.8\n"
            "\n[apksigner certificates]\nSigner #1 ...\n"
        ).encode()
        sidecar = f"{digest}  {apk}\n".encode()
        for offset, (filename, raw) in enumerate(((apk, None), (apk + ".metadata.txt", payload), (apk + ".sha256", sidecar))):
            ident = len(assets) + 1
            asset = {
                "id": ident, "name": filename, "size": 100, "state": "uploaded",
                "browser_download_url": f"https://github.com/{REPO}/releases/download/untagged-a1/{filename}",
                "digest": f"sha256:{digest if offset == 0 else hashlib.sha256(raw).hexdigest()}",
            }
            assets.append(asset)
            if raw is not None:
                blobs[ident] = raw
    release = {
        "id": 22, "name": TAG, "tag_name": "untagged-a1", "draft": draft,
        "body": "## What's changed\n\n- previous immutable changelog entry\n\n## Assets\n"
                "- Previous release policy remains unchanged.\n",
        "assets": assets,
    }
    return release, blobs


class ReleaseNotesTests(unittest.TestCase):
    def perform(self, release, blobs, *, mutate_api=None):
        state = copy.deepcopy(release)
        patches = []

        def fake_api(path, body=None):
            if mutate_api:
                mutate_api(state, len(patches), body)
            if body is not None:
                patches.append(body)
                state.update(body)
            return copy.deepcopy(state)

        with patch.object(notes, "api", fake_api), patch.object(
            notes, "asset_bytes", lambda _repo, ident: blobs[ident]):
            changed = notes.reconcile(REPO, 22, TAG, SHA)
        return state, patches, changed

    def test_one_triplet_generates_matched_links_and_readback(self):
        release, blobs = fixture()
        updated, patches, changed = self.perform(release, blobs)
        self.assertTrue(changed)
        self.assertEqual(len(patches), 1)
        self.assertIn(notes.BEGIN, updated["body"])
        self.assertIn(notes.END, updated["body"])
        self.assertIn("app-release.apk.metadata.txt", updated["body"])
        self.assertIn("app-release.apk.sha256", updated["body"])
        self.assertIn("previous immutable changelog entry", updated["body"])
        self.assertIn("Previous release policy remains unchanged", updated["body"])

    def test_multiple_triplets_and_idempotence(self):
        release, blobs = fixture(("app-release", "lsposed-api100-bridge"))
        updated, _, _ = self.perform(release, blobs)
        self.assertEqual(updated["body"].count("| [Download]("), 2)
        again, patches, changed = self.perform(updated, blobs)
        self.assertEqual(again["body"], updated["body"])
        self.assertEqual(patches, [])
        self.assertFalse(changed)

    def test_only_managed_section_replaced(self):
        release, blobs = fixture()
        release["body"] += notes.BEGIN + "\nold stale assets\n" + notes.END + "\nManual appendix.\n"
        updated, _, _ = self.perform(release, blobs)
        self.assertNotIn("old stale assets", updated["body"])
        self.assertTrue(updated["body"].endswith("Manual appendix.\n"))

    def test_refuse_published_release(self):
        release, blobs = fixture(draft=False)
        with self.assertRaisesRegex(notes.ReconciliationError, "Published releases"):
            self.perform(release, blobs)

    def test_refuse_missing_triplet(self):
        release, blobs = fixture()
        release["assets"].pop()
        with self.assertRaisesRegex(notes.ReconciliationError, "Incomplete APK"):
            self.perform(release, blobs)

    def test_refuse_sidecar_digest_mismatch(self):
        release, blobs = fixture()
        meta_id = next(a["id"] for a in release["assets"] if a["name"].endswith(".metadata.txt"))
        blobs[meta_id] += b"malicious change"
        with self.assertRaisesRegex(notes.ReconciliationError, "Remote SHA-256 mismatch"):
            self.perform(release, blobs)

    def test_refuse_apk_hash_mismatch(self):
        release, blobs = fixture()
        release["assets"][0]["digest"] = "sha256:" + "b" * 64
        with self.assertRaisesRegex(notes.ReconciliationError, "remote APK hash differs"):
            self.perform(release, blobs)

    def test_refuse_wrong_source_commit(self):
        release, blobs = fixture()
        with patch.object(notes, "api", return_value=release), patch.object(
            notes, "asset_bytes", lambda _repo, ident: blobs[ident]):
            with self.assertRaisesRegex(notes.ReconciliationError, "source SHA"):
                notes.reconcile(REPO, 22, TAG, "b" * 40)

    def test_refuse_ambiguous_anchors(self):
        release, blobs = fixture()
        release["body"] += notes.BEGIN + "\nstale\n"
        with self.assertRaisesRegex(notes.ReconciliationError, "Ambiguous or damaged"):
            self.perform(release, blobs)

    def test_refuse_unexpected_asset(self):
        release, blobs = fixture()
        extra = copy.deepcopy(release["assets"][0])
        extra["name"] = "Auxio-TS-v26.9.8-topway-twmusic-release.apk"
        extra["browser_download_url"] = (
            f"https://github.com/{REPO}/releases/download/untagged-a1/{extra['name']}"
        )
        release["assets"].append(extra)
        with self.assertRaisesRegex(notes.ReconciliationError, "Unexpected or unpaired"):
            self.perform(release, blobs)

    def test_refuse_race_to_published(self):
        release, blobs = fixture()
        def race(state, patches, body):
            if patches == 0 and body is None and getattr(race, "calls", 0) == 1:
                state["draft"] = False
            race.calls = getattr(race, "calls", 0) + 1
        with self.assertRaisesRegex(notes.ReconciliationError, "changed after inspection"):
            self.perform(release, blobs, mutate_api=race)

    def test_refuse_failed_readback(self):
        release, blobs = fixture()
        def bad_readback(state, patches, body):
            if patches == 1 and body is None:
                state["body"] = "externally edited"
        with self.assertRaisesRegex(notes.ReconciliationError, "PATCH/read-back"):
            self.perform(release, blobs, mutate_api=bad_readback)


if __name__ == "__main__":
    unittest.main()
