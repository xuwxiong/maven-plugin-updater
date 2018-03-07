call 00_set_rtc_credentials %1


FOR /F %%i in (projectsScm.txt) do (mvn tools-api:rtc-load -Dusername=%USERNAME% -Dpassword=%PWD% -DrtcUri=%RTC_URI% -Dworkspace=DEV_release_%USERNAME%_%%i_%BRANCH%_WS)