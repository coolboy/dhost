package dhost.examples.gamedemo;

import java.awt.geom.Point2D;
import java.util.HashMap;


public class GameEventHandler {
	private HashMap<Integer,PeerAvatar> players;
    private HashMap<Integer, Projectile> projectiles;
    int projectileID;
	
	public GameEventHandler(HashMap<Integer,PeerAvatar> _players, HashMap<Integer, Projectile> _projectiles){
		players = _players;
		projectiles = _projectiles;
		projectileID = 1;
		
	}
	
	public int spawnProjectile(Integer parentID, Point2D.Double dest){
		Point2D.Double start = players.get(parentID).getPosition();
		int retval;
		synchronized(projectiles){
			retval = projectileID;
			projectiles.put(projectileID, new Projectile(this,projectileID,parentID,start,dest));
			projectileID++;
			if(projectileID > 100000000)projectileID=1;
		}
		return retval;
		
		
	}
	
	public void killProjectile(Integer projectileID){
		synchronized(projectiles){
			if(projectiles.containsKey(projectileID)){
				projectiles.get(projectileID).setDone();
				projectiles.remove(projectileID);
			}
		}
	}
	public void killPeer(Integer peerID){
		synchronized(players){
			if(players.containsKey(peerID)){
				players.get(peerID).setDone();
				players.remove(peerID);
			}
		}
		
	}
	public void addPeer(Integer peerID, Point2D.Double position){
		players.put(peerID, new PeerAvatar(peerID,position));
	}
	public void moveAvatar(Integer id, Point2D.Double dest){
		synchronized(players){
			if(players.containsKey(id)){
				players.get(id).move(dest);
			}
		}
	}
	public void moveAvatar(Integer id, Point2D.Double start, Point2D.Double dest){
		
		synchronized(players){
			if(players.containsKey(id)){
				players.get(id).move(start,dest);
			}
		}
	}
	
}
