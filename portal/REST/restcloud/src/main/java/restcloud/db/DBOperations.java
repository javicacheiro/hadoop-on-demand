package main.java.restcloud.db;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import main.java.restcloud.domain.HadoopStartRequest;
import main.java.restcloud.domain.Login;

public class DBOperations {

	// ** OPERATIONS ** //
	// **************** //
	/**
	 * Register the user in the database if it is not yet
	 * 
	 * @param login
	 */
	public static void registerUserIfNotYet(Login login) {
		try {
			if (isUserOnDb(login.getUser()) == 0) {
				Connection con = establishConnection();
				PreparedStatement dml = con
						.prepareStatement("INSERT INTO user (username) VALUES (?)");
				dml.setString(1, login.getUser());
				dml.execute();
				dml.close();
				con.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Checks if the given username 'user' is on the db or not
	 * 
	 * @param user
	 * @return 0: User is not on db. 1: User is on db. 2: Error occurred.
	 */
	public static byte isUserOnDb(String user) {
		try {
			Connection con = establishConnection();
			if (con == null)
				throw new Exception("Could not establish connection");
			Statement stm = con.createStatement();
			ResultSet rs = stm
					.executeQuery("SELECT * FROM user WHERE username='" + user
							+ "'");
			if (rs.first() == true)
				return 1;
			return 0;
		} catch (Exception ex) {
			return 2;
		}
	}
	
	/**
	 * Insert information relative to the performed hadoop start request
	 * @param id
	 * @param hsr
	 */
	public static void insertHadoopStartRequestInfo(String id, HadoopStartRequest hsr){
		try{
			long clusterId = Long.parseLong(id);
			Connection con = establishConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO cluster "
					+"(_id, userId, name, passwd, size, replication, blocksize, "
					+ "reducetasks, submitTime) VALUES (?,?,?,?,?,?,?,?,now())");
		
			String name="hadoop-1.1.2-"+id;
			
			ps.setLong(1, clusterId); // clusterId (_id)
			ps.setLong(2, findIdUserByUsername(hsr.getUser())); // userId
			ps.setString(3,name); // name
			ps.setString(4,"abcdefghi123456789"); // passwd
			ps.setShort(5,hsr.getSize());// size
			ps.setShort(6,hsr.getDfsReplicas());// replication
			ps.setShort(7,hsr.getDfsBlockSize());// blocksize
			ps.setShort(8,hsr.getReduceTasksNumber());// reducetasks
			
			ps.execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Find the userId for the given username
	 * @param username
	 * @return -1 If username does not exist. -2 If error occurred. Positive number equals to id
	 */
	public static long findIdUserByUsername(String username){
		try{
			Connection con = establishConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT _id FROM user WHERE username='"+username+"'");
			if(rs.first()==false)
				return -1;
			return rs.getLong(1);
		}catch(Exception ex){
			return -2;
		}
	}
	
	/**
	 * Generates a clusterId which values is the las clusterId value found in dddbb plus one
	 * @return Cluster id
	 */
	public static String generateClusterId(){
		try{
			BigInteger maxClusterId = new BigInteger(obtainMaxClusterId());
			maxClusterId = maxClusterId.add(BigInteger.ONE);
			return maxClusterId.toString();
		}catch(NullPointerException npex){
			/*
			 * hacer un max(_id) en SQL cuando no hay ids no es que no devuelva nada,
			 * es que devuelve NULL
			 */
			return "1";
		}
	}
	
	/**
	 * Obtains the max value stored in the table cluster of DDBB hadoop for field _id
	 * @return _id max value @ hadoop.cluster
	 */
	public static String obtainMaxClusterId(){
		try{
			Connection con = establishConnection();
			ResultSet rs = con.prepareStatement("SELECT max(_id) FROM cluster;").executeQuery();
			if(!rs.first())
				return "0";
			else return rs.getString(1);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves the username corresponding to the given userId
	 * @param userId
	 * @return
	 * 		String "xxx" where XXX is username
	 * 		String "" when no user with such id has been found
	 * 		String null when something unexpected occurred
	 */
	public static String getUsernameByUserid(String userId){
		try{
			Connection con = establishConnection();
			ResultSet rs = con.prepareStatement("SELECT username FROM user "
					+"WHERE _id="+userId).executeQuery();
			if(!rs.first())
				return "";
			return rs.getString(1);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 * 		String "xxx" where XXX is userId
	 * 		String "" when no cluster with given id has been found
	 * 		String null when something unexpected occurred
	 */
	public static String getUserIdFromClusterWithId(String clusterId){
		try{
			Connection con = establishConnection();
			ResultSet rs = con.prepareStatement("SELECT userId FROM cluster "
					+"WHERE _id="+clusterId).executeQuery();
			if(!rs.first())
				return "";
			return rs.getString(1);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	// ** CONNECTION UTILS ** //
	// ********************** //
	public static Connection establishConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://" + DBData.DBHOST + "/" + DBData.DBNAME;
			Connection con = DriverManager.getConnection(url, DBData.DBUSER,
					DBData.DBPASS);
			return con;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
