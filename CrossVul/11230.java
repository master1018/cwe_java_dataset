package org.dom4j;
import org.testng.annotations.Test;
public class AllowedCharsTest {
    @Test
    public void localName() {
        QName.get("element");
        QName.get(":element");
        QName.get("elem:ent");
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void localNameFail() {
        QName.get("!element");
    }
    @Test
    public void qname() {
        QName.get("element", "http:
        QName.get("ns:element", "http:
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void qnameFail1() {
        QName.get("ns:elem:ent", "http:
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void qnameFail2() {
        QName.get(":nselement", "http:
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementLT() {
        DocumentHelper.createElement("element<name");
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementGT() {
        DocumentHelper.createElement("element>name");
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementAmpersand() {
        DocumentHelper.createElement("element&name");
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElement() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("element>name");
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementQualified() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("element>name", "http:
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementQualifiedPrefix() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("ns:element>name", "http:
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementPrefix() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("ns>:element", "http:
    }
}
