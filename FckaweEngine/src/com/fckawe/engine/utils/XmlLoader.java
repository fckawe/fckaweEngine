package com.fckawe.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fckawe.engine.core.Session;

/**
 * Helper that loads the content of an XML file into a simple HashMap. Each
 * value is identified by a key (tree path from the XML file). The value is
 * stored a String.
 * 
 * @author fckawe
 */
public class XmlLoader {

	private final String filePath;

	private final InputStream inStream;

	private final Map<String, String> values = new HashMap<String, String>();

	/**
	 * Creates a new instance, specifying the XML file to load.
	 * 
	 * @param filePath
	 *            The XML file to load.
	 */
	public XmlLoader(final String filePath) {
		this.filePath = filePath;
		inStream = Session.class.getResourceAsStream(filePath);
		load();
	}

	/**
	 * Loads or reloads the content of the XML file, which was specified with
	 * the object instantiation.
	 */
	public void load() {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser parser = factory.newSAXParser();
			Handler handler = new Handler();
			parser.parse(inStream, handler);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(
					"Cannot read XML file '"
							+ filePath
							+ "' due to problems with configurating the SAX xml parser!",
					e);
		} catch (SAXException e) {
			throw new RuntimeException("Cannot read XML file '" + filePath
					+ "' due to problems with the SAX xml parser!", e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read XML file '" + filePath
					+ "' due to I/O problems!", e);
		}
	}

	/**
	 * Returns the map with the content of the XML file. Key is the simple
	 * string representation of the XML path.
	 * 
	 * @return The content map.
	 */
	public Map<String, String> getValues() {
		return values;
	}

	/**
	 * Handler that tells the SAX parser how to interpret the read content out
	 * of the XML file.
	 * 
	 * @author fckawe
	 */
	private class Handler extends DefaultHandler {

		private static final String FCKAWE_ID = "fckawe_id";

		private final Breadcrumb path = new Breadcrumb();

		private final StringBuilder content = new StringBuilder();

		private boolean inElement;

		/**
		 * The SAX parser has found the opening tag of an element. First, all
		 * the attributes of the opening tag get appended to the result map.
		 * Then the SAX parser will continue its work.
		 */
		@Override
		public void startElement(final String uri, final String localName,
				final String qName, final Attributes attrs) throws SAXException {
			String pathPart = qName;

			for (int i = 0; i < attrs.getLength(); i++) {
				String attrQName = attrs.getQName(i);
				String value = attrs.getValue(attrQName);
				if (attrQName.equals(FCKAWE_ID)) {
					pathPart += "." + value;
					break;
				}
			}

			path.push(pathPart);

			for (int i = 0; i < attrs.getLength(); i++) {
				String attrQName = attrs.getQName(i);
				if (attrQName.equals(FCKAWE_ID)) {
					continue;
				}
				String value = attrs.getValue(attrQName);
				path.push(attrQName);
				String key = path.toString();
				if (values.containsKey(key)) {
					value = values.get(key) + value;
				}
				values.put(key, value);
				path.pop();
			}

			content.setLength(0);
			inElement = true;
		}

		/**
		 * This method is called for read characters between the tags. If the
		 * parser is currently positioned inside of an element the characters
		 * get appended to the content variable.
		 */
		@Override
		public void characters(final char[] cBuf, final int offset,
				final int len) throws SAXException {
			if (inElement) {
				content.append(new String(cBuf, offset, len));
			}
		}

		/**
		 * The SAX parser has found the closing tag of an element. If we found
		 * content characters for that element it gets appended to the result
		 * map.
		 */
		@Override
		public void endElement(final String uri, final String localName,
				final String qName) throws SAXException {
			inElement = false;
			if (content.length() > 0) {
				String key = path.toString();
				String value = content.toString();
				if (values.containsKey(key)) {
					value = values.get(key) + value;
				}
				values.put(key, value);
			}
			content.setLength(0);
			path.pop();
		}

	}

}
