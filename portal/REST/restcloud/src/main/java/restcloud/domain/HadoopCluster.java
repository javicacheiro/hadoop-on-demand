package main.java.restcloud.domain;

import java.util.ArrayList;

import main.java.restcloud.db.DBOperations;

public class HadoopCluster {
	// ** CONSTANTS ** //
	// *************** //
	public final static String USER_NOT_FOUND = "#USER NOT FOUND#"; // Valor que
																	// se le da
																	// a 'user'
																	// cuando no
																	// se
																	// encuentra
																	// uno
	public final static String GROUP_NOT_FOUND = "#GROUP NOT FOUND#"; // Valor
																		// que
																		// se le
																		// da a
																		// 'group'
																		// cuando
																		// no se
																		// encuentra
																		// uno
	public final static String NAME_NOT_FOUND = "#NAME NOT FOUND#"; // Valor que
																	// se le da
																	// a 'name'
																	// cuando no
																	// se
																	// encuentra
																	// uno

	// ** ATTRIBUTES ** //
	// **************** //
	private String id;
	private String user;
	private String group;
	private String name;
	private ArrayList<VirtualMachine> vms;
	private int exitStatus;

	// ** CONSTRUCTORS ** //
	// ****************** //
	public HadoopCluster() {
	}

	public HadoopCluster(String id, String user, String group, String name,
			ArrayList<VirtualMachine> vms, int exitStatus) {
		this.id = id;
		this.user = user;
		this.group = group;
		this.name = name;
		this.vms = vms;
		this.exitStatus = exitStatus;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<VirtualMachine> getVms() {
		return vms;
	}

	public void setVms(ArrayList<VirtualMachine> vms) {
		this.vms = vms;
	}
	
	public int getExitStatus(){
		return exitStatus;
	}
	
	public void setExitStatus(int exitStatus){
		this.exitStatus = exitStatus;
	}

	// ** toString ** //
	// ************** //
	@Override
	public String toString() {
		String vmsToString = "";
		if (vms != null) {
			for (int i = 0; i < vms.size(); i++) {
				vmsToString += vms.get(i).toString();
			}
		}

		return "Cluster{ " + "id:\"" + id + "\", " + "user:\"" + user + "\", "
				+ "group:\"" + group + "\", " + "name : \"" + name + "\", "
				+ "vms : \"" + vmsToString + "\""
				+ "\"exitStatus\":\""+exitStatus+"\" }";
	}

	// ** METHODS ** //
	// ************* //

	// ** parse 'onevm list' ** //
	public void parse(String id, ArrayList<String> onevmListLines) {
		this.id = id;
		parseUser(onevmListLines);
		parseGroup(onevmListLines);
		parseName(onevmListLines);
		parseVirtualMachines(onevmListLines);
	}

	public void parseUser(ArrayList<String> onevmListLines) {
		user = parseSplittingByWhitespaces(2, USER_NOT_FOUND, onevmListLines);
	}

	public void parseGroup(ArrayList<String> onevmListLines) {
		group = parseSplittingByWhitespaces(3, GROUP_NOT_FOUND, onevmListLines);
	}

	public void parseName(ArrayList<String> onevmListLines) {
		name = parseSplittingByWhitespaces(4, NAME_NOT_FOUND, onevmListLines);
		name = name.substring(0, name.lastIndexOf("-"));
	}

	/**
	 * Requires name
	 */
	public void parseVirtualMachines(ArrayList<String> onevmListLines) {
		vms = new ArrayList<VirtualMachine>(0);
		for (String vmLine : onevmListLines) {
			if (vmLine.contains(name)) {
				VirtualMachine vm = new VirtualMachine();
				vm.parseVmLine(vmLine);
				vms.add(vm);
			}
		}
	}
	
	/**
	 * Obtain its virtualmachines IPs using 'oneip vmid'
	 */
	public void obtainIps(){
		Thread threads[] = new Thread[vms.size()];
		for(int i = 0 ; i < vms.size() ; i++){
			final int index = i;
			threads[i] = new Thread(){
				@Override
				public void run(){
					vms.get(index).obtainIp(user);
				}
			};
			threads[i].start();
		}
		
		for(Thread t : threads){
			while(t.isAlive()){
				try{
					Thread.sleep(5);
				}catch(Exception ex){}
			}
		}
	}
	
	public void obtainExitStatus(){
		exitStatus = DBOperations.obtainExitStatusForCluster(id);
	}

	// ** UTILS ** //
	// *********** //
	/**
	 * Parses the first occurrence
	 */
	private String parseSplittingByWhitespaces(int position,
			String notFoundReturn, ArrayList<String> onevmListLines) {
		for (int i = 1; i < onevmListLines.size(); i++) { // Ignorar linea de
															// cabecera (la 0)
			String line = onevmListLines.get(i);
			if (line != null) {
				if (line.length() > 0 && line.contains("hadoop-" + id)) {
					line = line.replaceAll("\\s+", " ");
					String ret = (line.split(" ").length > position) ? line
							.split(" ")[position] : notFoundReturn;
					return ret;
				}
			}
		}

		return notFoundReturn;
	}
}
