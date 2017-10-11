EQX : MANUAL

COMMANDS : 
	[cls | clear] :  
		=> Clear the screen 
	[tree | path| dir | mydir]:
		=> Graphicaly displays the directory structure of a folder or the main path of the compiler
	[ext | exit | bye] :
		=> Quits the tunnel or the compiler (EXIT)
	[load] :
		=> load file (.eqx) Extension
GETTING STARTED : 
	MANUAL INSTALLATION (using java)  :
		=> Install JDK(java development Kit JDK 8)
	(Updating the PATH Environment Variable)
		=> (THIS IS THE ORIGINAL PATH FOR JAVA ENVIRONMENT) 
			=>C:\> "C:\Program Files\Java\jdk1.8.0\bin\javac" MyClass.java
			=>It is useful to set the PATH variable permanently so it will persist after rebooting.
			=>To set the PATH variable permanently, add the full path of the jdk1.8.0\bin directory to the PATH variable. Typically, this full path looks something like C:\Program Files\Java\jdk1.8.0\bin. Set the PATH variable as follows on Microsoft 
			=> (TO START SET THE PATH)
				=> Click Start, then Control Panel, then System.
				=> Click Advanced, then Environment Variables.
				=> Add the location of the bin folder of the JDK installation to the PATH variable in System Variables. The following is a typical value for the PATH variable: (C:\Program Files\Java\jdk1.8.0\bin)
			=> (NOTE)
				=> The PATH environment variable is a series of directories separated by semicolons (;) and is not case-sensitive. Microsoft Windows looks for programs in the PATH directories in order, from left to right.
				=> You should only have one bin directory for a JDK in the path at a time. Those following the first instance are ignored.
				=> If you are not sure where to add the JDK path, append it.
				=> The new path takes effect in each new command window you open after setting the PATH variable.
			=> (TEST)
				=> Start CMD then type "java"
				=> If success then procced to the next step.
	((EQX) MANUAL (NON-BATCH-FILE (.bat) start))
		=> Start CMD
		=> then type CD(Current Directory) + the path of the compiler ex. cd "path_of_the_compiler"/src/
		=> then type "javac lib/*.java"
		=> if done then type "javac EQX.java"
		=> if success then type "java EQX"
		=> if success you can see the EQX welcome message.
	(USING BATCH-FILE (.bat))
		=> just start start.exe then automatically start the EQX compiler 
	(BASIC Syntax)
		=> print : print output
			ex. print(1+1)  = (output) 2 
		  Assign Variable : 
			ex. a = 20 
			    print(a)  = (output) 20
			ex. a = 20
		 	    b = 10
			    print(a+b) = (output) 30
			ex. a = 10
			    b = 10
   			    print((a*b) + (a+b) + a)
			    (output) = 130
			ex  name = "Tom"
			    age = 22
			    print("name: " + name + " age: "+age)
			    (output) : "name : Tom age: 22"
	(Using File .eqx (Extensions))
		=> create a file (.eqx) extensions
			ex. sample.eqx
		=> then start create basic syntax of that file 
			ex. (sample.eqx) => a = 10
					    b = 20 
					    print(a+b)
		=> then execute the file using load on the EQX Compiler
			ex. load sample.eqx
			then => return the output of the file 
				(output) =  30
		
			

		

