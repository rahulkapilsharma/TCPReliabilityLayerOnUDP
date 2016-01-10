/**
 * Write a Java program that reverses the order of elements in an integer
 * array. First, it should define and initialize an integer array and then 
 * print the array out by calling an integer array printing method with 
 * header similar to public static void printArray(int[] array). The array
 * printing method should format the array by including the “[ “ and the 
 * “]” characters at the beginning and the end of the arrays and print the
 * array elements on the same line, separated by a “,” (comma). Then, it 
 * should call another method, e.g. public static void reverse(int[] array),
 * that reverses the order of the elements of the array referred to by its 
 * input argument. The order reversing algorithm should work “in place”, 
 * without creating additional arrays for temporary storage. One reasonable 
 * approach is to swap (exchange) the first element with the last element, 
 * the second element with the next to last element, and so on. Note that 
 * the method should correctly handle arrays with both even and odd number
 * of elements. Finally, after returning from the reversing method, the 
 * program should print the reversed array using the above integer array 
 * printing method.
 */
package com.homeWork2;
import java.util.Scanner;

public class ReverseArray {

	public static void main(String[] args) {
		//scanner object creation
		Scanner read = new Scanner(System.in);
	
		//taking input from user 
		System.out.println("please enter array size");
		int arraySize 	= read.nextInt();

		//declare and intialize array as required by user
		int[] array 	= new int[arraySize];

		System.out.println("please enter array elements");
		for(int i=0;i<array.length;i++)
		{
			array[i] 	= read.nextInt();
		}
		
		System.out.print("The original array: ");
		printArray(array);
		
		System.out.print("The reversed array: ");
		reverse(array);
		
		//suppress the leakage warning
		read.close();
	}
	
	/*
	 * prints the array 
	 */
	public static void printArray(int[] array)
	{
		//formatting the print
		System.out.print("[ ");
		for(int j=0;j<array.length-1;j++)
			System.out.print(array[j]+", ");
		System.out.println(array[array.length-1]+" ]");
	}
	
	/*
	 * method to reverse the array works for odd
	 * as well as even terms
	 */
	public static void reverse(int[] array)
	{
		//declaration and intialization
		int forwardCntr =0;
		
		//for loop to traverse and swap values
		for(int reverseCntr=array.length-1;reverseCntr>=(array.length>>1);reverseCntr--)
		{
			int temp 			= array[forwardCntr];
			array[forwardCntr]	= array[reverseCntr];
			array[reverseCntr]	= temp;
			forwardCntr++;
		}
		
		//prints the array after reversing
		printArray(array);
	}
}

/*
 Console:
 please enter array size
4
please enter array elements
1
2
3
4
The original array: [ 1, 2, 3, 4 ]
The reversed array: [ 4, 3, 2, 1 ]

please enter array size
7
please enter array elements
1
2
3
4
5
6
7
The original array: [ 1, 2, 3, 4, 5, 6, 7 ]
The reversed array: [ 7, 6, 5, 4, 3, 2, 1 ]

 */
