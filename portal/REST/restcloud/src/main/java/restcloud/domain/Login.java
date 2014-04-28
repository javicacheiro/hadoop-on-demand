package main.java.restcloud.domain;

public class Login {
	private String user;
	private String pass;

    public String getUser() {
        return user;
    }
    
    public String getPass() {
    	return pass;
    }

    public Login setUser(final String user) {
        this.user = user;
        return this;
    }
    
    public Login setPass(final String pass){
    	this.pass = pass;
    	return this;
    }

    @Override
    public String toString() {
        return "Login{" +
                "user='" + user + "'," +
                "pass='" + pass + "'" +
                '}';
    }
}
