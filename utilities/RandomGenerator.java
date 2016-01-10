package comm.utilities;

import java.util.*;

public class RandomGenerator 
{
	private static final int SEQ_SIZE_BYTES				= 4;
	private static final int KEY_SIZE_BYTES 			= 16;

	//static fields that do not change on creation of new objects
	static private byte[] initialSeqNum = new byte[SEQ_SIZE_BYTES];
	static private byte[] initialSecKey = new byte[KEY_SIZE_BYTES];

	/*
	*	Generate Random Sequence number and Security key and hardcode on
	*	transmitter and receiver side
	*/
	public static void main(String[] args) 
	{
		initRandomGenerator();
	}
	public static void initRandomGenerator()
	{    
		Random generator = new Random();
		//Random generator = new Random(SEED);

		//generate random 32 bit sequence number
		generator.nextBytes(initialSeqNum);	

		//generate random 128 bit security key number
		generator.nextBytes(initialSecKey);	

		System.out.println("Initial seq Num"+Arrays.toString(initialSeqNum));
		System.out.println("Initial Sec key"+Arrays.toString(initialSecKey));
	}
}
