package net.baquara.dbservices.utils;

import java.util.LinkedList;
import java.util.List;

import net.baquara.dbservices.dbschema.Column;
import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.dbschema.TableSchema;

/**
 * Checks all primary and foreign keys of a table. This class was created
 * because Table object is generated by xsd
 */
public class TableKeys {
	
	public class ForeignKey {
		private final String id;
		private final String columnName;
		private final Table referencedTable;
		private final Column referencedColumn;
		
		public ForeignKey(String id, String columnName, Table referencedTable, Column referencedColumn) {
			this.id = id;
			this.columnName = columnName;
			this.referencedTable = referencedTable;
			this.referencedColumn = referencedColumn;
		}

		public String getId() {
			return id;
		}
		
		public String getColumnName() {
			return columnName;
		}
		
		public Table getReferencedTable() {
			return referencedTable;
		}

		public Column getReferencedColumn() {
			return referencedColumn;
		}
	}
	
	private String primaryKeyId;
	private Column primaryKeyColumn;
	private List<ForeignKey> foreignKeys;
	
	public TableKeys(Table table, TableSchema tableSchema) {
		this.foreignKeys = new LinkedList<ForeignKey>();
		
		for (Column column : table.getColumn()) {
			if (column.getPrimaryKeyId() != null) {
				primaryKeyId = column.getPrimaryKeyId();
				this.primaryKeyColumn = column;
			}
			if (column.getForeignKeyId() != null) {
				String columnName = column.getName();
				String foreignKeyId = column.getForeignKeyId();
				for (Table table1 : tableSchema.getTable())
					for (Column column1: table1.getColumn())
						if (column1.getPrimaryKeyId() == null)
							continue;
						else if (column1.getPrimaryKeyId().equals(foreignKeyId)) {
							ForeignKey foreignKey = new ForeignKey(foreignKeyId, columnName, table1, column1);
							foreignKeys.add(foreignKey);
						}
			}
		}
	}
	
	public boolean containForeignKeyByID(String id) {
		for (ForeignKey foreignKey : foreignKeys)
			if (foreignKey.getId().equals(id))
				return true;
		return false;
	}

	public String getPrimaryKeyId() {
		return primaryKeyId;
	}

	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public Column getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}

	
	

}
