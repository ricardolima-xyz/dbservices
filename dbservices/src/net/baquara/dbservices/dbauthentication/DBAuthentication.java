package net.baquara.dbservices.dbauthentication;

public abstract class DBAuthentication {

	public enum Outcome {
		/** Connection Successful */
		Success,
		/** Incorrect username or password provided. */
		AuthenticationError,
		/** Database has not been created or initialized.*/
		DatabaseError,
		/** Database managment system is not accessible. */
		ConnectionError
	}

	protected DBAuthentication() {}

	public abstract DBAuthentication.Outcome testAuthentication(String path, String username, String password);

}
