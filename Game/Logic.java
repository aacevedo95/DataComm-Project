package game;

import javax.swing.JTextField;

import server.Client;
import server.ClientGUI;
import server.ServerGUI;

public class Logic {

	/*
	 * Finds any commands in the chat and parses it, later invokes command and damage methods.
	 */
	public static void chatCommands(JTextField text, Client client){
		if(text.getText().charAt(0) == '/') {
			if(text.getText().equalsIgnoreCase("/hp") 
					|| text.getText().equalsIgnoreCase("/health")) 
				text.setText("Current health: "+ client.getHealth());
			else {
				if(command(text.getText()).charAt(0) == 'X')text.setText(command(text.getText()));
				else {
					client.setHealth(client.getHealth() - damage(text.getText()));
					text.setText(client.username + " casts " + command(text.getText()) + " It dealt "+  damage(text.getText())
					+" points of damage!!");
				}
			}
		}
	}

	/*
	 * Calculates damage, uses a 20 sided die as a damage multiplier.
	 */
	public static double damage(String command){
		int d20 = (int)(Math.random()*20+1);
		double multiplier = 0;
		double dmg = 0;
		if(d20 <= 5) multiplier = 0;
		else if(d20 >= 6 || d20 <= 15 ) multiplier = 1;
		else if (d20 >= 16) multiplier = 1.5;
		else if (d20 == 20) multiplier = 2;
		if(command.equalsIgnoreCase("/fire")) dmg = (multiplier*d20 + 4);  
		if(command.equalsIgnoreCase("/attack") || command.equalsIgnoreCase("/atk")) dmg = multiplier*d20 + 3;

		return dmg;
	}
	
	/*
	 * The commands the user can input.
	 */
	public static String command(String text) {
		if(text.equalsIgnoreCase("/fire")) return "Fire!";
		if(text.equalsIgnoreCase("/lightning")) return "Lightning!";
		if(text.equalsIgnoreCase("/Ice")) return "Ice!";
		if(text.equalsIgnoreCase("/attack") || text.equalsIgnoreCase("/atk")) return "Attack!";
		if(text.equalsIgnoreCase("/look left")) return "You see a lush green forrest.";
		if(text.equalsIgnoreCase("/look right")) return "You see an abandones house.";

		return "X-Error! \""+ text + "\" not recognized try again.";
	}
	
	/*
	 * Runs the program, calling the main methods in ClientGUI and ServerGUI 
	 * 
	 */
	public static void run(){
		ClientGUI.main(null);
		ClientGUI.main(null);
		ServerGUI.main(null);
	}

}