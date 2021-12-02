// Student Name:
// Student ID:
// Date:

package MyProject; 
import EE324StandardProjectClasses.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;   

public class PathFinderUtils {

	/**
	 * Exercise 1: Return a list of all points of a ShapeMap that are visible from a specified point p
	 * @author Rory Kilby
	 * @param p Reference point
	 * @param map The ShapeMap object
	 * @return List of all points visible from p
	 */
	public static List<Point2D> visibleFrom(Point2D p, ShapeMap map) 
	{
		List<Point2D> visiblePoints = map.getAllPoints();
		
		// Test if points intersect their own polygon
		for (Polygon2D poly : map)
		{
			for (Point2D dst : poly.asPointsArray())
			{
				// Lines inside polygon return false
				if (!poly.touches(p, dst))
				{
					visiblePoints.remove(dst);
					
				}
			}
		}
		
		// Test if points intersect other polygons
		List<Point2D> visPointsCpy = new ArrayList<Point2D>(visiblePoints);
		
		for (Point2D dst : visPointsCpy)
		{
			for (Polygon2D poly : map)
			{
				if (!poly.touches(dst, p) && poly.intersects(dst, p))
				{
					visiblePoints.remove(dst);
				}
			}
		}
		
		return visiblePoints;
	}

}
