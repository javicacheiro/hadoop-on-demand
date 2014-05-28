package main.java.restcloud.domain;

public class SSHKey {
	// ** CONSTANTS ** //
	// *************** //
	public final static int LAST_FRAGMENT_CHAR_LENGTH = 10;
	
	// ** ATTRIBUTES ** //
	// **************** //
	private String username;
	private String key;
	
	// ** CONSTRUCTORS ** //
	// ****************** //
	public SSHKey(){}
	
	public SSHKey(String username, String key) {
		this.username = username;
		this.key = key;
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKeyLastFragment(){
		try{
			String keyHead = key.substring(0,7);
			
			if(keyHead.equals("ssh-dss")){
				return key.substring(576,586);
			}else if(keyHead.equals("ssh-rsa")){
				return key.substring(370,380);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}

	// ** toString ** //
	@Override
	public String toString() {
		return "SSHKey [username=" + username + ", key=" + key + "]";
	};
	
	
	
}
