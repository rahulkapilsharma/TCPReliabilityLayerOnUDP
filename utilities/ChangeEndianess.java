package comm.utilities;

public class ChangeEndianess 
{
	
	public static void switchDataPktEndianess(byte[] packet,
												int packetLength,
												int pktTypeLen,
												int seqNumLength,
												int lengthField,
												int integrityLen,
												IntegrityCheck object)
	{
		int idx = 0;

		//skip packet type field
		idx = idx + pktTypeLen;

		/*
		 * Change endianess for multi-byte field of Sequence number
		 */
		int forwardCntr = idx;
		for(int i=idx+seqNumLength-1;i>=((seqNumLength>>1)+idx);i--)
		{
			object.swap(packet,i,forwardCntr);
			forwardCntr++;
		}
		idx = idx + seqNumLength;

		//skip length field
		idx = idx + lengthField;

		/*
		 * Change endianess for multi-byte field of Data
		 */
		forwardCntr = idx;
		for(int i=idx+packetLength-1;i>=((packetLength>>1)+idx);i--)
		{
			object.swap(packet,i,forwardCntr);
			forwardCntr++;
		}

		idx = idx + packetLength;
		
		/*
		 * Change endianess for multi-byte field of Integrity length
		 */
		forwardCntr = idx;
		for(int i=idx+integrityLen-1;i>=((integrityLen>>1)+idx);i--)
		{
			object.swap(packet,i,forwardCntr);
			forwardCntr++;
		}
	}
	
	public static void switchAckPktEndianess(byte[] packet,
			int pktTypeLen,
			int ackLength,
			int integrityLen,
			IntegrityCheck object)
	{
		int idx = 0;

		//skip packet type field
		idx = idx + pktTypeLen;

		/*
		 * Change endianess for multi-byte field of Ack number
		 */
		int forwardCntr = idx;
		for(int i=idx+ackLength-1;i>=((ackLength>>1)+idx);i--)
		{
			object.swap(packet,i,forwardCntr);
			forwardCntr++;
		}

		idx = idx + ackLength;

		/*
		 * Change endianess for multi-byte field of Integrity length
		 */
		forwardCntr = idx;
		for(int i=idx+integrityLen-1;i>=((integrityLen>>1)+idx);i--)
		{
			object.swap(packet,i,forwardCntr);
			forwardCntr++;
		}
	}
}
