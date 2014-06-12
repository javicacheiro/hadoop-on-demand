package main.java.restcloud.rest;

import java.io.File;
import java.io.PrintWriter;

import main.java.restcloud.db.DBOperations;
import main.java.restcloud.domain.Login;
import main.java.restcloud.domain.Message;

import org.joda.time.DateTime;

import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component @RestxResource
public class UserResource {
	// ** CONSTANTS ** //
	// *************** //
	public final static String LOGINS_FOLDER_PATH = "/home/cesga/albertoep/logins/";
	
	// ** REQUESTS ** //
	// ************** //

	@POST("/test")
	@PermitAll
    public Message test(Message msg) {
        return msg.setMessage(String.format(
                "%s @ %s",
                msg.getMessage(), DateTime.now().toString("HH:mm:ss")));
    }
	
	/**
	 * This method receives the corresponding login data and dump it to a file
	 * called one_auth
	 * @param login Login object containing user and pass
	 * @return If everything went OK return a Message which value is 'OK'.
	 * If something unexpected occurred return a Message with information relative
	 * to the exception.
	 */
	@POST("/users")
	@PermitAll
	public Message userRegister(Login login){
		try{
			dumpLoginToFile(LOGINS_FOLDER_PATH+login.getUser()+"/.one/",login);
			DBOperations.registerUserIfNotYet(login);
		}catch(Exception ex){
			return new Message().setMessage("FAIL\nException ocurred:\n"+ex.toString());
		}
		return new Message().setMessage("OK");
	}
	
	
	// ** METHODS ** //
	// ************* //
	/**
	 * This method check if the passed directory (as path through the String directory parameter)
	 * exists and if it doesn't then tries to create it.
	 * Once it is sure the directory exists (if not throws Exception) the next step is to create
	 * the file one_auth with the received login data.
	 * @param directory Directory's path where the file is going to be located
	 * @param login Data relative to the login (user and pass)
	 * @throws Exception UserResouce.public Message userRegister(Login login) understands
	 * how to manage the thrown exception
	 */
	private void dumpLoginToFile(String directory, Login login) throws Exception{
			// Directory
			File dir = new File(directory);
			if(!dir.exists())
				if(!dir.mkdirs())
					throw new Exception("Couldn't create directory to allocate login file.");
			
			// File
			//File file = new File(dir.getAbsolutePath()+File.separator+"login_"+login.getUser());
			File file = new File(dir.getAbsolutePath()+File.separator+"one_auth");
			
			// Output
			PrintWriter out = new PrintWriter(file);
			out.println(login.getUser()+":"+login.getPasswd());
			out.close();
	}
	
}
