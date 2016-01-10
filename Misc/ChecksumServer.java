import java.net.*;
import java.util.*;

public class ChecksumServer {

	public static void main (String[] args) throws Exception
	{
		final int PORT 				= 9999;
		
		//sufficient length, assume max client will send 20 bytes
		final int SIZE 				= 20;
		
		final int SIZE_RESPONSE		= 3;

		DatagramSocket socServer	= null;

		try
		{
			//creating random byte array
			byte[] sendData = new byte[SIZE_RESPONSE];
			byte[] receiveData = new byte[SIZE];

			//Construct the data gram packet
	         DatagramPacket recvPacket = new DatagramPacket(receiveData,receiveData.length) ;
	         DatagramPacket sendPacket = null;

			// Construct the socket
			socServer = new DatagramSocket(PORT) ;
			
			while(true)
			{
	            // Receive a packet
				socServer.receive(recvPacket) ;
	            // Print the packet
	            System.out.println( recvPacket.getAddress() + " " 
	            + recvPacket.getPort() + ": " + Arrays.toString(receiveData) ) ;

	            //fill the response
	            if((recvPacket.getLength() & 1)==0) //if even
	            {
	            	//no error status
	            	sendData[0] = 0;
	            	
	            	//compute check sum
	            	long sum = 0;

	            	for(int i=0;i<receiveData.length;i=i+2)
	            	{ 
	            		//make 16 bit word and mask  
	            		long num = (((receiveData[i]<<8)&0x0000ff00|
	            	       (receiveData[i+1])&0x000000ff)) ;
	            		
	            		//mask to remove negative bit extension
	            		num = num & 0x0000ffff;
	            		
	            		//add all 16 bit numbers
	            		sum = sum +num;
		            	System.out.printf("\n %d %d",i,num);
	            	}
	            	
	            	System.out.printf("\n 0x%x",sum);

	            	// 32-bit sum to 16 bits and do 16 bit masking
	            	//to remove sign extension and end around carry
	            	while(sum>>16!=0)
	                   sum = (sum & 0xffff) + (sum >> 16);

	            	//take complement
	               sum = ~sum;
	               System.out.println("\nChecksum "+(sum & 0xffff));
	               sendData[1] = (byte)((sum>>8) & 0xFF);
	               sendData[2] = (byte)((sum) & 0xFF);
	            }
	            else
	            {
	            	//error status
	    			sendData[0] = 1;
	    			
	    			//checksum msb zero
	    			sendData[1] = 0;
	    			
	    			//checksum lsb zero
	    			sendData[2] = 0;
	            }
	            sendPacket =new DatagramPacket(sendData,
	            							sendData.length,
	            							recvPacket.getAddress(),
	            							recvPacket.getPort()) ;


	            // Return the packet to the sender
	            socServer.send(sendPacket) ;
			}

		}
		catch(Exception e)
		{
			System.out.println("Unknown Exception received");
			//print stack status at time of exception
			e.printStackTrace();
		}
	}

}
