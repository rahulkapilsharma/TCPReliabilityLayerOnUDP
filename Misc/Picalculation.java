/**
 * H2.1 Write a Java program that approximates the value of pi (3.1415926536...)
 * numerically using recursion (i.e. the method solving the problem should call 
 * itself repeatedly with different input parameters). The main method should call
 * a worker method with header similar to public static double calculatePi(double
 * currentSum, int power),
 */
package com.homeWork2;


public class Picalculation {

	public static void main(String[] args) 
	{
		//declaration and intialization
		int 	intialPower 	= 0;  //start with zero intial power
		double 	intialSum 		= 0.0;// intial sum value is zero
		
		//call the recursive function that will call itself 
		//until a TOLERANCE under 1e-4 is achieved
		double 	result = calculatePi(intialSum,intialPower);
		System.out.format("The approximate value for PI is: %.16f\n",result);
	}

	/*Worker method to compute value with the help of recursion*/
	 public static double calculatePi(double currentSum, int power)
	 {
		 //Constant for tolerance 
		 final double TOLERANCE 	= 1e-4;
		 
		 //multiplication factor as formula used is for pi/4
		 final int 	  MUL_FACTOR	= 4;

		 //using & to see if it is odd or even number
		 double termMag = ((power&1) == 1)?(-1f/(2*power+1)):(1f/(2*power+1));

		 if((Math.abs(termMag))>TOLERANCE)
		 {
			 return calculatePi(currentSum + MUL_FACTOR*termMag, ++power);
		 }
		 else
		 {
			 return (currentSum+MUL_FACTOR*termMag);
		 }
	 }
}//class

/*
CONSOLE:
The approximate value for PI is: 3.1417925545829350
*/