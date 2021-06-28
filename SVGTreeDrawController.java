/**
 * Created on 2014. 11. 25.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.svgtreedraw;

import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.xml.sax.InputSource;

import hufs.cse.svgtreedraw.SaxSVGParseHandler;
import hufs.cse.svgtreedraw.DrawPanel;
import hufs.cse.svgtreedraw.GrimShape;
import hufs.cse.svgtreedraw.SVG2GrimShapeTranslator;
import oracle.xml.parser.v2.SAXParser;


public class SVGTreeDrawController {

	static final int MINPROGRESSVALUE = 2;

	private SVGTreeDrawController thisClass = this;  // (수정)
	private SVGTreeDrawModel model = null;
	private SVGTreeDrawView view = null;
	private Scanner inscan = null;
	private String[] Ds = null;  // (수정)
	private int id;  // (수정)
	
	TreeBuildWorker treebuildworker = null;  // (수정)

	public SVGTreeDrawController(SVGTreeDrawModel model){
		this.model = model;
		this.view = model.view;
	}

	public void buildXMLTree(){
		TreeBuildWorker tbWorker = new TreeBuildWorker();
		tbWorker.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if ("progress".equals(e.getPropertyName())) {
					view.progressBar.setValue((Integer)e.getNewValue());
					if (view.progressBar.getValue()<MINPROGRESSVALUE) {
						view.progressBar.setIndeterminate(true);
					}
					else {
						view.progressBar.setStringPainted(true); 
						view.progressBar.setIndeterminate(false);
					}	
				}
			}
		});
		view.progressBar.setValue(0);
		view.progressBar.setStringPainted(false); 
		tbWorker.setProgressValue(0);

		tbWorker.execute();
		System.out.println("TreeBuildWorker Launched");

	}
	public void readFile2View(){
		
		model.attsMapList = new ArrayList<HashMap<String, String>>();  // (수정)
		
		try {
			inscan = new Scanner(model.getSelectedFile());
			String inline = "";
			while (inscan.hasNext()){
				inline = inscan.nextLine();
				view.jtaDataView.append(inline+"\n");
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(view, e);
		}

	}
	
	public void clearPanel(){
		view.gmodel.shapeList.clear();
		view.drawPanel.repaint();
	}
	
	public void setPanel(){  // (수정)
		
		setDsArray();
		view.jtaDataView.setVisible(false);
		
		view.drawPanel = new DrawPanel(view.gmodel);
		view.drawPanel.setPreferredSize(new Dimension(view.gmodel.getPanWidth(), view.gmodel.getPanHeight()));
		view.setBounds(50, 50, view.gmodel.getPanWidth()+200, view.gmodel.getPanHeight());
		view.jspRight.setViewportView(view.drawPanel);
		view.invalidate();
		
		view.gmodel.shapeList = new ArrayList<GrimShape>();
		
	}
	
	public void setDsArray(){  // (수정)
		
		HashMap<String, String>[] map = new HashMap[model.attsMapList.size()];
		Ds = new String[model.attsMapList.size()];
		
		for(int i=0; i<model.attsMapList.size(); i++){
			map[i] = model.attsMapList.get(i);
			Ds[i] = map[i].get("d");
		}
		
	}
	
	public int returnI(String d_value){
		
		for(int i=0; i<model.attsMapList.size(); i++){
			if( d_value.equals(Ds[i]) ){
				return i;
			}
		}
		
		return -1;
	}
	
	class TreeBuildWorker extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			view.progressBar.setIndeterminate(true);
			setProgressValue(0);
			
			SaxSVGParseHandler saxTreeHandler = new SaxSVGParseHandler(thisClass, model);  // (수정)

			try {             
				SAXParser saxParser = new SAXParser();
				saxParser.setContentHandler(saxTreeHandler);
				saxParser.parse(new InputSource(new FileInputStream(model.getSelectedFile())));
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(view, e);
			}

			return null;
		}
		/** Code executed after the background thread finishes */
		@Override
		protected void done() {
			view.jtreeView = new JTree(model.getSaxTreeModel());
			view.jtreeView.setFont(new Font("Consolas", Font.PLAIN, 14));
			view.jtreeView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			view.jtreeView.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode node = 
							(DefaultMutableTreeNode)view.jtreeView.getLastSelectedPathComponent();
					
					/* if nothing is selected */ 
					if (node == null) return;

					/* retrieve the node that was selected */ 
					String nodeInfo = (String)node.getUserObject();
					
					
					if (nodeInfo.equals("path")){ 
						
						if(SVGTreeDrawView.isDraw() == true){  // (수정)
							String d;
							Enumeration<DefaultMutableTreeNode> c = node.children();
							while (c.hasMoreElements()){
								nodeInfo = (String)c.nextElement().getUserObject();
							}
							d = nodeInfo.substring(4, nodeInfo.length());
							
							id = returnI(d);
						}
						
						if(SVGTreeDrawView.isDraw() == false){  // (수정)
							Enumeration<DefaultMutableTreeNode> chenum = node.children();
							StringBuilder sb = new StringBuilder();
							while (chenum.hasMoreElements()){
								nodeInfo = (String)chenum.nextElement().getUserObject();
								sb.append(nodeInfo);
								sb.append('\n');
							}
							view.jtaDataView.setText(sb.toString());
						}
						else if(SVGTreeDrawView.isDraw() == true){
							// 클릭한 path에 대해 panel에 그림을 그림
							SvgDrawWorker sdWorker = new SvgDrawWorker();
							sdWorker.execute();
						}
					}


				}
			});

			view.jspLeft.setViewportView(view.jtreeView);

			setProgress(100);

		}

		public void setProgressValue(int val){
			setProgress(val);
		}

	}
	
	class SvgDrawWorker extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			view.progressBar.setIndeterminate(true);
			setProgressValue(0);
			
			int drawCount = 0;
			
				HashMap<String, String> map = model.attsMapList.get(id);  // (수정)
				
				ArrayList<GrimShape> gslist = SVG2GrimShapeTranslator.translateSVG2Shape(map);
				if (gslist != null && gslist.size()!=0){
					view.gmodel.shapeList.addAll(gslist);
					view.drawPanel.repaint();
				}
				drawCount++;
				int percentProgress = (int)(100.0 * drawCount / model.getPathNodeCount());
				if (percentProgress % 5 == 0){
					setProgressValue(percentProgress);
				}
			
			
			return null;
		}
		/** Code executed after the background thread finishes */
		@Override
		protected void done() {
			view.progressBar.setIndeterminate(false);
			setProgress(100);
			view.drawPanel.repaint();
			
		}

		public void setProgressValue(int val){
			setProgress(val);
		}

	}

}
