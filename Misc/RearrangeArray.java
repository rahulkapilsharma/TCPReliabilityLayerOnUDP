/**
 * H3.2 Write a Java program that separates the even and odd numbers in
 * an integer array. First, it should prompt the user to enter 10 integer
 * values and store them in an array. Then, it should rearrange the numbers
 * in the array in such a way that the even numbers are at the beginning of
 * the array and the odd numbers are at the end of the array. Implement the
 * algorithm in such a way that it rearranges the array elements “in place”,
 * that is, without storing/copying the array elements in any temporary array.
 * In this case, the only allowed operation is swapping (exchanging) two elements
 * in the array. The original order among the array elements can be completely 
 * destroyed as long as the even numbers are moved to the beginning of the array
 * and the odd numbers are moved to the end. The program should display the reordered
 * array elements. Note: The time complexity of the algorithm should be O(n), which
 * means that the number of operations (element swaps) to arrive at the desired 
 * element arrangement can be at most proportional to the size of the array.
 */

package homeWork3;
import java.util.Scanner;

public class RearrangeArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//declaration and intialization
		final int TOTAL_NUMBER = 10;

		//take user input
		System.out.printf("Please enter %d integers:\n", TOTAL_NUMBER);

		Scanner read = new Scanner(System.in);
		int[] inputArray = new int[TOTAL_NUMBER];

		for(int i=0; i<TOTAL_NUMBER; i++)
		{
			System.out.printf("The %d. integer: ",i+1);
			inputArray[i] = read.nextInt();
		}

		int tempVar = 0;
		int oddCounter = 0;
		
		//Single loop to separate even and odd
		for(int i=0; i<TOTAL_NUMBER; i++)
		{
			//we will keep to counters one starts from end and other from starting
			int ifOdd1 	= inputArray[i] & 1;

			if(ifOdd1 == 1)
			{
				oddCounter++;
			}
			else
			{
				if(oddCounter > 0)
				{
					//swap the values with the
					tempVar 					= inputArray[i];
					inputArray[i] 				= inputArray[i-oddCounter];
					inputArray[i-oddCounter] 	= tempVar;

				}
			}

		}

		//printout the rearranged array 
		System.out.printf("The resulting array is\n");
		for(int i=0; i<TOTAL_NUMBER; i++)
		{
			System.out.printf("The %d. integer: %d\n",i+1,inputArray[i]);
		}
		
		//for leakage warning suppression
		read.close();
		
	}
}//class

/*
Please enter 10 integers:
The 1. integer: 1
The 2. integer: 2
The 3. integer: 3
The 4. integer: 4
The 5. integer: 5
The 6. integer: 6
The 7. integer: 7
The 8. integer: 8
The 9. integer: 9
The 10. integer: 10
The resulting array is
The 1. integer: 2
The 2. integer: 4
The 3. integer: 6
The 4. integer: 8
The 5. integer: 10
The 6. integer: 3
The 7. integer: 7
The 8. integer: 1
The 9. integer: 9
The 10. integer: 5
*/
