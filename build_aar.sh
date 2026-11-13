#!/usr/bin/env bash
set -e
chmod +x ./gradlew
echo "Building TeamDarkCore Release AAR..."
./gradlew :TeamDarkCore:assembleRelease --no-daemon --stacktrace
echo "Done! AAR location: TeamDarkCore/build/outputs/aar/TeamDarkCore-release.aar"
