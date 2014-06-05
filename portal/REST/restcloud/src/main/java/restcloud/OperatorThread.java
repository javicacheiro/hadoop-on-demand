package main.java.restcloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.java.restcloud.db.DBOperations;

public abstract class OperatorThread extends Thread{
	// ** ATTRIBUTES ** //
	// **************** //
	String userId;
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public OperatorThread(String userId){
		this.userId = userId;
	}
	
	// ** BEHAVIOR ** //
	// ************** //
	@Override
	public void run(){
		try{
			String nodesId[] = DBOperations.getActiveNodesIdForUser(userId);
			
			ArrayList<Thread> threads = new ArrayList<Thread>(0);
			
			// OBTENIENDO IDS DE TODOS LOS NODOS DEL USUARIO --
			String username = DBOperations.getUsernameByUserid(userId);
			String cmd = Utils.generateExport(username)
				+" && onevm list | awk 'NR>1{print $1}'";
			
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			ArrayList<String> ids = new ArrayList<String>();
			while(in.ready())
				ids.add(in.readLine());
			// -- OBTENIDOS //
			
			for(final String nodeId : nodesId){
				if(Utils.stringIsInArray(nodeId,ids.toArray(new String[0]))){
					final String ip = obtainNodeIp(nodeId);
					Thread t = new Thread(){
						@Override
						public void run(){
							operations(ip,nodeId);
						}
					};
					threads.add(t);
					t.start();
				}
			}
		
			
			// Se puede comentar la seccion de codigo que sigue pero entonces
			// pueden producirse inconsistencias de informacion en el lado del
			// portal tal y como esta realizado a fecha de
			// 			2014 05 26
			for(Thread t : threads){
				try{
					while(t.isAlive()){
						sleep(20);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
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
	 * Must be overridden with the actions which desire to perform for each node.
	 * @param ip IP address of the current node
	 */
	void operations(String ip, String nodeId){
		
	}
	

}
