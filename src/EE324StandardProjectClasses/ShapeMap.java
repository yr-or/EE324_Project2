// CMcA DCU - R1.1 - October 2020

package EE324StandardProjectClasses;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A collection of Polygon2D objects along with a source and a destination point which
 * together specify a path finding problem to be solved.
 * <p><b>NOTE:</b> This class uses the Polygon2D and Point2D classes.</p>
 * <p><b>NOTE:</b> Requires Sedgewick's and Wayne's StdDraw library.</p>
 * 
 * @author Conor McArdle (conor.mcardle@dcu.ie)
 */
public class ShapeMap implements Iterable<Polygon2D> {

	private Point2D srcPoint;   // source point on map
	private Point2D destPoint;  // destination point on map
	private ArrayList<Polygon2D> polygons;  // polygon data

	/** Construct an empty ShapeMap. */
	public ShapeMap() {
		polygons = new ArrayList<Polygon2D>();
	}

	/** Construct a ShapeMap with a specified source and destination point. No polygons are created.
	 * @param src the path source (starting) point
	 * @param dest the path destination (ending) point
	 */
	public ShapeMap(Point2D src, Point2D dest) {
		polygons = new ArrayList<Polygon2D>();
		srcPoint = new Point2D(src);
		destPoint = new Point2D(dest);
	}

	/** Construct a ShapeMap with a specified set of polygons and a specified source and destination point.
	 * @param polys the list of polygons to construct the map with
	 * @param src the path source (starting) point
	 * @param dest the path destination (ending) point
	 */
	public ShapeMap(ArrayList<Polygon2D> polys, Point2D src, Point2D dest) {
		// defensive copy
		polygons = new ArrayList<Polygon2D>();
		for (int i=0; i<polys.size(); i++) {
			polygons.add(new Polygon2D(polys.get(i)));
		}
		srcPoint = new Point2D(src);
		destPoint = new Point2D(dest);
	}

	/** Construct a ShapeMap using a specified input map file.
	 * @param fileName to load map from
	 */
	public ShapeMap(String fileName) {
		MapFileReader mapReader = new MapFileReader(fileName);
		srcPoint = mapReader.getSourcePoint();
		destPoint = mapReader.getDestinationPoint();
		polygons = mapReader.parsePolygonData();
	}

	/** Add a new polygon shape to this map.
	 * @param poly the polygon to add to the map
	 */
	public void addPolygon(Polygon2D poly) {
		polygons.add(poly);
	}

	/** Get the source point (path starting point) of this map.
	 * @return the source point as a Point2D object
	 */
	public Point2D sourcePoint() {
		return srcPoint;
	}

	/** Get the destination point (path ending point) of this map.
	 * @return the destination point as a Point2D object
	 */
	public Point2D destinationPoint () {
		return destPoint;
	}

	/** Iterator supporting for-each style iteration over all polygons in this map. 
	 * Example:<br>
	 * {@code for (Polygon2D poly : sm) { poly.draw(); } // sm a ShapeMap object}
	 * */
	@Override
	public Iterator<Polygon2D> iterator() {
		Iterator<Polygon2D> it = new Iterator<Polygon2D>() {
			private int currentIndex = 0;
			@Override
			public boolean hasNext() { return (currentIndex < polygons.size()); }
			@Override
			public Polygon2D next() { return polygons.get(currentIndex++); }
			@Override
			public void remove() { throw new UnsupportedOperationException(); }
		};
		return it;
	}

	/** Get all points of all polygons in this shape map - does not include the source and destination points.
	 * @return list of all map points
	 */
	public List<Point2D> getAllPoints() {
		List<Point2D> points = new ArrayList<Point2D>();
		for (Polygon2D poly : polygons) {
			for (Point2D p : poly) {
				points.add(p);
			}
		}
		return points;
	}

	/** Draw perimeter of each polygon in this ShapeMap. Requires StdDraw library. **/
	public void draw() {
		for (int i=0; i<polygons.size(); i++) {
			polygons.get(i).draw();
		}
	}

	/** Draw perimeter of each polygon in this ShapeMap, with a given color. Requires StdDraw library.
	 * @param clr the pen color to use
	 **/
	public void draw(Color clr) {
		Color savedColor = StdDraw.getPenColor();
		StdDraw.setPenColor(clr);
		this.draw();
		StdDraw.setPenColor(savedColor);
	}

	/** Draw perimeter of each polygon in this ShapeMap, with a given color and pen width.
	 * Requires StdDraw library.
	 * @param clr the pen color to use
	 * @param penWidth width of the pen to use
	 **/
	public void draw(Color clr, double penWidth) {
		double savedPenWidth = StdDraw.getPenRadius();
		StdDraw.setPenRadius(penWidth);
		this.draw(clr);
		StdDraw.setPenRadius(savedPenWidth);
	}

	/** Draw and fill each polygon in this ShapeMap. Requires StdDraw library. */
	public void drawFilled() {
		for (int i=0; i<polygons.size(); i++) {
			polygons.get(i).drawFilled();
		}
	}

	/** Draw and fill each polygon in this ShapeMap, with a given color. Requires StdDraw library.
	 * @param clr the pen color to use
	 **/
	public void drawFilled(Color clr) {
		Color savedColor = StdDraw.getPenColor();
		StdDraw.setPenColor(clr);
		this.drawFilled();
		StdDraw.setPenColor(savedColor);
	}

	/** Demonstrates construction and drawing of a shape map containing a single polygon. */
	public static void shapeMapDemo() {
		// Make a polygon
		Polygon2D shape1 = new Polygon2D();
		shape1.addPoint(new Point2D(0.1,0.1));
		shape1.addPoint(new Point2D(0.1,0.9));
		shape1.addPoint(new Point2D(0.9,0.5));
		// Make source and destination points and draw them
		Point2D src = new Point2D(0.5,0.95);
		Point2D dest = new Point2D(0.5,0.05);
		src.draw(Color.GREEN,0.05);
		dest.draw(Color.RED,0.05);
		// make a shape map from the polygon and source and destination points
		ShapeMap sm = new ShapeMap(src,dest);
		sm.addPolygon(shape1);
		// draw all polygons in shape map (only one in this example)
		sm.drawFilled(Color.ORANGE);
		sm.draw(Color.BLACK,0.03);
	}

}
