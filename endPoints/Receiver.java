// After receiving the last data packet (packet type: aah) and sending acknowledgment for it, the
// receiver should display the received byte sequence on the screen.
// on receiving end packet inform the receiver
// If an acknowledgment is received for a packet after some timeout events, the timeout value and the
// timeout counter should be reset to their initial values.
// close sender, close receiver show outputs
// case where acknowledgment packet can not be accepted -> wrong IC field or packet type
// case where data packet can't be accepted
// check for pad length in protocol on both side
// test RC4 with third party as (int byte conversion may cause some problems)
// TODO close the sockets after error before exiting program
// packet lost, retransmit, 
// ack lost . retransmit, send previous one, if ack received reset timer
// MSB LSB changes
// project report (10-15 pages) discussing the problem, their design and solution 
// (for example, flow charts, block diagrams, UML class diagrams,
// state transition diagrams, etc.), and the application’s output.
//TODO limit check like in RC4
//TODO socket TX position change
// code modularization,
// class design, code block organization, class and variable naming, comments, etc. 


package comm.endPoints;

import java.net.*;
import comm.utilities.*;

public class Receiver {

	/*default values*/
	static final int PORT 				= 9999;
	static final int DATA_SIZE			= 500;
	static final int PCKT_SIZE 			= 40;
	static final int PCKT_SIZE_RESPONSE	= 9;
	static final int MPS				= 30;

	static final int PCKT_TYPE_LEN	 	= 1;
	static final int LENGTH_FIELD		= 1;
	static final int SEQ_NUM_LEN	 	= 4;
	static final int INTEGRITY_LEN 		= 4;
	static final int ACK_NUM_LEN	 	= 4;

	static final int NEXT_PACKET		= 0x55;
	static final int END_PACKET			= 0xAA;
	static final int ACK_PACKET			= 0xFF;

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
		DatagramSocket socRx 			= null;
		DatagramPacket sendPacket		= null;
		DatagramPacket receivePacket	= null;
		int  seqNum	= ByteIntConversion.byteToInt(initialSeqNum);

		/*
		 * Allocate send packet for Acknowledgment
		 * and receive packet from transmitter for
		 * maximum data size 
		 */
		byte[] sendData 	= new byte[PCKT_SIZE_RESPONSE];
		byte[] receiveData 	= new byte[PCKT_SIZE];

		/*
		 * Buffer to store all 500 bytes
		 */
		byte[] allReceivedData = new byte[DATA_SIZE];

		/*
		 * initialize integrity stream vector
		 */
		
		//for data stream integrity calculation
		IntegrityCheck objectData = new IntegrityCheck(initialSecKey.length,initialSecKey);
		
		//for data stream integrity calculation
		IntegrityCheck objectAck  = new IntegrityCheck(initialSecKey.length,initialSecKey);

		System.out.println("*****************************************************\n"
						 + "*********************RECEIVER UP*********************\n"
						 + "*****************************************************");

		try
		{
			/*
			 * creating socket
			 */
			socRx = new DatagramSocket(PORT);

			/*
			 * Prepare Datagram packet for reception
			 */
			InetAddress host = InetAddress.getLocalHost();

			receivePacket = new DatagramPacket
					(receiveData, receiveData.length, host, PORT) ;	

		}
		catch(Exception e)
		{
			System.out.println("Exception: Socket or datagrams can not be intialized");

			//print stack status at time of exception
			e.printStackTrace();
			socRx.close();
		}

		int packetCount =0;
		int offset		=0;

		while(true)
		{
			/*
			 * receive data packet
			 */
			try
			{
				socRx.receive(receivePacket);

				/*
				 * print Received individual packet
				 */
				System.out.printf("\nReceived DATA %d packet: ",packetCount++);

				for(int i=0;i<receivePacket.getLength();i++)
				{
					if(i%20 ==0)
						System.out.printf("\n");
					System.out.printf("%02x ",receiveData[i]);
				}
				System.out.printf("\n");

				/*
				 * check for duplicate packet 
				 * in case Ack send last time was lost
				 */
				byte[] seqNumReceived 	= new byte[SEQ_NUM_LEN];
				int    seqNumReceivedInt;
				//boolean duplicateFlag = false;

				for(int i=0;i<SEQ_NUM_LEN;i++)
					seqNumReceived[i] = receiveData[PCKT_TYPE_LEN + i];
//					seqNumReceived[i] = receiveData[SEQ_NUM_LEN + PCKT_TYPE_LEN - 1 - i];

				seqNumReceivedInt = ByteIntConversion.byteToInt(seqNumReceived);

				System.out.println("\nseq Num"+seqNumReceivedInt+""+seqNum);

				/*
				 * Check if it is duplicate packet
				 */
				if(seqNumReceivedInt == (seqNum - receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN]))
				{
					/*
					 * If it is duplicate packet send previous Ack
					 */
					System.out.println("\n duplicate received");
					socRx.send(sendPacket);
				}
				else
				{
					/*
					 * If not a duplicate packet, 
					 * validate the received data
					 * else discard all packets 
					 */
					if(validateDataPacket(receiveData, 
							receivePacket.getLength(),
							seqNum, seqNumReceivedInt, objectData))
					{
						//Increase seq Num (ordinal number for next expected byte)
						seqNum = seqNum + receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN];

						/*
						 * copy data to buffer
						 */
						int dataLength = receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN];

						for(int dataIdx=0;dataIdx<dataLength;dataIdx++)
							allReceivedData[offset+dataIdx] = 
							receiveData[LENGTH_FIELD+PCKT_TYPE_LEN + SEQ_NUM_LEN+dataIdx];

						offset += receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN];

						/*
						 * Prepare Tx Packet
						 */
						prepareTxPacket(ACK_PACKET,seqNum+1,sendData);

						byte[] integrityData = objectAck.calculateIntegrity(PCKT_TYPE_LEN+ACK_NUM_LEN, sendData);

						/*
						 * appending the Integrity data
						 */
						for(int i=0;i<integrityData.length;i++)
							sendData[PCKT_TYPE_LEN+ACK_NUM_LEN+i] = integrityData[i];

						/*ChangeEndianess.switchAckPktEndianess(sendData, 
								PCKT_TYPE_LEN, 
								ACK_NUM_LEN,
								INTEGRITY_LEN,
								objectAck);*/

						sendPacket =new DatagramPacket(sendData,
								sendData.length,
								receivePacket.getAddress(),
								receivePacket.getPort()); 

						/*if(!counterTest1)
						{
							counterTest1 = true;
						}
						else*/
						{
							socRx.send(sendPacket);
							System.out.println("Ack send");
						}
						/*
						 * print the data sequence if it is last packet
						 * and acknowledgment has been send
						 */
						if(receiveData[PCKT_TYPE_LEN-1] == (byte)END_PACKET)
						{
							System.out.printf("All packets received correctly \nData Sequence received:");
							for(int i=0;i<DATA_SIZE;i++)
							{
								if(i%MPS ==0)
									System.out.printf("\n");
								System.out.printf("%02x ",allReceivedData[i]);
							}
							System.out.println("\n\nLast Packet received correctly. Turning off the receiver");
							System.exit(0);
						}
					}
					else
					{
						/*
						 * Roll back Integrity check counters as Ack not send
						 * for invalid packets
						 */
						System.out.println("\nrolling back RC4 algorithm counters");
						objectData.rollbackRC4Counter();

					}
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

	static boolean counterTest = false;
	static boolean counterTest1 = false;

	private static boolean validateDataPacket(byte[] receiveData,
												int length, 
													int seqNum, 
														int seqNumReceived, 
															IntegrityCheck object)
	{
		//convert packet to little endian
		/*ChangeEndianess.switchDataPktEndianess(receiveData, 
												receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN], 
												PCKT_TYPE_LEN,
												SEQ_NUM_LEN,
												LENGTH_FIELD,
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
		
		//2. check for valid data packet type and in-order seq number
		if((receiveData[PCKT_TYPE_LEN-1] == (byte)NEXT_PACKET) || 
				(receiveData[PCKT_TYPE_LEN-1] == (byte)END_PACKET))
		{
			//check for correct in order sequence number
			/*byte[] seqNumReceived = new byte[SEQ_NUM_LEN];
			
			for(int i=0;i<SEQ_NUM_LEN;i++)
				seqNumReceived[i] = receiveData[PCKT_TYPE_LEN + i];
			
			dataPacketFlag = (ByteIntConversion.byteToInt(seqNumReceived) == seqNum);*/
			dataPacketFlag = (seqNumReceived == seqNum);
		}

		//simulate packet validation fail
		/*if(!counterTest)
		{
			receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN] = 31;
			counterTest = true;
		}*/
		
		//3. length boundary check
		boolean payloadLengthFlag = (receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN] <= MPS);

		System.out.println("length "+receiveData[PCKT_TYPE_LEN + SEQ_NUM_LEN]);
		System.out.printf("integrity value "+integrityFlag+""+dataPacketFlag+""+payloadLengthFlag);
		//return if all conditions are valid
		return (integrityFlag & dataPacketFlag & payloadLengthFlag);
	}
	
	public static void prepareTxPacket(int packetType, 
										int ackNum,
											byte[] scratchBuffer)
	{
		//Construct the data gram packet
		int idx = 0;

		scratchBuffer[idx] 	= (byte)packetType;
		idx 			= idx + PCKT_TYPE_LEN;

		ByteIntConversion.intToByte(scratchBuffer, idx, ackNum);
		
		idx = idx + ACK_NUM_LEN;
	}
}
