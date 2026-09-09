#!/usr/bin/env python3
"""Deterministic, network-free Manual Release version/asset/dispatch matrix."""
from __future__ import annotations

import argparse
import importlib.util
import json
import os
import subprocess
import sys
import tempfile
from pathlib import Path
from types import ModuleType

REPO = Path.cwd()
VERSION_TOOL = REPO / "scripts/release-orchestrator.py"
ASSET_TOOL = REPO / "scripts/release-asset-matrix.py"
SELECT = REPO / "scripts/manual-release/03-select-maintained-release-assets.sh"

class Failure(RuntimeError): pass

def check(value: bool, message: str) -> None:
    if not value: raise Failure(message)

def load(path: Path, name: str) -> ModuleType:
    spec=importlib.util.spec_from_file_location(name,path)
    if spec is None or spec.loader is None: raise Failure(f"cannot load {path}")
    module=importlib.util.module_from_spec(spec); sys.modules[name]=module; spec.loader.exec_module(module); return module

def write(path: Path, values: list[str]) -> None:
    path.write_text(''.join(f'{v}\n' for v in values),encoding='utf-8')

def test_versions(v: ModuleType) -> None:
    S=v.SemVer; V=v.VersionResolution
    v647,v650,v651,v655=S(6,4,7),S(6,5,0),S(6,5,1),S(6,5,5)
    check(v.choose_version_resolution(source_version=v647,git_versions={v647,v650},release_versions={v647,v650},draft_versions=set(),requested_mode='auto',requested_version=None)==V(v651,'create_new_release','increment_latest_complete_version'),'next patch changed')
    check(v.choose_version_resolution(source_version=v647,git_versions={v647,v650},release_versions={v647},draft_versions=set(),requested_mode='auto',requested_version=None)==V(v650,'repair_existing_release','resume_latest_tag_without_release'),'tag-only resume changed')
    check(v.choose_version_resolution(source_version=v647,git_versions={v647,v650},release_versions={v647},draft_versions=set(),requested_mode='auto',requested_version=v655)==V(v655,'create_new_release','explicit_new_tag'),'explicit new tag changed')

def plan(a: ModuleType, root: Path, variants: list[str], *, debug='workflow_artifacts', existing: list[str]|None=None, replace='false', mode='create_new_release') -> dict:
    vf=root/'variants'; ef=root/'existing'; out=root/'plan.json'; write(vf,variants); write(ef,existing or [])
    a.command_plan_assets(argparse.Namespace(mode=mode,release_tag='v26.6.9',selected_variants_file=str(vf),debug_destination=debug,existing_assets_file=str(ef),replace=replace,output=str(out)))
    return json.loads(out.read_text())

def test_assets(a: ModuleType, root: Path) -> None:
    default=['standard','standard_debug','topway_twmedia','topway_twmedia_debug']
    p=plan(a,root,default)
    check(p['build_variants']==default,'default build order changed')
    check(len(p['upload_names'])==6,'default release must upload two APK triplets')
    check(len(p['debug_workflow_names'])==6,'default debug companions must remain workflow artifacts')
    full=default+['topway_twmusic_magisk','lsposed_bridge','lsposed_bridge_debug']
    p=plan(a,root,full)
    names=p['upload_names']
    check(any(n.endswith('-topway-twmusic-magisk.zip') for n in names),'Magisk ZIP absent')
    check(not any('topway-twmusic' in n.lower() and n.lower().endswith('.apk') for n in names),'raw topwayTwMusic APK leaked')
    complete=a.triplet(a.VARIANT_NAMES['topway_twmusic_magisk'].format(tag='v26.6.9'))
    p=plan(a,root,['topway_twmusic_magisk'],existing=complete,mode='repair_existing_release')
    check(p['build_variants']==[] and p['upload_names']==[],'complete Magisk triplet rebuilt during repair')
    partial=complete[:2]
    p=plan(a,root,['topway_twmusic_magisk'],existing=partial,mode='repair_existing_release')
    check(p['build_variants']==['topway_twmusic_magisk'],'partial Magisk triplet not rebuilt')
    check(p['replace_names']==partial,'partial Magisk repair replacement set wrong')

def run_select(root: Path, standard: str, media: str, magisk: str, bridge: str, debug: str, should_pass=True) -> list[str]:
    out=root/'selection.out'; out.write_text('')
    env=os.environ.copy(); env.update(RUNNER_TEMP=str(root),GITHUB_OUTPUT=str(out),INCLUDE_STANDARD=standard,INCLUDE_TOPWAY_TWMEDIA=media,INCLUDE_TOPWAY_TWMUSIC_MAGISK=magisk,INCLUDE_LSPOSED_BRIDGE=bridge,PUBLISH_DEBUG_APKS=debug)
    cp=subprocess.run(['bash',str(SELECT)],cwd=REPO,env=env,text=True,capture_output=True,timeout=15,check=False)
    if should_pass and cp.returncode: raise Failure(f'selection failed: {cp.stderr}')
    if not should_pass:
        check(cp.returncode != 0,'invalid selection accepted'); return []
    selected_path=next(line.split('=',1)[1] for line in out.read_text().splitlines() if line.startswith('selected_file='))
    return Path(selected_path).read_text().splitlines()

def test_selection(root: Path) -> None:
    check(run_select(root,'true','true','false','false','false')==['standard','standard_debug','topway_twmedia','topway_twmedia_debug'],'default selection wrong')
    selected=run_select(root,'true','true','true','true','false')
    check('topway_twmusic_magisk' in selected and 'lsposed_bridge' in selected,'opt-in matrix incomplete')
    check('topway_twmusic' not in selected,'raw exact-package APK became selectable')
    run_select(root,'true','false','false','true','false',False)
    run_select(root,'false','false','false','false','false',False)
    run_select(root,'true','true','false','false','maybe',False)

def manifest_entry(a: ModuleType, variant: str, destination: str) -> dict:
    debug=variant.endswith('_debug')
    return dict(filename=a.VARIANT_NAMES[variant].format(tag='v26.6.9'),variant=variant,asset_kind='test',sha256='a'*64,application_id=a.EXPECTED_IDENTITY[variant],version_name='26.6.9-DEBUG' if debug else '26.6.9',version_code=26060900,signer_sha256='B'*64,source_commit='1'*40,release_tag='v26.6.9',destination=destination)

def test_manifest(a: ModuleType, root: Path) -> None:
    variants=[('standard','release'),('topway_twmedia','release'),('topway_twmusic_magisk','release'),('lsposed_bridge','release'),('standard_debug','workflow_artifacts'),('topway_twmedia_debug','workflow_artifacts'),('lsposed_bridge_debug','workflow_artifacts')]
    entries=[manifest_entry(a,v,d) for v,d in variants]
    manifest=root/'manifest.json'; expected=root/'expected'; manifest.write_text(json.dumps(entries)); write(expected,[e['filename'] for e in entries])
    args=argparse.Namespace(manifest=str(manifest),expected_built_names_file=str(expected),version_name='26.6.9',version_code='26060900',release_tag='v26.6.9',source_commit='1'*40,debug_destination='workflow_artifacts')
    a.command_validate_manifest(args)
    bad=[dict(e) for e in entries]; bad[2]['filename']='Auxio-TS-v26.6.9-topway-twmusic-release.apk'; manifest.write_text(json.dumps(bad))
    try: a.command_validate_manifest(args)
    except a.AssetPlanError: pass
    else: raise Failure('raw topwayTwMusic manifest was accepted')

def main() -> int:
    try:
        v=load(VERSION_TOOL,'release_version_tool'); a=load(ASSET_TOOL,'release_asset_tool')
        with tempfile.TemporaryDirectory() as td:
            root=Path(td); test_versions(v); test_assets(a,root); test_selection(root); test_manifest(a,root)
    except Exception as exc:
        print(f'Manual Release matrix: FAIL: {exc}',file=sys.stderr); return 1
    print('Manual Release three-variant matrix: PASS'); return 0

if __name__=='__main__': raise SystemExit(main())
