package server;

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
				System.out.println(username +": " + in.readLine());
				System.out.println(game.Logic.commands("Fire", username));
			}
			
			//error catching
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
			System.exit(1);
		} 
	}
}