package comm.endPoints;

import java.net.*;
import java.io.*;
import java.util.*;
import comm.utilities.*;

public class Transmitter {

	/*default values*/
	static final int PORT 				= 9999;
	static final int PCKT_SIZE 			= 40;
	static final int PCKT_SIZE_RESPONSE	= 9;
	static final int MPS				= 30;
	static final int DATA_SIZE			= 500;

	static final int PCKT_TYPE_LEN	 	= 1;
	static final int LENGTH_FIELD		= 1;
	static final int SEQ_NUM_LEN	 	= 4;
	static final int INTEGRITY_LEN 		= 4;
	static final int ACK_NUM_LEN		= 4;
	
	static final int HEADER_LEN 		= (PCKT_TYPE_LEN + SEQ_NUM_LEN+ LENGTH_FIELD);

	static final int NEXT_PACKET		= 0x55;
	static final int END_PACKET			= 0xAA;
	static final int ACK_PACKET			= 0xFF;

	static final int TIME_OUT_VALUE		= 1000; //1sec
	static final int MAX_NUM_RETX		= 4; 	// 4 time outs to receive a packet before we transmit

	/*
	 * Hard coding Security key and Sequence Number after creating from
	 * random generator
	 */
	static private byte[] initialSeqNum = new byte[]{35, 85, 111, -52};
	static private byte[] initialSecKey = new byte[]{-34, 106, 77, -112,
													51, -20, 11, 49, 119,
													-102, 114, -29, -22,
													74, 26, 88};
	
	public static void main(String[] args) throws Exception
	{
		DatagramSocket socTx = null;
		System.out.println("*****************************************************\n"
						 + "*******************TRANSMITTER***********************\n"
						 + "*****************************************************\n");

		System.out.println("************** Starting Data Transfer ***************");
		/*
		 * generate random 500 bytes
		 * creating random byte array
		 */
		byte[] sendData = new byte[DATA_SIZE];
		new Random().nextBytes(sendData);	

		System.out.printf("Data Sequence to be send:");
		for(int i=0;i<DATA_SIZE;i++)
		{
			if(i%MPS ==0)
				System.out.printf("\n");
			System.out.printf("%02x ",sendData[i]);
		}
		System.out.printf("\n");
		
		/*
		 * calculate number of packets and take ceil if not exact multiple of MPS
		 */
		int numPackets 	= sendData.length/MPS + ((sendData.length%MPS>0)?1:0);
	
		/*
		 * send n-1 packets out of n packets with packet type 55h
		 */
		int  packetLength = MPS;
		int  offset = 0;
		int  seqNum	= ByteIntConversion.byteToInt(initialSeqNum);
		
		/*
		 * initialize integrity stream vector
		 */
		IntegrityCheck objectData = new IntegrityCheck(initialSecKey.length,initialSecKey);
		IntegrityCheck objectAck  = new IntegrityCheck(initialSecKey.length,initialSecKey);

		byte[] sendPacket 		= new byte[PCKT_SIZE];
		byte[] receivePacket 	= new byte[PCKT_SIZE_RESPONSE];

		for(int packetNum = 0; packetNum<numPackets-1;packetNum++)
		{
			prepareTxPacket(NEXT_PACKET, seqNum, packetLength, sendData, offset, sendPacket);
			//if(packetNum==0)
				//sendPacket[5] = MPS+1;
			byte[] integrityData = objectData.calculateIntegrity(packetLength+HEADER_LEN, sendPacket);

			/*
			 * append Integrity data
			 */
			for(int i=0;i<integrityData.length;i++)
				sendPacket[packetLength+HEADER_LEN+i] = integrityData[i];
			
			/*
			* Change Packet Endianess from Little to Big Endian
			*/
			/*ChangeEndianess.switchDataPktEndianess(sendPacket, packetLength, PCKT_TYPE_LEN,
										SEQ_NUM_LEN, LENGTH_FIELD, INTEGRITY_LEN, objectData);*/
			
			
			/*
			 * print the packet
			 */
			System.out.printf("\nsending DATA %d packet: ",packetNum);
			for(int i=0;i<PCKT_SIZE;i++)
			{
				if(i%20 ==0)
					System.out.printf("\n");
				System.out.printf("%02x ",sendPacket[i]);
			}
			System.out.printf("\n");
						
			//Increase offset so correct data is read from the 500 bytes buffer
			offset = offset + MPS;

			// Increase sequence Number for next packet
			seqNum = seqNum + MPS;

			/*
			 * Stop and wait protocol, send the packet and wait for valid 
			 * Acknowledgment packet from the receiver. If time Out occurs,
			 * retransmit the packet until communication failure of 4th 
			 * timeout
			 */
			if(!stopAndWaitProtocol(sendPacket,PCKT_SIZE,receivePacket, seqNum+1, 
										socTx, objectAck))
			{
				System.out.println("\nError : Communication failure between Tx and Rx");
				System.exit(0);
			}
		}
		
		/*
		 * send nth packet with packet type AAh
		 */
		packetLength = sendData.length % MPS;

		prepareTxPacket(END_PACKET, seqNum, packetLength, sendData, offset, sendPacket);
		byte[] integrityData = objectData.calculateIntegrity(packetLength+HEADER_LEN, sendPacket);

		/*
		 * append Integrity data
		 */
		for(int i=0;i<integrityData.length;i++)
			sendPacket[packetLength+HEADER_LEN+i] = integrityData[i];

		/*
		* Change Packet Endianess from Little to Big Endian
		*/
		/*ChangeEndianess.switchDataPktEndianess(sendPacket, packetLength, PCKT_TYPE_LEN,
				SEQ_NUM_LEN, LENGTH_FIELD, INTEGRITY_LEN, objectData);*/
		
		seqNum = seqNum + packetLength;
		
		/*
		 * print the packet
		 */
		System.out.printf("\nsending DATA %d packet: ",numPackets-1);
		for(int i=0;i<packetLength + HEADER_LEN + INTEGRITY_LEN;i++)
		{
			if(i%20 ==0)
				System.out.printf("\n");
			System.out.printf("%02x ",sendPacket[i]);
		}
		System.out.printf("\n");
		
		/*
		 * Stop and wait protocol, send the packet and wait for valid 
		 * Acknowledgment packet from the receiver. If time Out occurs,
		 * retransmit the packet until communication failure of 4th 
		 * timeout
		 */
		if(!stopAndWaitProtocol(sendPacket,packetLength + HEADER_LEN + INTEGRITY_LEN, 
								receivePacket, seqNum+1, socTx, objectAck))
		{
			System.out.println("Error : Communication failure between Tx and Rx");
			System.exit(0);
		}
	}
	static boolean counterTest = false;

	public static boolean stopAndWaitProtocol(byte[] sendData, 
												int sendPacketSize, 
												byte[] receiveData,
												int ackNum, 
												DatagramSocket socTx,
												IntegrityCheck object)
	{
		DatagramPacket sendPacket		= null;
		DatagramPacket receivePacket	= null;
		
		try
		{
			/*
			 * creating socket
			 */
			socTx = new DatagramSocket();
			/*
			 * Prepare Datagram packet for reception
			 */
			InetAddress host = InetAddress.getLocalHost();
			sendPacket = new DatagramPacket
					( sendData, sendPacketSize, host, PORT) ;	

			receivePacket = new DatagramPacket
					(receiveData, receiveData.length, host, PORT) ;	

		}
		catch(Exception e)
		{
			System.out.println("Exception: Socket or datagrams can not be intialized");
			//print stack status at time of exception
			e.printStackTrace();
		}
		
		//reset timeout value and timeout counter for next packet reception
		int reTxCounter 				= 0;
		int cumulativeTimeOut			= TIME_OUT_VALUE;
		while(reTxCounter<MAX_NUM_RETX)
		{	
			/*
			 * for time profiling
			 */
			//long start = System.nanoTime();    
			//long elapsedTime = 0; 

			try
			{
				/*
				 * send Packet and set the timeout value
				 */
				System.out.println("Sending Packet reTxCounter "+reTxCounter+
						"timerValue"+cumulativeTimeOut);
				socTx.send(sendPacket);
				socTx.setSoTimeout(cumulativeTimeOut);

				/*
				 * receive packet
				 */
				socTx.receive(receivePacket);

				/*
				 * validate acknowledgment contents
				 * only then send new packet, else 
				 * drop this acknowledgment
				 */
				if(validateAckPacket(receiveData, 
										receivePacket.getLength(),
											ackNum,
												object))
				{
					System.out.println("Valid Ack received");
					return true;
				}
				else
				{
					System.out.println("Dropping Ack packets due to inconsistent data");
					
					/*
					 * roll back counters for integrity check
					 */
					object.rollbackRC4Counter();

					reTxCounter++;
//					/cumulativeTimeOut = cumulativeTimeOut<<1;
				}
			}
			catch(InterruptedIOException e)
			{
				System.out.printf("\n************Timeout %d: No Ack Received***************",
						reTxCounter);
				System.out.printf("\n*******Timeout at %d ms, Retransmission %d**********\n",
						cumulativeTimeOut,reTxCounter);
				//for correcting value
				//receiveData[5] = MPS;
				reTxCounter++;

				//TODO should be increase time out
				cumulativeTimeOut = cumulativeTimeOut<<1;
			}
			catch(Exception e)
			{
				System.out.println("Unknown Exception received");
				
				//print stack status at time of exception
				e.printStackTrace();
			}
			
			//elapsedTime = System.nanoTime() - start;
			//System.out.println("time taken for reception: "+elapsedTime/1000000);
		}
		return false;
	}
	
	private static boolean validateAckPacket(byte[] receiveData,
												int length, 
												 int ackNum,
													IntegrityCheck object)
	{
		//convert packet to little endian
		/*ChangeEndianess.switchAckPktEndianess(receiveData, 
				PCKT_TYPE_LEN, 
				ACK_NUM_LEN,
				INTEGRITY_LEN,
				object);*/

		//calculate integrity data
		byte[] integrityDataCalculated = object.calculateIntegrity(length-INTEGRITY_LEN, receiveData);

		//1. compare Integrity data calculated with sender's integrity field
		byte[] integrityDatareceived = new byte[INTEGRITY_LEN];

		for(int i=0;i<INTEGRITY_LEN;i++)
			integrityDatareceived[i] = receiveData[length-INTEGRITY_LEN+i];

		boolean integrityFlag = object.compare(integrityDataCalculated, integrityDatareceived);
		boolean dataPacketFlag = false;
				
		/*
		 * check for correct ack num
		 */
		if(receiveData[PCKT_TYPE_LEN-1] == (byte)ACK_PACKET)
		{
			//check for correct in order sequence number
			byte[] ackNumReceived = new byte[ACK_NUM_LEN];
			
			for(int i=0;i<ACK_NUM_LEN;i++)
				ackNumReceived[i] = receiveData[PCKT_TYPE_LEN + i];
			
			dataPacketFlag = (ByteIntConversion.byteToInt(ackNumReceived) == ackNum);
			
			/*if(!counterTest)
			{
				counterTest = true;
				dataPacketFlag = false;
			}*/
		}
		
		System.out.println("ack num: "+ackNum+", integrityFlag " + integrityFlag +", dataPacketFlag "+dataPacketFlag);

		return (integrityFlag & dataPacketFlag);
	}	
	
	public static void prepareTxPacket(int packetType, 
			int seqNum,
				int packetLength,
					byte[] originalData,
						int offset,
						 		byte[] scratchBuffer)
	{
		//Construct the data gram packet
		int idx = 0;

		scratchBuffer[idx] 	= (byte)packetType;

		idx 			= idx + PCKT_TYPE_LEN;

		ByteIntConversion.intToByte(scratchBuffer, idx, seqNum);

		idx = idx + SEQ_NUM_LEN;

		scratchBuffer[idx]		= (byte)packetLength;
		idx = idx + LENGTH_FIELD;
		
		/*
		 * send data multi-byte block in Big-Endian format
		 */
		for(int i=0;i<packetLength;i++)
			scratchBuffer[idx+i] =  originalData[offset+i];

	}

}