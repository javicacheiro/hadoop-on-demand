package main.java.restcloud.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import main.java.restcloud.Constants;
import main.java.restcloud.DataObtainerThread;
import main.java.restcloud.SSHKeyAddThread;
import main.java.restcloud.Utils;
import main.java.restcloud.db.DBOperations;
import main.java.restcloud.domain.ClusterInfo;
import main.java.restcloud.domain.ClusterList;
import main.java.restcloud.domain.DataNode;
import main.java.restcloud.domain.HadoopStartRequest;
import main.java.restcloud.domain.Message;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component @RestxResource
public class ClusterResource {

	/* * * * * * * * * * * * * * * * * * * * * *
	 * *   HTTP REQUEST: GET                 * *
	 * *   URL: /clusters                    * *
	 * *   MAIN METHOD: listClusters         * *
	 * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * This method call the 'onevm' util of the server to obtain a list of all
	 * virtual machines which state is 'runn' (running).
	 * @return A list of all the clusters showing id, user, group, name, state, 
	 * ucpu, umem, host and time (uptime in fact)
	 * @see ClusterList
	 */
	@GET("/clusters")
	@PermitAll
	public ClusterList listClusters(String user){
		try{
			ClusterList cl = new ClusterList();
			
			// Logearse como usuario
			/* Pendiente de hacer, se lanzara para el usuario albertoep */
			
			
			// ejecutar el comando con /bin/sh -c para que lo lea de una string ya que tiene pipes
			// y podria fallar/responder algo raro
			// Ejemplo: Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","onevm list | tr -s ' ' | tail -n +2"});
			String cmd = Utils.generateExport(user) + " && onevm list";
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			//Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /home/albertoep/tmp/onevmlist"});
			
			// Esperar a que finalice la ejecucion del comando
			p.waitFor();
			
			
			// Leer y procesar la salida estandar del comando
			ArrayList<String> onevmListLines = new ArrayList<String>();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while(in.ready())
				onevmListLines.add(in.readLine());
			cl.parseOnevmListLines(onevmListLines);
			cl.fillClustersExitStatus();
			cl.obtainIps();
			
			
			// Return
			return cl;
			
		}catch(Exception ex){
			/*return new ClusterList().addCluster(new HadoopCluster(1, "ERROR",ex.getMessage(),
					ex.toString(), java.util.Arrays.toString(ex.getStackTrace()), (short)0, "", "", ""));
			*/
			ex.printStackTrace();
			return null;
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * *
	 * *   HTTP REQUEST: GET                 * *
	 * *   URL: /clusters/{id}               * *
	 * *   MAIN METHOD: obtainClusterInfo    * *
	 * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * This method gets the cluster information making two calls to system process.
	 * The first call uses 'onevm list' and the second call uses 'hadoop-status'
	 * (a version of hadoop-status modified to work besides the server,
	 * not yet in fact but later).
	 * @param id ID of the cluster which info is going to be retrieved
	 * @return A bundle of information relative to the specified cluster:
	 * ID, User, Group, Number of running VMs, Total amount of VMs, Used memory,
	 * Used cpu, Available data nodes, Dead data nodes, Distributed File System
	 * configured capacity, DFS present capacity, DFS Remaining capacity,
	 * DFS used capacity, Under replicated blocks, Blocks with corrupt replicas,
	 * Missing blocks, Task Trackers and for each datanode found it shows:
	 * Name, Decommission status, Configured capacity, DFS used, DFS remaining,
	 * Last contact date.
	 * @see ClusterInfo
	 * @see DataNode
	 */
	@GET("/clusters/{id}")
	@PermitAll
	public ClusterInfo obtainClusterInfo(String id){
		try{
			// Obtener datos
			ClusterInfo info = new ClusterInfo();
			ArrayList<String> onevmListLines = doOnevmList(id);
			ArrayList<String> hadoopStatusLines = doHadoopStatus(id);
			//System.out.printf("%s\n\n\n\n%s\n\n\n\n",java.util.Arrays.toString(onevmListLines.toArray()),java.util.Arrays.toString(hadoopStatusLines.toArray()));
			
			// Pasar datos a ClusterInfo
			fillClusterInfo(info,id,onevmListLines, hadoopStatusLines);
			
			return info;
		}catch(Exception ex){
			return null;
		}
	}
	
	// ** UTIL METHODS ** //
	// ****************** //
	private ArrayList<String> doOnevmList(String id){
		try{
			String userId = DBOperations.getUserIdFromClusterWithId(id);
			String user = DBOperations.getUsernameByUserid(userId); // username
			//Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /home/albertoep/tmp/onevmlist"});
			String cmd = Utils.generateExport(user)+ " && "+ "onevm list";
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			
			// Return
			return buildArrayListOfLines(p.getInputStream());
			
		}catch(Exception ex){
			return null;
		}
	}
	
	private ArrayList<String> doHadoopStatus(String id){
		try{
			String userId = DBOperations.getUserIdFromClusterWithId(id);
			String user = DBOperations.getUsernameByUserid(userId); // username
			//Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /home/albertoep/tmp/hadoop-status"});
			String cmd = Utils.generateExport(user) + " && "+Constants.HADOOP_STATUS_PATH+" -R -c "+id;
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			
			// Return
			return buildArrayListOfLines(p.getInputStream());
			
		}catch(Exception ex){
			return null;
		}
	}
	
	private void fillClusterInfo(ClusterInfo info, String id, ArrayList<String> onevmListLines, ArrayList<String> hadoopStatusLines){
		info.setId(id);
		
		// onevm list parsing
		info.parseUser(onevmListLines);
		info.parseGroup(onevmListLines);
		info.parseVmRunning(onevmListLines);
		info.parseVmTotal(onevmListLines);
		info.parseMem(onevmListLines);
		info.parseUcpu(onevmListLines);
		
		if(info.getUser()!=null && info.getGroup()!=null){
			// hadoop-status parsing
			info.parseADataNodes(hadoopStatusLines);
			info.parseDDataNodes(hadoopStatusLines);
			info.parseDfsConfiguredCapacity(hadoopStatusLines);
			info.parseDfsPresentCapacity(hadoopStatusLines);
			info.parseDfsRemaining(hadoopStatusLines);
			info.parseDfsUsed(hadoopStatusLines);
			info.parseUnderReplicatedBlocks(hadoopStatusLines);
			info.parseBlocksCorruptReplicas(hadoopStatusLines);
			info.parseMissingBlocks(hadoopStatusLines);
			info.parseDataNodes(hadoopStatusLines);
			info.parseTaskTracers(hadoopStatusLines);
		}
	}
	
	private ArrayList<String> buildArrayListOfLines(InputStream in){
		try{
			ArrayList<String> ret = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while(br.ready()){
				ret.add(br.readLine());
			}
			in.close();
			return ret;
		}catch(Exception ex){
			return null;
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * *
	 * *   HTTP REQUEST: POST                * *
	 * *   URL: /clusters                    * *
	 * *   MAIN METHOD: createHadoopCluster  * *
	 * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * This method call the server's hadoop-start script (in fact it call its own
	 * modified version of it) and wait until it can read the first line of stdin.
	 * Once the first line is read the request end and the id (which was read) is
	 * returned but the cluster is not full started yet but in process.
	 * @param hsr
	 * @return If everything went OK then it returns a Message containing the id of the
	 * created cluster. If something unexpected occurred then it sends a Message with
	 * information relative to the exception.
	 * @see HadoopStartRequest
	 */
	@POST("/clusters")
	@PermitAll
	public Message createHadoopCluster(HadoopStartRequest hsr){
		if(Utils.countUserClusters(hsr.getUser()) < Constants.USER_MAX_CLUSTERS){
			if(hsr.getSize() < Constants.USER_MAX_VMS_FOR_CLUSTER){
				try{
					// enable-hadoop si procede
					//enableHadoop(hsr.getUser());
					
					// hadoop-start
					Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c",
							Utils.generateExport(hsr.getUser())
							+" && "+hsr.generateCmd()});
					
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String id = "no_id_found";
					
					String firstLine="";
					while ((firstLine=in.readLine()) != null){
						if(firstLine.length()<1)
							Thread.sleep(5);
						else break;
					}
							
					String arr[] = firstLine.split(":");
					id = arr[1].split("-")[1];
					
					// Async Jobs
					new DataObtainerThread(p,id,hsr).start();
					
					// return
					return new Message().setMessage("id:"+id);
					
				}catch(Exception ex){
					return new Message().setMessage("Could not create hadoop cluster: "+ex.toString()+
							"\nEXCPETION STACK TRACE:"+java.util.Arrays.toString(ex.getStackTrace()));
				}
			}else
				return new Message().setMessage("Max number of allowed VMs ("
						+Constants.USER_MAX_VMS_FOR_CLUSTER+") was exceeded.");
		}else
			return new Message().setMessage("Max number of allowed clusters ("
					+Constants.USER_MAX_CLUSTERS+") was exceeded.");
	}
	
	// ** UTIL METHODS ** //
	// ****************** //
	private void enableHadoop(String user) throws Exception{
		// Paths
		String pubKeyPath = "/home/cesga/"+user+"/.ssh/id_dsa.pub";
		String templateDirPath = "/home/cesga/"+user;
		String templateFilePath = templateDirPath+"/oneuser_template";
		
		// Files
		File pubKeyFile = new File(pubKeyPath);
		File templateDir = new File(templateDirPath);
		File templateFile = new File(templateFilePath);
		
		// Obtener PubKey
		String pubKey = readPubKey(pubKeyFile);
		if(pubKey==null){
			throw new Exception("Failed obtaining PubKey to generate oneuser_template.");
		}
		
		// Generar Template
		if(!templateDir.exists())
			if(!templateDir.mkdirs())
				throw new Exception("Failed creating directory to allocate oneuser_template.");
		PrintWriter out = new PrintWriter(templateFile);
		out.println("SSH_KEY=\""+pubKey+"\"");
		out.println("SSH_PUBLIC_KEY=\""+pubKey+"\"");
		out.close();
	}
	
	/**
	 * 
	 * @param f PubKey File
	 * @return
	 */
	private String readPubKey(File f){
		BufferedReader in;
		String ret = null;
		try{
			in = new BufferedReader(new FileReader(f));
			while(in.ready()){ 
				ret += in.readLine();
			}
			in.close();
			return ret;
		}catch(Exception ex){
			return null;
		}
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * *
	 * *   HTTP REQUEST: DELETE              * *
	 * *   URL: /clusters/{id}               * *
	 * *   MAIN METHOD: deleteHadoopCluster  * *
	 * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * This method uses the modified version of hadoop-stop script to drop the user's VMs
	 * and also removes the clustername file [NEW VERSION DOES NOT REMOVE clustername FILE].
	 * Before this the method check if 'onevm list' answers a virtual machine which name
	 * matches the given id.
	 * @param id
	 * @return Message which tells what happened
	 */
	@DELETE("/clusters/{id}")
	@PermitAll
	public Message deleteHadoopCluster(String id){
		try{
			// Obtener userId del cluster cuya id coincida con aquella pasad como parametro
			String userId = DBOperations.getUserIdFromClusterWithId(id); 
			
			// Coger el nombre de usuario por cluster.userId=useruser._id
			String user = DBOperations.getUsernameByUserid(userId); // username
			
			if(vmNamedWithClusterIDExists(id, user)){
				// hadoop-stop
				String cmd = Utils.generateExport(user) + " && "+Constants.HADOOP_STOP_PATH+" -R -c "+id;
				Process hs = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",
				cmd}); // Hadoop Stop
				hs.waitFor();
				DBOperations.updateStopTimeForCluster(id);
				
				// rm clustername // Ya no hace falta
				/*Process rmcn = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",
						"rm -f /home/cesga/albertoep/.hadoop-on-demand/clustername"}); // rm clustername
				rmcn.waitFor();*/
				
				// Comprobar posibles fallos
				Message failMessage = null;
				
				// Comprobar que clustername ya no exista // Ya no hace falta
				/*if(clusternameStillExists())
					failMessage = new Message().setMessage("File clustername still exists.");*/
				
				// Comprobar que no hay maquinas virtuales cuyo name coincida con la id del cluster
				if(vmNamedWithClusterIDExists(id, user))
					failMessage = (failMessage == null) ?
							new Message().setMessage("At least one virtual machine which name matches "+
									"with cluster id still existing.")
							: failMessage.setMessage(failMessage.getMessage()+"\n"+
									"At least one virtual machine which name matches "+
									"with cluster id still existing.");
				
				return ((failMessage==null) ?
						new Message().setMessage("Hadoop cluster with id ["+id+"]: DELETED SUCCESSFULLY")
						: failMessage);
			}else
				return new Message().setMessage("A hadoop cluster with the given id ["+id+"] does not exist.");
		}catch(Exception ex){
			return new Message().setMessage("An exception ocurred while trying to delete"+
					"hadoop cluster with id ["+id+"]:\n"+ex.toString());
		}
	}
	
	// ** UTIL METHODS ** //
	// ****************** //
	/**
	 * This method instantiate a File using the path of clustername file then it
	 * calls the File.exists method to check if clustername exists or not
	 * @return True: clustername file exists. False: clustername file doesn't exist.
	 * If an exception ocurred this method will return true.
	 */
	private boolean clusternameStillExists(){
		try{
			File file = new File("/home/cesga/albertoep/.hadoop-on-demand/clustername");
			
			if(file.exists())
				return true;
			
			return false;
		}catch(Exception ex){
			return true;
		}
	}
	
	/**
	 * This method reads the answer of calling 'onevm list' and check if any
	 * virtual machine name matches the id as a hadoop cluster name will do 
	 * @return True: At least un virtual machine which name matches cluster id exists.
	 * If an exception ocurred this method will return true.
	 */
	private boolean vmNamedWithClusterIDExists(String id, String user){
		try{
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",
					Utils.generateExport(user)+" && onevm list"});
			p.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while(in.ready()){
				String line = in.readLine();
				if(line.contains(id)){
					line = line.replaceAll("\\s+"," ");
					String[] arr = line.split(" ");
					if(arr[4].split("-")[1].equals(id)){
						in.close();
						return true;
					}
				}
			}
			
			in.close();
			return false;
		}catch(Exception ex){
			return true;
		}
	}
}
