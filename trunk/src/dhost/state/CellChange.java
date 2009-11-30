package dhost.state;

/**
 * Represents changes made to a game state Cell
 *
 */
public class CellChange {

	private int cellID; // cell ID associated with this change
	
	// Create a CellChange object from a String serialized form
	public CellChange(String str) {

	}

	public int getCellID() {
		return cellID;
	}
	
	/** Create a String serialized form of this CellChange object
	 */
	public String toString() {
		return null;
		
	}

}
