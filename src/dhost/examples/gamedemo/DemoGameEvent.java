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
	private Integer objectThreeID;
	private Point2D.Double primaryPosition;
	private Point2D.Double secondaryPosition;

	
	
	 // used by game code for..???  seems flaky because of global collisions
	
	//an event is identified by its origin ID + eventID, so there arent collisions.
	//not used by the game code though, can be done at network level, also not necessarily
	//used by anything, just seemed like there could be a lot of potential reasons for
	//needing to identify an event
	//note origin ID is set closer to network level
	private int eventID;
	
	//must be set at game level because only the game knows the relative monitor
	//resource weights of its events....
	private double monitorWeight;
	
	
	// Construct a DemoGameEvent from a serialized version
	public DemoGameEvent(String payload)
	{
		appEventType = AppEventType.DEMO_GAME_EVENT;
		decodeString(payload);
	}
	
	// Constructor for new events
	public DemoGameEvent()
	{
		appEventType = AppEventType.DEMO_GAME_EVENT;
	}
	
	// Format is:  type,obj1ID,obj2ID,pos1x,pos1y,pos2x,pos2y
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
		
		
		
		objectOneID = Integer.parseInt(eventFields[1]);
		objectTwoID = Integer.parseInt(eventFields[2]);
		objectThreeID = Integer.parseInt(eventFields[3]);
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
		this.objectThreeID = -1;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.eventID = eventID;
		this.monitorWeight = 0;
		
	}
	
	public void setAsNewProjectileEvent(Integer parentID, Integer eventID,
			Integer projID,	Point2D.Double destPos, Point2D.Double currPos)
	{
		gameEventType = DemoGameEventType.NEW_PROJECTILE;
		this.objectOneID = parentID;
		this.objectTwoID = projID;		
		this.objectThreeID = -1;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.eventID = eventID;
		this.monitorWeight = 1;
	}
	
	//event ID for collision event is never used
	public void setAsCollisionEvent(Integer parentID, Integer eventID,
			Integer projID,	Integer targetID)
	{
		gameEventType = DemoGameEventType.COLLISION;
		this.objectOneID = parentID;
		this.objectTwoID = projID;
		this.objectThreeID = targetID;
		this.primaryPosition = new Point2D.Double(0,0);
		this.secondaryPosition =new Point2D.Double(0,0);
		this.eventID = eventID;
		this.monitorWeight = 0;
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
		StringBuilder sb = new StringBuilder(gameEventType+","+
			objectOneID + "," + objectTwoID + "," +objectThreeID+","+
			Double.toString(primaryPosition.getX()) + "," +
			Double.toString(primaryPosition.getY()) + "," +
			Double.toString(secondaryPosition.getX()) +	"," +
			Double.toString(secondaryPosition.getY())
			);
		
		return sb.toString();
	}

	

	public Integer getEventID() {
		return eventID;
	}


	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
	
	public void setMonitorWeight(Double weight){
		monitorWeight = weight;
	}
	
	public double getMonitorWeight(){
		return monitorWeight;
	}

	

	public void setObjectThreeID(Integer objectThreeID) {
		this.objectThreeID = objectThreeID;
	}

	public Integer getObjectThreeID() {
		return objectThreeID;
	}
}
