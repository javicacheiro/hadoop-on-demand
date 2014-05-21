package main.java.restcloud;

import main.java.restcloud.domain.ClusterList;
import main.java.restcloud.domain.HadoopCluster;
import main.java.restcloud.domain.VirtualMachine;
import main.java.restcloud.rest.UserResource;

/**
 * Contains util static methods
 * 
 * @author albertoep
 */
public class Utils {
	/**
	 * Generates the exporting sentence for ONE_AUTH corresponding to the given user.
	 * @param user
	 * @return
	 */
	public static String generateExport(String user) {
		return "export ONE_AUTH=" + UserResource.LOGINS_FOLDER_PATH
				+ user + "/.one/one_auth";
	}
	
	/**
	 * Forces the first CPU of each array of virtual machines for each cluster to have 2 as value
	 * and all the next to have 1
	 * @param cl
	 */
	public static void giveStaticCPUValueToNodes(ClusterList cl){
		boolean isFirst = true;
		
		for(HadoopCluster c : cl.getClusters()){
			isFirst = true;
			for(VirtualMachine vm : c.getVms()){
				if(isFirst){
					vm.setUcpu((short)2);
					isFirst = false;
				}else
					vm.setUcpu((short)1);
			}
		}
	}

}
