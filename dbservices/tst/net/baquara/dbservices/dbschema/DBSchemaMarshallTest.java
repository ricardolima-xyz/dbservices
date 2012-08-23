package net.baquara.dbservices.dbschema;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.baquara.dbservices.dbschema.Column;
import net.baquara.dbservices.dbschema.DBSchema;
import net.baquara.dbservices.dbschema.DataType;
import net.baquara.dbservices.dbschema.ObjectFactory;
import net.baquara.dbservices.dbschema.SuperUser;
import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.dbschema.TableSchema;

public class DBSchemaMarshallTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Column col = new Column();
			col.setName("telefone");
			col.setDatatype(DataType.STRING);
			
			Table table = new Table();
			table.setName("fornecedores");
			table.getColumn().add(col);
			
			TableSchema ts = new TableSchema();
			ts.getTable().add(table);
			
			SuperUser superUser = new SuperUser();
			superUser.setUsername("user");
			superUser.setPassword("pass");
			
			DBSchema dbSchema = new DBSchema();
			dbSchema.setSuperUser(superUser);
			dbSchema.setTableSchema(ts);
			
			JAXBContext context = JAXBContext.newInstance("net.baquara.dbservices.dbschema");
			Marshaller marshaller = context.createMarshaller();
			JAXBElement<DBSchema> element = new ObjectFactory()
					.createDbSchema(dbSchema);
			marshaller.marshal(element, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
