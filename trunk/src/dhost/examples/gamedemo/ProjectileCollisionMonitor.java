package dhost.examples.gamedemo;

import java.util.HashMap;



public class ProjectileCollisionMonitor implements Runnable{
	private GameController gController;
	private HashMap<Integer,PeerAvatar> players;
	private Projectile myProjectile;
	private DServer dServer;
	private int monitorNumber;
	
	public ProjectileCollisionMonitor(DServer dServer, GameController gController, int monitorNumber, Projectile proj){
		this.dServer = dServer;
		this.gController = gController;
		this.monitorNumber = monitorNumber;
		players=gController.getGameStateManager().getPlayerMap();
		myProjectile = proj;
		
		new Thread(this).start();
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
		
								
								gController.handleProjectilePeerCollision(p.getID(),myProjectile.getParentID(), myProjectile.getID());
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
		
		dServer.monitorFinished(monitorNumber);
	}
	

}
