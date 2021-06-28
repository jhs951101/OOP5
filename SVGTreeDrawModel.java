/**
 * Created on 2014. 11. 25.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.svgtreedraw;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.TreeModel;

public class SVGTreeDrawModel {

	public SVGTreeDrawView view = null;
	
	private File selectedFile = null;
	
	private TreeModel saxTreeModel = null;
	
	ArrayList<HashMap<String, String>> attsMapList = null;  // (수정)
	
	private int pathNodeCount = 0;  // (수정)
	
	private int svgWidth = 400;
	private int svgHeight = 400;  // (수정)
	
	public SVGTreeDrawModel(SVGTreeDrawView view){
		this.view = view;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	public TreeModel getSaxTreeModel() {
		return saxTreeModel;
	}

	public void setSaxTreeModel(TreeModel xmlTreeModel) {
		this.saxTreeModel = xmlTreeModel;
	}
	
	public int getPathNodeCount() {  // (수정)
		return pathNodeCount;
	}

	public void setPathNodeCount(int pathNodeCount) {  // (수정)
		this.pathNodeCount = pathNodeCount;
	}
	
	public int incPathNodeCount(){  // (수정)
		return ++pathNodeCount;
	}
	
	public int getSvgWidth() {  // (수정)
		return svgWidth;
	}

	public void setSvgWidth(int svgWidth) {  // (수정)
		this.svgWidth = svgWidth;
		view.gmodel.setPanWidth(svgWidth);
	}

	public int getSvgHeight() {  // (수정)
		return svgHeight;
	}

	public void setSvgHeight(int svgHeight) {  // (수정)
		this.svgHeight = svgHeight;
		view.gmodel.setPanHeight(svgHeight);
	}
}
