/*
 * NOTE: Use this class instead of MavenXpp3Writer
 * because MavenXpp3Writer completely rewrites file based on
 * the object "model".
 * Order and indentation are changed and all comments are removed.
 * So, to minimize the changes, this class updates the version of dependencies.
 */
package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.utils.Consts;
import intact.maven.plugins.versionupdater.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PomWriter {
	private final File file;

	private final Document document;
	private final XPath xpath;

	public PomWriter(File pFile) throws Exception {
		file = pFile;
        String fileString = FileUtils.readFileToString(file);
        if (fileString.split("\r\n").length > 1) {
            System.setProperty("line.separator", "\r\n");
        } else if (fileString.split("\r").length > 1) {
            System.setProperty("line.separator", "\r");
        } else {
            System.setProperty("line.separator", "\n");
        }
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		document = domFactory.newDocumentBuilder()
				.parse(file.getAbsolutePath());
		xpath = XPathFactory.newInstance().newXPath();
	}

	public void write(Pom pPom) throws TransformerException {
		update(pPom);
		flush();
	}

	private void flush() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "3");
		transformer.transform(new DOMSource(document), new StreamResult(file));
	}

	public void update(Pom pPom) {
		try {
			Node project = (Node) xpath.compile("/project").evaluate(document,
					XPathConstants.NODE);
			if (null == project) {
				return;
			}

			updateVersion(pPom.getVersion(), project);

			NodeList nodeList = project.getChildNodes();

			if (null != pPom.getModel().getScm()) {
				checkIsPresentNodeScm(nodeList);
			}

			String nodeName = null;

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				nodeName = node.getNodeName();

				if ("parent".equals(nodeName)) {
					updateVersion(pPom.getModel().getParent().getVersion(),
							node);
				}
				if ("properties".equals(nodeName)) {
					updateProperties(pPom, node);
				}
				if ("scm".equalsIgnoreCase(nodeName)) {
					if (null != pPom.getModel().getScm())
						updateScm(pPom.getModel().getScm().getConnection(),
								node);
				}
				if ("dependencies".equals(nodeName)) {
					updateDependencies(pPom.getDependencies(), node);
				}
				if ("dependencyManagement".equals(nodeName)) {
					Node dependencies = (Node) xpath.compile("dependencies")
							.evaluate(node, XPathConstants.NODE);
					if (null != dependencies) {
						updateDependencies(pPom.getDependencyManagement()
								.getDependencies(), dependencies);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public void appendNodeSms() {
		Element element = document.getDocumentElement();
		Node connection = (Element) document.createElement("connection");
		connection.appendChild(document.createTextNode(Consts.CONNECTION));
		String scm = "scm";
		Element scmNode = (Element) document.createElement(scm);
		scmNode.appendChild(connection);
		element.appendChild(scmNode);
	}

	private void checkIsPresentNodeScm(NodeList nodeList)
			throws XPathExpressionException {
		String scm = "scm";
		String nodeName = "";
		boolean isPresent = false;
		for (int i = 0; i < nodeList.getLength(); i++) {
			nodeName = nodeList.item(i).getNodeName();
			if (nodeName.equals(scm)) {
				isPresent = true;
				break;
			}
		}
		if (!isPresent) {
			this.appendNodeSms();
		}
	}

	public void updateVersion(String pVersion, Node pNode)
			throws XPathExpressionException {
		if (null == pVersion) {
			return;
		}

		Node version = (Node) xpath.compile("version").evaluate(pNode,
				XPathConstants.NODE);

		if (null != version.getFirstChild()) {
			version.getFirstChild().setNodeValue(pVersion);
		}
	}

	public void updateProperties(Pom pPom, Node pNode)
			throws XPathExpressionException {
		for (Object element : pPom.getModel().getProperties().keySet()) {
			String key = (String) element;
			String value = pPom.getModel().getProperties().getProperty(key);
			String valuePropertyKey = xpath.compile(
					Consts.XPATHPROPERTIES + key).evaluate(pNode);
			if (!valuePropertyKey.equals(value)) {
				Node node = (Node) xpath.compile(Consts.XPATHPROPERTIES + key)
						.evaluate(pNode, XPathConstants.NODE);
				if (null == node) {
					node = document.createElement(key);
					node.appendChild(document.createTextNode(value));
					pNode.appendChild(node);
				} else {
					node.getFirstChild().setNodeValue(value);
				}
			}
		}
		if (!pPom.getModel().getProperties().containsKey(Consts.SCMREPOSITORY)){
		    removeScmRepositoryWorkspaceProperty(pNode);
        }
	}

	private void removeScmRepositoryWorkspaceProperty(Node pNode) {
		NodeList nodes = pNode.getChildNodes();
		Node currentNode;
		for (int i = 0; i < nodes.getLength(); i++) {
			currentNode = nodes.item(i);
			if (currentNode.getNodeName().endsWith(Consts.SCMREPOSITORY)) {
				pNode.removeChild(currentNode);
			}
		}
	}

	public void updateScm(String pConnection, Node pNode) {
		NodeList nodeList = pNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeName = node.getNodeName();
			if (Consts.THECONNECTION.equals(nodeName)) {
				node.getFirstChild().setNodeValue(pConnection);
			}
		}
	}

	public void updateDependencies(List<Dependency> pDependencies, Node pNode)
			throws XPathExpressionException {
		NodeList nodeList = pNode.getChildNodes();

		List<String> exsitingDependencies = new ArrayList<String>();
		List<Dependency> toAddDependencies = new ArrayList<Dependency>();
		String groupId = null;
		String artifactId = null;

		Node groupIdNode = null;
		Node artifactIdNode = null;
		Node typeNode = null;
		Node scopeNode = null;
		
		Dependency currentDependency = null;
		String version = null;
		String dependencyType = null;
		String scope = null;

		Node currentNode = null;

		for (int i = 0; i < nodeList.getLength(); i++) 
		{			
			currentNode = nodeList.item(i);
			
			if ("dependency".equals(currentNode.getNodeName())) 
			{
				groupIdNode = (Node) xpath.compile("groupId").evaluate(currentNode, XPathConstants.NODE);
				artifactIdNode = (Node) xpath.compile("artifactId").evaluate(currentNode, XPathConstants.NODE);
				typeNode = (Node) xpath.compile("type").evaluate(currentNode, XPathConstants.NODE);
				scopeNode = (Node) xpath.compile("scope").evaluate(currentNode, XPathConstants.NODE);

				groupId = groupIdNode.getFirstChild().getNodeValue();
				artifactId = artifactIdNode.getFirstChild().getNodeValue();
				dependencyType = (null != typeNode) ? typeNode.getFirstChild().getNodeValue():"";
				scope = (null != scopeNode) ? scopeNode.getFirstChild().getNodeValue():"";

				exsitingDependencies.add(groupId + artifactId);

				currentDependency = findDependency(groupId, artifactId,dependencyType,scope, pDependencies);

				if (null != currentDependency) {
					version = currentDependency.getVersion();
					dependencyType = currentDependency.getType();
					dependencyType = ("jar".equalsIgnoreCase(dependencyType) ? null: dependencyType);
					scope = currentDependency.getScope();

					updateOrAddNode(currentNode, "version", version);
					updateOrAddNode(currentNode, "type", dependencyType);
					updateOrAddNode(currentNode, "scope", scope);
					updateOrAddNodeExlusions(currentNode, currentDependency.getExclusions());
				} else {
					pNode.removeChild(currentNode);
				}
			}
		}

		for (Dependency dependency : pDependencies) {
			groupId = dependency.getGroupId();
			artifactId = dependency.getArtifactId();

			if (!exsitingDependencies.contains(groupId + artifactId)) {
				toAddDependencies.add(dependency);
			}
		}

		addDependencies(toAddDependencies, pNode);
	}

	private void addDependencies(List<Dependency> pDependencies, Node pNode)
			throws XPathExpressionException {
		Node dependencyNode = null;
		Node groupNode = null;
		Node artifactNode = null;
		Node versionNode = null;
		Node typeNode = null;
		Node scopeNode = null;

		String groupId = null;
		String artifactId = null;
		String version = null;
		String dependencyType = null;
		String scope = null;

		for (Dependency dependency : pDependencies) {
			groupId = dependency.getGroupId();
			artifactId = dependency.getArtifactId();
			version = dependency.getVersion();
			scope = dependency.getScope();
			dependencyType = dependency.getType();

			dependencyNode = document.createElement("dependency");

			groupNode = document.createElement("groupId");
			groupNode.appendChild(document.createTextNode(groupId));

			artifactNode = document.createElement("artifactId");
			artifactNode.appendChild(document.createTextNode(artifactId));

			versionNode = document.createElement("version");
			versionNode.appendChild(document.createTextNode(version));

			dependencyNode.appendChild(groupNode);
			dependencyNode.appendChild(artifactNode);
			dependencyNode.appendChild(versionNode);

			if (StringUtils.isNotBlank(dependencyType) && !"jar".equalsIgnoreCase(dependencyType)) 
			{
				typeNode = document.createElement("type");
				typeNode.appendChild(document.createTextNode(dependencyType));

				dependencyNode.appendChild(typeNode);
			}

			if (StringUtils.isNotBlank(scope)) {
				scopeNode = document.createElement("scope");
				scopeNode.appendChild(document.createTextNode(scope));

				dependencyNode.appendChild(scopeNode);
			}			

			updateOrAddNodeExlusions(dependencyNode, dependency.getExclusions());

			pNode.appendChild(dependencyNode);
		}
	}
	
	private void updateOrAddNodeExlusions(Node pCurrentNode, List<Exclusion> pExclusions) throws XPathExpressionException 
	{

		Node tagNode = (Node) xpath.compile("exclusions").evaluate(pCurrentNode, XPathConstants.NODE);
		
		if (null != tagNode) 
		{
			NodeList exclusionsNodes =  tagNode.getChildNodes();
			
			if (null != exclusionsNodes)
			{
				int nbExclusions = exclusionsNodes.getLength();
				List<Node> exclusionsNodeAsList = new ArrayList<Node>();
				
				for(int i=0;i<nbExclusions;i++)
				{
					exclusionsNodeAsList.add(exclusionsNodes.item(i));
				}
				
				for(Node exclusionNode:exclusionsNodeAsList)
				{
					tagNode.removeChild(exclusionNode);					
				}
			}
		}		
		
		if (CollectionUtils.isNotEmpty(pExclusions))
		{
			if (null == tagNode) 
			{
				tagNode = document.createElement("exclusions");
				pCurrentNode.appendChild(tagNode);
			}
			
			Node exclusionNode = null;

			for(Exclusion exclusion:pExclusions)
			{
				exclusionNode = document.createElement("exclusion");
				
				updateOrAddNode(exclusionNode, "groupId", exclusion.getGroupId());
				updateOrAddNode(exclusionNode, "artifactId", exclusion.getArtifactId());
				
				tagNode.appendChild(exclusionNode);				
			}
		}
		else
		{
			if (null != tagNode) 
			{
				pCurrentNode.removeChild(tagNode);
			}			
		}
	}
	

	private void updateOrAddNode(Node pCurrentNode, String pTagName,String pNodeValueFromDependency) throws XPathExpressionException 
	{
		Node tagNode = (Node) xpath.compile(pTagName).evaluate(pCurrentNode, XPathConstants.NODE);
		
		if (StringUtils.isNotBlank(pNodeValueFromDependency)) 
		{
			if (null != tagNode) 
			{
				tagNode.getFirstChild().setNodeValue(pNodeValueFromDependency);
			} 
			else 
			{
				tagNode = document.createElement(pTagName);
				tagNode.appendChild(document.createTextNode(pNodeValueFromDependency));
				pCurrentNode.appendChild(tagNode);
			}
		}
		else
		{
			if (null != tagNode) 
			{
				pCurrentNode.removeChild(tagNode);
			} 			
		}
	}

	private static Dependency findDependency(String pGroupId,
											 String pArtifactId, 
											 String pDependencyType,
											 String pScope,
											 List<Dependency> pDependencies) 
	{
		String dependencyToFindKey = Utils.getKey(pGroupId, pArtifactId, pDependencyType,pScope);
		String currentDependencyKey = null;
		
		for (Dependency dependency : pDependencies) 
		{
			currentDependencyKey = Utils.getKey(dependency);
			
			if (dependencyToFindKey.equalsIgnoreCase(currentDependencyKey)) 
			{
				return dependency;
			}
		}
		
		return null;
	}

	public void fetchNode(String pPath) {
		String p[] = pPath.split("/");
		// search nodes or create them if they do not exist
		Node n = document;
		for (int i = 0; i < p.length; i++) {
			NodeList kids = n.getChildNodes();
			Node nfound = null;
			for (int j = 0; j < kids.getLength(); j++) {
				if (kids.item(j).getNodeName().equals(p[i])) {
					nfound = kids.item(j);
					break;
				}
			}
			n = nfound;
		}

	}
}
