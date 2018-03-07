FOR /F %%i in (projects.txt) do (
cd %%i
mvn versions:set -DnewVersion=%1  -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true
cd..
) 