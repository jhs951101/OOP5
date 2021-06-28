/**
 * Created on 2014. 11. 25.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */

package hufs.cse.svgtreedraw;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author cskim
 *
 */
public class SaxSVGParseHandler extends DefaultHandler{

	private SVGTreeDrawController control = null;  // (수정)
	private SVGTreeDrawModel model = null;  // (수정)
	private DefaultMutableTreeNode currentNode = null;
	private DefaultMutableTreeNode previousNode = null;
	private DefaultMutableTreeNode rootNode = null;

	private int currPathCount = 0;
	
	public SaxSVGParseHandler(SVGTreeDrawController control, SVGTreeDrawModel model){  // (수정)
		this.control = control;
		this.model = model;  // (수정)
		rootNode = new DefaultMutableTreeNode("Dummy Root");
	}
	@Override
	public void startDocument(){
		currentNode = rootNode;
	}
	@Override
	public void endDocument(){
		model.setSaxTreeModel(new DefaultTreeModel(rootNode.getFirstChild()));  // (수정)
	}
	@Override
	public void characters(char[] data,int start,int end){
		String str = new String(data,start,end); 
		//System.out.println("characters="+str);
		if (!str.equals("") && Character.isLetter(str.charAt(0)))
			currentNode.add(new DefaultMutableTreeNode(str));           
	}
	@Override
	public void startElement(String uri,String qName,String lName,Attributes atts){
		//System.out.println("start lName="+lName);
		previousNode = currentNode;
		currentNode = new DefaultMutableTreeNode(lName);
		// Add attributes as child nodes //
		attachAttributeList(currentNode,atts);
		previousNode.add(currentNode);
		if (lName.equals("svg")){
			String swidth = atts.getValue("width");
			String sheight = atts.getValue("height");

			if (swidth==null || swidth.equals("")) swidth = "400";
			if (sheight==null || sheight.equals("")) sheight ="400";

			int ipx = swidth.indexOf("px");
			if (ipx >= 0){
				swidth = swidth.substring(0, ipx);
			}
			ipx = sheight.indexOf("px");
			if (ipx >= 0){
				sheight = sheight.substring(0, ipx);
			}
			System.out.println("w="+swidth+" h="+sheight);

			model.setSvgWidth(Integer.parseInt(swidth));  // (수정)
			model.setSvgHeight(Integer.parseInt(sheight));  // (수정)
			
			
		}
		else if (lName.equals("path")){
			String pathDef = atts.getValue("d");
			model.view.jtaDataView.append(pathDef+"\n");  // (수정)
			//System.out.println(pathDef);
			HashMap<String, String> attsMap = new HashMap<String, String>();
			for (int i=0;i<atts.getLength();i++){
				String attname = atts.getLocalName(i);
				String attvalue = atts.getValue(attname);
				attsMap.put(attname, attvalue);
				//System.out.println(attname+"="+attvalue);
			}
			model.attsMapList.add(attsMap);  // (수정)
			
			currPathCount++;
			int percentProgress = (int)(100.0 * currPathCount / model.getPathNodeCount());  // (수정)
			if (percentProgress % 5 == 0){
				control.treebuildworker.setProgressValue(percentProgress);  // (수정)
				//System.out.println("progress="+percentProgress);
			}
			
		}
	}
	@Override
	public void endElement(String uri,String qName,String lName){
		//System.out.println("end lName="+lName);
		if (currentNode.getUserObject().equals(lName))
			currentNode = (DefaultMutableTreeNode)currentNode.getParent(); 
		/*
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
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
