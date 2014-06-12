package main.java.restcloud.rest;

import main.java.restcloud.Constants;
import main.java.restcloud.SSHKeyAddThread;
import main.java.restcloud.SSHKeyDelThread;
import main.java.restcloud.db.DBOperations;
import main.java.restcloud.domain.Message;
import main.java.restcloud.domain.SSHKey;
import main.java.restcloud.domain.SSHKeys;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component @RestxResource
public class KeyResource {

	// ** REQUESTS ** //
	// ************** //
	/**
	 * 
	 * @param key
	 * @return 	OK - Success
	 * 			ERROR - Error
	 */
	@POST("/key")
	@PermitAll
	public Message addSSHKey(final SSHKey key){
		try{
			
				DBOperations.insertSSHKey(key);
				SSHKeyAddThread skat = new SSHKeyAddThread(
						""+DBOperations.findIdUserByUsername(key.getUsername()));
				
				skat.start();
				skat.join();
				
			return new Message().setMessage("OK");
		}catch(Exception ex){
			ex.printStackTrace();
			return new Message().setMessage("ERROR");
		}
	}

	/**
	 * 
	 * @param key
	 * @return 	OK - Success
	 * 			ERROR - Error
	 */
	@DELETE("/key")
	@PermitAll
	public Message delKey(final SSHKey key){
		try{
			SSHKeyDelThread sshKeyDelThread = new SSHKeyDelThread(
					DBOperations.findIdUserByUsername(key.getUsername())+"",
					new String[]{key.getKeyLastFragment()});
			sshKeyDelThread.start();
			
			sshKeyDelThread.join();

			/*new Thread(){
				@Override
				public void run(){*/
					DBOperations.deleteSSHKey(key);
				/*}
			}.start();*/
			
			return new Message().setMessage("OK");
		}catch(Exception ex){
			ex.printStackTrace();
			return new Message().setMessage("ERROR");
		}
	}
	
	
	@DELETE("/keys")
	@PermitAll
	public Message multidelKey(final SSHKeys keys){
		try{
			String cmd = Constants.SSHKEY_MULTIDEL_PATH;
			for(String key : keys.getKeys()){
				cmd += " '"+key+"'";
			}
			
			/*Process p = */Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			
			new Thread(){
				@Override
				public void run(){
					for(String key : keys.getKeys()){
						DBOperations.deleteSSHKey(new SSHKey(keys.getUser(), key));
					}
				}
			}.start();
			
			return new Message().setMessage("OK");
		}catch(Exception ex){
			ex.printStackTrace();
			return new Message().setMessage("ERROR");
		}
		
	}
	
	@GET("/keys")
	@PermitAll
	public SSHKeys getKeysForUser(String user){
		SSHKeys sshkeys = DBOperations.getSSHKeysForUser(
				DBOperations.findIdUserByUsername(user)+"");
		sshkeys.setUser(user);
		return sshkeys;
	}
	
}
