@echo off
echo Compiling files...
javac *.java

echo Generating javadoc...
javadoc -d doc -quiet *.java

echo Packing class, resource, and documentation files into .jar archive...
jar -cfm PassGen.jar manifest.txt *.class res doc
mkdir bin
move PassGen.jar bin
echo.
echo Executable jar file located in the bin folder
pause