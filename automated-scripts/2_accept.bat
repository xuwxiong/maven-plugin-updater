call 00_set_rtc_credentials %1

call lscm login -r %RTC_URI% -u %USERNAME% -P %PWD%

call lscm accept -P %PWD%