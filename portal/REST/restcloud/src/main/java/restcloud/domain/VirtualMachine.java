package main.java.restcloud.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import main.java.restcloud.Utils;

public class VirtualMachine {
	// ** CONSTANTS ** //
	// *************** //
	public final static String VMID_NOT_FOUND = "#VMID NOT FOUND#";
	public final static String STATUS_NOT_FOUND = "#STATUS NOT FOUND#";
	public final static short UCPU_NOT_FOUND = -2;
	public final static String UMEM_NOT_FOUND = "#UMEM NOT FOUND#";
	public final static String HOST_NOT_FOUND = "#HOST NOT FOUND#";
	public final static String TIME_NOT_FOUND = "#TIME NOT FOUND#";
	public final static String NAME_NOT_FOUND = "#NAME NOT FOUND#";

	// ** ATTRIBUTES ** //
	// **************** //
	private String vmid;
	private String status;
	private short ucpu;
	private String umem;
	private String host;
	private String time; // Tiempo que lleva funcionando
	private String name;
	private String ip;

	// ** CONSTRUCTOR ** //
	// ***************** //
	public VirtualMachine() {
	}

	public VirtualMachine(String vmid, String status, short ucpu, String umem,
			String host, String time, String name, String ip) {
		this.vmid = vmid;
		this.status = status;
		this.ucpu = ucpu;
		this.umem = umem;
		this.host = host;
		this.time = time;
		this.name = name;
		this.ip = ip;
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //
	public String getVmid() {
		return vmid;
	}

	public void setVmid(String vmid) {
		this.vmid = vmid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public short getUcpu() {
		return ucpu;
	}

	public void setUcpu(short ucpu) {
		this.ucpu = ucpu;
	}

	public String getUmem() {
		return umem;
	}

	public void setUmem(String umem) {
		this.umem = umem;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getIp(){
		return ip;
	}
	
	public void setIp(){
		this.ip = ip;
	}
	
	// ** toString ** //
	@Override
	public String toString() {
		return "VirtualMachine{ " + "\"vmid\":\"" + vmid + "\", "
				+ "\"status\":\"" + status + "\"," + "\"ucpu\":" + ucpu + ", "
				+ "\"umem\":\"" + umem + "\", " + "\"host\":\"" + host + "\","
				+ "\"time\":\"" + time + "\", " + "\"ip\":\"" + ip+"\"}";
	}

	// ** METHODS ** //
	// ************* //
	public void parseVmLine(String vmLine) {
		String line = vmLine.replaceAll("\\s+", " ");
		String arr[] = line.split(" ");
		if (arr != null) {
			vmid = (arr[1] != null) ? arr[1] : VMID_NOT_FOUND;
			status = (arr[5] != null) ? arr[5] : STATUS_NOT_FOUND;
			ucpu = (arr[6] != null) ? Short.parseShort(arr[6]) : UCPU_NOT_FOUND;
			umem = (arr[7] != null) ? arr[7] : UMEM_NOT_FOUND;
			host = (arr[8] != null) ? arr[8] : HOST_NOT_FOUND;
			name = (arr[4] != null) ? arr[4] : NAME_NOT_FOUND;

			if (arr[9] != null) {
				time = "";
				for (int i = 9; i < arr.length; i++) {
					time += arr[i] + " ";
				}
			} else
				time = TIME_NOT_FOUND;
		}
	}
	
	public void obtainIp(String user){
		try{
			String cmd = Utils.generateExport(user)+" && oneip "+vmid;
			Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",cmd});
			p.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if(in.ready()){
				ip = in.readLine();
			}else
				System.out.println("IP Could not be obtained through VirtualMachine.obtainIp()");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
