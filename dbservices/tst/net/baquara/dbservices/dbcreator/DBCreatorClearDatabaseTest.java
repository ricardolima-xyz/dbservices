package net.baquara.dbservices.dbcreator;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.baquara.dbservices.DBServicesOutput;
import net.baquara.dbservices.Database;
import net.baquara.dbservices.dbcreator.DBCreator;
import net.baquara.dbservices.dbcreator.DBCreatorFactory;



public class DBCreatorClearDatabaseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DBCreatorClearDatabaseTest().run();		
	}
	
	private final DBServicesOutput output = new DBServicesOutput() {
		@Override
		public void writeLine(String line) {
			System.out.println(line);
		}
	};
	
	public void run() {
		try {
			ResourceBundle b = ResourceBundle.getBundle("test");
			Map<String, String> requiredFields = new HashMap<String, String>();
			InputStream dbSchemaIS = getClass().getResourceAsStream("/db-schema.xml");
			DBCreator creator = DBCreatorFactory.create(Database.valueOf(b.getString("database")));
			creator.clearDatabase(b.getString("path"), requiredFields, dbSchemaIS, output);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
