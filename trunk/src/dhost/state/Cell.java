package dhost.state;

import java.util.ArrayList;
import java.util.HashMap;

public class Cell
{
	private int CellID;
	private boolean hasChanges;
	private CellChange stateChanges = null;
	
	// cache field names in a hashmap
	private HashMap<String,Integer> fieldNamesToIDs = 
			new HashMap<String,Integer>();
	
	private ArrayList<CellField> fields = new ArrayList<CellField>();
	
	public String getFieldAsString(String fieldName)
	{
		return fields.get(fieldNamesToIDs.get(fieldName)).getStringValue();
	}
	
	public int getFieldAsInteger(String fieldName)
	{
		return fields.get(fieldNamesToIDs.get(fieldName)).getIntegerValue();
	}
	
	public String getFieldIDAsString(int fieldID)
	{
		return fields.get(fieldID).getStringValue();
	}
	
	public int getFieldIDAsInteger(int fieldID)
	{
		return fields.get(fieldID).getIntegerValue();
	}
	
	
	public boolean hasUpdates() {
		// TODO Auto-generated method stub
		return hasChanges;
	}

	/**
	 * Retrieve the changes to this cell in the form of a CellChange object
	 * This probably ought to be synchronized..
	 *  
	 * @return current state changes to this cell
	 */
	public CellChange getStateChange() {
		// TODO Auto-generated method stub
		return stateChanges;
	}
	
	
}
