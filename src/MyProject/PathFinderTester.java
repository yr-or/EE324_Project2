package MyProject;

import java.awt.Color;
import EE324StandardProjectClasses.ShapeMap;
import edu.princeton.cs.introcs.StdDraw;

// Write any unit or temporary tests for path finder development here
// THIS CODE FILE IS NOT GRADED. - Don't put any code here that is part
// of your final path finder solution.

public class PathFinderTester {
	// Basic test to load and draw a map file
	public static void main(String args[]) {
		StdDraw.setCanvasSize(800, 800);
		StdDraw.setScale(-0.05, 1.05);
		ShapeMap inputMap = new ShapeMap("src//MAPS//TEST-MAP-2.TXT");
		inputMap.draw(Color.BLUE);
		inputMap.sourcePoint().draw(Color.RED, 0.02);
		inputMap.destinationPoint().draw(Color.GREEN, 0.02);
	}
}
