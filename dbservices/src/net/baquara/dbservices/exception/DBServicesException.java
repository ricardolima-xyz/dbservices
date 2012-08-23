package net.baquara.dbservices.exception;

public class DBServicesException extends Exception {

	/**
	 * Exception thrown when something wrong happens on DBServices.
	 */
	private static final long serialVersionUID = 1L;

	public DBServicesException() {
		super();
	}

	public DBServicesException(String message) {
		super(message);
	}

	public DBServicesException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBServicesException(Throwable cause) {
		super(cause);
	}

}
