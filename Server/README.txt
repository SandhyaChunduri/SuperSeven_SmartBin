To build this project, MAVEN needs to be installed and included in the system path.
Here is an example to set it on the Windows command prompt:
set PATH=c:\software\apache-maven-3.1.1\bin;%PATH%

Once path is set, the command 'mvn' should be recognized in the command prompt window.

To build the project, run the test code and create the war file, following command is to be used:
cd <PROJECT_BASEDIR>
mvn install
Note that the test code refers to MongoDB that is setup in our private network. Hence this command will fail to build the war when run outside of our VPN.
Hence the following command needs to be used to skip the tests.

mvn install -DskipTests

To run only the test code, following command is to be used from the project's base directory:
mvn test

To clean the project, following command is to be used from the project's base directory:
mvn clean

