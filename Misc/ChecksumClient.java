import java.net.*;
import java.io.*;
import java.util.*;

public class ChecksumClient {

	public static void main(String[] args) throws Exception
	{
		final int PORT 				= 9999;
		final int SIZE 				= 7;
		final int SIZE_RESPONSE		= 3;
		final int TIME_OUT_VALUE	= 5000; //5sec
		DatagramSocket socClient 	= null;
		
		try
		{
			//creating random byte array
			byte[] sendData = new byte[SIZE];
			new Random().nextBytes(sendData);	
		
			//print the random byte array elements
			System.out.println("The sent bytes:");
			for(int i=0;i<sendData.length;i++)
				System.out.printf("Byte %d. = %d\n",i,sendData[i]);

			//server machine is same so getting local host
			InetAddress host = InetAddress.getLocalHost();
			
			//Construct the data gram packet
	         DatagramPacket packet = new DatagramPacket
	        		 ( sendData, sendData.length, host, PORT) ;

			// Construct the socket
			socClient = new DatagramSocket() ;
			
			//sending the data
			socClient.send(packet);
			
			//creating byte array for receive packet
			byte[] receiveData = new byte[SIZE_RESPONSE];
			packet.setData(receiveData);
			packet.setLength(receiveData.length);
			
			// Socket timeout 5s
	         socClient.setSoTimeout(TIME_OUT_VALUE) ;
	         
	         // Wait for response from the server
	         socClient.receive(packet);
	         
	         //print the response
			System.out.println("The received bytes:");
			for(int i=0;i<receiveData.length;i++)
				System.out.printf("Byte %d. = %d\n",i,receiveData[i]);
	         
			if(receiveData[0] ==0)
			{
				System.out.printf("checkSumMsbByte = %d, checksumLsbByte = %d\n",
						receiveData[1],receiveData[2]);	
				
				int checksum = (((receiveData[1]<<8)&0x0000ff00|
	 	            	       (receiveData[2])&0x000000ff)) ;
	 	            		
	    		//mask to remove negative bit extension
	    		checksum = checksum & 0x0000ffff;
				System.out.printf("OK status code. "
						+ "The returned checksum is: %d, Hex: %04x",
						checksum,checksum);
			}
			else
			{
				System.out.println("Error status code. Incorrect number of sent bytes.");
			}
			
	         //close the socket
			 if( socClient != null )
				 socClient.close() ;
		}
		catch(InterruptedIOException e)
		{
			System.out.println("Client socket timeout! Exception object"+e);
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Unknown Exception received");
			//print stack status at time of exception
			e.printStackTrace();
		}
	}
}

/*
Console:
The sent bytes:
Byte 0. = 22
Byte 1. = 79
Byte 2. = 90
Byte 3. = -120
Byte 4. = -58
Byte 5. = 51
Byte 6. = 85
Byte 7. = -76
The received bytes:
Byte 0. = 0
Byte 1. = 115
Byte 2. = 64
checkSumMsbByte = 115, checksumLsbByte = 64
OK status code. The returned checksum is: 29504, Hex: 7340

The sent bytes:
Byte 0. = 105
Byte 1. = 44
Byte 2. = -2
Byte 3. = -47
Byte 4. = -47
Byte 5. = -73
Byte 6. = 86
The received bytes:
Byte 0. = 1
Byte 1. = 0
Byte 2. = 0
Error status code. Incorrect number of sent bytes

The sent bytes:
Byte 0. = 86
Byte 1. = 17
Byte 2. = 83
Byte 3. = 99
Byte 4. = -26
Byte 5. = -88
Byte 6. = -7
Client socket timeout! Exception objectjava.net.SocketTimeoutException: Receive timed out

*/