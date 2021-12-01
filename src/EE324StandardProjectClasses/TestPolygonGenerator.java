// CMcA DCU - R1.1 - October 2020

package EE324StandardProjectClasses;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * A set of static methods for generating different types of test polygons.
 * <p>These methods will be useful for testing your hull finding algorithm.
 * Each method returns a polygon, as an array of Point2D objects.
 * Note that polygons are "open" (first point not repeated as last point in array).</p>
 * <p>Rounding is applied to point coordinate values to avoid potential floating-point
 * rounding issues. (This helps eliminate precision problems when using these
 * generated polygons with the JTS library (or your own geometry algorithms). See:
 * https://locationtech.github.io/jts/jts-faq.html#robustness Section D).</p>
 * 
 * @author Conor McArdle (conor.mcardle@dcu.ie)
 */
public class TestPolygonGenerator {

	/**
	 * Makes a 10 point polygon that contains some collinear points (pattern 1)
	 * @return the points of the polygon as an array of Point2D
	 */
	public static Point2D[] collinear1() {
		Point2D[] poly = new Point2D[10];
		poly[0] = new Point2D(0.2,0.2);
		poly[1] = new Point2D(0.2,0.3);
		poly[2] = new Point2D(0.2,0.45);
		poly[3] = new Point2D(0.2,0.7);
		poly[4] = new Point2D(0.3,0.7);
		poly[5] = new Point2D(0.45,0.7);
		poly[6] = new Point2D(0.7,0.7);
		poly[7] = new Point2D(0.7,0.45);
		poly[8] = new Point2D(0.7,0.2);
		poly[9] = new Point2D(0.45,0.2);
		return poly;
	}

	/**
	 * Makes a 10 point polygon that contains some collinear points (pattern 2)
	 * @return the points of the polygon as an array of Point2D
	 */
	public static Point2D[] collinear2() {
		Point2D[] poly = new Point2D[10];
		poly[0] = new Point2D(0.45,0.2);
		poly[1] = new Point2D(0.7,0.2);
		poly[2] = new Point2D(0.7,0.45);
		poly[3] = new Point2D(0.7,0.7);
		poly[4] = new Point2D(0.45,0.7);
		poly[5] = new Point2D(0.3,0.7);
		poly[6] = new Point2D(0.2,0.7);
		poly[7] = new Point2D(0.2,0.45);
		poly[8] = new Point2D(0.2,0.3);
		poly[9] = new Point2D(0.2,0.2);
		return poly;
	}

	/** Make a random set of points uniformly distributed within the unit square from (0,0) to (1,1).
	 * Precision of point coordinates is limited to 4 places of decimals.
	 * @param size number of point in generated polygon
	 * @return the points of the polygon, as an array of Point2D
	 * @exception IllegalArgumentException if size is less than 3
	 */
	public static Point2D[] randomPolyUniform(int size) {
		if (size < 3)
			throw new IllegalArgumentException("randomPolyUniform() size must be 3 or greater");
		Point2D[] pg = new Point2D[size];
		// limit precision of point coordinates to avoid precision problems
		BigDecimal x;
		BigDecimal y;
		for (int i=0; i<size; i++) {
			x = BigDecimal.valueOf(Math.random());
			x = x.setScale(4, RoundingMode.HALF_UP);
			y = BigDecimal.valueOf(Math.random());
			y = y.setScale(4, RoundingMode.HALF_UP);
			pg[i] = new Point2D(x.doubleValue(), y.doubleValue());
		}
		return pg;
	}

	/** Make a random set of points normally-distributed and centered on point (0.5,0.5).
	 * Variance of distribution is set so that points are likely to remain with the
	 * unit square from (0,0) to (1,1). Precision of point coordinates is limited to 4 places of decimals.
	 * @param size number of points in generated polygon
	 * @return the points of the polygon as an array of Point2D
	 * @exception IllegalArgumentException if size is less than 3
	 */
	public static Point2D[] randomPolyGaussian(int size) {
		if (size < 3)
			throw new IllegalArgumentException("randomPolyGaussian() size must be 3 or greater");
		Point2D[] pg = new Point2D[size];
		// limit precision of point coordinates to avoid precision problems
		BigDecimal x;
		BigDecimal y;
		Random randgen = new Random();
		for (int i=0; i<size; i++) {
			x = BigDecimal.valueOf(randgen.nextGaussian()/8.0 + 0.5);
			x = x.setScale(4, RoundingMode.HALF_UP);
			y = BigDecimal.valueOf(randgen.nextGaussian()/8.0 + 0.5);
			y = y.setScale(4, RoundingMode.HALF_UP);
			pg[i] = new Point2D(x.doubleValue(), y.doubleValue());
		}
		return pg;
	}

}
