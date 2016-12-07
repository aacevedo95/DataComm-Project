package server;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * The server as a GUI
 */
public class ServerSideGUI extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	private JButton stopStart;
	private JTextArea chat, event;
	private JTextField tPortNumber;
	private Server server;


	// server constructor that receive the port to listen to for connection as parameter
	ServerSideGUI(int port) {
		super("Chat Server");
		server = null;
		JPanel north = new JPanel();
		JPanel center = new JPanel(new GridLayout(2,1));
		
		north.add(new JLabel("Port number: "));
		tPortNumber = new JTextField("  " + port);
		north.add(tPortNumber);
		stopStart = new JButton("Start");
		stopStart.addActionListener(this);
		north.add(stopStart);
		add(north, BorderLayout.NORTH);
		chat = new JTextArea(80,80);
		chat.setEditable(false);
		updateRoomMsg("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(80,80);
		event.setEditable(false);
		updateEventMsg("Events log.\n");
		
		center.add(new JScrollPane(event));	
		add(center);
		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}		

	void updateRoomMsg(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length());
	}
	void updateEventMsg(String str) {
		event.append(str);
		event.setCaretPosition(chat.getText().length());
	}

	// start or stop where clicked
	public void actionPerformed(ActionEvent e) {
		if(server != null) {
			server.stop();
			server = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception er) {
			updateEventMsg("Invalid port number");
			return;
		}
		
		server = new Server(port, this);
		new ServerRunning().start();
		stopStart.setText("Stop");
		tPortNumber.setEditable(false);
	}

	// entry point to start the Server
	public static void main(String[] arg) {
		new ServerSideGUI(1500);
	}

	/*
	 * If the user click the X button to close the application
	 * I need to close the connection with the server to free the port
	 */
	public void windowClosing(WindowEvent e) {
		// if my Server exist
		if(server != null) {
			try {
				server.stop();
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		// dispose the frame
		dispose();
		System.exit(0);
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	private static InetAddress getLocalAddress(){
		try
		{
			Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
			while (b.hasMoreElements())
			{
				for (InterfaceAddress f : b.nextElement().getInterfaceAddresses())
					if (f.getAddress().isSiteLocalAddress())
						return f.getAddress();
			}
		}
		catch (SocketException e)
		{
			System.out.println("Error getting local address:");
			System.out.println(e.getMessage());
		}
		return null;
	}

	/*
	 * A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			System.out.println(getLocalAddress());
			server.start();         
			
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			updateEventMsg("Server crashed\n");
			server = null;
		}
	}

}
