package main.java.restcloud.domain;

import java.util.ArrayList;

public class ClusterList {
	// ** ATTRIBUTES ** //
	// **************** //
	private ArrayList<HadoopCluster> clusters;

	// ** CONSTRUCTORS ** //
	// ****************** //
	public ClusterList() {
		clusters = new ArrayList<HadoopCluster>(0);
	}

	// ** GETTERS n SETTERS ** //
	// *********************** //
	public ArrayList<HadoopCluster> getClusters() {
		return clusters;
	}

	public HadoopCluster getCluster(int index) {
		return clusters.get(index);
	}

	public ClusterList setClusters(ArrayList<HadoopCluster> clusters) {
		this.clusters = clusters;
		return this;
	}

	public ClusterList setCluster(int index, HadoopCluster cluster) {
		clusters.set(index, cluster);
		return this;
	}

	public ClusterList addCluster(HadoopCluster cluster) {
		clusters.add(cluster);
		return this;
	}

	// ** METHODS ** //
	// ************* //

	// ** parse 'onevm list' ** //
	public void parseOnevmListLines(ArrayList<String> onevmListLines) {
		clusters = new ArrayList<HadoopCluster>(0);
		ArrayList<String> clusterIds = new ArrayList<String>(0);

		// Obtener clusterIds
		for (int i = 1; i < onevmListLines.size(); i++) { // Se evita la linea 0
															// porque contiene
															// las cabeceras
			String s = onevmListLines.get(i);
			if (s.length() > 0) {
				s = s.replaceAll("\\s+", " ");
				String arr[] = s.split(" ");
				String id = arr[4].split("-")[1];
				if (!clusterIds.contains(id))
					clusterIds.add(id);
			}
		}

		// Añadir datos de cada cluster
		for (String id : clusterIds) {
			HadoopCluster cluster = new HadoopCluster();
			cluster.parse(id, onevmListLines);
			clusters.add(cluster);
		}
	}
	
	// ** obtain all vm ip using 'oneip vmid' ** //
	public void obtainIps(){
		Thread threads[] = new Thread[clusters.size()];
		
		for(int i = 0 ; i < clusters.size() ; i++){
			final int index = i;
			threads[i] = new Thread(){
				@Override
				public void run(){
					clusters.get(index).obtainIps();
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
	
	// ** obtain for each cluster its exitStatus on DDBB ** //
	public void fillClustersExitStatus(){
		for(HadoopCluster cluster : clusters){
			cluster.obtainExitStatus();
		}
	}

	// ** toString() ** //

	@Override
	public String toString() {
		String ret = "ClusterList{ ";
		for (HadoopCluster s : clusters) {
			ret += s + "\n";
		}
		ret += "}";
		return ret;
	}

}
