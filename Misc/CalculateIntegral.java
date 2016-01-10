/**
 * H3.3 Write a Java program that calculates the value of the integral of 
 * sinx over 0 to pi interval numerically. (The exact value of the integral is
 * 2.) The main method should call a worker method with the header public static
 * double calcIntegral(double lowerLimit, double upperLimit, int numSubIntervals),
 * and it should display the value of the integral returned by the worker method.
 * The worker method should take the above three parameters: L, the lower limit 
 * of integration, U, the upper limit of the integration, and N, the number of 
 * subintervals between the lower and the upper limits.
 */

package homeWork3;
import java.util.Scanner;

public class CalculateIntegral {

	public static void main(String[] args) {

		// constant values
		final double LOWER_LIMIT = 0.0;
		final double UPPER_LIMIT = Math.PI;
		
		//take user input for intervals
		Scanner read = new Scanner(System.in);
		System.out.print("Please enter number of sub intervals: ");
		int numSubIntervals = read.nextInt();

		//call the worker method
		double result = calcIntegral(LOWER_LIMIT, UPPER_LIMIT,
												numSubIntervals);

		System.out.format("The value of the integral is: %.4f", result);
		
		read.close();
	}
	
	/*
	 * Worker method to calculate Integral
	 */
	public static double calcIntegral(double lowerLimit, 
										double upperLimit, int
											numSubIntervals)
	{
		double delta 			= (upperLimit - lowerLimit)/numSubIntervals;
		double xi 	 			= 0.0;
		double sum				= 0.0;
		
		for(int i=0; i<numSubIntervals;i++)
		{
			xi 			= lowerLimit + i*delta;		
			sum 		= sum+((Math.sin(xi) + Math.sin(xi+delta))/2)*delta;
		}
		return sum;
	}

}//class

/**
CONSOLE:
Please enter number of sub intervals: 1000
The value of the integral is: 2.0000

Please enter number of sub intervals: 100
The value of the integral is: 1.9998
*/