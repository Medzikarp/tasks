package com.company;

import java.util.Arrays;
import java.util.Random;

public class Search {
    public static void main(String[] args) {

        // Generates sorted array of strings
        int[] array = new Random().ints(1, 1, 2).sorted().toArray();
        System.out.println(Arrays.toString(array));

        System.out.println(binarySearch(array, 2));
    }

    /**
     *  Binary search s.t. returns the lowest index of targeted value searched for in an array
     *  in logarithmic time with respect to the length of the input O(log n).
     */
    public static int binarySearch (int[] array, int target) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Null or empty array.");
        }
        // Set boundaries in which we will search
        int low = 0;
        int high = array.length-1;

        // While boundaries won't meet
        while (high >= low) {

            int mid = (low + high)/2;
            // Go to the right half in array if searched number is higher
            if (array[mid] < target) {
                low = mid + 1;
            // Go to the left half in array if searched number is lower
            } else if (array[mid] > target) {
                high = mid - 1;
            // But if the target is the same as the middle item
            } else if (array[mid] == target) {
                // Go to left the half if there is a
                if ((mid - 1 >= 0) && (array[mid-1] == target)) {
                    high = mid - 1;
                } else {
                    return mid;
                }
            }
        }

        return -1;
    }
}
