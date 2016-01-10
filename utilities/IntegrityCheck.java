package comm.utilities;
import java.util.*;

public class IntegrityCheck 
{	
	private static final int STATE_VECTOR_SIZE 			= 256;
	private static final int INTEGRITY_LEN 				= 4;

	private int countI = 0;
	private int countJ = 0;
	
	private int previousCountI = 0;
	private int previousCountJ = 0;
	
	//allocate memory to state vector
	private byte[] stateVector 				= new byte[STATE_VECTOR_SIZE];
	private byte[] previousStateVector 		= new byte[STATE_VECTOR_SIZE];

	
	//constructor doing Initialization of vector space
	public IntegrityCheck(int keylen, byte[] secretKey)
	{	
		/*
		 * RC4 allows key between 1 and 256 byte length
		 * In our case key length is 16 bytes (128 bit only)
		 */
		if(keylen>=1 && keylen<=256)
		{
			//set s[0]=0; s[1] = 1.....s[255]=255
			for(int i=0;i<STATE_VECTOR_SIZE;i++)
			{
				stateVector[i] 		= (byte)i;
				//System.out.println(stateVector[i]);
			}
			for(int i=0;i<16;i++)
				System.out.printf("%02x",secretKey[i]);
			System.out.println(" ");
			//do permutation of S
			int j=0;
			for(int i=0;i<STATE_VECTOR_SIZE;i++)
			{
				//System.out.println(i+" "+j+ " "+stateVector[i]+" "+secretKey[i%keylen]);
				j =	(j+(stateVector[i]&0xff)+secretKey[i%keylen]) & (STATE_VECTOR_SIZE-1); 
				swap(stateVector,i,j);
			}
			//System.out.println(Arrays.toString(stateVector));
		}
		else
		{
			 throw new IllegalArgumentException("Error: Key length should be between 1 and 256");
		}
	}

	
	public byte[] calculateIntegrity(int packetLen, byte[] packet)
	{
		/*
		 * check if packet length (data+header) is multiple of 4
		 */
		int padLength = 0;
		if(packetLen%4!=0)
		{
			padLength = 4 - (packetLen%4);
		}
		
		//generate encrypted stream 
		byte[] cryptedData 	= new byte[packetLen+padLength];
	
		encryptStreamGeneration(packet, packetLen, padLength, cryptedData);

		//compress the encrypted data into 4 bytes
		byte[] compressedData 	= new byte[INTEGRITY_LEN];
		compressStream(cryptedData, packetLen, compressedData);
		
		//for(int i=0;i<compressedData.length;i++)
			//System.out.println("Integrity field: " + (compressedData[i]&0xff));
		return compressedData;
	}
	
	public void encryptStreamGeneration(byte[] packet, 
											int packetLen,
												int padLength,
													byte[] cryptedData)
	{
		int t;
		int keyByte;

		/*
		 * save previous values for rolling back 
		 * in case invalid ACK is received
		 */
		previousCountI	= countI;
		previousCountJ  = countJ;
		
		for(int i=0;i<packetLen;i++)
			System.out.printf("%02x",packet[i]);
		for(int i=0;i<STATE_VECTOR_SIZE;i++)
			previousStateVector[i] = stateVector[i];
		
		System.out.printf("counters %d %d",countI,countJ);
		//do the stream generation
		for(int byteNum = 0; byteNum < packetLen + padLength; byteNum++)
		{
			countI = (countI+1) & (STATE_VECTOR_SIZE-1);
			countJ = (countJ+(stateVector[countI]&0xff)) & (STATE_VECTOR_SIZE-1);
	    	
			swap(stateVector,countI,countJ);
	    	
	    	t 		= ((stateVector[countI]&0xff)+(stateVector[countJ]&0xff)) & (STATE_VECTOR_SIZE-1);
	    	keyByte = stateVector[t];	 
	    
	    	if(byteNum<packetLen)
	    		cryptedData[byteNum]	=	(byte)(keyByte^packet[byteNum]);
	    	else
	    		cryptedData[byteNum]	=	(byte)(keyByte);

	    	//if(byteNum<packetLen)
	    		//System.out.println("packet byteNum"+cryptedData[byteNum]);
		}
    	//System.out.println(packetLen);
    	//System.out.println(padLength);
		for(int i=0;i<packetLen;i++)
			System.out.printf("%02x",cryptedData[i]);
	}

	public void compressStream(byte[] uncompressed, 
										int packetLen, 
										 	byte[] compressed)
	{
		int i=0;
		//do compression into 4 bytes
		for(byte j=0;j<INTEGRITY_LEN;j++)
		{
			i = j;
			compressed[j] = 0;

			while(i<packetLen)
			{
				compressed[j] = (byte)(compressed[j]^uncompressed[i]);
				i=i+INTEGRITY_LEN;
			}
		}
	}
	
	public void rollbackRC4Counter()
	{
		countI = previousCountI;
		countJ = previousCountJ;
		
		for(int i=0;i<STATE_VECTOR_SIZE;i++)
			stateVector[i] = previousStateVector[i];
	}
	public boolean compare(byte[] S1, byte[] S2)
	{
		if(Arrays.equals(S1,S2))
			return true;
		else
			return false;
	}
	
	public void swap(byte[] S, int a, int b)
	{
		byte temp = S[a];
		S[a] 	=	S[b];
		S[b]	= 	temp;
	}
}
