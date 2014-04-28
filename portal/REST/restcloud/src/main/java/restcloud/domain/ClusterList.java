package main.java.restcloud.domain;

import java.util.ArrayList;

public class ClusterList {
	private ArrayList<HadoopCluster> clusters;
	
	public ClusterList(){
		clusters = new ArrayList<HadoopCluster>(0);
	}
	
	public ArrayList<HadoopCluster> getClusters(){
			return clusters;
	}
	
	public HadoopCluster getCluster(int index){
		return clusters.get(index);
	}
	
	public ClusterList setClusters(ArrayList<HadoopCluster> clusters){
		this.clusters = clusters;
		return this;
	}
	
	public ClusterList setCluster(int index, HadoopCluster cluster){
		clusters.set(index, cluster);
		return this;
	}
	
	public ClusterList addCluster(HadoopCluster cluster){
		clusters.add(cluster);
		return this;
	}
	
	@Override
	public String toString() {
		String ret = "ClusterList{ ";
		for(HadoopCluster s: clusters){
			ret += s+"\n";
		}
		ret+="}";
		return ret;
	}
	
}


