#!/usr/bin/env python3
"""Rename the remaining FBox/SBox Java compatibility packages to TDCore.
This is a source rename, NOT a rewrite of precompiled ELF JNI symbol tables.
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "TeamDarkCore" / "src" / "main"
REPL = [
    ("com.fbox.core", "com.tdcore.legacy"),
    ("com/fbox/core", "com/tdcore/legacy"),
    ("com.sbox", "com.tdcore.compat"),
    ("com/sbox", "com/tdcore/compat"),
    ("SboxCore", "TDCoreCompat"),
    ("sbox", "tdcore"),
    ("SBOX", "TDCORE"),
    ("FBox", "TDCore"),
    ("fbox", "tdcore"),
    ("FBOX", "TDCORE"),
]
EXTS = {".java", ".kt", ".aidl", ".xml", ".gradle", ".kts", ".mk", ".cpp",
        ".cc", ".c", ".h", ".hpp", ".pro", ".txt", ".md", ".json",
        ".properties", ".sh"}
SKIP = {".git", ".gradle", "build", ".idea", "jniLibs"}

changed = moved = 0
for p in SOURCE.rglob("*"):
    if not p.is_file() or any(x in SKIP for x in p.relative_to(SOURCE).parts):
        continue
    if p.suffix not in EXTS and p.name not in {"Android.mk", "Application.mk"}:
        continue
    try:
        old = p.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        continue
    new = old
    for a, b in REPL:
        new = new.replace(a, b)
    if new != old:
        p.write_text(new, encoding="utf-8")
        changed += 1

paths = sorted(
    (p for p in SOURCE.rglob("*") if p.is_file()
     and not any(x in SKIP for x in p.relative_to(SOURCE).parts)),
    key=lambda p: len(p.parts), reverse=True,
)
for p in paths:
    relative = str(p.relative_to(SOURCE))
    updated = relative
    for a, b in REPL:
        updated = updated.replace(a, b)
    if updated == relative:
        continue
    dst = SOURCE / updated
    dst.parent.mkdir(parents=True, exist_ok=True)
    if dst.exists():
        raise RuntimeError(f"Rename collision: {relative} -> {updated}")
    p.rename(dst)
    moved += 1

for p in sorted(SOURCE.rglob("*"), key=lambda p: len(p.parts), reverse=True):
    if p.is_dir():
        try:
            p.rmdir()
        except OSError:
            pass
print(f"TDCore package migration: {changed} files changed, {moved} files relocated")
