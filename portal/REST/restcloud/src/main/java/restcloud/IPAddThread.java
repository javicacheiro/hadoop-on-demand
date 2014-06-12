package main.java.restcloud;

import main.java.restcloud.db.DBOperations;

/**
 * This thread takes care of synchronizing the database ips with the active
 * nodes of the given user adding those ips inside database to iptables
 * 
 * @author albertoep
 * 
 */
public class IPAddThread extends OperatorThread {
	// ** CONSTRUCTOR ** //
	// ***************** //
	public IPAddThread(String userId) {
		super(userId);
	}

	// ** INTERNAL METHODS ** //
	// ********************** //
	@Override
	void operations(String ip, String nodeId) {
		if(DBOperations.isMasterNode(nodeId)){
			String[] ips = DBOperations.getIpsForUser(userId);
			for (String ipToAdd: ips) {
				try {
					String cmd = "ssh root@" + ip + " \""
							+ Constants.IP_ADD_PATH + " '" + ipToAdd + "'\"";
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
