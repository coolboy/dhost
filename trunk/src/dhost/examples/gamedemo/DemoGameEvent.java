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
		primaryPosition = new Point2D.Double(new Double(eventFields[5]),new Double(eventFields[6]));
		secondaryPosition = new Point2D.Double(new Double(eventFields[7]),new Double(eventFields[8]));
		monitorWeight = Double.parseDouble(eventFields[9]);
		monitors = new Vector<Integer>();
		for(int i = 10;i<eventFields.length; i++){
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
	public Point2D.Double getPrimaryPosition(){
		return primaryPosition;
		
	}
	
	public Point2D.Double getSecondaryPosition(){
		return secondaryPosition;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder( ""+eventType+"|"+ originID +"|"+ eventID +"|"+ objectOneID +"|"+objectTwoID +"|"+ 
					Double.toString(primaryPosition.getX())+"|"+ Double.toString(primaryPosition.getY())+"|"+
					Double.toString(secondaryPosition.getX())+"|"+ Double.toString(secondaryPosition.getY()) + 
					"|" + Double.toString(monitorWeight));
		for(Integer i: monitors){
			sb.append("|"+i);
		}
		return sb.toString();
	}

}
