package phonePackage;

public class PhoneTest {

	public static void main(String[] args) 
	{
		//general constructor
		Phone aPhone = new Phone(123,"Nokia 6600",1500.0);
		
		// explicit  toString() invocation
		System.out.print("\nThe object aPhone => " + aPhone.toString()); 

		//object cloning: object created by the clone() method 
		Phone clonedPhone = aPhone.clone(); 
		System.out.print("\nThe cloned object aPhone => " + clonedPhone);//aPhone.toString()); 

		// modifying the object: mutator methods
		aPhone.setClockFrequency(1600.5);;
		System.out.print("\n\nThe object aPhone after modification => " +aPhone);

		// accessor methods
		System.out.printf("\ncloned phone details are (%d,%s,%.2f).",
								clonedPhone.getSerialNumber(), 
								clonedPhone.getType(),
								clonedPhone.getClockFrequency()); 

	}//main ends here

}//class ends here

/*
 * 
CONSOLE:
The object aPhone => 
Phone Serial number: 123 Phone type: Nokia 6600 clock frequency: 1500.0
The cloned object aPhone => 
Phone Serial number: 123 Phone type: Nokia 6600 clock frequency: 1500.0

The object aPhone after modification => 
Phone Serial number: 123 Phone type: Nokia 6600 clock frequency: 1600.5
cloned phone details are (123,Nokia 6600,1500.0).
 */
