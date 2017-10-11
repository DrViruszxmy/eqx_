package lib;

import java.io.BufferedReader;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class Lib {
	Scanner scan = new Scanner(System.in);
	Map<String, String> tokens;
	BufferedReader br;
	String[][] c =  new String [10][10];
	//extensions ni cya
	String[] ext = new String[] {"eqx","EQX"};
	
	//para ni cya sa command sa environment sa compiler
	String[][] commandValue = new String [][] {{"cls","clear"},
											   {"path","dir","tree","mydir"},
											   {"ext","exit","bye"}};
	/**
	 * Enumerate kay nag assume na equal jud ni cya sa index sa usa ka string 
	 * example kaning TokenType kay naa na cyay predefined na variable 
	 * dapat magka coencide jud ni cya ani na string 
	 * */
	enum TokenType {
        WRD, NUM, STR, LABEL, eLINE,
        EQUALS, OPRTR, _P, P_, EOF, LINE
    }						    
	enum TokenizeState {
	    DFLT, WRD, NUM, STR
	}
	// print without line
	void print(String argument) {
		System.out.print(argument);
	}
	// print in line
	void println(String argument) {
		System.out.println(argument);
	}
	// welcome message for the program
	String welcomeMessage() {
		return "EQX COMPILER LANGUAGE : \n";
	}
	// goodbye message for the program
	void goodbyeMessage(){
		System.out.println("BYE\n");
		System.exit(0);
	}
	// get the current path of the system
	String getCurrentPath() {
		return Paths.get(".").toAbsolutePath().normalize().toString();
	}
	//match using equalIgnoreCase
	boolean match(String variable, String delimiter){
		return (variable.equalsIgnoreCase(delimiter))? true : false;
	}
	//match within an array of string
	boolean anyMatch(String[] arr, String targetValue) {
	    for (String s: arr) {
	        if (s.equals(targetValue))
	            return true;
	    }
	    return false;
	}
}
