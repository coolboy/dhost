package dhost.examples.gamedemo;

import dhost.event.AppEvent;
import dhost.event.Event;
import dhost.event.EventMonitor;
import dhost.event.MonitorVoteHandler;
import dhost.event.Propagater;

public class DemoGameEventMonitor implements EventMonitor {

	private GameController gameController;
	private MonitorVoteHandler monitorVoteHandler;
	private Propagater propagater;
	
	public DemoGameEventMonitor(GameController gameController, Propagater propagater){
		this.gameController = gameController;
		this.propagater = propagater;
		monitorVoteHandler = propagater.getMonitorVoteHandler();
	}
	
	public void setMonitorVoteHandler(MonitorVoteHandler monitorVoteHandler) {
		this.monitorVoteHandler = monitorVoteHandler;
		// TODO Auto-generated method stub

	}
	
	public void monitorEvent(Event event) {
		
		DemoGameEvent dge = null;
		
		// Unpack the DemoGameEvent from the general Event, then process
		System.out.println("Monitoring event");
		AppEvent ae = event.getAppEvent();
		if (ae instanceof DemoGameEvent)
		{
			dge = (DemoGameEvent)ae;
			if (dge.getType() == DemoGameEventType.NEW_PROJECTILE)
			{
				new ProjectileCollisionMonitor(this,event,gameController,
					gameController.getGameStateManager().getProjectile(
					 dge.getObjectOneID(),dge.getObjectTwoID()));
			}
		}
		else{
			System.out.println("Error: Event monitor received invalid event.");
		}

	}
	
	public void handleEventFromMonitor(Event event,DemoGameEvent dGameEvent){
		Event voteEvent = new Event(propagater.getLocalID());
		voteEvent.setAppEvent(dGameEvent);
		monitorVoteHandler.handleVote(event.getMonitors(), voteEvent);
		
	}

	
	

}
