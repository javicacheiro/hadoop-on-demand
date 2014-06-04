package main.java.restcloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.java.restcloud.db.DBOperations;
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
	
	public static boolean nodeIdIsOnOnevmList(String nodeId, String userId){
		try{
			String username = DBOperations.getUsernameByUserid(userId);
			String cmd = Utils.generateExport(username)
				+" && onevm list | awk 'NR>1{print $1}'";
			
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			ArrayList<String> ids = new ArrayList<String>();
			while(in.ready())
				ids.add(in.readLine());
			
			for(String id: ids){
				if(id.equals(nodeId))
					return true;
			}
			
			return false;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

}
