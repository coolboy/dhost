package dhost.state;


/**
 * Class for accessing cell field data by type
 */
// TODO: This is hilariously crude..
// We'd probably cache the heck out of this object in real life using a more
// sophisticated data structure
public class CellField
{
	private String name = null;
	private CellFieldType type = CellFieldType.STRING;

	private String valueString = null;
	private int valueInteger = 0;
	
	// To avoid doing conversions all the time, we'll keep track of whether
	// we need to string-convert our values..
	private boolean hasStringForm = false;
	private boolean hasIntegerForm = false;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CellFieldType getType() {
		return type;
	}

	public void setType(CellFieldType type) {
		this.type = type;
	}
	
	public void setStringValue(String value)
	{
		hasStringForm = true;
		hasIntegerForm = false;
		valueString = value;
	}
	
	public void setIntegerValue(int value)
	{
		hasIntegerForm = true;
		hasStringForm = false;
		valueInteger = value;
	}
	
	public String getStringValue()
	{
		if (!hasStringForm)
		{
			valueString = Integer.toString(valueInteger);
			hasStringForm = true;
		}
		
		return valueString; 
	}

	public int getIntegerValue()
	{
		if (!hasIntegerForm)
		{
			valueInteger = Integer.parseInt(valueString);
			hasIntegerForm = true;
		}
		
		return valueInteger;
	}
}
