//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.27 at 09:54:00 PM BRT 
//


package net.baquara.dbservices.dbschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DBSchema complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DBSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="super-user" type="{}SuperUser"/>
 *         &lt;element name="table-schema" type="{}TableSchema"/>
 *         &lt;element name="table-data" type="{}TableData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DBSchema", propOrder = {
    "superUser",
    "tableSchema",
    "tableData"
})
public class DBSchema {

    @XmlElement(name = "super-user", required = true)
    protected SuperUser superUser;
    @XmlElement(name = "table-schema", required = true)
    protected TableSchema tableSchema;
    @XmlElement(name = "table-data")
    protected List<TableData> tableData;

    /**
     * Gets the value of the superUser property.
     * 
     * @return
     *     possible object is
     *     {@link SuperUser }
     *     
     */
    public SuperUser getSuperUser() {
        return superUser;
    }

    /**
     * Sets the value of the superUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuperUser }
     *     
     */
    public void setSuperUser(SuperUser value) {
        this.superUser = value;
    }

    /**
     * Gets the value of the tableSchema property.
     * 
     * @return
     *     possible object is
     *     {@link TableSchema }
     *     
     */
    public TableSchema getTableSchema() {
        return tableSchema;
    }

    /**
     * Sets the value of the tableSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableSchema }
     *     
     */
    public void setTableSchema(TableSchema value) {
        this.tableSchema = value;
    }

    /**
     * Gets the value of the tableData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tableData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTableData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TableData }
     * 
     * 
     */
    public List<TableData> getTableData() {
        if (tableData == null) {
            tableData = new ArrayList<TableData>();
        }
        return this.tableData;
    }

}