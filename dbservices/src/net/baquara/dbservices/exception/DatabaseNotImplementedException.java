package net.baquara.dbservices.exception;


public class DatabaseNotImplementedException extends Exception {

	private static final long serialVersionUID = 1L;

	public DatabaseNotImplementedException(String databaseName) {
		super("Database " + databaseName + "not implemented.");
	}
	
	

}
