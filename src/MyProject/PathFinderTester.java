package MyProject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import EE324StandardProjectClasses.Point2D;
import EE324StandardProjectClasses.Polygon2D;
import EE324StandardProjectClasses.ShapeMap;
import edu.princeton.cs.introcs.StdDraw;

// Write any unit or temporary tests for path finder development here
// THIS CODE FILE IS NOT GRADED. - Don't put any code here that is part
// of your final path finder solution.

public class PathFinderTester {
	// Test visibleFrom method
	public static void main(String[] args) 
	{
		StdDraw.setCanvasSize(800, 800);
		StdDraw.setScale(-0.05, 1.05);
		StdDraw.clear(Color.DARK_GRAY);
		ShapeMap inputMap = new ShapeMap("src//MAPS//TEST-MAP-1.TXT");
		ShapeMap hullMap = new ShapeMap(inputMap.sourcePoint(), inputMap.destinationPoint());
		for (Polygon2D poly : inputMap)
		{
			hullMap.addPolygon(poly.getHull());
		}
		hullMap.drawFilled(Color.cyan);
		inputMap.drawFilled(Color.gray);
		
		
		Point2D sourcePt = inputMap.sourcePoint();
		Point2D destPt = inputMap.destinationPoint();
		sourcePt.draw(Color.RED, 0.02);
		destPt.draw(Color.GREEN, 0.02);
		
		List<Point2D> visiblePoints = new ArrayList<Point2D>();
		

		for (Point2D source : hullMap.getAllPoints())
		{
			visiblePoints = PathFinderUtils.visibleFrom(source, hullMap);
			for (Point2D p : visiblePoints)
			{
				source.drawTo(p, Color.orange);
			}
		}
	}
}
