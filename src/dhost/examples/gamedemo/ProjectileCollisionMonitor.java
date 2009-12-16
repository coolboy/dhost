package dhost.examples.gamedemo;

import java.util.HashMap;

import dhost.event.Event;



public class ProjectileCollisionMonitor implements Runnable{
	private DemoGameEventMonitor demoGameEventMonitor;
	private GameController gController;
	private HashMap<Integer,PeerAvatar> players;
	private Projectile myProjectile;
	private Event event;//the general event type that this monitor was made to monitor
	
	public ProjectileCollisionMonitor(DemoGameEventMonitor dgev,Event event,GameController _gController, Projectile proj){
		if(proj!=null){
			this.event = event;
			demoGameEventMonitor = dgev;
			gController = _gController;
			players=gController.getGameStateManager().getPlayerMap();
			myProjectile = proj;
		
			new Thread(this).start();
		}
	}
	
	public void run(){
		boolean done = false;
		
		while(!done){
			if(myProjectile.done()){
				done=true;
			}
			else{
				synchronized(players){
					for(PeerAvatar p: players.values()){
						if(!p.getID().equals(myProjectile.getParentID())){
							if(p.intersects(myProjectile.getRectangle2D())){
								System.out.println("collision detected");
								DemoGameEvent dGameEvent = new DemoGameEvent();
								dGameEvent.setAsCollisionEvent(myProjectile.getParentID(),0,myProjectile.getID(),
										p.getID());
								demoGameEventMonitor.handleEventFromMonitor(event,dGameEvent);
								done=true;
								break;
							}
						}
					}
				}
			}
			try{
				Thread.sleep(20);
   	 		}
   	 		catch (InterruptedException e) {}	
		}
	}
	

}
