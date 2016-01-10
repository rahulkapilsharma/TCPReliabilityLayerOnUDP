package comm.utilities;

public class ByteIntConversion 
{
	public static int byteToInt(byte[] byteArray)
	{
		int intNumber = (byteArray[0]<<24)&0xff000000|
				(byteArray[1]<<16)&0x00ff0000|
				(byteArray[2]<< 8)&0x0000ff00|
				(byteArray[3]<< 0)&0x000000ff;
		return intNumber;
	}
	
	public static void intToByte(byte[] buffer, int startIdx, int intNum)
	{
		buffer[startIdx] 		= (byte) (intNum >> 24);
		buffer[startIdx+1] 		= (byte) (intNum >> 16);
		buffer[startIdx+2] 		= (byte) (intNum >> 8);
		buffer[startIdx+3] 		= (byte) (intNum);
	}
}
