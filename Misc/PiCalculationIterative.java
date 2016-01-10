/**
 * H2.2 Write a Java program that solves H2.1, but without recursion. 
 * Instead, inside of the worker method, use a loop to iterate over the
 * calculations, and the next term and the sum of all the terms calculated
 * so far should be stored in local variables and updated during each 
 * iteration. The only difference between this and the previous homework 
 * problem is the implementation of the algorithm inside the body of the
 * calculatePi(.) method.
 */

package com.homeWork2;

public class PiCalculationIterative {

	public static void main(String[] args) 
	{
		//call the iteartive function that uses do while 
		//until a TOLERANCE under 1e-8 is achieved
		double 	result = calculatePi();
		System.out.format("The approximate value for PI is: %.16f\n",result);
	}

	/*Worker method to compute value with the help of loop*/
	 public static double calculatePi()
	 {
		 //Constant for tolerance 
		 final double TOLERANCE 	= 1e-8;
		 
		 //multiplication factor as formula used is for pi/4 
		 final int 	  MUL_FACTOR	= 4;

		 //declaration and intialization
		 double sum 	= 0.0; //total sum zero
		 double termMag = 0.0; //current term zero
		 int	power 	= 0;   //intial power is zero
		 
		 do
		 {
			 termMag 	= ((power&1) == 1)?(-1.0f/(2*power+1)):(1.0f/(2*power+1));
			 sum 		= sum + termMag;
			 power++;
		 }while(Math.abs(termMag)>TOLERANCE);
		 
		 return MUL_FACTOR*sum;
	 }
}//class

/*
CONSOLE:
The approximate value for PI is: 3.1415926953497113
*/