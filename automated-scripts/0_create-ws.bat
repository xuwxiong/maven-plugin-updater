@ECHO OFF
cls

call 00_set_rtc_credentials %1

ECHO ....................................................................
ECHO 1 - CREATE WORKSPACE...
ECHO usage : create-ws username password branch
ECHO example : create-ws user toto D11BUGFIX
ECHO ....................................................................

FOR /F %%i in (projectsScm.txt) do (

FOR /f "delims=." %%a IN ("%%i") DO (call :Foo %%a-area %%i)

)
:Foo
set AREA=%1
echo f-%AREA% %1
IF not x%AREA:soa=%==x%AREA% (
ECHO UUSOA-%1
call mvn tools-api:create-rtc-workspace -Dusername=%USERNAME% -Dpassword=%PWD% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:add-rtc-target-flow -Dusername=%USERNAME% -Dpassword=%PWD% -DprojectArea=%AREA% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dstream=%2_%BRANCH%_STR -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:set-rtc-current-target-flow -Dusername=%USERNAME% -Dpassword=%PWD% -DprojectArea=%AREA% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dstream=%2_%BRANCH%_STR -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:set-rtc-default-target-flow -Dusername=%USERNAME% -Dpassword=%PWD% -DprojectArea=%AREA% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dstream=%2_%BRANCH%_STR -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:add-rtc-components-from-stream -Dusername=%USERNAME% -Dpassword=%PWD% -DprojectArea=%AREA% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dstream=%2_%BRANCH%_STR -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:rtc-remote-accept -Dusername=%USERNAME% -Dpassword=%PWD% -DprojectArea=%AREA% -DrtcUri=https://prod-rtc-b2e.iad.ca.inet/ccm -Dstream=%2_%BRANCH%_STR -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
call mvn tools-api:rtc-load -Dusername=%USERNAME% -Dpassword=%PWD% -DrtcUri=%RTC_URI% -Dworkspace=DEV_release_%USERNAME%_%2_%BRANCH%_WS
) ELSE (ECHO UUHO-%1)
