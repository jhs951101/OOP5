package hufs.cse.svgtreedraw;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;

public class SVG2GrimShapeTranslator {

	public static ArrayList<GrimShape> translateSVG2Shape(HashMap<String, String> attsmap){

		ArrayList<GrimShape> grimShapeList =  new ArrayList<GrimShape>();

		ParseAttribute pa = new ParseAttribute(attsmap);
		
		String pathDefString = attsmap.get("d");
		//System.out.println("<!-- path def "+pathDefString+" -->");
		SVGPath2Path2DParser p2lParser = new SVGPath2Path2DParser(pathDefString);
		ArrayList<Path2D> p2dList = p2lParser.getPath2DList();
		//System.out.println("p2dlist size="+p2dList.size());
		
		for (Path2D path:p2dList){
			if (pa.isFill()){
				GrimShape gs = new GrimShape(path, 0, pa.getFillColor(), true);
				grimShapeList.add(gs);
			}
			if (pa.isStroke()){
				GrimShape gs = new GrimShape(path, pa.getStrokeWidth(), pa.getStrokeColor(), false);
				grimShapeList.add(gs);
			}
		}

		return grimShapeList;
	}
}
