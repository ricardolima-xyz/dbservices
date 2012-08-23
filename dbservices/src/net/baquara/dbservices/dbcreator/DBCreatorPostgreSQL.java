package net.baquara.dbservices.dbcreator;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.baquara.dbservices.DBSchemaLoader;
import net.baquara.dbservices.DBServicesOutput;
import net.baquara.dbservices.Database;
import net.baquara.dbservices.dbschema.Column;
import net.baquara.dbservices.dbschema.DBSchema;
import net.baquara.dbservices.dbschema.DataType;
import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.exception.DBServicesException;
import net.baquara.dbservices.utils.TableKeys;
import net.baquara.dbservices.utils.TableKeys.ForeignKey;
import net.baquara.dbservices.xml.XMLDataConversor;

public class DBCreatorPostgreSQL extends DBCreator {

	private final DateFormat postgreDateFormat;

	public DBCreatorPostgreSQL() {
		postgreDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void clearDatabase(String path, Map<String, String> accessFields, InputStream dbSchemaInputStream, DBServicesOutput output) throws DBServicesException {

		DBSchema dbSchema = new DBSchemaLoader().loadDBSchema(dbSchemaInputStream);
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		// Separating database name from database location
		int i;
		for (i = path.length() - 1; i >= 0; i--)
			if (path.charAt(i) == '/' || path.charAt(i) == ':')
				break;

		String databaseHost = path.substring(0, i);
		String databasename = path.substring(i + 1);
		String password = accessFields.get(Database.POSTGRESQL.accessFields[0]);

		try {
			Connection connection = DriverManager.getConnection(databaseHost, "postgres", password);
			Statement statement = connection.createStatement();

			// Deleting database
			StringBuilder sb1 = new StringBuilder();
			sb1.append("drop database ");
			sb1.append(databasename);
			sb1.append(";");

			statement.execute(sb1.toString());
			if (output != null)
				output.writeLine(sb1.toString());

			// Deleting super user
			StringBuilder sb2 = new StringBuilder();
			sb2.append("drop user ");
			sb2.append(dbSchema.getSuperUser().getUsername());
			sb2.append(";");

			statement.execute(sb2.toString());
			if (output != null)
				output.writeLine(sb2.toString());

			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DBServicesException(e);
		}
	}

	@Override
	protected void createDBStructure(String path, Map<String, String> accessFields, DBSchema dbSchema, DBServicesOutput output) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}

		// Separating database name from database location
		int i;
		for (i = path.length() - 1; i >= 0; i--)
			if (path.charAt(i) == '/' || path.charAt(i) == ':')
				break;

		String databaseHost = path.substring(0, i);
		String databasename = path.substring(i + 1);
		String password = accessFields.get(Database.POSTGRESQL.accessFields[0]);

		// Setting connection using master user (postgres user)
		Connection connection = DriverManager.getConnection(databaseHost, "postgres", password);
		Statement statement = connection.createStatement();

		// Creating super user
		StringBuilder sb1 = new StringBuilder();
		sb1.append("create user ");
		sb1.append(dbSchema.getSuperUser().getUsername());
		sb1.append(" with password \'");
		sb1.append(dbSchema.getSuperUser().getPassword());
		sb1.append("\' superuser;");

		statement.execute(sb1.toString());
		if (output != null)
			output.writeLine(sb1.toString());

		// Creating data base
		StringBuilder sb2 = new StringBuilder();
		sb2.append("create database ");
		sb2.append(databasename);
		sb2.append(" with owner = ");
		sb2.append(dbSchema.getSuperUser().getUsername());
		sb2.append(";");

		statement.execute(sb2.toString());
		if (output != null)
			output.writeLine(sb2.toString());

		// Granting privileges
		StringBuilder sb3 = new StringBuilder();
		sb3.append("grant all privileges on database ");
		sb3.append(databasename);
		sb3.append(" to ");
		sb3.append(dbSchema.getSuperUser().getUsername());
		sb3.append(";");

		statement.execute(sb3.toString());
		if (output != null)
			output.writeLine(sb3.toString());

		// Closing connecton using master user (postgres)
		statement.close();
		connection.close();
	}

	protected void createTableOnDB(Connection connection, Table table, TableKeys tableKeys, DBServicesOutput output) throws SQLException {
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(table.getName());
		builder.append("(");
		for (Column column : table.getColumn()) {
			builder.append(column.getName());
			builder.append(' ');
			builder.append(sqlDataType(column));
			builder.append(',');
		}
		if (tableKeys.getPrimaryKeyId() != null) {
			builder.append("constraint \"");
			builder.append(tableKeys.getPrimaryKeyId());
			builder.append("\" primary key (");
			builder.append(tableKeys.getPrimaryKeyColumn().getName());
			builder.append(')');
			builder.append(',');
		}

		for (ForeignKey foreignKey : tableKeys.getForeignKeys()) {
			builder.append("constraint \"");
			builder.append(foreignKey.getId());
			builder.append("\" foreign key (");
			builder.append(foreignKey.getColumnName());
			builder.append(") references ");
			builder.append(foreignKey.getReferencedTable().getName());
			builder.append(" (");
			builder.append(foreignKey.getReferencedColumn().getName());
			builder.append(") match simple on update cascade on delete set null");
			builder.append(',');
		}

		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");

		Statement statement = connection.createStatement();
		statement.execute(builder.toString());
		if (output != null)
			output.writeLine(builder.toString());
		statement.close();
	}

	protected String sqlDataFormat(String content, DataType dataType) throws ParseException {
		if (XMLDataConversor.denotesNullObject(content))
			return "null";
		switch (dataType) {
		case BOOLEAN:
			return content;
		case DATE:
			Date d = XMLDataConversor.desserialize(content);
			return "'" + postgreDateFormat.format(d) + "'";
		case DOUBLE:
			return content;
		case INTEGER:
			return content;
		case LONG:
			return content;
		case STRING:
			return "'" + content + "'";
		default:
			throw new Error("Type " + dataType.toString() + "not implemented!");
		}
	}

	// Needs Column object because of varchar size
	private String sqlDataType(Column column) {
		StringBuilder sqlDataType = new StringBuilder();
		switch (column.getDatatype()) {
		case BOOLEAN:
			sqlDataType.append("boolean");
			break;
		case DATE:
			sqlDataType.append("timestamp");
			break;
		case DOUBLE:
			sqlDataType.append("float");
			break;
		case INTEGER:
			sqlDataType.append("integer");
			break;
		case LONG:
			sqlDataType.append("bigint");
			break;
		case STRING:
			sqlDataType.append("varchar(");
			sqlDataType.append(column.getSize());
			sqlDataType.append(")");
			break;
		default:
			throw new Error("Type " + column.getDatatype().toString() + "not implemented!");
		}
		return sqlDataType.toString();
	}

}
