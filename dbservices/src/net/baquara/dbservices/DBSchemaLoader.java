package net.baquara.dbservices;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.baquara.dbservices.dbschema.DBSchema;
import net.baquara.dbservices.exception.DBServicesException;

public class DBSchemaLoader {

    @SuppressWarnings("unchecked")
    public DBSchema loadDBSchema(InputStream inputStream)
	    throws DBServicesException {
	try {
	    JAXBContext context = JAXBContext
		    .newInstance("net.baquara.dbservices.dbschema");
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    JAXBElement<DBSchema> element = (JAXBElement<DBSchema>) unmarshaller
		    .unmarshal(inputStream);
	    return element.getValue();
	} catch (JAXBException e) {
	    throw new DBServicesException(e);
	}

    }
}
