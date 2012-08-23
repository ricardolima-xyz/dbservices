package net.baquara.dbservices;

import net.baquara.dbservices.dbauthentication.DBAuthentication;
import net.baquara.dbservices.dbauthentication.DBAuthenticationFactory;
import net.baquara.dbservices.dbcreator.DBCreator;
import net.baquara.dbservices.dbcreator.DBCreatorFactory;
import net.baquara.dbservices.exception.DatabaseNotImplementedException;

/**
 * DBServices main class. This is a factory class that calls all the services
 * availabe. It's only necessary to inform the database implementation to get a
 * service.
 */
public class DBServices {

    /**
     * Creates a DBAuthentication instance.
     * 
     * @param database
     *            The type of database used. Possible values are
     *            net.baquara.dbservices.Database enum values.
     * @return New DBAuthentication instance.
     */
    public static DBAuthentication createDBAuthentication(Database database) {
	return DBAuthenticationFactory.create(database);
    }

    /**
     * Creates a DBAuthentication instance.
     * 
     * @param database
     *            The type of database used. Possible values are the string
     *            equivalents of net.baquara.dbservices.Database enum values.
     * @return New DBAuthentication instance.
     * @throws DatabaseNotImplementedException
     *             If database provided is not implemented by DBServices.
     */
    public static DBAuthentication createDBAuthentication(String database)
	    throws DatabaseNotImplementedException {
	try {
	    Database databaseEnum = Database.valueOf(database);
	    return createDBAuthentication(databaseEnum);
	} catch (IllegalArgumentException e) {
	    throw new DatabaseNotImplementedException(database);
	}
    }

    /**
     * Creates a DBCreator instance.
     * 
     * @param database
     *            The type of database used. Possible values are
     *            net.baquara.dbservices.Database enum values.
     * @return New DBCreator instance.
     */
    public static DBCreator createDBCreator(Database database) {
	return DBCreatorFactory.create(database);
    }

    /**
     * Creates a DBCreator instance.
     * 
     * @param database
     *            The type of database used. Possible values are the string
     *            equivalents of net.baquara.dbservices.Database enum values.
     * @return New DBCreator instance.
     * @throws DatabaseNotImplementedException
     *             If database provided is not implemented by DBServices.
     */
    public static DBCreator createDBCreator(String database)
	    throws DatabaseNotImplementedException {
	try {
	    Database databaseEnum = Database.valueOf(database);
	    return createDBCreator(databaseEnum);
	} catch (IllegalArgumentException e) {
	    throw new DatabaseNotImplementedException(database);
	}
    }
}
