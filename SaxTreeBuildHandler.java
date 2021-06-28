/**
 * Created on 2014. 11. 25.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */

package hufs.cse.svgtreedraw;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author cskim
 *
 */
public class SaxTreeBuildHandler extends DefaultHandler{

	private SVGTreeDrawModel model = null;
	private DefaultMutableTreeNode currentNode = null;
	private DefaultMutableTreeNode previousNode = null;
	private DefaultMutableTreeNode rootNode = null;

	public SaxTreeBuildHandler(SVGTreeDrawModel model){
		this.model = model;
		rootNode = new DefaultMutableTreeNode("Dummy Root");
	}
	@Override
	public void startDocument(){
		currentNode = rootNode;
	}
	@Override
	public void endDocument(){
		model.setSaxTreeModel(new DefaultTreeModel(rootNode.getFirstChild()));
	}
	@Override
	public void characters(char[] data,int start,int end){
		String str = new String(data,start,end);              
		if (!str.equals("") && Character.isLetter(str.charAt(0)))
			currentNode.add(new DefaultMutableTreeNode(str));           
	}
	@Override
	public void startElement(String uri,String qName,String lName,Attributes atts){
		previousNode = currentNode;
		currentNode = new DefaultMutableTreeNode(lName);
		// Add attributes as child nodes //
		attachAttributeList(currentNode,atts);
		previousNode.add(currentNode);              
	}
	@Override
	public void endElement(String uri,String qName,String lName){
		if (currentNode.getUserObject().equals(lName))
			currentNode = (DefaultMutableTreeNode)currentNode.getParent();              
		//System.out.println("node "+lName+" end");
		
	}
	private void attachAttributeList(DefaultMutableTreeNode node,Attributes atts){
		for (int i=0;i<atts.getLength();i++){
			String name = atts.getLocalName(i);
			String value = atts.getValue(name);
			node.add(new DefaultMutableTreeNode(name + " = " + value));
		}
	}
	public TreeModel getSaxTreeModel(){
		// Remove Dummy Root and Build TreeModel    	
		return new DefaultTreeModel(rootNode.getFirstChild());
	}

}
