package streets;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import javax.swing.SwingUtilities;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SearchThread implements Runnable {

	private static final String urlPrefix = "http://geoportal.wroclaw.pl/rest/rest/streets/byphrase/0/100/";
	private static final String login = "dostep@publiczny";
	private static final String password = "publiczny";
	private StreetSearchFrame frame;
	private Node nNode;
	private NodeList nList;

	public SearchThread(StreetSearchFrame test) {
		this.frame = test;
	}

	public void run() {

		/*
		 * offset = 0, the maximum is 100 (the site doesn't display more than
		 * 100 results)
		 */

		String uri = (urlPrefix + frame.txtFieldSearch.getText() + ".xml");

		try {
			URL url = new URL(uri);

			// Sets login and password to access server.
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(login, password.toCharArray());
				}
			});

			// Create and setup connection.
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// Read server response.
			InputStream xml = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml); // Create document from a stream

			doc.getDocumentElement().normalize(); // extract the root element

			// Extract list of elements with "street" tag.
			nList = doc.getElementsByTagName("street");

			// Switch back to main thread and update UI.
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.pnlLoadingCircle.setVisible(false);

					if (nList.getLength() == 0) {
						frame.pnlLoadingCircle.setVisible(false);
						frame.txtAreaResultSet.setText("No results.");
					} else {
						for (int temp = 0; temp < nList.getLength(); temp++) {

							nNode = nList.item(temp);

							// check if the node is type of Node
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {

								Element eElement = (Element) nNode;

								String string = "Id : " + eElement.getElementsByTagName("id").item(0).getTextContent()
										+ "\n" + "Name : "
										+ eElement.getElementsByTagName("nameOfficial").item(0).getTextContent() + "\n"
										+ "Type : "
										+ eElement.getElementsByTagName("typeName").item(0).getTextContent() + "\n"
										+ "Status : "
										+ eElement.getElementsByTagName("status").item(0).getTextContent() + "\n"
										+ "-------------------------------" + "\n";

								frame.txtAreaResultSet.append(string);
							}
						}
					}
				}
			});
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
