package net.baquara.dbservices.dbauthentication;

import java.util.ResourceBundle;

import net.baquara.dbservices.DBServices;

public class DBAuthenticationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DBAuthenticationTest().run();
	}
	
	
	public void run() {
		ResourceBundle b = ResourceBundle.getBundle("test");

		try {
			DBAuthentication dbAuthentication = DBServices.createDBAuthentication(b.getString("database"));
			DBAuthentication.Outcome outcome = dbAuthentication.testAuthentication(b.getString("path"), b.getString("dbauthentication.test.username"), b.getString("dbauthentication.test.password"));
			System.out.println(outcome);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
