package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author Mariya Karasseva
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class RotationalPointScanner {
	private Point[] points;

	private Point medianCoordinatePoint; // point whose x and y coordinates are
											// respectively the medians of
											// the x coordinates and y
											// coordinates of those points in
											// the array
											// points[].
	private Algorithm sortingAlgorithm;

	protected String outputFileName; // "select.txt", "insert.txt", "merge.txt",
										// or "quick.txt"

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[]. Set
	 * outputFileName.
	 * 
	 * @param pts
	 *            input array of points
	 * @throws IllegalArgumentException
	 *             if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0)
			throw new IllegalArgumentException();
		points = new Point[pts.length];
		for (int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
		sortingAlgorithm = algo;
		setOutputFileName(algo);
	}

	/**
	 * This constructor reads points from a file. Set outputFileName.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException
	 *             if the input file contains an odd number of integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo)
			throws FileNotFoundException, InputMismatchException {
		File file = new File(inputFileName);
		Scanner sc = new Scanner(file);
		int counter = 0;
		while (sc.hasNextInt()) {
			counter++;
			sc.nextInt();
		}
		// sc.close();
		if (counter % 2 != 0)
			throw new InputMismatchException();
		points = new Point[counter / 2];
		sc = new Scanner(file);
		int i = 0;
		while (sc.hasNextInt()) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			Point pt = new Point(x, y);
			points[i] = pt;
			i++;
		}
		sortingAlgorithm = algo;
		setOutputFileName(algo);
		sc.close();
	}

	/**
	 * Carry out three rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b)
	 * Sort points[] again by the y-coordinate to get the median y-coordinate.
	 * c) Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates. d) Sort points[] again by the polar angle with respect to
	 * medianCoordinatePoint.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of
	 * SelectionSorter, InsertionSorter, MergeSorter, or QuickSorter to carry
	 * out sorting. Copy the sorting result back onto the array points[] by
	 * calling the method getPoints() in AbstractSorter.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {

		AbstractSorter aSorter;
		switch (sortingAlgorithm) {
		case InsertionSort: {
			aSorter = new InsertionSorter(points);
			break;
		}
		case QuickSort: {
			aSorter = new QuickSorter(points);
			break;
		}
		case MergeSort: {
			aSorter = new MergeSorter(points);
			break;
		}
		case SelectionSort: {
			aSorter = new SelectionSorter(points);
			break;
		}
		default: {
			aSorter = null;
			break;
		}
		}

		// Sort by x
		aSorter.setComparator(0);
		long start1 = System.nanoTime();
		aSorter.sort();
		long finish1 = System.nanoTime();
		int xMCP = aSorter.getMedian().getX();
		
		//aSorter.getPoints(points);
		//System.out.println(sortingAlgorithm + ":");
		//System.out.println(toString());
		
		// Sort by Y
		aSorter.setComparator(1);
		long start2 = System.nanoTime();
		aSorter.sort();
		long finish2 = System.nanoTime();
		int yMCP = aSorter.getMedian().getY();
		
		//aSorter.getPoints(points);
		//System.out.println(sortingAlgorithm + ":");
		//System.out.println(toString());
		
		// Sort by angle
		medianCoordinatePoint = new Point(xMCP, yMCP);
		// System.out.println(medianCoordinatePoint.getX() + ", " +
		// medianCoordinatePoint.getY());
		aSorter.setReferencePoint(medianCoordinatePoint);
		aSorter.setComparator(2);
		long start3 = System.nanoTime();
		aSorter.sort();
		long finish3 = System.nanoTime();

		scanTime = (finish1 - start1) + (finish2 - start2) + (finish3 - start3);

		aSorter.getPoints(points);

		// create an object to be referenced by aSorter according to
		// sortingAlgorithm.
		// for each of the three
		// rounds of sorting, have aSorter do the following:
		//
		// a) call setComparator() with an argument 0, 1, or 2. in case it is 2,
		// must
		// have made
		// the call setReferencePoint(medianCoordinatePoint) already.
		//
		// b) call sort().
		//
		// sum up the times spent on the three sorting rounds and set the
		// instance
		// variable scanTime.
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project
	 * description.
	 */
	public String stats() {
		String s = String.format("%-17s", sortingAlgorithm);
		s += String.format("%-7d", points.length);
		s += String.format("%-10d", scanTime);
		return s;
	}

	/**
	 * Write points[] after a call to scan(). When printed, the points will
	 * appear in order of polar angle with respect to medianCoordinatePoint with
	 * every point occupying a separate line. The x and y coordinates of the
	 * point are displayed on the same line with exactly one blank space in
	 * between.
	 */
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < points.length; i++) {
			s += "(" + points[i].getX() + ", " + points[i].getY() + ")\n";
		}
		return s;
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException {
		File file = new File(outputFileName);
		PrintWriter writer = new PrintWriter(file);
		writer.print(toString());
		writer.close();
	}

	/**
	 * This method is called after each scan for visually check whether the
	 * result is correct. You just need to generate a list of points and a list
	 * of segments, depending on the value of sortByAngle, as detailed in
	 * Section 4.1. Then create a Plot object to call the method myFrame().
	 */
	public void draw() {
		int numSegs = 0; // number of segments to draw
		for (int i = 0; i < points.length - 1; i++)
			if (!points[i].equals(points[i + 1])) {
				numSegs += 2; // Increase by 2 since we will add a segment
								// between the points and a segment from a point
								// to medianCoordinatePoint
			}
		if (!points[points.length - 1].equals(points[0]))
			numSegs += 2;
		// Based on Section 4.1, generate the line segments to draw for display
		// of the
		// sorting result.
		// Assign their number to numSegs, and store them in segments[] in the
		// order.
		Segment[] segments = new Segment[numSegs];
		int segIdx = 0;
		for (int i = 0; i < points.length - 1; i++)
			if (!points[i].equals(points[i + 1])) {
				Segment seg = new Segment(points[i], points[i + 1]);
				segments[segIdx] = seg;
				segIdx++;
				Segment seg1 = new Segment(points[i], medianCoordinatePoint);
				segments[segIdx] = seg1;
				segIdx++;
			}
		if (!points[points.length - 1].equals(points[0])) {
			Segment seg = new Segment(points[points.length - 1], points[0]);
			segments[segIdx] = seg;
			segIdx++;
			Segment seg1 = new Segment(points[points.length - 1], medianCoordinatePoint);
			segments[segIdx] = seg1;
			segIdx++;
		}

		String sort = null;

		switch (sortingAlgorithm) {
		case SelectionSort:
			sort = "Selection Sort";
			break;
		case InsertionSort:
			sort = "Insertion Sort";
			break;
		case MergeSort:
			sort = "Mergesort";
			break;
		case QuickSort:
			sort = "Quicksort";
			break;
		default:
			break;
		}

		// The following statement creates a window to display the sorting
		// result.
		Plot.myFrame(points, segments, sort);

	}

	private void setOutputFileName(Algorithm algo) {
		switch (sortingAlgorithm) {
		case SelectionSort:
			outputFileName = "select.txt";
			break;
		case InsertionSort:
			outputFileName = "insert.txt";
			break;
		case MergeSort:
			outputFileName = "merge.txt";
			break;
		case QuickSort:
			outputFileName = "quick.txt";
			break;
		default:
			break;
		}
	}
}
