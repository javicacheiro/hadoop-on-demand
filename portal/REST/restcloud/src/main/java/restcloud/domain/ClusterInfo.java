package main.java.restcloud.domain;

import java.util.ArrayList;

/**
 * ClusterInfo class is a blend of Data Structure for the informatio and util methods to
 * parse it from the sources 'onevm list' and 'hadoop-status' (parsing methods such as parseUser).
 * In order to work properly this class requieres Open Nebula 4.4.0 version
 * @author Alberto
 *
 */
public class ClusterInfo {
	// ** ATTRIBUTES ** //
	// **************** //
	/**
	 * Cluster id
	 */
	private String id;
	/**
	 * Cluster user
	 */
	private String user;
	/**
	 * Cluster group
	 */
	private String group;
	/**
	 * Number of running virtual machines
	 */
	private int vmRunning; // Numero de maquinas en ejecución
	/**
	 * Total amount of virtual machines
	 */
	private int vmTotal; // Numero total de maquinas
	/**
	 * Amount of memory used by the cluster (sum of the memory used for each virtual machine
	 */
	private String umem; // Total de memoria consumido por las vm
	/**
	 * Cluster's amount of cpu usage
	 */
	private int ucpu; // Total de cpus usadas
	/**
	 * Number of cluster's available data nodes
	 */
	private int aDataNodes; // Available Data Nodes
	/**
	 * Number of cluster's dead data nodes
	 */
	private int dDataNodes; // Dead Data Nodes
	/**
	 * Distributed File System configured capacity in bytes
	 */
	private double dfsConfiguredCapacity; // en bytes
	/**
	 * Distributed File System present capacity in bytes
	 */
	private double dfsPresentCapacity; // en bytes
	/**
	 * Distributed File System remaining capacity in bytes
	 */
	private double dfsRemaining; // en bytes
	/**
	 * Distributed File System used capacity in bytes
	 */
	private double dfsUsed; // en bytes
	/**
	 * Under replicated blocks
	 */
	private long underReplicatedBlocks;
	/**
	 * Number of blocks with corrupt replicas
	 */
	private long blocksCorruptReplicas;
	/**
	 * Missing blocks
	 */
	private long missingBlocks;
	/**
	 * DataNodes
	 * @see DataNode
	 */
	ArrayList<DataNode> dataNodes;
	/**
	 * ArrayList containing taskTracker hadoop-status relative lines (one per each)
	 */
	ArrayList<String> taskTrackers;

	
	// ** CONSTRUCTORS ** //
	// ****************** //
	/**
	 * Instantiates an empty ClusterInfo
	 */
	public ClusterInfo(){
		
	}

	/**
	 * Instantiates a ClusterInfo and assign to each field the corresponding parameter
	 */
	public ClusterInfo(String id, String user, String group, int vmRunning,
			int vmTotal, String umem, int ucpu, int aDataNodes, int dDataNodes,
			double dfsConfiguredCapacity, double dfsPresentCapacity,
			double dfsRemaining, double dfsUsed, long underReplicatedBlocks,
			long blocksCorruptReplicas, long missingBlocks,
			ArrayList<DataNode> dataNodes, ArrayList<String> taskTrackers) {
		super();
		this.id = id;
		this.user = user;
		this.group = group;
		this.vmRunning = vmRunning;
		this.vmTotal = vmTotal;
		this.umem = umem;
		this.ucpu = ucpu;
		this.aDataNodes = aDataNodes;
		this.dDataNodes = dDataNodes;
		this.dfsConfiguredCapacity = dfsConfiguredCapacity;
		this.dfsPresentCapacity = dfsPresentCapacity;
		this.dfsRemaining = dfsRemaining;
		this.dfsUsed = dfsUsed;
		this.underReplicatedBlocks = underReplicatedBlocks;
		this.blocksCorruptReplicas = blocksCorruptReplicas;
		this.missingBlocks = missingBlocks;
		this.dataNodes = dataNodes;
		this.taskTrackers = taskTrackers;
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}


	public int getVmRunning() {
		return vmRunning;
	}


	public void setVmRunning(int vmRunning) {
		this.vmRunning = vmRunning;
	}


	public int getVmTotal() {
		return vmTotal;
	}


	public void setVmTotal(int vmTotal) {
		this.vmTotal = vmTotal;
	}


	public String getUmem() {
		return umem;
	}


	public void setUmem(String umem) {
		this.umem = umem;
	}


	public int getUcpu() {
		return ucpu;
	}


	public void setUcpu(int ucpu) {
		this.ucpu = ucpu;
	}


	public int getaDataNodes() {
		return aDataNodes;
	}


	public void setaDataNodes(int aDataNodes) {
		this.aDataNodes = aDataNodes;
	}


	public int getdDataNodes() {
		return dDataNodes;
	}


	public void setdDataNodes(int dDataNodes) {
		this.dDataNodes = dDataNodes;
	}


	public double getDfsConfiguredCapacity() {
		return dfsConfiguredCapacity;
	}


	public void setDfsConfiguredCapacity(double dfsConfiguredCapacity) {
		this.dfsConfiguredCapacity = dfsConfiguredCapacity;
	}


	public double getDfsPresentCapacity() {
		return dfsPresentCapacity;
	}


	public void setDfsPresentCapacity(double dfsPresentCapacity) {
		this.dfsPresentCapacity = dfsPresentCapacity;
	}


	public double getDfsRemaining() {
		return dfsRemaining;
	}


	public void setDfsRemaining(double dfsRemaining) {
		this.dfsRemaining = dfsRemaining;
	}


	public double getDfsUsed() {
		return dfsUsed;
	}


	public void setDfsUsed(double dfsUsed) {
		this.dfsUsed = dfsUsed;
	}


	public long getUnderReplicatedBlocks() {
		return underReplicatedBlocks;
	}


	public void setUnderReplicatedBlocks(long underReplicatedBlocks) {
		this.underReplicatedBlocks = underReplicatedBlocks;
	}


	public long getBlocksCorruptReplicas() {
		return blocksCorruptReplicas;
	}


	public void setBlocksCorruptReplicas(long blocksCorruptReplicas) {
		this.blocksCorruptReplicas = blocksCorruptReplicas;
	}


	public long getMissingBlocks() {
		return missingBlocks;
	}


	public void setMissingBlocks(long missingBlocks) {
		this.missingBlocks = missingBlocks;
	}


	public ArrayList<DataNode> getDataNodes() {
		return dataNodes;
	}


	public void setDataNodes(ArrayList<DataNode> dataNodes) {
		this.dataNodes = dataNodes;
	}


	public ArrayList<String> getTaskTrackers() {
		return taskTrackers;
	}


	public void setTaskTrackers(ArrayList<String> taskTrackers) {
		this.taskTrackers = taskTrackers;
	}
	
	// ** toString ** //
	@Override
	public String toString() {
		return "ClusterInfo [id=" + id + ", user=" + user + ", group=" + group
				+ ", vmRunning=" + vmRunning + ", vmTotal=" + vmTotal
				+ ", umem=" + umem + ", ucpu=" + ucpu + ", aDataNodes="
				+ aDataNodes + ", dDataNodes=" + dDataNodes
				+ ", dfsConfiguredCapacity=" + dfsConfiguredCapacity
				+ ", dfsPresentCapacity=" + dfsPresentCapacity
				+ ", dfsRemaining=" + dfsRemaining + ", dfsUsed=" + dfsUsed
				+ ", underReplicatedBlocks=" + underReplicatedBlocks
				+ ", blocksCorruptReplicas=" + blocksCorruptReplicas
				+ ", missingBlocks=" + missingBlocks + ", dataNodes="
				+ dataNodes + ", taskTrackers=" + taskTrackers + "]";
	}
	
	// ** METHODS ** //
	// ************* //
	
	// ** onvm list parsing methods ** //
	/**
	 * parseFirstIdInOnevmList assigns to the field id the value of the first occurrence of
	 * ClusterId found in the given ArrayList of String
	 * @param onevmListLines ArrayList of String which contains the cluster id
	 * This lines must be generated by 'onevm list' so they can be properly parsed 
	 */
	public void parseFirstIdInOnevmList(ArrayList<String> onevmListLines){
		String line;
		if((line=onevmListLines.get(1))!=null){
				line = line.replaceAll("\\s+"," ");
				String arr[] = line.split(" ");
				id = arr[4].split("-")[1];
		}
	}
	
	/**
	 * parseUser assigns to the user field the value of the first occurrence of user found
	 * in the given ArrayList of String which is related with the cluster ID.
	 * So this method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String which contains the user.
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseUser(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String arr[] = line.split(" ");
						if(arr[4].split("-")[1].equals(id)){
							user = arr[2];
							return;
						}
					}
				}
			}
		}
	}
	
	/**
	 * parseUser assigns to the user field the value of the first occurrence of group found
	 * in the given ArrayList of String which is related with the cluster ID.
	 * So this method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String which contains the group.
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseGroup(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String arr[] = line.split(" ");
						if(arr[4].split("-")[1].equals(id)){
							group = arr[3];
							return;
						}
					}
				}
			}
		}
	}
	
	/**
	 * parseVmRunning counts all the running virtual machines related with Cluster ID.
	 * So this method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String which lines are taken from 'onevm list' return.
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseVmRunning(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			vmRunning = 0;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String arr[] = line.split(" ");
						if(arr[5].equals("runn") && arr[4].split("-")[1].equals(id)) // State == runn && id == id
							vmRunning++;
						
					}
				}
			}
		}
	}
	
	/**
	 * parseVmTotal counts all the virtual machines related with Cluster ID.
	 * So this method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String which lines are taken from 'onevm list' return.
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseVmTotal(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			vmTotal = 0;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String arr[] = line.split(" ");
						if(arr[4].split("-")[1].equals(id)) // id == id
							vmTotal++;
						
					}
				}
			}
		}
	}
	
	/**
	 * parseMem sum all the UMEM values given by 'onevm list' related with the cluster ID
	 * and assign this value to field umem.
	 * This method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String where there is a value for the UMEM 'field'
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseMem(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			double memBytes = 0;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String[] arr = line.split(" ");
						if(arr[4].split("-")[1].equals(id)){
							String mem = arr[7];
							switch(mem.charAt(mem.length()-1)){
								case 'T': // Tera
									memBytes += (double)Integer.parseInt(mem.substring(0,mem.length()-1))*1024*1024*1024*1024;
									break;
								case 'G': // Giga
									memBytes += (double)Integer.parseInt(mem.substring(0,mem.length()-1))*1024*1024*1024; 
									break;
								case 'M': // Mega
									memBytes += (double)Integer.parseInt(mem.substring(0,mem.length()-1))*1024*1024;
									break;
								case 'K': // Kilo
									memBytes += (double)Integer.parseInt(mem.substring(0,mem.length()-1))*1024;
									break;
								case 'B': // Byte
									memBytes += (double)Integer.parseInt(mem.substring(0,mem.length()-1));
									break;
							}
						}
					}
				}
			}
			
			if(memBytes / 1024.0 <= 1)
				umem = memBytes+"B";
			else if(memBytes / (1024.0*1024.0) <= 1)
				umem = (memBytes/1024.0)+"K";
			else if(memBytes / (1024.0*1024.0*1024.0) <= 1)
				umem = (memBytes/(1024.0*1024.0))+"M";
			else if(memBytes / (1024.0*1024.0*1024.0*1024.0) <= 1)
				umem = (memBytes/(1024.0*1024.0*1024.0))+"G";
			else
				umem = (memBytes/(1024.0*1024.0*1024.0*1024.0))+"T"; 
				
		}
	}
	
	/**
	 * parseUcpu sum all the UCPU values given by 'onevm list' related with the cluster ID
	 * and assign this value to field ucpu.
	 * This method REQUIRES from the field ID to have the correct value.
	 * @param onevmListLines ArrayList of String where there is a value for the UCPU 'field'
	 * This lines must be generated by 'onevm list' so they can be properly parsed
	 */
	public void parseUcpu(ArrayList<String> onevmListLines){
		if(id!=null){
			String line;
			for(int i = 1 ; i < onevmListLines.size() ; i++){
				if((line=onevmListLines.get(i))!=null){
					if(line.length()>0){
						line = line.replaceAll("\\s+"," ");
						String[] arr = line.split(" ");
						if(arr[4].split("-")[1].equals(id)){
							ucpu += Integer.parseInt(arr[6]);
						}
					}
				}
			}
		}
	}
	
	// ** hadoop-status parsing methods ** //
	/**
	 * parseADataNodes parse the stdin given by the script hadoop-status and find
	 * the number of available data nodes.
	 * @param hsLines ArrayList of String which contains the number of available datanodes
	 * This lines must be generated by 'hadoop-status' so they can be properly parsed
	 */
	public void parseADataNodes(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Datanodes available")){
				aDataNodes = Integer.parseInt(line.split("[(]")[1].split(" ")[0]);
				return;
			}
		}
	}
	
	public void parseDDataNodes(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Datanodes available")){
				dDataNodes = Integer.parseInt(line.split(", ")[1].split(" d")[0]);
				return;
			}
		}
	}
	
	public void parseDfsConfiguredCapacity(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Configured Capacity:")){
				line = line.split("Configured Capacity:")[1].split("[(]")[0];
				line = line.substring(1,line.length()-1);
				dfsConfiguredCapacity = Double.parseDouble(line);
				return;
			}
		}
	}
	
	public void parseDfsPresentCapacity(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Present Capacity:")){
				line = line.split("Present Capacity:")[1].split("[(]")[0];
				line = line.substring(1,line.length()-1);
				dfsPresentCapacity = Double.parseDouble(line);
				return;
			}
		}
	}
	
	public void parseDfsRemaining(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("DFS Remaining:")){
				line = line.split("DFS Remaining:")[1].split("[(]")[0];
				line = line.substring(1,line.length()-1);
				dfsRemaining = Double.parseDouble(line);
				return;
			}
		}
	}
	
	public void parseDfsUsed(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("DFS Used:")){
				line = line.split("DFS Used:")[1].split("[(]")[0];
				line = line.substring(1,line.length()-1);
				dfsUsed = Double.parseDouble(line);
				return;
			}
		}
	}
	
	public void parseUnderReplicatedBlocks(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Under replicated blocks: ")){
				line = line.split("Under replicated blocks: ")[1];
				underReplicatedBlocks = Long.parseLong(line);
				return;
			}
		}
	}
	
	public void parseBlocksCorruptReplicas(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Blocks with corrupt replicas: ")){
				line = line.split("Blocks with corrupt replicas: ")[1];
				blocksCorruptReplicas = Long.parseLong(line);
				return;
			}
		}
	}
	
	public void parseMissingBlocks(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("Missing blocks: ")){
				line = line.split("Missing blocks: ")[1];
				missingBlocks = Long.parseLong(line);
				return;
			}
		}
	}
	
	public void parseDataNodes(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			if(hsLines.get(i).contains("Name:")){
				if(dataNodes==null)
					dataNodes = new ArrayList<DataNode>(0);
				DataNode dn = new DataNode();
				dn.parseNameLine(hsLines.get(i));
				i++;
				dn.parseDecommisionStatusLine(hsLines.get(i));
				i++;
				dn.parseConfiguredCapacityLine(hsLines.get(i));
				i++;
				dn.parseDfsUsedLine(hsLines.get(i));
				i++;
				dn.parseNonDfsUsedLine(hsLines.get(i));
				i++;
				dn.parseDfsRemainingLine(hsLines.get(i));
				i+=3;
				dn.parseLastContactLine(hsLines.get(i));
				dataNodes.add(dn);		
			}
		}
	}
	
	public void parseTaskTracers(ArrayList<String> hsLines){
		for(int i = 0 ; i < hsLines.size() ; i++){
			String line;
			if((line=hsLines.get(i)).contains("===> Task trackers")){
				for(int j = i+1 ; j < hsLines.size() ; j++){
					if((line=hsLines.get(j)).startsWith("tracker_")){
						if(taskTrackers==null)
							taskTrackers = new ArrayList<String>(0);
						taskTrackers.add(line);
					}
				} 
			}
		}
	}
}
