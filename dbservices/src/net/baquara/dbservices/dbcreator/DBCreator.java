package net.baquara.dbservices.dbcreator;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import net.baquara.dbservices.DBSchemaLoader;
import net.baquara.dbservices.DBServicesOutput;
import net.baquara.dbservices.dbschema.Column;
import net.baquara.dbservices.dbschema.DBSchema;
import net.baquara.dbservices.dbschema.DataType;
import net.baquara.dbservices.dbschema.Entry;
import net.baquara.dbservices.dbschema.Row;
import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.dbschema.TableData;
import net.baquara.dbservices.exception.DBServicesException;
import net.baquara.dbservices.exception.TableSchemaHasCyclesException;
import net.baquara.dbservices.utils.SortedTableList;
import net.baquara.dbservices.utils.TableKeys;

public abstract class DBCreator {

	//TODO precisa retornar erro se DBSchema não for bem formado!
	
	public abstract void clearDatabase(String path,
			Map<String, String> accessFields, InputStream dbSchemaInputStream, DBServicesOutput output)
			throws DBServicesException;

	public void createDatabase(String path, Map<String, String> accessFields, InputStream dbSchemaInputStream, DBServicesOutput output) throws DBServicesException, SQLException, TableSchemaHasCyclesException {
			
		DBSchema dbSchema = new DBSchemaLoader().loadDBSchema(dbSchemaInputStream);
		createDBStructure(path, accessFields, dbSchema, output);
			
		// Setting connection using super user (recently created user)
		Connection connection = DriverManager.getConnection(path , dbSchema
				.getSuperUser().getUsername(), dbSchema.getSuperUser()
				.getPassword());
			//Mapping tabledata by their table name
			Map<String, TableData> tableDataMap = new HashMap<String, TableData>();
			for (TableData tableData : dbSchema.getTableData())
				tableDataMap.put(tableData.getTable(), tableData);
			// Table topological sort
			SortedTableList sortedTableList = new SortedTableList(dbSchema.getTableSchema()); 
			
			// Creating tables
			for (Table table : sortedTableList)
				createTableOnDB(connection, table, new TableKeys(table, dbSchema.getTableSchema()), output);
			// Inserting data
			for (Table table : sortedTableList)
				insertDataOnDB(connection, table, tableDataMap
						.get(table.getName()), output);
			connection.commit();
			connection.close();
	}

	protected abstract void createDBStructure(String path, Map<String, String> accessFields, DBSchema dbSchema, DBServicesOutput output) throws SQLException;
	
	protected abstract void createTableOnDB(Connection connection, Table table, TableKeys tableKeys, DBServicesOutput output) throws SQLException;

	public void insertDataOnDB(Connection connection, Table table,
			TableData tableData, DBServicesOutput output) throws DBServicesException, SQLException {
		if (tableData == null)
			return;

		// Mapping columns and its datatypes
		Map<String, DataType> columnMap = new HashMap<String, DataType>();
		for (Column c : table.getColumn())
			columnMap.put(c.getName(), c.getDatatype());

		Statement statement;
		for (Row row : tableData.getRow()) {
			statement = connection.createStatement();
			StringBuilder builder = new StringBuilder();
			builder.append("insert into ");
			builder.append(table.getName());
			builder.append(" (");

			// Inserting column names
			for (Entry entry : row.getEntry()) {
				builder.append(entry.getColumn());
				builder.append(" ,");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append(" ) values (");

			// Inserting column values
			for (Entry entry : row.getEntry()) {
				try {
					DataType type = columnMap.get(entry.getColumn());
					builder.append(sqlDataFormat(entry.getValue(), type));
					builder.append(" ,");
				} catch (ParseException e) {
					throw new DBServicesException(e);
				}
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append(")");
			if (output != null) output.writeLine(builder.toString());
			statement.execute(builder.toString());
			statement.close();			
		}
		
	}
	
	protected abstract String sqlDataFormat(String content,	DataType dataType) throws ParseException;

}
