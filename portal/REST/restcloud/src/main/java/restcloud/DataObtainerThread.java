package main.java.restcloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.java.restcloud.db.DBOperations;
import main.java.restcloud.domain.ClusterList;
import main.java.restcloud.domain.HadoopStartRequest;

public class DataObtainerThread extends Thread {
	// ** ATTRIBUTES ** //
	// **************** //
	private Process process;
	private String clusterId;
	private HadoopStartRequest hsr;
	
	// ** CONSTRUCTOR ** //
	// ***************** //
	public DataObtainerThread(Process process, String clusterId, HadoopStartRequest hsr){
		this.process = process;
		this.clusterId = clusterId;
		this.hsr = hsr;
	}
	
	// ** BEHAVIOR ** //
	// ************** //
	@Override
	public void run (){
		try{
			DBOperations.insertHadoopStartRequestInfo(clusterId, hsr);
			
			int exitValue = process.waitFor();
			
			DBOperations.setClusterExitStatus(clusterId, exitValue);
			
			switch(exitValue){
				case 0:
					onSuccess(exitValue);
					break;
				default:
					System.out.println("DataObtainerThread process exit value was different than 0");
					break;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	// ** INTERNAL METHODS ** //
	// ********************** //
	
	// ** onSuccess means exit value was 0 ** //
	private void onSuccess(int exitValue){
		ClusterList cl = obtainClusterList();
		DBOperations.insertInfoIntoDB(cl);
		
		String userId = ""+DBOperations.findIdUserByUsername(hsr.getUser());
		
		// Synchronize keys
		new SSHKeyAddThread(userId).start();
		
		// Syncrhonize ips
		new IPAddThread(userId).start();
	}
	
	private ClusterList obtainClusterList(){
		try{
			ClusterList cl = new ClusterList();
			String cmd = Utils.generateExport(hsr.getUser()) + " && onevm list";
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			
			ArrayList<String> onevmListLines = new ArrayList<String>();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while(in.ready())
				onevmListLines.add(in.readLine());
			cl.parseOnevmListLines(onevmListLines);
			
			Utils.giveStaticCPUValueToNodes(cl);
			
			return cl;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
