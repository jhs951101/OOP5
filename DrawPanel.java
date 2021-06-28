package hufs.cse.svgtreedraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {

	private GrimPanModel model = null;

	
	public DrawPanel(GrimPanModel model){
		this.model = model;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i=0; i<model.shapeList.size(); ++i){
			GrimShape gs = model.shapeList.get(i);
			gs.draw(g2);
		}
		/*
		for (GrimShape grimShape:model.shapeList){
			grimShape.draw(g2);
		}
		*/
		g2.setColor(model.getShapeColor());
		g2.setStroke(model.getShapeStroke());

		if (model.isShapeFill() 
				&& model.getEditState()==GrimPanModel.SHAPE_PENCIL){
			if (model.curDrawShape != null)
				g2.fill(model.curDrawShape);
		}
		else {
			if (model.curDrawShape != null)
				g2.draw(model.curDrawShape);
		}
	}

}
