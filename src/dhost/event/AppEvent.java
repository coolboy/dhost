package dhost.event;

// Simple interface that all application event classes must implement 
public interface AppEvent
{
	// Return a serialized string representation of this application event
	public String toString();

	// Return whether this application event needs monitor(s) assigned
	public boolean needsMonitor();

	public AppEventType getAppEventType();
}
