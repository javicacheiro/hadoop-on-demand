package main.java.restcloud.domain;

public class SSHKeys {
	// ** ATTRIBUTES ** //
	// **************** //
	private String user;
	private String[] keys;
	
	// ** CONSTRUCTORS ** //
	// ****************** //
	public SSHKeys(){
		keys = new String[0];
	}
	
	public SSHKeys(String user, String[] keys) {
		super();
		this.user = user;
		this.keys = keys;
	}
	
	// ** GETTERS n SETTERS ** //
	// *********************** //
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	
	public void addKey(String key){
		String[] aux = new String[keys.length+1];
		
		for(int i = 0 ; i < keys.length ; i++){
			aux[i] = keys[i];
		}
		
		aux[aux.length-1] = key;
		
		keys=aux;
	}

	
}
