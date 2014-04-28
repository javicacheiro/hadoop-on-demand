package main.java.restcloud.domain;

public class HadoopCluster{
	private int id;
	private String user;
	private String group;
	private String name;
	private String state;
	private short ucpu;
	private String umem;
	private String host;
	private String time; // Tiempo que lleva funcionando
	
	public HadoopCluster(int id, String user, String group, String name, String state,
			short ucpu, String umem, String host, String time){
		this.id = id;
		this.user = user;
		this.group = group;
		this.name = name;
		this.state = state;
		this.ucpu = ucpu;
		this.umem = umem;
		this.host = host;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public String getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public short getUcpu() {
		return ucpu;
	}

	public String getUmem() {
		return umem;
	}

	public String getHost() {
		return host;
	}

	public String getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return "Cluster{ "+
				"id : '"+id+"',"+
			    "user : '"+user+"',"+
			    "group : '"+group+"',"+
			    "name : '"+name+"',"+
			    "state : '"+state+"',"+
			    "ucpu : '"+ucpu+"',"+
			    "umem : '"+umem+"',"+
			    "host : '"+host+"',"+
			    "time : '"+time+"'"+
			    "}";
	}
}
