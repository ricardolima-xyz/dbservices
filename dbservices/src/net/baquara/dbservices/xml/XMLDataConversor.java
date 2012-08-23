package net.baquara.dbservices.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLDataConversor {

	public final static DateFormat XMLDateFormat = new SimpleDateFormat(
			"yyyy.MM.dd 'at' HH:mm:ss z");
	
	public final static String XMLNullObject = "null";

	public static boolean denotesNullObject(String xmlString) {
		if (xmlString == null) return true; 
		// TODO: A linha acima foi inserida pra evitar erro em um 
		// backup mal formado. Ocorre quando um xml schema possui
		// colunas no tabledata que nao existem mais no tableschema.
		else return xmlString.equals(XMLNullObject);
	}

	public static Date desserialize(String string) throws ParseException {
		return denotesNullObject(string) ? null : XMLDateFormat.parse(string);
	}

	public static String serialize(Boolean boo) {
		if (boo == null)
			return XMLNullObject;
		else
			return Boolean.toString(boo);
	}

	public static String serialize(Date date) {
		if (date == null)
			return XMLNullObject;
		else
			return XMLDateFormat.format(date);
	}

	public static String serialize(Double dbl) {
		if (dbl == null)
			return XMLNullObject;
		else
			return Double.toString(dbl);
	}

	public static String serialize(Integer itg) {
		if (itg == null)
			return XMLNullObject;
		else
			return Integer.toString(itg);
	}

	public static String serialize(Long lng) {
		if (lng == null)
			return XMLNullObject;
		else
			return Long.toString(lng);
	}

	public static String serialize(String str) {
		if (str == null)
			return XMLNullObject;
		else
			return str;
	}
}
