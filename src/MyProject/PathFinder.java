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
	
	public static double dotProd(Point2D p0, Point2D p1, Point2D p2) {
		Point2D p0p1 = new Point2D(p1.X()-p0.X(), p1.Y()-p0.Y());
		Point2D p0p2 = new Point2D(p2.X()-p0.X(), p2.Y()-p0.Y());
		return (p0p1.X()*p0p2.X() + p0p1.Y()*p0p2.Y());
	}
	
	public static double dotProd(Point2D a0, Point2D a1, Point2D b0, Point2D b1) {
		// move lines to origin
		Point2D a0a1 = new Point2D(a1.X()-a0.X(), a1.Y()-a0.Y());
		Point2D b0b1 = new Point2D(b1.X()-b0.X(), b1.Y()-b0.Y());
		return (a0a1.X()*b0b1.X() + a0a1.Y()*b0b1.Y());
	}
	
	
	public static double weight(double dp, Point2D s, Point2D p, Point2D source, Point2D dest, ShapeMap map)
	{
		Double max_val = -100.0d;
		for (Point2D p2 : PathFinderUtils.visibleFrom(p, map))
		{
			if (p2.isEqual(dest)) {
				max_val = 100.0d;
				break;
			}
			Double val = weight(dotProd(p, p2, source, dest), p, p2, source, dest);
			if (val > max_val) 
			{
				max_val = val;
			}
		}
		return max_val + weight(dp, s, p, source, dest);
	}
	
	public static double weight(double dp, Point2D s, Point2D p, Point2D source, Point2D dest)
	{
		// calculate distance traveled along source->dest
				Double score = dp;
				if (dest.Y() > source.Y() && p.Y() > dest.Y()) 
				{
					score = score - (p.Y()-dest.Y());
				}
				if (dest.Y() < source.Y() && p.Y() < dest.Y()) 
				{
					score = score - (dest.Y()-p.Y());
				}
				double distance = s.distanceTo(p);
				// Calculate distance ratio
				score = score / s.distanceTo(p);

				return score;
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
		List<Point2D> prevPoints = new ArrayList<Point2D>();
		
		while (!s.isEqual(dest))
		{
			// Sort by angle
			List<Point2D> visiblePoints = PathFinderUtils.visibleFrom(s, map);
			List<Point2D> sortedPoints = new ArrayList<Point2D>(visiblePoints);
			
			// Sort points by angle
			/*
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
			*/
			
			// Find closest angle point
			Point2D choice = sortedPoints.get(0);
			double max_val = -100.0;
			double weight = 0.0d;
			for (Point2D p : sortedPoints)
			{
				s.drawTo(p, Color.orange);
				//StdDraw.pause(200);
				
				double dp = dotProd(s, p, source, dest);
				
				weight = weight(dp, s, p, source, dest, map);
				System.out.println("dot product: " + dp);
				System.out.println("weighting: " + weight);
				
				if (p.isEqual(dest)) {
					choice = p;
					break;
				}
				else if (weight > max_val) {
					max_val = weight;
					choice = p;
				}
			}
			// Draw chosen point
			s.drawTo(choice, Color.red);
			// Update points
			prevPoints = sortedPoints;
			prevPoints.add(s);
			s = choice;
			path.add(s);
			
			StdDraw.pause(200);
			
			// Reset canvas
			StdDraw.clear(Color.DARK_GRAY);
			map.drawFilled(Color.gray);
			source.draw(Color.red, 0.015);
			dest.draw(Color.red, 0.015);
			source.drawTo(dest, Color.green);
		}
		
		for (int i=0; i<path.size()-1; i++)
		{
			path.get(i).drawTo(path.get(i+1), Color.red);
		}
		
		return path;
	}
	
	public static void main(String[] args)
	{
		ShapeMap inputMap = new ShapeMap("src//MAPS//DEMO-MAP-3.TXT");
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
