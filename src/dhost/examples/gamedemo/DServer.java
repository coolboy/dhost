package dhost.examples.gamedemo;

import dhost.event.AppEvent;
import dhost.event.Event;
import dhost.event.EventHandler;
import dhost.event.Propagater;


public class DServer implements EventHandler
{
	private GameController gController;
	private Propagater propagater;
	private Integer localPeerID;
	
	public DServer(Propagater p)
	{
		propagater = p;
	}
	
	public void handleEventFromClient(DemoGameEvent gameEvent)
	{
		// Package our DemoGameEvent into a full event, then propagate it..
		Event event = new Event(localPeerID);
		event.setAppEvent(gameEvent);
		
		propagater.propagate(event);
	}
	
	// Propagator calls this to pass an incoming message along..
	public void handleEvent(Event event)
	{
		DemoGameEvent dge = null;
		
		// Unpack the DemoGameEvent from the general Event, then process
		AppEvent ae = event.getAppEvent();
		if (ae instanceof DemoGameEvent)
		{
			dge = (DemoGameEvent)ae;
		}
		// TODO: need some error handling here..
		
		gController.handleEventFromServer(dge);
		
		if (event.getPeerMonitorResponsibility(localPeerID))
		{
			monitorEvent(dge);
		}
	}
	
	public void setGameController(GameController g)
	{
		gController = g;
	}
	
	public void setLocalPeerID(Integer id)
	{
		localPeerID = id;
	}
		
	// This functionality moved to Propagator instead, where we can access
	// NetworkState to determine current loading of peers
	/*
	private void assignMonitors(DemoGameEvent event)
	{
		if (peerVector.size() == 1)
		{
			event.setMonitors(peerVector);
		}
		//TODO implement monitor assignment/management algorithm
	}
	*/
	
	private void monitorEvent(DemoGameEvent gameEvent)
	{
		if (gameEvent.getType() == DemoGameEventType.NEW_PROJECTILE)
		{
			new ProjectileCollisionMonitor(gController,
				gController.getGameStateManager().getProjectile(
				 gameEvent.getObjectOneID(),gameEvent.getObjectTwoID()));
		}
	}
}
