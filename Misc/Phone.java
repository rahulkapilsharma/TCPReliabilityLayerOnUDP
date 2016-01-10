/**
 * H4.1 Write a Java class, Phone, that represents a phone. It should have
 * three fields, one representing its serial number, one representing its
 * type (i.e. Nokia 6510), and one representing its clock frequency in GHz.
 * The class should also have the methods shown in the UML diagram 
 */
package phonePackage;

public class Phone {
	
	// ------------- attributes ------------- //
	//instance attributes private 
	private long 	serialNumber;
	private String 	type;
	private double	clockFreqGHz;
	
	// ------------- public methods ------------- //
	//constructor method
	public Phone(long aSerialNumber, String aType, 
						double aClockFreqGHz)
	{
		serialNumber	= 	aSerialNumber;
		type			=	aType;
		clockFreqGHz	=	aClockFreqGHz;
	}//Phone ends

	public Phone clone()
	{
		return new Phone(serialNumber,type,clockFreqGHz);
	}//clone ends
	
	//instance methods: accessors
	public long getSerialNumber()
	{
		return serialNumber;
	}//getSerialNumber ends
	
	public String getType()
	{
		return type;
	}//getType ends
	
	public double getClockFrequency()
	{
		return clockFreqGHz;
	}//getClockFrequency ends
	
	//String printing
	public String toString()
	{
		return "Phone Serial number: "+serialNumber+
				" Phone type: "+type+
				" clock frequency: "+clockFreqGHz;
	}//toString ends
	
	//instance methods: mutator
	public void setClockFrequency(double newClockFrequencyGHz)
	{
		clockFreqGHz = newClockFrequencyGHz;
	}
}//class ends here
