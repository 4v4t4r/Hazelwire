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
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hazelwire.main.Generator;
import org.hazelwire.modules.Flag;
import org.hazelwire.modules.Module;
import org.hazelwire.modules.Option;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will generate the manifest file that is needed to be able to use the virtualmachine with the game backend.
 * Basicly it just collects the flag information and the location of the deployscript so the server can give the flags to the clients.
 * @author Tim Strijdhorst
 *
 */
public class ManifestGenerator
{		
	public static String saveManifestToDisk(String filePath) throws IOException, ParserConfigurationException, TransformerException
	{
		Collection<Module> modules = Generator.getInstance().getModuleSelector().getSelectedModules().values();
		Iterator<Module> iterate = modules.iterator();
		
		File outputFile = new File(filePath);
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		
		Element rootElement = document.createElement("MANIFEST");
		document.appendChild(rootElement);
		
		while(iterate.hasNext())
		{
			Element moduleRoot = document.createElement("MODULE");
			
			Module module = iterate.next();
			Collection<Flag> flags = module.getFlags();
			Iterator<Flag> flagIterator = flags.iterator();
			
			//<name>
			Element em = document.createElement("name");
			em.appendChild(document.createTextNode(module.getName()));
			moduleRoot.appendChild(em);
			
			try
			{
				if(module.getServicePort() != 0)
				{
					//<serviceport>
					em = document.createElement("serviceport");
					em.appendChild(document.createTextNode(String.valueOf(module.getServicePort())));
					moduleRoot.appendChild(em);
				}
			}
			catch(NumberFormatException e)
			{
				//do nothing ~_~ 
			}
			
			//<deploypath>
			em = document.createElement("deployscript");
			em.appendChild(document.createTextNode(module.getDeployPath()));
			moduleRoot.appendChild(em);
			
			//<flags>
			em = document.createElement("flags");
			
			while(flagIterator.hasNext())
			{
				Element flagElement = document.createElement("flag");
				Element pointsElement = document.createElement("points");
				pointsElement.appendChild(document.createTextNode(String.valueOf(flagIterator.next().getPoints())));
				flagElement.appendChild(pointsElement);
				em.appendChild(flagElement);
			}
			moduleRoot.appendChild(em);
			rootElement.appendChild(moduleRoot);
		}		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(document);
	    StreamResult result = new StreamResult(outputFile);
	    transformer.transform(source, result);
	    String results = result.toString();
	        
		return results;
	}
}
