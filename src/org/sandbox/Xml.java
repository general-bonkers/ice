package org.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
 
public class Xml {
	public static void main(String[] args) {
 
	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File("c:\\test.xml");
 
	  try {
 
		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List list = rootNode.getChildren("staff");
 
		for (int i = 0; i < list.size(); i++) {
 
		   Element node = (Element) list.get(i);
		   System.out.println("First Name : " + node.getChildText("firstname"));
		   System.out.println("Last Name : " + node.getChildText("lastname"));
		   System.out.println("Nick Name : " + node.getChildText("nickname"));
		   System.out.println("Salary : " + node.getChildText("salary"));
 
		}
 
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  } catch (JDOMException jdomex) {
		System.out.println(jdomex.getMessage());
	  }
	}
}