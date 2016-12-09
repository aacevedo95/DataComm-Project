package server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
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
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		getContentPane().setForeground(Color.WHITE);
		getContentPane().setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		server = null;
		JPanel north = new JPanel();
		north.setToolTipText("");
		north.setForeground(Color.WHITE);
		north.setBackground(Color.BLACK);
		JPanel center = new JPanel(new GridLayout(1,2));
		
		JLabel label = new JLabel("Port number: ");
		label.setForeground(Color.WHITE);
		north.add(label);
		tPortNumber = new JTextField("  " + port);
		north.add(tPortNumber);
		stopStart = new JButton("Start");
		stopStart.addActionListener(this);
		north.add(stopStart);
		getContentPane().add(north, BorderLayout.NORTH);
		chat = new JTextArea(50,50);
		chat.setBackground(Color.BLACK);
		chat.setLineWrap(true);
		chat.setForeground(Color.WHITE);
		chat.setEditable(false);
		updateRoomMsg("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(10,10);
		event.setForeground(Color.WHITE);
		event.setLineWrap(true);
		event.setBackground(Color.BLACK);
		event.setEditable(false);
		updateEventMsg("Events log.\n");
		
		center.add(new JScrollPane(event));	
		getContentPane().add(center, BorderLayout.EAST);
		addWindowListener(this);
		setSize(1129, 311);
		ImageIcon image = new ImageIcon("C:/Users/PapayaCarlos/Desktop/Dragon_Header.png");
		JLabel imagelabel = new JLabel(image);
		add(imagelabel, BorderLayout.SOUTH);
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



	/*
	 * A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			
			server.start();         
			
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			updateEventMsg("Server crashed\n");
			server = null;
		}
	}

}
