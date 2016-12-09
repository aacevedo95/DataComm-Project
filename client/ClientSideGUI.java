package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



/*
 * The Client with its GUI
 */
public class ClientSideGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField tf;
	private JTextField tfServer, tfPort;
	private JButton login, logout;
	private JTextArea ta;
	private boolean connected;
	private Client client;
	private int defaultPort;
	private String defaultHost;
	private JLabel header;
	private JLabel header_1;

	// Constructor connection receiving a socket number
	ClientSideGUI(String host, int port) {
		super("Chat Client");
		getContentPane().setBackground(Color.BLACK);
		getContentPane().setForeground(Color.WHITE);
		setForeground(Color.WHITE);
		defaultPort = port;
		defaultHost = host;
		JPanel southPanel = new JPanel();
		southPanel.setForeground(Color.WHITE);
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		Font font = new Font("Arial", Font.BOLD,12);
	
		ta = new JTextArea("\t\t\t\t\tWelcome to Dungeons & Sockets\r\n\t            \t\t\t                 You may think this is a normal chat, but it isn't.\r\n \t     \t\t\t      These are are some of the commads to get you started\r\n\t\t\t\t\t\t/fire\r\n\t\t\t\t\t\t/lightning\r\n\t\t\t\t\t\t/ice\r\n\t\t\t\t\t\t/attack\r\n \t\t\t\tYou can check your own health by typing /hp. Good Luck!\r\n\r\n", 80, 80);
		ta.setForeground(Color.RED);
		ta.setBackground(Color.BLACK);
		ta.setFont(font);
		
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.setForeground(Color.WHITE);
		centerPanel.setBackground(Color.BLACK);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(ta);
		centerPanel.add(scrollPane);
		ta.setEditable(false);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		login = new JButton("Login");
		login.setForeground(Color.GRAY);
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);
		
		header = new JLabel("DUNGEONS & SOCKETS", SwingConstants.CENTER);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Zapf Dingbats", Font.BOLD, 40));
		getContentPane().add(header, BorderLayout.NORTH);
		header_1 = new JLabel("DUNGEONS && SOCKETS", SwingConstants.CENTER);
		header_1.setForeground(Color.WHITE);
		header_1.setFont(new Font("Arial", Font.BOLD, 50));
		getContentPane().add(header_1, BorderLayout.NORTH);

		southPanel.add(northPanel);
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		serverAndPort.setBackground(Color.BLACK);
		serverAndPort.setForeground(Color.WHITE);
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel label_1 = new JLabel("Server Address:  ");
		label_1.setForeground(Color.WHITE);
		serverAndPort.add(label_1);
		serverAndPort.add(tfServer);
		JLabel label_2 = new JLabel("Port Number:  ");
		label_2.setForeground(Color.WHITE);
		serverAndPort.add(label_2);
		serverAndPort.add(tfPort);
		JLabel label_3 = new JLabel("");
		label_3.setForeground(Color.WHITE);
		serverAndPort.add(label_3);
		northPanel.add(serverAndPort);

		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setForeground(new Color(0, 0, 0));
		northPanel.add(tf);
		tf.setBackground(Color.WHITE);
		tf.requestFocus();
		southPanel.add(login);
		southPanel.add(logout);
		northPanel.setBackground(Color.BLACK);
		southPanel.setBackground(Color.BLACK);
		
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(996, 640);
		
		ImageIcon image = new ImageIcon("C:/Users/PapayaCarlos/Desktop/25444-qusymik6.jpg");
		JLabel imagelabel = new JLabel(image);
		southPanel.add(imagelabel);
		setVisible(true);
	}

	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		
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
		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message		
			client.sendMessage(new Message(Message.MESSAGE, tf.getText()));
			tf.setText("");
			return;
		}

		if(o == login) {
			String username = tf.getText().trim();
			if(username.length() == 0)
				return;
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
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
