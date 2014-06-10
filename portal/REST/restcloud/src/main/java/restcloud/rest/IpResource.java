package main.java.restcloud.rest;

import main.java.restcloud.IPAddThread;
import main.java.restcloud.IPDelThread;
import main.java.restcloud.db.DBOperations;
import main.java.restcloud.domain.IP;
import main.java.restcloud.domain.IPs;
import main.java.restcloud.domain.Message;
import main.java.restcloud.domain.SSHKeys;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component @RestxResource
public class IpResource {
	// ** REQUESTS ** //
		// ************** //
		/**
		 * 
		 * @param ip
		 * @return 	OK - Success
		 * 			ERROR - Error
		 */
		@POST("/ip")
		@PermitAll
		public Message addIp(final IP ip){
			try{
				
					DBOperations.insertIP(ip);
					IPAddThread ipat = new IPAddThread(
							""+DBOperations.findIdUserByUsername(ip.getUsername()));
					
					ipat.start();
					ipat.join();
					
				return new Message().setMessage("OK");
			}catch(Exception ex){
				ex.printStackTrace();
				return new Message().setMessage("ERROR");
			}
		}

		/**
		 * 
		 * @param ip
		 * @return 	OK - Success
		 * 			ERROR - Error
		 */
		@DELETE("/ip")
		@PermitAll
		public Message delIp(final IP ip){
			try{
				IPDelThread ipDelThread = new IPDelThread(
						DBOperations.findIdUserByUsername(ip.getUsername())+"",
						new String[]{ip.getIP()});
				ipDelThread.start();
				
				ipDelThread.join();
				
				DBOperations.deleteIP(ip);
				
				return new Message().setMessage("OK");
			}catch(Exception ex){
				ex.printStackTrace();
				return new Message().setMessage("ERROR");
			}
		}
		
		
		@GET("/ip")
		@PermitAll
		public IPs getIpsForUser(String user){
			String arr[] = DBOperations.getIpsForUser(
					""+DBOperations.findIdUserByUsername(user));
			
			IPs ips = new IPs(user, arr); 
			return ips;
		}
}
