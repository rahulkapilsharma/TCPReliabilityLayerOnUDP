package cellTowerPackage;

public class CellTowerTest {

	public static void main(String[] args) 
	{
		//general constructor
		CellTower tower = new CellTower("AT&T",45,200);
		
		// explicit  toString() invocation
		System.out.print("\nThe object tower => " + tower.toString()); 

		// modifying the object: mutator methods
		tower.setAntennaHeight(70);
		System.out.print("\n\nThe object tower after modification => " +tower);

		// modifying the object: mutator methods invalid value
		tower.setAntennaHeight(-70);
		System.out.print("\n\nThe object tower after modification with invalid value=> " +tower);

		// accessor methods
		System.out.printf("\nUsing accessor methods (%s,%.2f,%.2f).",
										tower.getOperator(), 
										tower.getAntennaHeight(),
										tower.getCellRadius()); 

	}//main ends here

}

/*

CONSOLE:
The object tower => Cell phone Tower operator: AT&T antennaHeight: 45.0 cellRadius: 200.0

The object tower after modification => Cell phone Tower operator: AT&T antennaHeight: 70.0 cellRadius: 200.0
Using accessor methods (AT&T,70.00,200.00).
*/