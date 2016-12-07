package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Game.Logic;
import javafx.scene.text.FontBuilder;
import sun.font.FontFamily;
import com.sun.javafx.font.FontFactory;


/*
 * The Client with its GUI
 */
public class ClientSideGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField tf;
	private JTextField tfServer, tfPort;
	private JButton login, logout, whoIsIn;
	private JTextArea ta;
	private boolean connected;
	private Client client;
	private int defaultPort;
	private String defaultHost;
	private JLabel header;

	// Constructor connection receiving a socket number
	ClientSideGUI(String host, int port) {
		super("Chat Client");
		defaultPort = port;
		defaultHost = host;
		JPanel southPanel = new JPanel();
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		
		Font font = new Font("Arial", Font.BOLD,12);
		
		
	
		
		ta = new JTextArea("\t\tWelcome to Dungeons & Sockets\n" + "\t                You may think this is a normal chat, but it isn't.\n \t     These are are some of the commads to get you started\n" +
		"\t\t\t/fire\n\t\t\t/lightning\n\t\t\t/ice\n\t\t\t/attack\n \tYou can check your enemies health by typing /hp. Good Luck!", 80, 80);
		
		ta.setFont(font);
		
		
		
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		
		JScrollPane scrollPane = new JScrollPane(ta);
		centerPanel.add(scrollPane);
		ta.setEditable(false);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		
		whoIsIn = new JButton("Who is in");
		whoIsIn.addActionListener(this);
		whoIsIn.setEnabled(true);
		
		header = new JLabel("DUNGEONS & SOCKETS", SwingConstants.CENTER);
		header.setFont(new Font("Zapf Dingbats", Font.BOLD, 40));
		getContentPane().add(header, BorderLayout.NORTH);

		
		southPanel.add(northPanel);
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		northPanel.add(serverAndPort);

		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		northPanel.add(tf);
		tf.setBackground(Color.WHITE);
		tf.requestFocus();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
//		ImageIcon image = new ImageIcon("C:/Users/PapayaCarlos/Desktop/Dragon_Header.png");
//		JLabel imagelabel = new JLabel(image);
//		southPanel.set(imagelabel);
		
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
	}

	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Anonymous");
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		tf.removeActionListener(this);
		connected = false;
	}

	/*
	 * Button or JTextField clicked
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(new Message(Message.LOGOUT, ""));
			return;
		}
		// if it the who is in button
		if(o == whoIsIn) {
			client.sendMessage(new Message(Message.WHO, ""));				
			return;
		}
		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
			client.sendMessage(new Message(Message.MESSAGE, tf.getText()));				
			tf.setText("");
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port number, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;  
			}

			client = new Client(server, port, username, this);
			if(!client.start()) 
				return;
			tf.setText("");
			label.setText("Enter your message below");
			connected = true;

			login.setEnabled(false);
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			tf.addActionListener(this);
		}

	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientSideGUI("localhost", 1500);

	}


}
