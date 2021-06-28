/**
 * Created on Oct 29, 2012
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.svgtreedraw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 * @author cskim
 *
 */
public class GrimShape implements Serializable {
	private Shape grimShape = null;
	private float grimStrokeWidth = 1f;
	private Color grimColor = null;
	private boolean grimFill = false;
	/**
	 * 
	 */
	public GrimShape(Shape grimShape){
		this(grimShape, new BasicStroke(), Color.black, true);
	}
	/**
	 * @param grimShape
	 * @param grimStrokeWidth
	 * @param grimColor
	 * @param grimFill
	 */
	public GrimShape(Shape grimShape, Stroke grimStroke, Color grimColor, boolean grimFill) {
		super();
		this.grimShape = grimShape;
		this.grimStrokeWidth = ((BasicStroke)grimStroke).getLineWidth();
		this.grimColor = grimColor;
		this.grimFill = grimFill;
	}

	public GrimShape(Shape grimShape, float strokeWidth, Color grimColor, boolean grimFill) {
		super();
		this.grimShape = grimShape;
		this.grimStrokeWidth = strokeWidth;
		this.grimColor = grimColor;
		this.grimFill = grimFill;
	}
	/**
	 * @return the grimShape
	 */
	public Shape getGrimShape() {
		return grimShape;
	}
	/**
	 * @param grimShape the grimShape to set
	 */
	public void setGrimShape(Shape grimShape) {
		this.grimShape = grimShape;
	}
	/**
	 * @return the grimStrokeWidth
	 */
	public Stroke getGrimStroke() {
		return new BasicStroke(this.grimStrokeWidth);
	}
	/**
	 * @param grimStrokeWidth the grimStrokeWidth to set
	 */
	public void setGrimStrokeWidth(Stroke grimStroke) {
		this.grimStrokeWidth = ((BasicStroke)grimStroke).getLineWidth();
	}
	public void setGrimStrokeWidth(float swidth) {
		this.grimStrokeWidth = swidth;
	}
	/**
	 * @return the grimColor
	 */
	public Color getGrimColor() {
		return grimColor;
	}
	/**
	 * @param grimColor the grimColor to set
	 */
	public void setGrimColor(Color grimColor) {
		this.grimColor = grimColor;
	}
	/**
	 * @return the grimFill
	 */
	public boolean isGrimFill() {
		return grimFill;
	}
	/**
	 * @param grimFill the grimFill to set
	 */
	public void setGrimFill(boolean grimFill) {
		this.grimFill = grimFill;
	}
	public void draw(Graphics2D g2){
		g2.setStroke(new BasicStroke(this.grimStrokeWidth));
		g2.setColor(grimColor);
		
		if (isGrimFill()){
			g2.fill(grimShape);
		}
		else {
			g2.draw(grimShape);
		}
		
	}
	public boolean contains(double px, double py){
		return this.grimShape.contains(px, py);
	}
	public void translate(float dx, float dy){
		AffineTransform tr = new AffineTransform();
		tr.setToTranslation(dx, dy);
		this.grimShape = tr.createTransformedShape(this.grimShape);
	}
	
}
