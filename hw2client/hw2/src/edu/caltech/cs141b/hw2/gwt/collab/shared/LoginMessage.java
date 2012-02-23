package edu.caltech.cs141b.hw2.gwt.collab.shared;


/**
 * Information about login message
 */
public class LoginMessage extends Message {
	private String user;
	private String status;
	
	@SuppressWarnings({"UnusedDeclaration"})
	// For GWT RPC
	private LoginMessage() {
		super(Type.LOGIN_MSG);
	}
	
	public LoginMessage(String user, String status) {
		super(Type.LOGIN_MSG);
		this.user = user;
		this.status = status;
	}

	public String getUser() {
		return user;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String toString() {
		return "LOGIN_MSG/" + status;
	}
  
}
