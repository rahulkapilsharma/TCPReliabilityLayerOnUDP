/**
 * Write a Java program that defines a given search string, e.g. “dog”, and 
 * replaces each occurrence of that string with another string, e.g. “puppy”.
 * The program should prompt the user to enter a line of text and then print
 * out the text obtained after all string replacements. Hint: use the indexOf(.)
 * and the substring(.) methods of the String class.

 */
package com.homeWork2;
import java.util.Scanner;


public class ReplaceString {

	//Constant string
	public static final String OLD_WORD = "dog";
	public static final String NEW_WORD = "puppy";
	
	public static void main(String[] args) {
		//declaration and intialization
		int currentIndex 	=	0; //index of the searched word
		int nextIndex 		= 	0; //next occurrence index
		
		//get user input
		System.out.println("Please enter some text containing word dog: ");
		Scanner read = new Scanner(System.in);
		String str = read.nextLine();	
		
		//convert whole string to lower case
		str = str.toLowerCase();
		
		//check if there is no match
		if(str.indexOf(OLD_WORD, nextIndex)==-1)
		{
			System.out.println("No match found");
			System.exit(0);
		}
		else
		{
			currentIndex = str.indexOf(OLD_WORD, nextIndex);
		}

		//loop to traverse all the matching words and 
		//replaces with another string 
		while(currentIndex>=0 && nextIndex<str.length())
		{
			currentIndex = str.indexOf(OLD_WORD, nextIndex);
			System.out.print(str.substring(nextIndex,currentIndex) + NEW_WORD);
			nextIndex = currentIndex + OLD_WORD.length();
		}

		//suppress the leakage warning
		read.close();
	}
}//class

/*
CONSOLE:
Please enter some text containing word dog: 
hi dog hello dog bye dog
hi puppy hello puppy bye puppy
*/
