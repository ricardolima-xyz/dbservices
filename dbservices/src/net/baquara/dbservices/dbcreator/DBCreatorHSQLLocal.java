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

import net.baquara.dbservices.DBServicesOutput;
import net.baquara.dbservices.dbschema.Column;
import net.baquara.dbservices.dbschema.DBSchema;
import net.baquara.dbservices.dbschema.DataType;
import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.exception.DBServicesException;
import net.baquara.dbservices.utils.TableKeys;
import net.baquara.dbservices.utils.TableKeys.ForeignKey;
import net.baquara.dbservices.xml.XMLDataConversor;

public class DBCreatorHSQLLocal extends DBCreator {

	private final DateFormat hsqlDateFormat;

	public DBCreatorHSQLLocal() {
		hsqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void clearDatabase(String path, Map<String, String> accessFields,
			InputStream dbSchemaInputStream, DBServicesOutput output) throws DBServicesException {

		// Separating database name from database location
		int i;
		for (i = path.length() - 1; i >= 0; i--)
			if (path.charAt(i) == '/' || path.charAt(i) == ':')
				break;

		String databasename = path.substring(i + 1);
		
		output.writeLine("In order to delete the database located on ");
		output.writeLine(path);
		output.writeLine("you have to manually delete the follonging files/folders: ");
		output.writeLine("");
		output.writeLine("- file " + databasename + ".lck");
		output.writeLine("- file " + databasename + ".log");
		output.writeLine("- file " + databasename + ".properties");
		output.writeLine("- file " + databasename + ".script");
		output.writeLine("- file " + databasename + ".data");
		output.writeLine("- file " + databasename + ".data.old");
		output.writeLine("- file " + databasename + ".backup");
		output.writeLine("- file " + databasename + ".lobs");
		output.writeLine("- folder " + databasename + ".tmp");
		output.writeLine("");
		output.writeLine("The files/folders above are located on application's root directory. In most cases not all the files listed above will be present.");
	}

	@Override
	protected void createDBStructure(String path,
			Map<String, String> accessFields, DBSchema dbSchema, DBServicesOutput output)
			throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		
		// Setting connection using master user (postgres user)
		Connection connection = DriverManager.getConnection(path, dbSchema.getSuperUser().getUsername(), dbSchema.getSuperUser().getPassword());
		Statement statement = connection.createStatement();
		statement.close();
		connection.close();
	}

	protected void createTableOnDB(Connection connection, Table table, TableKeys tableKeys, DBServicesOutput output) throws SQLException {
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(table.getName());
		builder.append("( ");
		
		for (Column column : table.getColumn()) {
			builder.append(column.getName());
			builder.append(' ');
			builder.append(sqlDataType(column));
			builder.append(',');
		}
		if (tableKeys.getPrimaryKeyId() != null) {
			builder.append("constraint \"");
			builder.append(tableKeys.getPrimaryKeyId());
			builder.append(table.getName());
			builder.append("PK");
			builder.append("\" primary key (");
			builder.append(tableKeys.getPrimaryKeyColumn().getName());
			builder.append(')');
			builder.append(',');
		}

		for (ForeignKey foreignKey : tableKeys.getForeignKeys()) {
			builder.append("constraint \"");
			builder.append(foreignKey.getId());
			builder.append(table.getName());
			builder.append("FK");
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

		if (output != null) output.writeLine(builder.toString());
		Statement statement = connection.createStatement();
		statement.execute(builder.toString());
		
		statement.close();
	}

	protected String sqlDataFormat(String content, DataType dataType)
			throws ParseException {
		if (XMLDataConversor.denotesNullObject(content))
			return "null";
		switch (dataType) {
		case BOOLEAN:
			return content;
		case DATE:
			Date d = XMLDataConversor.desserialize(content);
			return "'" + hsqlDateFormat.format(d) + "'";
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
