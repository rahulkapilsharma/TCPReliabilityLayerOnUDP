/**
 * H4.2 Write a Java class, CellTower, that represents a cell tower 
 * (base station) in a cellular network. It should have three fields,
 * one representing the name of the operator (e.g. AT&T), one representing
 * the height of its antenna, and one representing the cell radius. The 
 * class should also have the methods shown in the UML diagram 
 */

package cellTowerPackage;

public class CellTower {
	
	// ------------- attributes ------------- //
	//instance attributes private 
	private String 	operator;
	private double	antennaHeight;
	private double	cellRadius;
	
	public static final String[] operatorArray =
		{"AT&T", "Verizon", "T-Mobile"};
	
	// ------------- public methods ------------- //
	//constructor method
	public CellTower(String anOperator, double anAntennaHeight, 
						double aCellRadius)
	{
		boolean isValid = checkTowerParams(anOperator,
											anAntennaHeight,
											aCellRadius);
		if(isValid)
		{
			operator		= 	anOperator;
			antennaHeight	=	anAntennaHeight;
			cellRadius		=	aCellRadius;
		}
		else
		{
			System.out.println("Invalid cell tower parameters."
					+ " CellTower object could not be created.");
			System.exit(0);	
		}
	}//CellTower ends

	//instance methods: accessors
	public String getOperator()
	{
		return operator;
	}//getOperator ends
	
	public double getAntennaHeight()
	{
		return antennaHeight;
	}//getAntennaHeight ends
	
	public double getCellRadius()
	{
		return cellRadius;
	}//getCellRadius ends
	
	//String printing
	public String toString()
	{
		return "Cell phone Tower operator: "+operator+
				" antennaHeight: "+antennaHeight+
				" cellRadius: "+cellRadius;
	}//toString ends
	
	//instance methods: mutator
	public void setAntennaHeight(double newHeight)
	{
		if(newHeight>0)
			antennaHeight = newHeight;
	}
	
	// ------------- private methods ------------- //
	private boolean checkTowerParams(String anOperator, double anAntennaHeight, 
			double aCellRadius)
	{
		boolean flag = false;

		if(anAntennaHeight <= 0 || aCellRadius<=0)
			return false;

		for(int i=0;i<operatorArray.length;i++)
		{
			if(operatorArray[i].equalsIgnoreCase(anOperator))
				flag = true;				
		}
		
		return flag;
	}
}//class ends here
