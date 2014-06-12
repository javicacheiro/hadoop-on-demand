package main.java.restcloud.domain;

public class Login {
	private String user;
	private String passwd;

    public String getUser() {
        return user;
    }
    
    public String getPasswd() {
    	return passwd;
    }

    public Login setUser(final String user) {
        this.user = user;
        return this;
    }
    
    public Login setPass(final String passwd){
    	this.passwd = passwd;
    	return this;
    }

    @Override
    public String toString() {
        return "Login{" +
                "user='" + user + "'," +
                "passwd='" + passwd + "'" +
                '}';
    }
}
