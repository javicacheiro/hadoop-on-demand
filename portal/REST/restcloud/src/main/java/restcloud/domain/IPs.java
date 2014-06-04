package main.java.restcloud.domain;

public class IPs {
	// ** ATTRIBUTES ** //
	// **************** //
	private String user;
	private String[] ips;
	
	// ** CONSTRUCTORS ** //
	// ****************** //
	public IPs(){
		ips = new String[0];
	}
	
	public IPs(String user, String[] ips) {
		super();
		this.user = user;
		this.ips = ips;
	}
	
	// ** GETTERS n SETTERS ** //
	// *********************** //
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String[] getIps() {
		return ips;
	}

	public void setIps(String[] ips) {
		this.ips = ips;
	}
	
	public void addIp(String ip){
		String[] aux = new String[ips.length+1];
		
		for(int i = 0 ; i < ips.length ; i++){
			aux[i] = ips[i];
		}
		
		aux[aux.length-1] = ip;
		
		ips=aux;
	}

	
}
