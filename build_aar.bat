@echo off
echo Building TeamDarkCore Release AAR...
call gradlew.bat :TeamDarkCore:assembleRelease --no-daemon --stacktrace
echo Done! AAR location: TeamDarkCore\build\outputs\aar\TeamDarkCore-release.aar
pause
