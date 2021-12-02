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
	
	public static class Graph 
	{
		private List<Vertex> vertices = new ArrayList<Vertex>();
		public Vertex source;
		public Vertex dest;
		
		public static class Vertex 
		{
			public Point2D node;
			public List<Point2D> neighbours;
			public int weight;
			
			public Vertex(Point2D n, List<Point2D> ne, int w) {
				node = n;
				neighbours = ne;
				weight = w;
			}
		}
		
		public Graph(ShapeMap map)
		{
			// Initialise source node
			source = new Vertex(map.sourcePoint(), PathFinderUtils.visibleFrom(map.sourcePoint(), map), 0);
			vertices.add(source);
			
			// Initialise all other nodes
			for (Point2D s : map.getAllPoints())
			{
				Vertex v = new Vertex(s, PathFinderUtils.visibleFrom(s, map), 1);
				vertices.add(v);
			}
			
			// Initialise end node
			dest = new Vertex(map.destinationPoint(), PathFinderUtils.visibleFrom(map.destinationPoint(), map), 0);
			vertices.add(dest);
		}
	
		public void Draw(ShapeMap map)
		{
			StdDraw.setCanvasSize(800, 800);
			StdDraw.setScale(-0.05, 1.05);
			StdDraw.clear(Color.DARK_GRAY);
			map.drawFilled(Color.GRAY);
			
			for (Vertex v : vertices) {
				v.node.draw(Color.red, 0.02);
			}
		}
	}
	
	public static void main(String[] args)
	{
		ShapeMap inputMap = new ShapeMap("src//MAPS//TEST-MAP-1.TXT");
		ShapeMap hullMap = new ShapeMap(inputMap.sourcePoint(), inputMap.destinationPoint());
		for (Polygon2D poly : inputMap)
		{
			hullMap.addPolygon(poly.getHull());
		}
		Graph visGraph = new Graph(hullMap);
		visGraph.Draw(hullMap);
		
		
	}

}
