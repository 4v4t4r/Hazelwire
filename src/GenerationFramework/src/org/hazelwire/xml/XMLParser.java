/*******************************************************************************
 * Copyright (c) 2011 The Hazelwire Team.
 *     
 * This file is part of Hazelwire.
 * 
 * Hazelwire is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hazelwire is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hazelwire.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.hazelwire.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Abstract XML parser that implements some basic functionality for use in children.
 * @author Tim Strijdhorst
 */
public abstract class XMLParser
{
	public Document dom;
	
	public XMLParser(String filePath) throws FileNotFoundException
	{
		File file = new File(filePath);
		
	    parseXML(new FileInputStream(file));
	}
	
	public XMLParser(InputStream in)
	{
		parseXML(in);
	}
	
	/**
	 * Create the factory and then create the document by parsing the XML information.
	 * @param lines String that holds the xml document
	 */
	public void parseXML(InputStream in)
	{
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	    try
	    {
	        DocumentBuilder db = dbf.newDocumentBuilder();
	
	        dom = db.parse(in);
	    }
	    catch(IOException e)
	    {
	        e.printStackTrace();
	    }
	    catch(ParserConfigurationException e)
	    {
	        e.printStackTrace();
	    }
	    catch(SAXException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public abstract Object parseDocument() throws Exception;
	
	/**
	* I take a xml element and the tag name, look for the tag and get
	* the text content
	* i.e for <employee><name>John</name></employee> xml snippet if
	* the Element points to employee node and tagName is name I will return John
	* @param ele
	* @param tagName
	* @return
	*/
	public String getTextValue(Element ele, String tagName)
	{
	    String textVal = null;
	    NodeList nl = ele.getElementsByTagName(tagName);
	    if(nl != null && nl.getLength() > 0) {
	        Element el = (Element)nl.item(0);
	        textVal = el.getFirstChild().getNodeValue();
	    }
	
	    return textVal;
	}
	
	/**
	* Calls getTextValue and returns an int value
	* @param ele
	* @param tagName
	* @return
	*/
	public int getIntValue(Element ele, String tagName) throws Exception
	{
	   return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	public long getLongValue(Element ele, String tagName) throws Exception
	{
	    return Long.parseLong(getTextValue(ele,tagName));
	}
}
