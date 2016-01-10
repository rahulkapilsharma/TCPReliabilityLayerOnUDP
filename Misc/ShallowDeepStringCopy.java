/**
 * H2.4 Write a Java program that produces both a shallow and a deep copy 
 * of a String array. First, it should define and initialize an original
 * array of String objects and print out the array by calling a String array
 * printing method (e.g. static void printStringArray(String[] array)). The
 * array printing method should print the String array in a format similar 
 * to H2.3. Then, it should call the shallow copy method (e.g. static 
 * String[] shallowCopyStringArray(String[] array)) that produces a shallow
 * copy of the input array (the references to the String objects are copied, 
 * not the String objects). The method should return a reference to the 
 * (shallow) copy of the array. The resulting copy should be printed using 
 * the String array printing method. After that, it should also call a deep 
 * copy method (e.g. static String[] deepCopyStringArray(String[] array)) that
 * produces a deep copy of the input array (the String objects are copied, and
 * the array copy contains references to the copied String objects). The method
 * should return a reference to the (deep) copy of the array. The resulting copy
 * should be printed using the String array printing method. (Note that since
 * String objects are immutable, the shallow copy is sufficient, and deep copying
 * does not make much practical sense. However, this is a good exercise for you 
 * to understand the differences between the two copying methods.)

 */
package com.homeWork2;
import java.util.Arrays;

public class ShallowDeepStringCopy {

	public static void main(String[] args) {
		//define a string array object
		String[] array= {"Jack", "Jill","on","the","hill"};
		
		//call for shallow copy
		String[] arrayRefernce = shallowCopyStringArray(array);
		System.out.println("Shallow copy: "+Arrays.toString(arrayRefernce));
		
		//call for deep copy
		String[] array2 = deepCopyStringArray(array);
		System.out.println("Deep copy: "+Arrays.toString(array2));
	}
	
	
	/*
	 * Shallow copy: only copies the reference to the object
	 */
	 static String[] shallowCopyStringArray(String[] array)
	 {
		 String[] arrayRefernce = array;
		 return arrayRefernce;
	 }
	 
	 
	/*
	 * Deep copy: copies all the string fields however as 
	 * strings are immutable shallow and deep copy are same
	 */
	 static String[] deepCopyStringArray(String[] array)
	 {
		 //make a string copy on the basis of array length
		 String[] arrayCopy = new String[array.length];
		 
		 //copies all string objects
		 for(int i=0;i<array.length;i++)
		 {
			 arrayCopy[i] = new String(array[i]);
		 }
		 
		 //alternate method
		 //String[] arrayCopy = Arrays.copyOf(array, array.length);;
		 return arrayCopy;
	 }

}

/*
CONSOLE:
Shallow copy: [Jack, Jill, on, the, hill]
Deep copy: [Jack, Jill, on, the, hill]
*/
