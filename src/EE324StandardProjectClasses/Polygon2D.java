// CMcA DCU - R1.3 - October 2020
// Revs:
// 25/10/2020 - added methods:
//   contains(Point2D p)
//   touches(Point2D p)
//   Polygon2D(List<Point2D> points)
// 26/10/2020 - methods edited:
//   draw(Color clr, double penWidth
//

package EE324StandardProjectClasses;
import edu.princeton.cs.introcs.StdDraw;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A 2D polygon class. Construct and draw polygons. Compute basic computational geometry
 * operations on polygons and compute the convex hull of a polygon.
 * The polygon is internally stored as an array of Point2D objects. Points can be retrieved by index.
 * 
 * <p><b>NOTE:</b> This class requires inclusion of the StdDraw and the JTS geometry libraries.<br></p>
 * 
 * @author Conor McArdle (conor.mcardle@dcu.ie)
 */
public class Polygon2D implements Iterable<Point2D> {

	private Point2D[] vertices;

	/** Construct an empty polygon. */
	public Polygon2D() {
		vertices = new Point2D[0];
	}

	/** Construct a polygon from an array of points. Order of points is preserved.
	 * @param points array of Point2D references
	 */
	public Polygon2D(Point2D[] points) {
		vertices = new Point2D[points.length];
		for (int i=0; i<points.length; i++) {
			vertices[i] = new Point2D(points[i]);
		}
	}

	/** Construct a polygon from a List of Point2D. Order of points is preserved.
	 * @param points List of Point2D references
	 */
	public Polygon2D(List<Point2D> points) {
		this(points.toArray(new Point2D[points.size()]));
	}	
	
	/** Copy constructor - makes a copy of the specified polygon.
	 * @param poly the polygon to copy
	 */
	public Polygon2D(Polygon2D poly) {
		vertices = new Point2D[poly.vertices.length];
		for (int i=0; i<poly.vertices.length; i++) {
			vertices[i] = new Point2D(poly.vertices[i]);
		}
	}

	/** Add a point to this polygon. Note: resizes the internal array of points storing the polygon.
	 * @param p the point to add to this polygon
	 */
	public void addPoint(Point2D p) {
		vertices = Arrays.copyOf(vertices, vertices.length + 1);
		vertices[vertices.length-1] = new Point2D(p);
	}

	/** The number of vertices of this polygon.
	 * @return number of polygon vertices
	 */
	public int size() {
		return vertices.length;
	}

	/** Get a specific vertex of the polygon.
	 * @param indx index of point to get
	 * @return the polygon vertex as a Point2D, returns null if index doesn't exist
	 */
    public Point2D get(int indx) {  // return point at given index
		if ((indx >= 0) && (indx < vertices.length))
			return new Point2D(vertices[indx]); // make copy of point object
		else
			return null;
	}
    
	/** This polygon as an array of points.
	 * @return the polygon as an array of Point2D
	 */
	public Point2D[] asPointsArray() {
		return Arrays.copyOf(vertices, vertices.length);
	}

	/** Get a string representation of this polygon's points.
	 * @return string representation of the polygon points
	 */
	@Override
	public String toString() {
		String polyAsString = "";
		for (int i=0; i<vertices.length; i++)
			polyAsString += vertices[i].toString() + " ";
		return polyAsString;
	}

	/** Iterator supporting for-each style iteration over all points in this polygon. 
	 * Example:<br>
	 * {@code for (Point2D pnt : poly) { pnt.draw(); } // poly a Polygon2D object}
	 * */
	@Override
	public Iterator<Point2D> iterator() {
		Iterator<Point2D> itr = new Iterator<Point2D>() {
			private int currentIndex = 0;
			@Override
			public boolean hasNext() { return (currentIndex < vertices.length); }
			@Override
			public Point2D next() { return vertices[currentIndex++]; }
			@Override
			public void remove() { throw new UnsupportedOperationException(); }
		};
		return itr;
	}
    
	
	///////////////////// POLYGON DRAWING METHODS ///////////////////////////////////////////////////
	//
	//
	/** Draw perimeter of this polygon - uses the StdDraw library. */
	public void draw() {
		for (int i=0; i<vertices.length; i++) {
			if (i < vertices.length-1)
				vertices[i].drawTo(vertices[i+1]);
			else
				vertices[i].drawTo(vertices[0]);
		}
	}

	/** Draw perimeter of this polygon with a given color - uses the StdDraw library. 
	 * @param clr the pen color to use
	 */
	public void draw(Color clr) {
		Color savedColor = StdDraw.getPenColor();
		StdDraw.setPenColor(clr);
		this.draw();
		StdDraw.setPenColor(savedColor);
	}

	/** Draw perimeter of this polygon with a given color and pen width - uses the StdDraw library.
	 * @param clr the pen color to use
	 * @param penWidth width of the pen to use
	 */
	public void draw(Color clr, double penWidth) {
		double savedPenWidth = StdDraw.getPenRadius();
		StdDraw.setPenRadius(penWidth);
		this.draw(clr);
		StdDraw.setPenRadius(savedPenWidth);
	}
	
	/** Draw and fill this polygon - uses the StdDraw library. */
	public void drawFilled() {
		double[] X = new double[vertices.length];
		double[] Y = new double[vertices.length];
		for (int i=0; i<vertices.length; i++) {
			X[i] = vertices[i].X();
			Y[i] = vertices[i].Y();
		}
		StdDraw.filledPolygon(X, Y);
	}
	
	/** Draw and fill this polygon with a given color - uses the StdDraw library.
	 * @param clr the pen color to use
	 */
	public void drawFilled(Color clr) {
		Color savedColor = StdDraw.getPenColor();
		StdDraw.setPenColor(clr);
		this.drawFilled();
		StdDraw.setPenColor(savedColor);
	}
	//
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	///////////////////// COMPUTATION GEOMETRY METHODS, IMPLEMENTED USING JTS ///////////////////////
    //
	//
	/** Test if a specified line segment intersects this polygon.<br>
	 * Returns true if this polygon and the specified line segment have at least one point in common.<br>
	 * Uses JTS method: boolean Geometry.intersects(Geometry g)
	 * @param p1 first point on line
	 * @param p2 second point on line
	 * @return true if line segment intersects this polygon, false otherwise
	 */
    public boolean intersects(Point2D p1, Point2D p2) {
		Geometry lineSeg = LineSegToJTSGeom(p1, p2);
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		return lineSeg.intersects(polygon);
    }

	/** Test if a specified point touches this polygon.<br>
	 * Uses JTS method: boolean Geometry.touches(Geometry g)
	 * @param p point to test
	 * @return true if point touches this polygon, false otherwise
	 */
    public boolean touches(Point2D p) {
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		GeometryFactory factory = new GeometryFactory();
		return polygon.touches(factory.createPoint(new Coordinate(p.X(),p.Y())));
    }
    
	/** Test if a specified line segment touches this polygon.<br>
	 * Returns true if this polygon and the line segment have at least one point in common,
	 * but none of the line segment points lie in the interior of the polygon perimeter.<br>
	 * Uses JTS method: boolean Geometry.touches(Geometry g)
	 * @param p1 first end point of line segment
	 * @param p2 second end point of line segment
	 * @return true if line segment touches this polygon, false otherwise
	 */
    public boolean touches(Point2D p1, Point2D p2) {
		Geometry lineSeg = LineSegToJTSGeom(p1, p2);
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		return lineSeg.touches(polygon);
    }

	/** Test if a specified point is contained inside this polygon.<br>
	 * Uses JTS method: boolean Geometry.contains(Geometry g)
	 * @param p point to test
	 * @return true if this polygon contains point, false otherwise
	 */
    public boolean contains(Point2D p) {
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		GeometryFactory factory = new GeometryFactory();
		return polygon.contains(factory.createPoint(new Coordinate(p.X(),p.Y())));
    }
    
	/** Test if a specified line segment is entirely contained inside this polygon.<br>
	 * Uses JTS method: boolean Geometry.contains(Geometry g)
	 * @param p1 first end point of line segment
	 * @param p2 second end point of line segment
	 * @return true if this polygon contains line segment, false otherwise
	 */
    public boolean contains(Point2D p1, Point2D p2) {
		Geometry lineSeg = LineSegToJTSGeom(p1, p2);
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		return polygon.contains(lineSeg);
    }
    
	/** Test if the specified line segment points are disjoint from this polygon.<br>
	 * Returns true if none of the line segment points lie on the perimeter or the interior
	 * of this polygon.<br>
	 * Uses JTS method: boolean Geometry.disjoint(Geometry g)
	 * @param p1 first end point of line segment
	 * @param p2 second end point of line segment
	 * @return true if line segment and this polygon are disjoint, false otherwise
	 */
    public boolean disjoint(Point2D p1, Point2D p2) {
		Geometry lineSeg = LineSegToJTSGeom(p1, p2);
		Geometry polygon = Polygon2DtoJTSPolygon(this);
		return lineSeg.disjoint(polygon);
    }
    
	/** Get the convex hull of this polygon.<br>
	 * Note: returned hull is not a closed polygon (first point is not repeated as the last point).<br>
	 * Uses JTS method: Geometry.convexHull()
	 * @return the convex hull of this polygon
	 */
	public Polygon2D getHull() {
		Geometry polyJTS = Polygon2DtoJTSPolygon(this);
		Geometry hullJTS = polyJTS.convexHull();
		return JTSPolygonToPolygon2D(hullJTS);
	}
	//
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	///////////////////// UTILITY JTS POLYGON METHODS ///////////////////////////////////////////////
    //
    // LineSegToJTSGeom() : Make a JTS line segment object with specified end points
    //
	private static Geometry LineSegToJTSGeom(Point2D p1, Point2D p2) {
		GeometryFactory factory = new GeometryFactory();
		Coordinate[] line_coords = {new Coordinate(p1.X(),p1.Y()),	new Coordinate(p2.X(),p2.Y())};
		return factory.createLineString(line_coords);
	}
	//
	// JTSPolygonToPolygon2D() : Convert JTS Polygon object to a Polygon2D object (NOTE: returned polygon is *not* closed)
	//
	private static Polygon2D JTSPolygonToPolygon2D(Geometry pg) {
		if (pg == null) throw new IllegalArgumentException("JTSPolygonToPolygon2D() null argument.");
		Coordinate[] coords = pg.getCoordinates();
		Polygon2D poly = new Polygon2D();
		for (int i=0; i<pg.getNumPoints()-1; i++)
			poly.addPoint(new Point2D(coords[i].x, coords[i].y));
		return poly;
	}
	//
	// Polygon2DtoJTSPolygon() : Convert Polygon2D to a JTS Polygon object (NOTE: returned polygon *is* closed)
	//
	private static Polygon Polygon2DtoJTSPolygon(Polygon2D poly) {
		if (poly == null) throw new IllegalArgumentException("Polygon2DtoJTSPolygon() null argument.");
		Coordinate[] coords = new Coordinate[poly.size()+1];
		for (int i=0; i<poly.size(); i++) {
			coords[i] = new Coordinate(poly.get(i).X(), poly.get(i).Y());
		}
		coords[poly.size()] = new Coordinate(poly.get(0).X(), poly.get(0).Y());
		GeometryFactory geometryFactory = new GeometryFactory();
		return geometryFactory.createPolygon(coords);
	}
	//
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////

}
