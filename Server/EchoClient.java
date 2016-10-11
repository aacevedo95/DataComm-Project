package Server;

import java.io.*;
import java.net.*;

public class EchoClient {
	public static void main(String[] args) throws IOException {
		String hostName = "ANDY-PC";
		int port = 6980;
		String username;
		BufferedReader user =	new BufferedReader( new InputStreamReader(System.in));
		
		System.out.print("Enter username: ");
		username = user.readLine();
		
		//begin connection
		try (
				Socket echoSocket = new Socket("ANDY-PC", port);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in =	new BufferedReader( new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
				) {
			String userInput;		
			System.out.println("You are now in the chat!\n");
			
			
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				if( userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("log out"))
					System.exit(0);
				
				System.out.println(username +": " + in.readLine());
				System.out.println(Game.Logic.commands("Fire", username));
			}
			
			//error catching
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Connection to " +
					hostName +" has been terminated.");
			System.exit(1);
		} 
	}
	
	
}