@ECHO OFF
COLOR 0a
CLS

javac -sourcepath "src" "src\com\tph\client\barrierclient\ClientShell.java" -d "out\production\The_Parking_House\Client"
PAUSE
java -Dfile.encoding=CP850 -classpath "out\production\The_Parking_House\Client;lib\mysql-connector-java-8.0.15.jar" com.tph.client.barrierclient.ClientShell
PAUSE
EXIT