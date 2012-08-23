package net.baquara.dbservices.dbauthentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBAuthenticationPostgreSQL extends DBAuthentication {

	@Override
	public DBAuthentication.Outcome testAuthentication(String path, String username, String password) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			return DBAuthentication.Outcome.ConnectionError;
		}
		try {
			Connection connection = DriverManager.getConnection(path, username, password);
			connection.close();
			return DBAuthentication.Outcome.Success;
		} catch (SQLException e) {
			if (e.getSQLState().equals("08004"))
				return DBAuthentication.Outcome.ConnectionError;
			else if (e.getSQLState().equals("3D000"))
				return DBAuthentication.Outcome.DatabaseError;
			else if (e.getSQLState().equals("28000"))
				return DBAuthentication.Outcome.AuthenticationError;
			else
				return DBAuthentication.Outcome.ConnectionError;
		}
	}

}
