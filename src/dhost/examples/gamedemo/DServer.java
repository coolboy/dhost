package dhost.examples.gamedemo;

import java.util.Vector;

import dhost.event.Event;
import dhost.event.Propagater;

public class DServer {
	private GameController gController;
	private Propagater propagater;
	private Integer localPeerID;
	private Vector<Integer> peerVector;
	
	public DServer(){
		peerVector = new Vector<Integer>();
	}
	
	public void handleEventFromClient(DemoGameEvent event){
		assignMonitors(event);
		if(event.getMonitors().contains(localPeerID)){
			monitorEvent(event);
		}
		sendEventToNetwork(event);
		
		
	}
	
	public void setPropagator(Propagater p){
		propagater = p;
	}
	
	public void handleEventFromNetwork(Event event){
		DemoGameEvent dGameEvent = (DemoGameEvent)event;
		sendDemoGameEventToClient(dGameEvent);
		if(dGameEvent.getMonitors().contains(localPeerID)){
			monitorEvent(dGameEvent);
		}
		
	}
	
	public void setGameController(GameController g){
		gController = g;
	}
	
	public void setLocalPeerID(Integer id){
		localPeerID = id;
		if (peerVector.size()==0){
			peerVector.add(id);
		}
	}
	
	public void setPeerVector(Vector<Integer> peers){
		peerVector = peers;
	}
	
	private void assignMonitors(DemoGameEvent event){
		if(peerVector.size()==1){
			event.setMonitors(peerVector);
		}
		//TODO implement monitor assignment/management algorithm
	}
	
	private void monitorEvent(DemoGameEvent event){
		if(event.getType()==DemoGameEventType.NEW_PROJECTILE){
			new ProjectileCollisionMonitor(gController, gController.getGameStateManager().getProjectile(event.getObjectOneID(),event.getObjectTwoID()));
		}
	}
	
	private void sendEventToNetwork(Event e){
		try{
			propagater.propagate(e);
		}
		catch(NullPointerException ex){
			System.out.println("propagator not initialized");
		}
	}
	private void sendDemoGameEventToClient(DemoGameEvent event){
		gController.handleEventFromServer(event);
	}
	
}
