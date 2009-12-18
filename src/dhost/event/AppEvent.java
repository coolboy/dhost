package dhost.event;

// Simple interface that all application event classes must implement 
public interface AppEvent
{
	// Return a serialized string representation of this application event
	public String toString();

	
	public AppEventType getAppEventType();
	
	//returns the locally set event ID of the event
	//events are identified by originID and event ID
	//so event IDs are allowed to collide
	public Integer getEventID();
	public double getMonitorWeight();
	public String getVoteHash();
	
								
}
