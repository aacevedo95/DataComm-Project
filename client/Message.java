package client;
import java.io.*;

public class Message implements Serializable {

	protected static final long serialVersionUID = 1112122200L;
	public static final int WHO = 0;
	public static final int MESSAGE = 1;
	public static final int LOGOUT = 2;
	private int type;
	private String message;
	
	// constructor
	Message(int type, String message) {
		this.type = type;
		this.message = message;
	}

	// getters
	public int getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
