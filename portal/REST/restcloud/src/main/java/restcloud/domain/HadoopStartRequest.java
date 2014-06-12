package main.java.restcloud.domain;

import main.java.restcloud.Constants;
import main.java.restcloud.db.DBOperations;

/**
 * HadoopStartRequest 
 * @author albertoep
 */
public class HadoopStartRequest {
	// ** CONSTANTS ** //
	// *************** //
	public final static short DEFAULT_SIZE = 10;
	public final static short DEFAULT_DFS_REPLICAS = 3;
	public final static short DEFAULT_DFS_BLOCK_SIZE = 16; // MB
	public final static short DEFAULT_REDUCE_TASKS_NUMBER = 1;
	
	// ** ATTRIBUTES ** //
	// **************** //
	//hadoop-start [-s SIZE] [-r dfs.replication] [-b  <dfs.block.size>] [-t <mapred.reduce.tasks>]
	private short size;
	private short dfsReplicas;
	private short dfsBlockSize;
	private short reduceTasksNumber;
	private String user;
	
	// ** CONSTRUCTORS ** //
	// ****************** //
	public HadoopStartRequest(){
		size = DEFAULT_SIZE;
		dfsReplicas = DEFAULT_DFS_REPLICAS;
		dfsBlockSize = DEFAULT_DFS_BLOCK_SIZE;
		reduceTasksNumber = DEFAULT_REDUCE_TASKS_NUMBER;
	}
	
	public HadoopStartRequest(short size, short dfsReplicas,
			short dfsBlockSize, short reduceTasksNumber, String user){
		this.size = size;
		this.dfsReplicas = dfsReplicas;
		this.dfsBlockSize = dfsBlockSize;
		this.reduceTasksNumber = reduceTasksNumber;
		this.user = user;
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //
	
	public short getSize() {
		return size;
	}

	public void setSize(short size) {
		this.size = size;
	}

	public short getDfsReplicas() {
		return dfsReplicas;
	}

	public void setDfsReplicas(short dfsReplicas) {
		this.dfsReplicas = dfsReplicas;
	}

	public short getDfsBlockSize() {
		return dfsBlockSize;
	}

	public void setDfsBlockSize(short dfsBlockSize) {
		this.dfsBlockSize = dfsBlockSize;
	}

	public short getReduceTasksNumber() {
		return reduceTasksNumber;
	}

	public void setReduceTasksNumber(short reduceTasksNumber) {
		this.reduceTasksNumber = reduceTasksNumber;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String user){
		this.user = user;
	}
	
	// ** METHODS ** //
	// ************* //
	public String generateCmd(){
		String cmd = Constants.HADOOP_START_PATH+" -R -c "+DBOperations.generateClusterId()
				+" -s "+size+" -r "+dfsReplicas+" -b "+dfsBlockSize+" -t "+reduceTasksNumber;
		return cmd;
	}
	
	// ** toString ** //
	// ************** //
	@Override
	public String toString() {
		return "{"+
				"size : '"+size+"',"+
				"dfsReplicas : '"+dfsReplicas+"',"+
				"dfsBlockSize : '"+dfsBlockSize+"',"+
				"reduceTasksNumber : '"+reduceTasksNumber+"'"+
				"user : '"+user+"'"+
				"}";
	}
}
