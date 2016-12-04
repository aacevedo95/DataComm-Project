package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client  {

	// input
	private ObjectInputStream input;		
	private ObjectOutputStream output;		
	private Socket socket;

	private ClientSideGUI clientGui;

	// the server, the port and the username
	private String server;
	public String username;
	private int port;
	public double health;

	/**
	 * Client's constructor.
	 * @param server
	 * @param port
	 * @param username
	 */
	Client(String server, int port, String username, ClientSideGUI cg, double health) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.clientGui = cg;
		this.health = health;
	}

	/*
	 * Sets the player health
	 */
	public void setHealth(double currentHealth){
		this.health = currentHealth;
	}

	/*
	 * Returns the player's health
	 */
	public double getHealth(){
		return this.health;
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		/* Creating both Data Stream */
		try
		{
			input  = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try
		{
			output.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		return true;
	}

	/*
	 * To send a message to the console or the GUI
	 */
	private void display(String msg) {
		clientGui.append(msg + "\n");
	}

	/*
	 * To send a message to the server
	 */
	void sendMessage(Message msg) {
		try {
			output.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}
	
	/*
	 * Check if the client just disconnected. If so, close input, output and socket.
	 */
	private void disconnect() {
		try { 
			if(input != null) input.close();
		}
		catch(Exception e) {} 
		try {
			if(output != null) output.close();
		}
		catch(Exception e) {} 
		try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} 

		if(clientGui != null)
			clientGui.connectionFailed();
	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) input.readObject();
					clientGui.append(msg);
				}
				catch(IOException e) {
					display("Server has close the connection: " + e);
					if(clientGui != null) 
						clientGui.connectionFailed();
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}
