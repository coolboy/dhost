package dhost.examples.gamedemo;

import java.awt.geom.Point2D;

import dhost.event.AppEvent;
import dhost.event.AppEventType;

public class DemoGameEvent implements AppEvent
{
	private DemoGameEventType gameEventType;
	private AppEventType appEventType;
	private Integer objectOneID;
	private Integer objectTwoID;
	private Point2D.Double primaryPosition;
	private Point2D.Double secondaryPosition;
	private boolean needsMonitor;
	
	 // used by game code for..???  seems flaky because of global collisions
	private int eventID;
	
	// Construct a DemoGameEvent from a serialized version
	public DemoGameEvent(String payload)
	{
		decodeString(payload);
	}
	
	// Constructor for new events
	public DemoGameEvent()
	{
		needsMonitor = false; // default
		appEventType = AppEventType.DEMO_GAME_EVENT;
	}
	
	// Format is:  type,ID,obj1ID,obj2ID,pos1x,pos1y,pos2x,pos2y
	public void decodeString(String payload)
	{
		String[] eventFields = payload.split(",");

		for (DemoGameEventType ty: DemoGameEventType.values())
		{
			if (eventFields[0].compareTo(ty.toString()) == 0)
			{
				gameEventType = ty;
				break;
			}
		}
		
		if (gameEventType == DemoGameEventType.NEW_PROJECTILE)
		{
			needsMonitor = true;
		}
		
		eventID = Integer.parseInt(eventFields[1]);
		objectOneID = Integer.parseInt(eventFields[2]);
		objectTwoID = Integer.parseInt(eventFields[3]);
		primaryPosition = new Point2D.Double(Double.parseDouble(eventFields[4]),
							Double.parseDouble(eventFields[5]));
		secondaryPosition = new Point2D.Double(Double.parseDouble(eventFields[6]),
							Double.parseDouble(eventFields[7]));
	}
	
	public void setAsMoveAvatarEvent(Integer avatarID, Integer eventID,
			Point2D.Double destPos, Point2D.Double currPos)
	{
		gameEventType = DemoGameEventType.MOVE_AVATAR;
		this.objectOneID = avatarID;
		this.objectTwoID = -1;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.eventID = eventID;
		needsMonitor = false;
	}
	
	public void setAsNewProjectileEvent(Integer parentID, Integer eventID,
			Integer projID,	Point2D.Double destPos, Point2D.Double currPos)
	{
		gameEventType = DemoGameEventType.NEW_PROJECTILE;
		this.objectOneID = parentID;
		this.objectTwoID = projID;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.eventID = eventID;
		needsMonitor = true;
	}
	
	public DemoGameEventType getType()
	{
		return gameEventType;
	}
	
	public AppEventType getAppEventType()
	{
		return appEventType;
	}
	
	public Integer getObjectOneID()
	{
		return objectOneID;
	}
	
	public Integer getObjectTwoID()
	{
		return objectTwoID;
	}
	
	public Point2D.Double getPrimaryPosition()
	{
		return primaryPosition;	
	}
	
	public Point2D.Double getSecondaryPosition()
	{
		return secondaryPosition;
	}
	
	// Serialize the demo game event's own data
	// note: we use comma delimiter instead of '|' to not conflict with Event's
	// serialization data format
	public String toString()
	{
		StringBuilder sb = new StringBuilder(
			objectOneID + "," + objectTwoID + "," +
			Double.toString(primaryPosition.getX()) + "," +
			Double.toString(primaryPosition.getY()) + "," +
			Double.toString(secondaryPosition.getX()) +	"," +
			Double.toString(secondaryPosition.getY())
			);
		
		return sb.toString();
	}

	public boolean needsMonitor() {
		return needsMonitor;
	}
	

	public Integer getEventID() {
		return eventID;
	}


	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
}
