@ECHO OFF
@title = EQX (Principles of Programing Language)
ECHO "load EQX resources..."
javac EQX\src\lib\*.java
cd EQX\src\

ECHO "load EQX main..."
javac EQX.java
dir
cls
java EQX