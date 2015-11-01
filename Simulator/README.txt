This is the application that simulates the actual physical bins (with sensors) updating the database.
It is a simple program that creates a Bin object with a randomly generated binID, fillLevel, temperature, humidity, latitiude and longitude.
The random data for each of these parameters is always within a range.
For example, Bin ID is any number between 1 to 1000.
Once this object is created, it is inserted (if not already present in db) or updated (if already exists in db) in the database.
The simulator keeps updating the database in regular intervals until the program is manually terminated.

NOTE: This simulator connects to a MongoDB which is setup in our network, hence it will fail to run properly outside of our VPN.
This can be built as a standalone application as:
javac src\sim\Simulator.java -d . -classpath jars\smartbin-server.jar
