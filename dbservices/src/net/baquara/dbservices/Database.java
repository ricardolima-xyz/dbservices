package net.baquara.dbservices;

/**
 * This enum lists all database implementations available to use with
 * DBServices.
 */
public enum Database {

    /** Denotes HyperSQL database implemented locally using a text file. */
    HSQLLOCAL(new String[] {}, "org.hsqldb.jdbc.JDBCDriver",
	    "org.hibernate.dialect.HSQLDialect"),

    /** Denotes PostgreSQL database. It's known to work with 8.2 - 8.4 versions. */
    POSTGRESQL(new String[] { "Postgres password" }, "org.postgresql.Driver",
	    "org.hibernate.dialect.PostgreSQLDialect");

    /** The required fields for a connection to the database */
    public final String[] accessFields;
    public final String jdbcDriver;
    public final String hibernateDialect;

    private Database(String[] accessFields, String jdbcDriver,
	    String hibernateDialect) {
	this.accessFields = accessFields;
	this.jdbcDriver = jdbcDriver;
	this.hibernateDialect = hibernateDialect;
    }

}
