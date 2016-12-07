package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.Message;
import game.Logic;


/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	private static int uniqueId;
	private ArrayList<ClientThread> usersList;
	private ServerSideGUI serverGui;
	private int port;
	private boolean isOnline;
	
	/**
	 * The server's no arg constructor.
	 * @param port
	 */
	public Server(int port) {
		this(port, null);
	}

	/**
	 * The server's constructor.
	 * @param port
	 * @param serverGui
	 */
	public Server(int port, ServerSideGUI serverGui) {
		this.serverGui = serverGui;
		this.port = port;
		usersList = new ArrayList<ClientThread>();
	}

	/**
	 * Start server.
	 */
	public void start() {
		isOnline = true;
		// create the server
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				Socket socket = serverSocket.accept(); 
				
				if(!isOnline)
					break;
				ClientThread client = new ClientThread(socket);  
				usersList.add(client);	
				client.start();
			}
			
			try {
				serverSocket.close();
				for(int i = 0; i < usersList.size(); ++i) {
					ClientThread thread = usersList.get(i);
					try {
						thread.input.close();
						thread.output.close();
						thread.socket.close();
					}
					catch(IOException err) {
						System.out.print(err);
 					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException err) {
			display("Exception on new ServerSocket: " + err + "\n");
		}
	}
	
	/**
	 * Stop server.
	 */
	@SuppressWarnings("resource")
	protected void stop() {
		isOnline = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
		}
	}
	
	/**
	 * Displays an event to the chat.
	 * @param msg
	 */
	private void display(String msg) {
		serverGui.updateEventMsg(msg + "\n");
	}
	
	/**
	 * Sends a broadcast to the chat
	 * @param message
	 */
	private void broadcast(String message) {
		String messageLf = message + "\n";
		if(serverGui == null)
			System.out.print(messageLf);
		else
			serverGui.updateRoomMsg(messageLf);   

		for(int i = usersList.size(); --i >= 0;) {
			ClientThread ct = usersList.get(i);
			if(!ct.writeMsg(messageLf)) {
				usersList.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	/**
	 * Synchronized method used to minimize thread errors.
	 * Logs off selected user.
	 * @param id The user's id.
	 */
	synchronized void remove(int id) {
		for(int i = 0; i < usersList.size(); ++i) {
			ClientThread client = usersList.get(i);
			if(client.id == id) {
				usersList.remove(i);
				return;
			}
		}
	}
	
//	synchronized void damageUser(double damage, Client client) {
//		for(int i = 0; i < usersList.size(); ++i) {
//			ClientThread clientThread = usersList.get(i);
//			if(clientThread.username == client.username) {
//				client.setHealth(client.getHealth() - damage);
//			}
//		}
//	}

	//runs the server
	public static void main(String[] args) {
		int portNumber = 1500;
		Server server = new Server(portNumber);
		server.start();
	}

	/** One instance of this thread will run for each client */
	public class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream input;
		ObjectOutputStream output;
		int id, newID;
		String username;
		Message msg;
		double health;

		/**
		 * ClientThread's constructor.
		 * @param socket
		 */
		ClientThread(Socket socket) {
			//Increment id for each user.
			id = ++uniqueId;
			health = 100;
			this.socket = socket;
			System.out.println("Thread trying to create Object Input/Output Streams");
			
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input  = new ObjectInputStream(socket.getInputStream());
				username = (String) input.readObject();
				display(username + " just connected." + "your ID is: " + id);
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		}
		
		public void setHealth(double currentHealth){
			this.health = currentHealth;
		}

		public double getHealth(){
			return this.health;
		}	

		public void run() {
			boolean keepGoing = true;
			while(keepGoing) {
				try {
					msg = (Message) input.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				String message = msg.getMessage();

				switch(msg.getType()) {
				case Message.MESSAGE:
					if(message.charAt(0) == '/'){
						message = Logic.command(message, usersList, username);
					}
					broadcast(username + ": " + message);
					break;
					
				case Message.LOGOUT:
					display(username + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
					
				case Message.WHO:
					writeMsg("List of the users connected\n");
					for(int i = 0; i < usersList.size(); ++i) {
						ClientThread thread = usersList.get(i);
						writeMsg((i+1) + ") " + thread.username);
					}
					break;
				}
			}
			remove(id);
			close();
		}
		


		
		/**
		 * Closes up the server.
		 */
		private void close() {
			try {
				if(output != null) output.close();
			}
			catch(Exception e) {}
			try {
				if(input != null) input.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		
		private boolean writeMsg(String msg) {
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				output.writeObject(msg);
			}
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}


