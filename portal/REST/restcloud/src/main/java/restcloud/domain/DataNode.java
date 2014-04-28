package main.java.restcloud.domain;

import java.util.GregorianCalendar;

public class DataNode {
	// ** ATTRIBUTES ** //
	// **************** //
	private String name;
	private String status;
	private double configuredCapacity; // en bytes
	private double dfsUsed; // en bytes
	private double nonDfsUsed; // en bytes
	private double dfsRemaining; // en bytes
	private GregorianCalendar lastContact;
	
	// ** CONSTRUCTORS ** //
	// ****************** //
	
	public DataNode(){
		
	}
	
	public DataNode(String name, String status, double configuredCapacity, 
			double dfsUsed, double nonDfsUsed, double dfsRemaining,
			String lastContact){
		
		GregorianCalendar gregorianCalendarLastContact = null;
		
		build(name, status, configuredCapacity, dfsUsed, nonDfsUsed, dfsRemaining,
				gregorianCalendarLastContact);
		
	}
	
	public DataNode(String name, String status, double configuredCapacity, 
			double dfsUsed, double nonDfsUsed, double dfsRemaining,
			GregorianCalendar lastContact){
		build(name, status, configuredCapacity, dfsUsed, nonDfsUsed, dfsRemaining,
				lastContact);
	}
	
	// build
	private void build(String name, String status, double configuredCapacity, 
			double dfsUsed, double nonDfsUsed, double dfsRemaining,
			GregorianCalendar lastContact){
		this.name = name;
		this.status = status;
		this.configuredCapacity = configuredCapacity;
		this.dfsUsed = dfsUsed;
		this.nonDfsUsed = nonDfsUsed;
		this.dfsRemaining = dfsRemaining;
		this.lastContact = lastContact;
		
	}
	
	// ** GETTERS n SETTERS ** //
	// *********************** //
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getConfiguredCapacity() {
		return configuredCapacity;
	}

	public void setConfiguredCapacity(double configuredCapacity) {
		this.configuredCapacity = configuredCapacity;
	}

	public double getDfsUsed() {
		return dfsUsed;
	}

	public void setDfsUsed(double dfsUsed) {
		this.dfsUsed = dfsUsed;
	}

	public double getNonDfsUsed() {
		return nonDfsUsed;
	}

	public void setNonDfsUsed(double nonDfsUsed) {
		this.nonDfsUsed = nonDfsUsed;
	}

	public double getDfsRemaining() {
		return dfsRemaining;
	}

	public void setDfsRemaining(double dfsRemaining) {
		this.dfsRemaining = dfsRemaining;
	}

	public GregorianCalendar getLastContact() {
		return lastContact;
	}

	public void setLastContact(GregorianCalendar lastContact) {
		this.lastContact = lastContact;
	}
	
	// ** toString ** //
	@Override
	public String toString() {
		return "DataNode [name=" + name + ", status=" + status
				+ ", configuredCapacity=" + configuredCapacity + ", dfsUsed="
				+ dfsUsed + ", nonDfsUsed=" + nonDfsUsed + ", dfsRemaining="
				+ dfsRemaining + ", lastContact=" + lastContact.get(GregorianCalendar.YEAR)+"-"+
				lastContact.get(GregorianCalendar.MONTH)+"-"+
				lastContact.get(GregorianCalendar.DAY_OF_MONTH)+" "+
				lastContact.get(GregorianCalendar.HOUR)+":"+
				lastContact.get(GregorianCalendar.MINUTE)+":"+
				lastContact.get(GregorianCalendar.SECOND) + "]";
	}
	
	// ** METHODS ** //
	// ************* //
	
	public void parseNameLine(String line){
		String[] arr = line.split(": ");
		name = arr[1];
	}
	
	public void parseDecommisionStatusLine(String line){
		String[] arr = line.split(": ");
		status = arr[1];
	}
	
	public void parseConfiguredCapacityLine(String line){
		String[] arr = line.split(": ");
		configuredCapacity = Double.parseDouble(arr[1].split("[(]")[0]);
	}
	
	public void parseDfsUsedLine(String line){
		String[] arr = line.split(": ");
		dfsUsed = Double.parseDouble(arr[1].split("[(]")[0]);
	}
	
	public void parseNonDfsUsedLine(String line){
		String[] arr = line.split(": ");
		nonDfsUsed = Double.parseDouble(arr[1].split("[(]")[0]);
	}
	
	public void parseDfsRemainingLine(String line){
		String[] arr = line.split(": ");
		dfsRemaining = Double.parseDouble(arr[1].split("[(]")[0]);
	}
	
	public void parseLastContactLine(String line){
		int year = 2014;
		int month = 4;
		int day = 28;
		int hour = 11;
		int min = 41;
		int sec = 30;
		lastContact = new GregorianCalendar(year,month,day,hour,min,sec);
		/*System.out.println(lastContact.get(GregorianCalendar.YEAR)+"-"+
			lastContact.get(GregorianCalendar.MONTH)+"-"+
			lastContact.get(GregorianCalendar.DAY_OF_MONTH)+" "+
			lastContact.get(GregorianCalendar.HOUR)+":"+
			lastContact.get(GregorianCalendar.MINUTE)+":"+
			lastContact.get(GregorianCalendar.SECOND));*/
	}
}