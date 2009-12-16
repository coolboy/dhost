package dhost.event;

import java.util.ArrayList;

/**
 *  Generic event class. Games and applications should implement
 *  AppEvent to create their own application specific event types.
 */
public class Event
{
	private AppEventType appEventType;
	private AppEvent appEvent;
	private Integer originID; // ID of event originator
	private Integer eventID;
	
	// I'm not sure if monitors belong in the core event class
	// They might be considered app-specific event data, although it is clear
	// that setting monitors requires network knowledge, so this should not
	// occur in game code at all!
	//
	// note: NetworkState is involved in choosing monitors, based on load!
	//
	//private boolean hasMonitors = false; // not all events register monitors
	private ArrayList<Integer> monitors; // list of assigned monitors
	private double monitorWeight; // relative resource load of an events monitor
	
	private String appEventData; // raw string data for the app event to process
	
	
	/**
	 * Construct an Event object from the String serialized form
	 * 
	 * @param payload the payload String from a network message
	 */
	public Event(String payload)
	{
		monitors = new ArrayList<Integer>();
		monitorWeight = 0;
		appEventType = null;
		
		decodeString(payload);
		
		// Create an app event of the correct type, if specified.
		if (appEventType != null)
		{
			appEvent = AppEventFactory.createEvent(appEventType, appEventData);
		}
	}

	public Event(Integer originID)
	{
		monitors = new ArrayList<Integer>();
		this.originID = originID;
	}

	private void decodeString(String payload)
	{
		
		String[] eventFields = payload.split("#");

		for (AppEventType typeName : AppEventType.values())
		{
			if (eventFields[0].compareTo(typeName.toString()) == 0)
			{
				appEventType = typeName;
				break;
			}
		}
		
		originID = new Integer(eventFields[1]);
		eventID = new Integer(eventFields[2]);
		
		monitorWeight = Double.parseDouble(eventFields[3]);
		
		// The actual list of monitors is comma-separated integers
		if (eventFields[4].length() > 0)
		{
			//hasMonitors = true;
			String[] monitorIDs = eventFields[4].split(",");		
			for (String mid : monitorIDs)
			{
				monitors.add(Integer.parseInt(mid));
			}

			appEventData = eventFields[5]; // remaining field is app event data
		}
	}
	
	
	/** 
	 * Create a String serialized form of this Event object
	 * 
	 * The format is:
	 * appEventName|originID|monitor_weight|monitors_list|<app_event_data>
	 * monitors_list is comma-separated values
	 * app_event_data is an arbitrary string handled by the app event type 
	 */
	public String toString()
	{
		StringBuilder monitorsList = new StringBuilder();
		
		for (int i : monitors)
		{
			monitorsList.append(i + ",");
		}
		
		StringBuilder sb = new StringBuilder(
				appEventType + "#" + originID +"#"+ eventID + "#" + monitorWeight + "#" +
				monitorsList.toString() + "#" + 
				appEvent.toString()
				);
		
		return sb.toString();
	}
	
	
	public AppEvent getAppEvent() {
		return appEvent;
	}


	public void setAppEvent(AppEvent appEvent) {
		this.appEvent = appEvent;
		this.eventID = appEvent.getEventID();
		this.monitorWeight = appEvent.getMonitorWeight();
		this.appEventType = appEvent.getAppEventType();
	}


	public Integer getOriginID() {
		return originID;
	}


	public void setOriginID(Integer originID) {
		this.originID = originID;
	}


	public void setMonitors(ArrayList<Integer> monitors) {
		this.monitors = monitors;
	}
	
	public ArrayList<Integer> getMonitors(){
		return monitors;
	}


	public boolean getPeerMonitorResponsibility(Integer localPeerID)
	{	
		return monitors.contains(localPeerID);
	}
	
	public double getMonitorWeight(){
		return monitorWeight;
	}


	
}
