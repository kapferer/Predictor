package filehandling;

import context.PreProAiContext;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads the Predictor-database XML file with the javax XML parser.
 * In this file the database URL, User and Passwords are stored.
 * 
 * @author Wolfgang Kapferer
 *
 */

public class ReadDatabaseXMLFile {

	private static Logger log = Logger.getLogger(ReadDatabaseXMLFile.class.getName());

	public void readDatabaseXMLFile(PreProAiContext preProAiContext) {

		StringBuilder tmp = new StringBuilder();
		tmp.append("\r\n");
		String logSeperator = "->";
		String nodeTextContent;

		try {

			File fXmlFile = new File(preProAiContext.getdBInfoFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			String tagName = "connectionURL";
			NodeList nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			preProAiContext.setConnectionString(nodeTextContent);

			tagName = "user";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			preProAiContext.setUser(nodeTextContent);

			tagName = "passwd";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, "***");
			preProAiContext.setPasswd(nodeTextContent);

			tagName = "schema";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			preProAiContext.setSchema(nodeTextContent);

			log.log(Level.INFO, tmp.toString(), "");

		} catch (Exception e) {
			log.log(Level.SEVERE, "Error readin parameterfile", e.toString());
		}

	}

	/**
	 * The string concatenator for the logger, for logging the read parameters.
	 * 
	 * @param tmp
	 * @param tagName
	 * @param logSeperator
	 * @param nodeTextContent
	 * @return Stringbuilder for the output for the logger
	 */
	private StringBuilder concatLogOutput(StringBuilder tmp, String tagName, String logSeperator,
			String nodeTextContent) {

		tmp.append(tagName);
		tmp.append(logSeperator);
		tmp.append(nodeTextContent);
		tmp.append("\r\n");

		return tmp;
	}

}
