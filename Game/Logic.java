package game;

import java.util.ArrayList;

import client.ClientSideGUI;
import server.Server.ClientThread;
import server.ServerSideGUI;

public class Logic {

	/*
	 * Calculates damage, uses a 20 sided die as a damage multiplier.
	 */
	public static double damage(String command){
		//20 Sided die, used to calculate multiplier.
		int d20 = (int)(Math.random()*20+1);
		double multiplier = 0;
		double dmg = 0;
		//Multiplier 
		if(d20 <= 5) multiplier = 0;
		else if(d20 >= 6 || d20 <= 15 ) multiplier = 1;
		else if (d20 >= 16) multiplier = 1.5;
		else if (d20 == 20) multiplier = 2;

		switch(command){
		case "/fire":
			dmg = (multiplier*d20 + 4);  
			break;
		case "/lightning":
			dmg = (multiplier*d20 + 3);  
			break;
		case "/ice":
			dmg = (multiplier*d20 + 2);  
			break;
		case "/attack":
			dmg = multiplier*d20 + 1;
			break;
		}
		return dmg;
	}

	/*
	 * The commands the user can input.
	 */
	public static String command(String text, ArrayList<ClientThread> client, String username) {
		double dmg;
		String result;
		String[] param = text.split(" ");
		int id = 1;
		if(param.length == 2) 
			id = Integer.parseInt(param[1]);
		else if(id > param.length || id == 0)
			return "Error! Invalid ID entered, try again!";

		
		switch(param[0]){
		//Add chance that rolls d20
		case "/hp":
			result = "Current health: " +  client.get(id).getHealth() + " of " + username;
			break;
		case "/fire":
			dmg = damage(param[0]);
			//client.get(id).setHealth(client.get(id).getHealth() - dmg );
			result =  " casts fire, it dealt " + dmg +" damage to " + username;
			break;
		case "/lightning":
			dmg = damage(param[0]);
			//client.get(id).setHealth(client.get(id).getHealth() - dmg );
			result =  " casts lightning, it dealt " + dmg+" damage!";
			break;
		case "/ice":
			dmg = damage(param[0]);
			//client.get(id).setHealth(client.get(id).getHealth() - dmg );
			result =  " casts ice, it dealt " + dmg+" damage!";
			break;
		case "/attack":
			dmg = damage(param[0]);
			//client.get(id).setHealth(client.get(id).getHealth() - dmg );
			result = " casts attack, it dealt " + dmg+" damage!";
			break;
		case "/d20":
			int d20 = (int)(Math.random()*20+1);
			result = "You roll a d20 and get a " + d20;
		default:
			result = "Error! \""+ text + "\" not recognized try again.";
			break;
		}
		return result;
	}

	/*
	 * Runs the program, calling the main methods in ClientGUI and ServerGUI 
	 * 
	 */
	public static void run(){
		ClientSideGUI.main(null);
		ClientSideGUI.main(null);
		ClientSideGUI.main(null);
		ServerSideGUI.main(null);
	}

}