package dhost.examples.gamedemo;

import java.awt.geom.Point2D;
import java.util.Vector;

import dhost.event.Event;

public class DemoGameEvent extends Event {
	private DemoGameEventType eventType;
	private Integer originID;//peer ID of origin
	private Integer eventID;
	private Integer objectOneID;
	private Integer objectTwoID;
	private Integer objectThreeID;
	private Point2D.Double primaryPosition;
	private Point2D.Double secondaryPosition;
	private double monitorWeight;
	private Vector<Integer> monitors;
	
	
	
	public DemoGameEvent(String payload) {
		super(payload);
		String[] eventFields = payload.split("|");
		for(DemoGameEventType ty: DemoGameEventType.values()){
			if(eventFields[0].compareTo(""+ty)==0){
				eventType = ty;
				break;
			}
		}
		originID = new Integer(eventFields[1]);
		eventID = new Integer(eventFields[2]);
		objectOneID = new Integer(eventFields[3]);
		objectTwoID = new Integer(eventFields[4]);
		objectThreeID = new Integer(eventFields[5]);
		primaryPosition = new Point2D.Double(new Double(eventFields[6]),new Double(eventFields[7]));
		secondaryPosition = new Point2D.Double(new Double(eventFields[8]),new Double(eventFields[9]));
		monitorWeight = Double.parseDouble(eventFields[10]);
		monitors = new Vector<Integer>();
		for(int i = 11;i<eventFields.length; i++){
			monitors.add(new Integer(eventFields[i]));
		}
		
		// TODO Auto-generated constructor stub
	}
	public DemoGameEvent(){
		super("");
		monitors = new Vector<Integer>();
	}
	public void setMonitors(Vector<Integer> monitors){
		this.monitors = monitors;
		super.setEventString(toString());
	}
	
	public Vector<Integer> getMonitors(){
		return monitors;
	}
	
	public void setAsMoveAvaEvent(Integer originID, Integer eventID,Integer avatarID, Point2D.Double destPos, Point2D.Double currPos){
		eventType = DemoGameEventType.MOVE_AVATAR;
		this.originID = originID;
		this.eventID = eventID;
		this.objectOneID = avatarID;
		this.objectTwoID = -1;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.monitorWeight = 0.0;
		super.setEventString(toString());
	}
	
	/*Object 1 id is the ID of the parent of the new projectile, object 2 is the ID of the projectile
	 * 
	 */
	public void setAsNewProjectileEvent(Integer originID, Integer eventID, Integer parentID, Integer projID,Point2D.Double destPos, Point2D.Double currPos){
		eventType = DemoGameEventType.NEW_PROJECTILE;
		this.originID = originID;
		this.eventID = eventID;
		this.objectOneID = parentID;
		this.objectTwoID = projID;
		this.primaryPosition = destPos;
		this.secondaryPosition = currPos;
		this.monitorWeight = 1.0;
		super.setEventString(toString());
	}
	
	public void setAsCollisionEvent(Integer originID, Integer eventID, Integer parentID, Integer projID, Integer targetID){
		eventType = DemoGameEventType.COLLISION;
		this.originID = originID;
		this.eventID = eventID;
		this.objectOneID = parentID;
		this.objectTwoID = projID;
		this.objectThreeID = targetID;
		this.primaryPosition =new Point2D.Double(0,0);//unused for this event type
		this.secondaryPosition = new Point2D.Double(0,0);//unused for this event type
		this.monitorWeight = 0.0;
		super.setEventString(toString());
	}
	
	public DemoGameEventType getType(){
		return eventType;
	}
	
	public Integer getOriginID(){
		return originID;
	}
	public Integer getEventID(){
		return eventID;
	}
	public Integer getObjectOneID(){
		return objectOneID;
	}
	public Integer getObjectTwoID(){
		return objectTwoID;
	}
	public Integer getObjectThreeID(){
		return objectThreeID;
	}
	public Point2D.Double getPrimaryPosition(){
		return primaryPosition;
		
	}
	
	public Point2D.Double getSecondaryPosition(){
		return secondaryPosition;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder( ""+eventType+"|"+//position 0 on split
				/*pos 1 on split*/originID +"|"+ 
				/*pos 2 on split*/eventID +"|"+
				/*pos 3 on split*/objectOneID +"|"+
				/*pos 4 on split*/objectTwoID +"|"+ 
				/*pos 5 on split*/objectThreeID +"|"+ 
				/*pos 6 on split*/Double.toString(primaryPosition.getX())+"|"+
				/*pos 7 on split*/Double.toString(primaryPosition.getY())+"|"+
				/*pos 8 on split*/Double.toString(secondaryPosition.getX())+"|"+ 
				/*pos 9 on split*/Double.toString(secondaryPosition.getY()) + "|" +
				/*pos 10 on split*/Double.toString(monitorWeight));
		for(Integer i: monitors){//pos 11+ on split
			sb.append("|"+i);
		}
		return sb.toString();
	}

}
