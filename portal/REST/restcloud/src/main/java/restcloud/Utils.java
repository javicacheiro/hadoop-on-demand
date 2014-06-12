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
	
	public static boolean stringIsInArray(String string, String array[]){
		try{
			for(String str: array){
				if(str.equals(string))
					return true;
			}
			return false;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public static int countUserClusters(String username){
		String onevmList[] = doCountUserClustersOnevmList(username);
		if(onevmList == null)
			return 0;

		ArrayList<String> clusterNames = new ArrayList<String>(0);
		
		for(String str : onevmList){
			String splitted[] = str.split(" ");
			String clusterName = splitted[4].substring(0,splitted[4].lastIndexOf("-"));
			
			if(clusterNames.indexOf(clusterName)==-1) // Si no esta en el ArrayList
				clusterNames.add(clusterName);
		}
		
		return clusterNames.size();
	}
	
	public static String[] doCountUserClustersOnevmList(String username){
		try{
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",
					generateExport(username)+" && onevm list | tail -n +2 |tr -s ' '"});
			p.waitFor();
			
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			while(br.ready())
				lines.add(br.readLine());
			
			if(lines.size()==1 && lines.get(0).length()<=1)
				return null;
			return lines.toArray(new String[0]);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

}
