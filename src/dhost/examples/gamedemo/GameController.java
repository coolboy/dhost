package dhost.examples.gamedemo;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashMap;


public class GameController{
	private HashMap<Integer,PeerAvatar> players;
    private HashMap<Integer, Projectile> projectiles;
    GameEventHandler gEventHandler;
    int localAvatarID = -1;
    
	public GameController(HashMap<Integer,PeerAvatar> _players,HashMap<Integer, Projectile> _projectiles){
		players = _players;
		projectiles = _projectiles;
		gEventHandler = new GameEventHandler(players,projectiles);
	}
	
	public void mouseButton1(Point2D.Double clickPoint){
		if(localAvatarID >=0){
			if(players.containsKey(localAvatarID)){
				gEventHandler.moveAvatar(new Integer(localAvatarID), clickPoint);
			}
		}
	}
	public void mouseButton3(Point2D.Double clickPoint){
		if(localAvatarID >=0){
			System.out.println("Mouse event handled");
			int projID = gEventHandler. spawnProjectile(localAvatarID, clickPoint);
   	 		new ProjectileCollisionMonitor(this,players,projectiles.get(projID));
		}
	}
	public void spawnLocalAvatar(Integer id, Point2D.Double position){
		if(localAvatarID<0){
			localAvatarID = id.intValue();
			gEventHandler.addPeer(id, position);
			players.get(id).setColor(Color.green);
		}
	}
	public void spawnPeerAvatar(Integer id, Point2D.Double position){
		if(!players.containsKey(id)){
			System.out.println("spawning peer "+ id);
			gEventHandler.addPeer(id, position);
		}
	}
	 public void handleProjectilePeerCollision(Integer peerID, Integer projectileID){
		 if(!projectiles.get(projectileID).parent().equals(peerID)){
			 gEventHandler.killPeer(peerID);
			 gEventHandler.killProjectile(projectileID);
		 }
	 }
}
