package main.java.restcloud.domain;

public class IP {
	// ** ATTRIBUTES ** //
	// **************** //
	private String username;
	private String IP;
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public IP(){}

	public IP(String username, String iP) {
		super();
		this.username = username;
		IP = iP;
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}
	
	
	
	
}
