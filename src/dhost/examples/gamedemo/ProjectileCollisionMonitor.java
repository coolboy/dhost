package dhost.examples.gamedemo;

import java.util.HashMap;



public class ProjectileCollisionMonitor implements Runnable{
	private GameController gController;
	private HashMap<Integer,PeerAvatar> players;
	private Projectile myProjectile;
	
	public ProjectileCollisionMonitor(GameController _gController, HashMap<Integer,PeerAvatar> _players, Projectile proj){
		players=_players;
		gController = _gController;
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
						if(!p.getID().equals(myProjectile.parent())){
							if(p.intersects(myProjectile.getRectangle2D())){
								System.out.println("collision detected");
								gController.handleProjectilePeerCollision(p.getID(), myProjectile.getID());
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
