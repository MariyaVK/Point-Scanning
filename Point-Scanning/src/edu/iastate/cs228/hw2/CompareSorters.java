package edu.iastate.cs228.hw2;

/**
 *  
 * @author Mariya Karasseva
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points
	 * with respect to their median coordinate point four times, each time using
	 * a different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning\n");
		System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");
		int trial = 1;
		int n = 0;
		String fileName = "";
		System.out.print("\nTrial " + trial + ": ");
		String key = sc.next();
		RotationalPointScanner insertionSort = null;
		RotationalPointScanner quickSort = null;
		RotationalPointScanner mergeSort = null;
		RotationalPointScanner selectionSort = null;
		while (key.equals("1") || key.equals("2")) {
			trial++;

			if (key.equals("1")) {
				do {
					System.out.print("Enter number of random points: ");
					n = sc.nextInt();
				} while (n <= 0);
				Random rand = new Random();
				Point[] points = generateRandomPoints(n, rand);
				insertionSort = new RotationalPointScanner(points, Algorithm.InsertionSort);
				quickSort = new RotationalPointScanner(points, Algorithm.QuickSort);
				mergeSort = new RotationalPointScanner(points, Algorithm.MergeSort);
				selectionSort = new RotationalPointScanner(points, Algorithm.SelectionSort);
			} else {
				System.out.println("Points from a file");
				System.out.print("File name: ");
				fileName = sc.next();
				insertionSort = new RotationalPointScanner(fileName, Algorithm.InsertionSort);
				quickSort = new RotationalPointScanner(fileName, Algorithm.QuickSort);
				mergeSort = new RotationalPointScanner(fileName, Algorithm.MergeSort);
				selectionSort = new RotationalPointScanner(fileName, Algorithm.SelectionSort);
			}

			//
			// Conducts multiple rounds of comparison of four sorting
			// algorithms.
			// Within
			// each round,
			// set up scanning as follows:
			//
			// a) If asked to scan random points, calls generateRandomPoints()
			// to
			// initialize
			// an array
			// of random points.
			//
			// b) Reassigns to the array scanners[] (declared below) the
			// references
			// to four
			// new
			// RotationalPointScanner objects, which are created using four
			// different values
			// of the Algorithm type: SelectionSort, InsertionSort, MergeSort
			// and
			// QuickSort.
			//
			//
			RotationalPointScanner[] scanners = new RotationalPointScanner[4];
			scanners[0] = selectionSort;
			scanners[1] = insertionSort;
			scanners[2] = mergeSort;
			scanners[3] = quickSort;

		

			System.out.println("\nalgorithm        size   time (ns)");
			System.out.println("-------------------------------------");
			for (int i = 0; i < scanners.length; i++) {
				scanners[i].scan();
				scanners[i].draw();
				System.out.println(scanners[i].stats());
				scanners[i].writePointsToFile();	
			}
			System.out.println("-------------------------------------\n");
			

			// For each input of points, do the following.
			//
			// a) Initialize the array scanners[].
			//
			// b) Iterate through the array scanners[], and have every scanner
			// call
			// the
			// scan() and draw()
			// methods in the RotationalPointScanner class. You can visualize
			// the
			// result of
			// each scan.
			// (Windows have to be closed manually before rerun.)
			//
			// c) After all four scans are done for the input, print out the
			// statistics
			// table (cf. Section 2).
			//
			// A sample scenario is given in Section 2 of the project
			// description.

			System.out.print("\nTrial " + trial + ": ");
			key = sc.next();
		}
		System.out.println("Thank you for using this tool");
	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] ×
	 * [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts
	 *            number of points
	 * @param rand
	 *            Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException
	 *             if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		try {
			Point[] arr = new Point[numPts];
			for (int i = 0; i < arr.length; i++) {
				int x = rand.nextInt(101) - 50;
				int y = rand.nextInt(101) - 50;
				arr[i] = new Point(x, y);
			}
			return arr;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
}
