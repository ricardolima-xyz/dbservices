package net.baquara.dbservices.dbauthentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;

public class DBAuthenticationHSQLLocal extends DBAuthentication {

	@Override
	public Outcome testAuthentication(String path, String username, String password) {

		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}

		try {
			Connection connection = DriverManager.getConnection(path + ";ifexists=true", username, password);
			connection.close();
		} catch (SQLInvalidAuthorizationSpecException e) {
			return Outcome.AuthenticationError;
		} catch (org.hsqldb.HsqlException e) {
			if (e.getErrorCode() == -465)
				return Outcome.ConnectionError;
			else
				return Outcome.DatabaseError;
		} catch (SQLException e) {
			if (e.getErrorCode() == -465)
				return Outcome.ConnectionError;
			else
				return Outcome.DatabaseError;
		}
		return Outcome.Success;
	}

}
