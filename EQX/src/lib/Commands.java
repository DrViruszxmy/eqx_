package lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Commands extends Lib{
	String containerCmd = "";
	public void start(){
		String in = "";
		print(welcomeMessage());
		while (!in.equalsIgnoreCase("exit")) {
			print(">> ");
			String cmd = scan.nextLine();
			check(cmd);
		}
	}
	void check(String cmd){
		if(anyMatch(commandValue[0],cmd)){
			command("cls");
			this.containerCmd = "";
			print(welcomeMessage());}
		else if(anyMatch(commandValue[1],cmd)){command("TREE " + getCurrentPath() + " /f /a");}
		else if(anyMatch(commandValue[2],cmd)){goodbyeMessage();}
		else if(cmd.trim().contains("load")){legalLoading(cmd);}
		else{
			if(cmd.contains("print")){
				EQX_lib eqx = new EQX_lib();
				eqx.interpret(containerCmd + cmd);
			}else if(cmd.contains("=")){
				this.containerCmd = (new StringBuilder()).append(containerCmd).append(cmd).toString();
			}else{

			}
		}
	}
	void command(String cmd){
		if (cmd != "") {
			try {
				new ProcessBuilder("cmd", "/c", cmd).inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void legalLoading(String arg){
		String[] splittedString = arg.split(" ");
		if (splittedString[0].contains("load")) {
			try {
				if (splittedString.length < 2) {
		            println("Usage: EQX <script>");
		            println("Where <script> is a relative path to a .eqx script to run.");
		            return;
		        }
				String data = new String(Files.readAllBytes(Paths.get(splittedString[1])), StandardCharsets.UTF_8);
				EQX_lib eqx = new EQX_lib();
				eqx.interpret(data);
			} catch (FileNotFoundException e) {
				print(splittedString[1] + " NOT FOUND\n");
			} catch (IOException e) {
				
			}

		}
	}
}
