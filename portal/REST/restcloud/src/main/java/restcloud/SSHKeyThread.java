package main.java.restcloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.java.restcloud.db.DBOperations;

public abstract class SSHKeyThread extends Thread{
	// ** ATTRIBUTES ** //
	// **************** //
	String userId;
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public SSHKeyThread(String userId){
		this.userId = userId;
	}
	
	// ** BEHAVIOR ** //
	// ************** //
	@Override
	public void run(){
		String nodesId[] = DBOperations.getActiveNodesIdForUser(userId);
		
		ArrayList<Thread> threads = new ArrayList<Thread>(0);
		
		for(String nodeId : nodesId){
			final String ip = obtainNodeIp(nodeId);
			Thread t = new Thread(){
				@Override
				public void run(){
					keysOperations(ip);
				}
			};
			threads.add(t);
			t.start();
		}
		
		// Se puede comentar la seccion de codigo que sigue pero entonces
		// pueden producirse inconsistencias de informacion en el lado del
		// portal tal y como esta realizado a fecha de
		// 			2014 05 26
		for(Thread t : threads){
			try{
				t.join();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	// ** INTERNAL METHODS ** //
	// ********************** //
	private String obtainNodeIp(String nodeId){
		try{
			String cmd = Utils.generateExport(DBOperations.getUsernameByUserid(userId))
					+" && oneip "+ nodeId;
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));			
			if(in.ready())
				return in.readLine();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Must be overridden with the actions which desire to perform related with the keys
	 * for each node.
	 * @param ip IP address of the current node
	 */
	void keysOperations(String ip){
		
	}
	

}
