
call 00_set_rtc_credentials %1

FOR /F %%i in (projects.txt) do (
cd %%i
mvn tools-api:rtc-deliver -Dusername=%USERNAME% -Dpassword=%PWD% -DrtcUri=%RTC_URI%
cd..
)