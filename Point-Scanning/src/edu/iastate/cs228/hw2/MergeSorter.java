package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;

/**
 *  
 * @author Mariya Karasseva
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = "mergesort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSortRec(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. One way is to make copies of the two halves of pts[], recursively
	 * call mergeSort on them, and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSortRec(Point[] pts) {
		int n = pts.length;
		if (n <= 1)
			return;
		int mid = n / 2;
		Point[] l = new Point[mid];
		Point[] r = new Point[n - mid];

		for (int i = 0; i < mid; i++) {
			l[i] = pts[i];
		}
		for (int i = mid; i < n; i++) {
			r[i - mid] = pts[i];
		}
		mergeSortRec(l);
		mergeSortRec(r);

		merge(pts, l, r);
	}

	/**
	 * @param a array to be merged into
	 * @param l left array to be merged
	 * @param r right array to be merged
	 */
	private void merge(Point[] a, Point[] l, Point[] r) {
		int left = l.length;
		int right = r.length;
		int i = 0, j = 0, k = 0;
		while (i < left && j < right) {
			if (pointComparator.compare(l[i],r[j]) <= 0) {
				a[k++] = l[i++];
			} else {
				a[k++] = r[j++];
			}
		}
		while (i < left) {
			a[k++] = l[i++];
		}
		while (j < right) {
			a[k++] = r[j++];
		}
	}
	// Other private methods in case you need ...

}
