package main.java.restcloud;

import main.java.restcloud.db.DBOperations;


/**
 * This thread takes care of synchronizing the database keys with the active nodes
 * of the given user deleting the given sshkeys
 * @author albertoep
 *
 */
public class IPDelThread extends OperatorThread{
	// ** ATTRIBUTES ** //
	// **************** //
	private String ips[];
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public IPDelThread(String userId, String ips[]){
		super(userId);
		this.ips = ips;
	}
	
	// ** INTERNAL METHODS ** //
	// ********************** //
	@Override
	void operations(String ip, String nodeId) {
		if(DBOperations.isMasterNode(nodeId)){
			for (String ipToDel : ips) {
				try {
					
					String cmd = "ssh root@" + ip + " \""
							+ Constants.IP_DEL_PATH + " '" + ipToDel + "'\"";
					Process p = Runtime.getRuntime().exec(
							new String[] { "/bin/sh", "-c", cmd });
					
					p.waitFor();
					//System.out.println(cmd);
					/*
					 * Hay que esperar a que acabe el proceso antes de ir a por la
					 * siguiente iteracion porque se esta escribiendo en un archivo
					 * y eso se considera seccion critica. No hacerlo podria dar
					 * lugar a inconsistencias.
					 */
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
