package main.java.restcloud;

import main.java.restcloud.db.DBOperations;

/**
 * This thread takes care of synchronizing the database keys with the active
 * nodes of the given user adding those keys inside database to authorized_keys
 * 
 * @author albertoep
 * 
 */
public class SSHKeyAddThread extends OperatorThread {
	// ** CONSTRUCTOR ** //
	// ***************** //
	public SSHKeyAddThread(String userId) {
		super(userId);
	}

	// ** INTERNAL METHODS ** //
	// ********************** //
	@Override
	void operations(String ip, String nodeId) {
		String[] keys = DBOperations.getKeysForUser(userId);
		for (String key : keys) {
			try {
				String cmdHadoop = "ssh hadoop@" + ip + " \""
						+ Constants.SSHKEY_ADD_PATH + " '" + key + "'\"";
				Process pHadoop = Runtime.getRuntime().exec(
						new String[] { "/bin/sh", "-c", cmdHadoop });
				
				String cmdRoot = "ssh root@" + ip + " \""
						+ Constants.SSHKEY_ADD_PATH + " '" + key + "'\"";
				Process pRoot = Runtime.getRuntime().exec(
						new String[] { "/bin/sh", "-c", cmdRoot });
				
				pHadoop.waitFor();
				pRoot.waitFor();
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
