***Choose one of the methods defined below to build and execute the application.***


## Maven Build & Execute

The following assumptions are made:

 1. Java 1.8+ is available and configured.  
 2. Maven 3.x+ is available and configured. 
 3. The txnlog.dat file is available in the build folder. 
    
Place the java project in the desired build folder and ensure that the *txnlog.dat* input file is present at the same level as the *src* folder.
Move to the build folder.

mvn clean

mvn package

java -jar target\protosolution-0.0.1-SNAPSHOT-jar-with-dependencies.jar txnlog.dat 

## Maven is not available... using just javac

The following assumptions are made:

 1. Java 1.8+ is available and configured.  
 2. The txnlog.dat file is available in the build folder. 
 
 Place the java project in the desired build folder and ensure that the *txnlog.dat* input file is present at the same level as the *src* folder.
 
Move to the build folder.

dir /s /B *.java > sources

mkdir target

javac -d target @sources

cd target

java com.proto.solution.Proto ..\txnlog.dat

## Maven is not available... use javac and create an executable jar

Place the java project in the desired build folder and ensure that the *txnlog.dat* input file is present at the same level as the *src* folder.

Move to the build folder.

dir /s /B *.java > sources

mkdir target

javac -d target @sources


jar cmf src\main\resources\META-INF\MANIFEST.MF Proto.jar -C ./TARGET/  .

java -jar Proto.jar txnlog.dat




