package game;

import server.Client;

public class Logic {

	public static String commands(String text, String user){

		if (text.equalsIgnoreCase("Fire"))
			return "\n   _O/\n     \\ \n     /\\_  \n     \\  ` \n     `   ";
		return "zero";

	}

	public static int damage(int command){
		int calc = 0;
		
		
		return calc;
	}
	
	public static void setCommand(String message){
		
		
	}
	
	public static String command(String text) {
		if(text.equalsIgnoreCase("/fire")) return "Fire!";
		if(text.equalsIgnoreCase("/attack") || text.equalsIgnoreCase("/atk")) return "Attack!";
		return "Error! \""+ text + "\" not recognized try again.";
	}

//	public static void decisions (){
//		
//	}
//	
//	public static String characters(int numOfPlayers, ){
//		
//	}
//	
	
}