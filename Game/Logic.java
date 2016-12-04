package game;

import javax.swing.JTextField;

import client.Client;
import client.ClientSideGUI;
import server.ServerSideGUI;

public class Logic {

	/*
	 * Finds any commands in the chat and parses it, later invokes command and damage methods.
	 */
	public static void chatCommands(JTextField text, Client client){
		//Initial if, checks if text is a command.
		if(text.getText().charAt(0) == '/') {
			//Returns your chosen command formatted nicely.
			text.setText(client.username +" " + command(text.getText(), client));
		}
	}

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
	public static String command(String text, Client client) {
		double dmg;
		String result;
		switch(text){
		default:
			result = "X-Error! \""+ text + "\" not recognized try again.";
			break;
		case "/hp":
			result = "Current health: "+ client.getHealth();
			break;
		case "/fire":
			dmg = damage(text);
			client.setHealth(client.getHealth() - dmg);
			result =  " casts fire, it dealt " + dmg+" damage!";
			break;
		case "/lightning":
			dmg = damage(text);
			client.setHealth(client.getHealth() - dmg);
			result =  " casts lightning, it dealt " + dmg+" damage!";
			break;
		case "/ice":
			dmg = damage(text);
			client.setHealth(client.getHealth() - dmg);
			result =  " casts ice, it dealt " + dmg+" damage!";
			break;
		case "/attack":
			dmg = damage(text);
			client.setHealth(client.getHealth() - dmg);
			result = " casts attack, it dealt " + dmg+" damage!";
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
		ServerSideGUI.main(null);
	}

}