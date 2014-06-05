package main.java.restcloud;


/**
 * This thread takes care of synchronizing the database keys with the active nodes
 * of the given user deleting the given sshkeys
 * @author albertoep
 *
 */
public class SSHKeyDelThread extends OperatorThread{
	// ** ATTRIBUTES ** //
	// **************** //
	private String keys[];
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public SSHKeyDelThread(String userId, String keys[]){
		super(userId);
		this.keys = keys;
	}
	
	// ** INTERNAL METHODS ** //
	// ********************** //
	@Override
	void operations(String ip, String nodeId) {
		for (String key : keys) {
			try {
				String cmdHadoop = "ssh hadoop@" + ip + " \""
						+ Constants.SSHKEY_DEL_PATH + " '" + key + "'\"";
				Process pHadoop = Runtime.getRuntime().exec(
						new String[] { "/bin/sh", "-c", cmdHadoop });
				
				String cmdRoot = "ssh root@" + ip + " \""
						+ Constants.SSHKEY_DEL_PATH + " '" + key + "'\"";
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
