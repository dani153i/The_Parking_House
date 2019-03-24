javac -sourcepath "src" "src\com\tph\server\ServerShell.java" -d "out\production\The_Parking_House\Server"
PAUSE
java -Dfile.encoding=CP850 -classpath "out\production\The_Parking_House\Server;lib\mysql-connector-java-8.0.15.jar" com.tph.server.ServerShell
PAUSE
EXIT