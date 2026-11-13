#!/usr/bin/env python3
"""Rename project-owned FeaturesticBox identifiers to TeamDarkCore.
Leave Android/third-party API namespaces intact; skip binary files.
Usage: python3 tools/teamdarkcore_rebrand.py
"""
from pathlib import Path
import os
import sys

ROOT = Path(__file__).resolve().parents[1]
REPLACEMENTS = [
    ("com.featurestic.box", "com.teamdark.core"),
    ("com/featurestic/box", "com/teamdark/core"),
    ("FeaturesticBoxCore", "TeamDarkCoreEngine"),
    ("FeaturesticBox", "TeamDarkCore"),
    ("FeaturesticLoader", "TeamDarkCoreLoader"),
    ("featuresticbox", "teamdarkcore"),
    ("featurestic_box", "teamdark_core"),
    ("featurestic-box", "teamdark-core"),
    ("Featurestic", "TeamDark"),
    ("featurestic", "teamdark"),
    # Safe TDCore-only names; JNI-linked NativeCore and bundled libfbox.so are retained.
    ("fbox_so_cache", "tdcore_so_cache"),
    ("fbox_so_assets", "tdcore_so_assets"),
    ("fboxApiLevel", "tdCoreApiLevel"),
    ("FEATURESTIC_CORE_SRC", "TDCORE_CORE_SRC"),
]
EXTS = {".java", ".kt", ".aidl", ".xml", ".gradle", ".kts", ".mk", ".cpp", ".cc", ".c", ".h", ".hpp", ".pro", ".txt", ".md", ".yml", ".yaml", ".json", ".properties", ".sh", ".bat", ".cmake"}
FILENAMES = {"Android.mk", "Application.mk", "CMakeLists.txt", "gradlew", ".gitignore"}
SKIP_DIRS = {".git", ".gradle", "build", ".idea", ".github", "tools"}
changed = 0
renamed = 0
for p in list(ROOT.rglob("*")):
    if not p.is_file() or any(part in SKIP_DIRS for part in p.relative_to(ROOT).parts):
        continue
    if p.suffix.lower() not in EXTS and p.name not in FILENAMES:
        continue
    data = p.read_bytes()
    if b"\x00" in data:
        continue
    try:
        old = data.decode("utf-8")
    except UnicodeDecodeError:
        continue
    new = old
    for a, b in REPLACEMENTS:
        new = new.replace(a, b)
    if new != old:
        p.write_text(new, encoding="utf-8")
        changed += 1
paths = sorted((p for p in ROOT.rglob("*") if p.is_file() and ".git" not in p.parts and "build" not in p.parts and "tools" not in p.parts), key=lambda x: len(x.parts), reverse=True)
for p in paths:
    relative = str(p.relative_to(ROOT))
    dest = relative
    for a, b in REPLACEMENTS:
        dest = dest.replace(a, b)
    if dest != relative:
        target = ROOT / dest
        target.parent.mkdir(parents=True, exist_ok=True)
        if target.exists():
            raise RuntimeError(f"Rename collision: {relative} -> {dest}")
        p.rename(target)
        renamed += 1
# Clean up empty renamed source directories.
for p in sorted(ROOT.rglob("*"), key=lambda x: len(x.parts), reverse=True):
    if p.is_dir() and ".git" not in p.parts:
        try:
            p.rmdir()
        except OSError:
            pass
print(f"TeamDarkCore refactor: {changed} text files modified, {renamed} files moved")
