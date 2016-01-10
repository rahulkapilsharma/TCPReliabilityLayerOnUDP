/**
 * H3.1 Write a Java program that shuffles (i.e. randomly reorders) a deck of cards. 
 * Assume that the deck only contains cards from one suit, e.g. Hearts. The program
 * should create a string array with all the cards in the deck before shuffling e.g.
 * in sorted order such as: “2 of Hearts”, “3 of Hearts”, ..., “King of Hearts” and
 * “Ace of Hearts” (13 cards in total). The program should print out the array by 
 * calling an array printing method (e.g. public static void printArray(String[] 
 * array)). The array printing method should format the array by including the 
 * “[ “ and the “]” characters at the beginning and the end of the arrays, printing
 * the array elements on the same line, separated by a “,” (comma). Then, the array
 * should be given to a method that randomly reorders (shuffles) the array elements
 * (the deck of cards), and returns a reference to the shuffled array (e.g. public
 * static String[] shuffleArray(String[] array)). Finally, the reordered array 
 * (the shuffled deck of cards) should also be printed using the above array printing
 * method. The deck shuffling algorithm should be implemented as follows. First, a 
 * card should be randomly chosen from the original (unshuffled) deck, and placed as
 * the first card in the shuffled deck. Then, another card should be chosen randomly 
 * form the original deck and placed as the second card in the shuffled deck. And so
 * on, the process should continue until all cards have been taken from the original 
 * deck. Note that during the shuffling process, the number of cards in the original
 * deck will decrease and the number of cards in the shuffled deck will increase. 
 * You also need to make sure that you select the same card in the original deck only
 * once. Hint: you can use the method int nextInt(int n) of class Random to generate a
 * random integer in the range [ 0,n − 1].
 */
package homeWork3;
import java.util.Random;

public class ShuffleCards {

	//static intializer so only static method accesses it
	private static int NUM_CARDS =13;
	
	//main method
	public static void main(String[] args) {

		String[] origArray = {"2 of Hearts", "3 of Hearts", "4 of Hearts", 
				"5 of Hearts", "6 of Hearts", "7 of Hearts", "8 of Hearts",
				"9 of Hearts",  "10 of Hearts", "Jack of Hearts",
				"Queen of Hearts",	"King of Hearts","Ace of Hearts"};

		//print original array
    	System.out.print("The deck before shuffling: ");
		printArray(origArray);

		//shuffle the array
		String[] shuffleArray = shuffleArray(origArray);
		
    	//print shuffled array
		System.out.print("The deck after shuffling: ");
		printArray(shuffleArray);

	}
    
	/*
	 * Method to print array of string objects
	 */
    public static void printArray(String[] array)
    {
    	System.out.print("[ ");
    	for(int i=0;i<NUM_CARDS-1;i++)
    		System.out.print(array[i] + ", ");
    	System.out.println(array[NUM_CARDS-1]+" ]");
    }
    
    public static String[] shuffleArray(String[] origArray)
    {
		String[] shuffledArray = new String[origArray.length];
		Random rand = new Random();
		
		//declaration and intialization
		int randNum,count = 0;
		//four bytes (32 bits) are sufficient to keep track of 13 elements
		int bitmap = 0;
		
		//choose a random number and take it from original array
		//and keep putting into shuffled array in increment order
    	for(int i=0;i<NUM_CARDS;i++)
    	{
    		randNum = rand.nextInt(NUM_CARDS);
    		
    		//create a bit map to check if the number is already generated
    		while(((bitmap>>randNum) & 1) != 0) //search a not generated number
    		{
    			//if already generated, generate again
        		randNum = rand.nextInt(NUM_CARDS);  			
    		}
    		
    		//update the bitmap, enable the particular generated number bit 
    		bitmap = bitmap | (1<<randNum); 
    		
    		//update shuffled array
    		shuffledArray[count++] = origArray[randNum]; 
    	}
    	
    	return shuffledArray;
    }
}//class

/*
CONSOLE:
The deck before shuffling: [ 2 of Hearts, 3 of Hearts, 4 of Hearts, 5 of Hearts, 
							6 of Hearts, 7 of Hearts, 8 of Hearts, 9 of Hearts, 
							10 of Hearts, Jack of Hearts, Queen of Hearts, 
							King of Hearts, Ace of Hearts ]
The deck after shuffling: [ 4 of Hearts, Jack of Hearts, 3 of Hearts, 8 of Hearts,
 							Queen of Hearts, King of Hearts, 10 of Hearts, 9 of Hearts,
 							 2 of Hearts, 7 of Hearts, 5 of Hearts, Ace of Hearts, 
 							 6 of Hearts ]
*/