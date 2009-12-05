package dhost.event;

// Generic event class that should be extended by games/apps
public class Event
{
	private String eventString;
	/**
	 * Construct an Event object from the String serialized form
	 * 
	 * @param payload the payload String from a network message
	 */
	public Event(String payload)
	{
		eventString = payload;
		// TODO Auto-generated constructor stub
	}
	
	public void setEventString(String s ){
		eventString =s;
	}

	/** 
	 * Create a String serialized form of this Event object
	 */
	public String toString() {
		return eventString;
		
	}
}
