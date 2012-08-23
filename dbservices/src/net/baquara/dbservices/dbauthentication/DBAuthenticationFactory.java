package net.baquara.dbservices.dbauthentication;

import net.baquara.dbservices.Database;

public class DBAuthenticationFactory {
	public static DBAuthentication create(Database database) {
		
		switch (database) {
		case HSQLLOCAL:
			return new DBAuthenticationHSQLLocal();
		case POSTGRESQL:
			return new DBAuthenticationPostgreSQL();
		default:
			throw new Error("Not implemented database" + database.toString());
		}
	}
}
