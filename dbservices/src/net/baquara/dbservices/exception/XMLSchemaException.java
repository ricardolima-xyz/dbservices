package net.baquara.dbservices.exception;

/** Exception thrown when xml schema file is missing some attribute. */
public class XMLSchemaException extends Exception {

	//TODO: A good idea would be showing WHERE on XML file the error IS!
	private static final long serialVersionUID = 1L;

	public XMLSchemaException() {
	}

	public XMLSchemaException(String message) {
		super(message);
	}

	public XMLSchemaException(Throwable cause) {
		super(cause);
	}

	public XMLSchemaException(String message, Throwable cause) {
		super(message, cause);
	}

}
