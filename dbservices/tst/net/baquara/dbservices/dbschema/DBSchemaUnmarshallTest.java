package net.baquara.dbservices.dbschema;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.baquara.dbservices.dbschema.DBSchema;

public class DBSchemaUnmarshallTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		try {			
			JAXBContext context = JAXBContext.newInstance("net.baquara.dbservices.dbschema");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<DBSchema> element = (JAXBElement<DBSchema>) unmarshaller.unmarshal(new File("res/db-schema.xml"));
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(element, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
