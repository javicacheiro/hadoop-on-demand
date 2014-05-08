package main.java.restcloud;

import main.java.restcloud.rest.UserResource;

/**
 * Contains util static methods
 * 
 * @author albertoep
 */
public class Utils {
	public static String generateExport(String user) {
		return "export ONE_AUTH=" + UserResource.LOGINS_FOLDER_PATH
				+ user + "/.one/one_auth";
	}

}
