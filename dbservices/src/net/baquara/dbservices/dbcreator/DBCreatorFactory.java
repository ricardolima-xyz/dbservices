package net.baquara.dbservices.dbcreator;

import net.baquara.dbservices.Database;

public class DBCreatorFactory {
	public static DBCreator create(Database database) {
		
		switch (database) {
		case HSQLLOCAL:
			return new DBCreatorHSQLLocal();
		case POSTGRESQL:
			return new DBCreatorPostgreSQL();
		default:
			throw new Error("Not implemented database" + database.toString());
		}
	}
}
