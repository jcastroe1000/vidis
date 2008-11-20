package vidis.sim.xml.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vidis.sim.classloader.modules.impl.AModuleFile;
import vidis.sim.classloader.modules.impl.dir.FileModuleFile;
import vidis.sim.exceptions.DocumentMalformedException;
import vidis.sim.xml.CommonDomParser;
import vidis.sim.xml.modules.dataStructure.DocumentData;
import vidis.sim.xml.modules.dataStructure.DocumentDataConnection;
import vidis.sim.xml.modules.dataStructure.DocumentDataLink;
import vidis.sim.xml.modules.dataStructure.DocumentDataNode;

/**
 * this class should - once finished - read .msim module files
 * 
 * @author dominik
 * 
 */
public class XMLModuleReader implements CommonDomParser {
	private AModuleFile simFile;
	private DocumentData document;

	private XMLModuleReader(AModuleFile simFile, DocumentData document) {
		this.simFile = simFile;
		this.document = document;
	}

	private static DocumentData analyze(Document document) throws DocumentMalformedException {
		// analyze here
		NodeList rootNodes = document.getElementsByTagName("module");
		if (rootNodes.getLength() == 1) {
			DocumentData data = DocumentData.getInstance();
			for (int i = 0; i < rootNodes.getLength(); i++) {
				Node module = rootNodes.item(i);
				if (module.hasChildNodes()) {
					int moduleNodesFound = 0;
					moduleNodesFound = analyze_module(data, module,
							moduleNodesFound);
					analyze_module_safecheck(moduleNodesFound);
				} else {
					throw new DocumentMalformedException("module malformed");
				}
			}
			return data;
		} else {
			return null;
		}
	}

	private static void analyze_module_safecheck(int moduleNodesFound)
			throws DocumentMalformedException {
		if (!(moduleNodesFound == 4 || moduleNodesFound == 5)) {
			throw new DocumentMalformedException("module malformed; expected 4 elements but got " + moduleNodesFound);
		}
	}

	private static int analyze_module(DocumentData data, Node module,
			int moduleNodesFound) throws DocumentMalformedException {
		for (int j = 0; j < module.getChildNodes().getLength(); j++) {
			Node moduleChild = module.getChildNodes().item(j);
			if (moduleChild.getNodeName().equalsIgnoreCase("id")) {
				moduleNodesFound = analyze_id(data,
						moduleNodesFound, moduleChild);
			} else if (moduleChild.getNodeName().equalsIgnoreCase("classpath")) {
				moduleNodesFound = analyze_classpath(data,
						moduleNodesFound, moduleChild);
			} else if (moduleChild.getNodeName().equalsIgnoreCase("nodeDensity")) {
				moduleNodesFound = analyze_nodeDensity(data,
						moduleNodesFound, moduleChild);
			} else if (moduleChild.getNodeName().equalsIgnoreCase("objects")) {
				moduleNodesFound = analyze_objects(data,
						moduleNodesFound, moduleChild);
			} else if (moduleChild.getNodeName().equalsIgnoreCase("connections")) {
				moduleNodesFound = analyze_connections(data,
						moduleNodesFound, moduleChild);
			}
		}
		return moduleNodesFound;
	}

	private static int analyze_objects(DocumentData data, int moduleNodesFound,
			Node moduleChild) throws DocumentMalformedException {
		for (int k = 0; k < moduleChild.getChildNodes().getLength(); k++) {
			Node node = moduleChild.getChildNodes().item(k);
			if (node.getNodeName().equalsIgnoreCase("node")) {
				analyze_objects_node(data, node);
			} else if (node.getNodeName().equalsIgnoreCase("link")) {
				analyze_objects_link(data, node);
			}
		}
		moduleNodesFound++;
		return moduleNodesFound;
	}

	private static void analyze_objects_link(DocumentData data, Node node)
			throws DocumentMalformedException {
		if (node.hasChildNodes()) {
			int nodeNodesFound = 0;
			DocumentDataLink dataLink = DocumentDataLink.getInstance();
			for (int l = 0; l < node.getChildNodes().getLength(); l++) {
				Node linkChild = node.getChildNodes().item(l);
				if (linkChild.getNodeName().equalsIgnoreCase("id")) {
					nodeNodesFound = analyze_objects_link_id(nodeNodesFound,
							dataLink, linkChild);
				} else if (linkChild.getNodeName().equalsIgnoreCase("class")) {
					nodeNodesFound = analyze_objects_link_class(nodeNodesFound,
							dataLink, linkChild);
				} else if (linkChild.getNodeName().equalsIgnoreCase("delay")) {
					nodeNodesFound = analyze_objects_link_delay(nodeNodesFound,
							dataLink, linkChild);
				} else if (linkChild.getNodeName().equalsIgnoreCase("variables")) {
					nodeNodesFound = analyze_objects_link_variables(
							nodeNodesFound, dataLink, linkChild);
				}
			}
			if (nodeNodesFound == 3 || nodeNodesFound == 4) {
				data.addOrUpdate(dataLink);
			} else {
				throw new DocumentMalformedException("node malformed; expected 2 elements but got " + nodeNodesFound);
			}
		} else {
			throw new DocumentMalformedException("node malformed");
		}
	}

	private static int analyze_objects_link_variables(int nodeNodesFound,
			DocumentDataLink dataLink, Node linkChild) {
		if (linkChild.hasChildNodes()) {
			for (int m = 0; m < linkChild.getChildNodes().getLength(); m++) {
				Node varNode = linkChild.getChildNodes().item(m);
				if (varNode.hasChildNodes()) {
					String varName = varNode.getNodeName();
					String varValue = varNode.getFirstChild().getNodeValue();
					dataLink.addVariable(varName, varValue);
				}
			}
			nodeNodesFound++;
		} else {
			// no vars set
		}
		return nodeNodesFound;
	}

	private static int analyze_objects_link_delay(int nodeNodesFound,
			DocumentDataLink dataLink, Node linkChild) {
		long delay = Long.parseLong(linkChild.getFirstChild().getNodeValue());
		dataLink.setDelay(delay);
		nodeNodesFound++;
		return nodeNodesFound;
	}

	private static int analyze_objects_link_class(int nodeNodesFound,
			DocumentDataLink dataLink, Node linkChild) {
		String classpath = linkChild.getFirstChild().getNodeValue();
		// System.out.println("node.class=" +
		// nodeChild.getFirstChild().getNodeValue());
		dataLink.setClasspath(classpath);
		nodeNodesFound++;
		return nodeNodesFound;
	}

	private static int analyze_objects_link_id(int nodeNodesFound,
			DocumentDataLink dataLink, Node linkChild) {
		String id = linkChild.getFirstChild().getNodeValue();
		// System.out.println("node.id=" +
		// nodeChild.getFirstChild().getNodeValue());
		dataLink.setId(id);
		nodeNodesFound++;
		return nodeNodesFound;
	}

	private static void analyze_objects_node(DocumentData data, Node node)
			throws DocumentMalformedException {
		if (node.hasChildNodes()) {
			int nodeNodesFound = 0;
			DocumentDataNode dataNode = DocumentDataNode.getInstance();
			for (int l = 0; l < node.getChildNodes().getLength(); l++) {
				Node nodeChild = node.getChildNodes().item(l);
				if (nodeChild.getNodeName().equalsIgnoreCase("id")) {
					nodeNodesFound = analyze_objects_node_id(nodeNodesFound,
							dataNode, nodeChild);
				} else if (nodeChild.getNodeName().equalsIgnoreCase("class")) {
					nodeNodesFound = analyze_objects_node_class(nodeNodesFound,
							dataNode, nodeChild);
				} else if (nodeChild.getNodeName().equalsIgnoreCase("variables")) {
					nodeNodesFound = analyze_objects_node_variables(
							nodeNodesFound, dataNode, nodeChild);
				}
			}
			if (nodeNodesFound == 2 || nodeNodesFound == 3) {
				data.addOrUpdate(dataNode);
			} else {
				throw new DocumentMalformedException("node malformed; expected 2 elements but got " + nodeNodesFound);
			}
		} else {
			throw new DocumentMalformedException("node malformed");
		}
	}

	private static int analyze_objects_node_variables(int nodeNodesFound,
			DocumentDataNode dataNode, Node nodeChild) {
		if (nodeChild.hasChildNodes()) {
			for (int m = 0; m < nodeChild.getChildNodes().getLength(); m++) {
				Node varNode = nodeChild.getChildNodes().item(m);
				if (varNode.hasChildNodes()) {
					String varName = varNode.getNodeName();
					String varValue = varNode.getFirstChild().getNodeValue();
					dataNode.addVariable(varName, varValue);
				}
			}
			nodeNodesFound++;
		} else {
			// no vars set
		}
		return nodeNodesFound;
	}

	private static int analyze_objects_node_class(int nodeNodesFound,
			DocumentDataNode dataNode, Node nodeChild) {
		String classpath = nodeChild.getFirstChild().getNodeValue();
		// System.out.println("node.class=" +
		// nodeChild.getFirstChild().getNodeValue());
		dataNode.setClasspath(classpath);
		nodeNodesFound++;
		return nodeNodesFound;
	}

	private static int analyze_objects_node_id(int nodeNodesFound,
			DocumentDataNode dataNode, Node nodeChild) {
		String id = nodeChild.getFirstChild().getNodeValue();
		// System.out.println("node.id=" +
		// nodeChild.getFirstChild().getNodeValue());
		dataNode.setId(id);
		nodeNodesFound++;
		return nodeNodesFound;
	}

	private static int analyze_connections(DocumentData data,
			int moduleNodesFound, Node moduleChild)
			throws DocumentMalformedException {
		for (int k = 0; k < moduleChild.getChildNodes().getLength(); k++) {
			Node connection = moduleChild.getChildNodes().item(k);
			if (connection.getNodeName().equalsIgnoreCase("connection")) {
				analyze_connections_connection(data, connection);
			}
		}
		moduleNodesFound++;
		return moduleNodesFound;
	}

	private static void analyze_connections_connection(DocumentData data,
			Node connection) throws DocumentMalformedException {
		if (connection.hasChildNodes()) {
			int connectionNodesFound = 0;
			DocumentDataConnection dataConnection = DocumentDataConnection.getInstance();
			for (int l = 0; l < connection.getChildNodes().getLength(); l++) {
				Node connectionChild = connection.getChildNodes().item(l);
				if (connectionChild.getNodeName().equalsIgnoreCase("nodeA")) {
					connectionNodesFound = analyze_connections_connection_nodeA(
							data, connectionNodesFound, dataConnection,
							connectionChild);
				} else if (connectionChild.getNodeName().equalsIgnoreCase("nodeB")) {
					connectionNodesFound = analyze_connections_connection_nodeB(
							data, connectionNodesFound, dataConnection,
							connectionChild);
				} else if (connectionChild.getNodeName().equalsIgnoreCase("link")) {
					connectionNodesFound = analyze_connections_connection_link(
							data, connectionNodesFound, dataConnection,
							connectionChild);
				}
			}
			if (connectionNodesFound == 3) {
				// add connection node
				data.addConnection(dataConnection);
			} else {
				throw new DocumentMalformedException("connection malformed; expected 3 elements but got " + connectionNodesFound);
			}
		} else {
			throw new DocumentMalformedException("connection malformed");
		}
	}

	private static int analyze_connections_connection_link(DocumentData data,
			int connectionNodesFound, DocumentDataConnection dataConnection,
			Node connectionChild) throws DocumentMalformedException {
		try {
			String nodeId = connectionChild.getFirstChild().getNodeValue();
			DocumentDataLink link = data.getLinkById(nodeId);
			if (link == null) {
				throw new DocumentMalformedException("connection malformed; could not find link-node '" + nodeId + "'");
			} else {
				dataConnection.setLink(link);
				connectionNodesFound++;
			}
		} catch (NumberFormatException e) {
			throw new DocumentMalformedException("connection malformed; expected long at delay");
		}
		return connectionNodesFound;
	}

	private static int analyze_connections_connection_nodeB(DocumentData data,
			int connectionNodesFound, DocumentDataConnection dataConnection,
			Node connectionChild) throws DocumentMalformedException {
		String nodeId = connectionChild.getFirstChild().getNodeValue();
		DocumentDataNode to = data.getNodeById(nodeId);
		if (to == null) {
			throw new DocumentMalformedException("connection malformed; could not find nodeB-node '" + nodeId + "'");
		} else {
			dataConnection.setNodeB(to);
			connectionNodesFound++;
		}
		return connectionNodesFound;
	}

	private static int analyze_connections_connection_nodeA(DocumentData data,
			int connectionNodesFound, DocumentDataConnection dataConnection,
			Node connectionChild) throws DocumentMalformedException {
		String nodeId = connectionChild.getFirstChild().getNodeValue();
		DocumentDataNode from = data.getNodeById(nodeId);
		if (from == null) {
			throw new DocumentMalformedException("connection malformed; could not find nodeA-node '" + nodeId + "'");
		} else {
			dataConnection.setNodeA(from);
			connectionNodesFound++;
		}
		return connectionNodesFound;
	}

	private static int analyze_nodeDensity(DocumentData data,
			int moduleNodesFound, Node moduleChild) {
		double density = Double.parseDouble(moduleChild.getFirstChild().getNodeValue());
		data.setNodeDensity(density);
		moduleNodesFound++;
		return moduleNodesFound;
	}

	private static int analyze_classpath(DocumentData data,
			int moduleNodesFound, Node moduleChild) {
		String classpath = moduleChild.getFirstChild().getNodeValue();
		// System.out.println("module.classpath=" + classpath);
		data.setClasspath(classpath);
		moduleNodesFound++;
		return moduleNodesFound;
	}

	private static int analyze_id(DocumentData data, int moduleNodesFound,
			Node moduleChild) {
		String id = moduleChild.getFirstChild().getNodeValue();
		// System.out.println("module.id=" + id);
		data.setId(id);
		moduleNodesFound++;
		return moduleNodesFound;
	}
	
	public static XMLModuleReader parse(File f) {
		return parse(new FileModuleFile(f));
	}

	public static XMLModuleReader parse(AModuleFile simFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(simFile.getInputStream()));
			return new XMLModuleReader(simFile, analyze(document));
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (DocumentMalformedException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public AModuleFile getSimFile() {
		return simFile;
	}

	public DocumentData getDocument() {
		return document;
	}

	public static XMLModuleReader parse(String configFile) {
		return parse(new File(configFile));
	}
}
