// Student Name:
// Student ID:
// Date:

package MyProject;
import EE324StandardProjectClasses.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.util.List;  
import java.util.ArrayList;

public class PathFinder {

	// Exercise 2
	// Write your main path finder methods here
	// Keep this file clean. Put any temporary test code into PathFinderTests.
	// Add any utility methods to PathFinderUtils, or create new classes as needed.
	// You may delete this comment.
	
	public static double crossProd(Point2D p0, Point2D p1, Point2D p2) {
		if (Point2D.collinearMid(new Point2D[]{p0, p1, p2}, 0, 1, 2) != -1)
		{
			return 0.0d;
		}
		Point2D p0p1 = new Point2D(p1.X()-p0.X(), p1.Y()-p0.Y());
		Point2D p0p2 = new Point2D(p2.X()-p0.X(), p2.Y()-p0.Y());
		return (p0p1.X() * p0p2.Y() - p0p1.Y() * p0p2.X());
	}
	
	public static List<Point2D> findPath(ShapeMap map)
	{
		List<Point2D> path = new ArrayList<Point2D>();
		
		StdDraw.setCanvasSize(800, 800);
		StdDraw.setScale(-0.05, 1.05);
		StdDraw.clear(Color.DARK_GRAY);
		map.drawFilled(Color.gray);
		
		// Draw line source->dest
		Point2D source = map.sourcePoint();
		Point2D dest = map.destinationPoint();
		source.draw(Color.red, 0.015);
		dest.draw(Color.red, 0.015);
		source.drawTo(dest, Color.green);
		path.add(source);
		
		Point2D s = source;
		Point2D prev_choice = null;
		// Maintain reference to previous points
		List<Point2D> prev_visible = null;
		
		while (!s.equals(dest))
		{
			// Sort by angle
			List<Point2D> visiblePoints = PathFinderUtils.visibleFrom(s, map);
			List<Point2D> sortedPoints = new ArrayList<Point2D>(visiblePoints);
			
			// Sort points by angle
			for (int j=0; j<sortedPoints.size()-1; j++)
			{
				for (int i=0; i<sortedPoints.size()-1-j; i++)
				{
					if (crossProd(s, sortedPoints.get(i), sortedPoints.get(i+1)) > 0)
					{
						Point2D tmp = sortedPoints.get(i);
						sortedPoints.set(i, sortedPoints.get(i+1));
						sortedPoints.set(i+1, tmp);
					}
				}
			}
			
			for (Point2D p : sortedPoints)
			{
				s.drawTo(p, Color.orange);
				StdDraw.pause(500);
				double val = Math.abs(crossProd(s, p, dest));
				System.out.println(val);
			}
			
			
			// Reset canvas
			StdDraw.clear(Color.DARK_GRAY);
			map.drawFilled(Color.gray);
			source.draw(Color.red, 0.015);
			dest.draw(Color.red, 0.015);
			source.drawTo(dest, Color.green);
		}
		return path;
	}
	
	public static void main(String[] args)
	{
		ShapeMap inputMap = new ShapeMap("src//MAPS//DEMO-MAP-2.TXT");
		ShapeMap hullMap = new ShapeMap(inputMap.sourcePoint(), inputMap.destinationPoint());
		for (Polygon2D poly : inputMap)
		{
			hullMap.addPolygon(poly.getHull());
		}
		
		List<Point2D> path = findPath(hullMap);
		for (int i=0; i<path.size()-1; i++)
		{
			path.get(i).drawTo(path.get(i+1), Color.red); 
		}
	}

}
