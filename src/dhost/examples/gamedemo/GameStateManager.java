package dhost.examples.gamedemo;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;


public class GameStateManager {
	private HashMap<Integer,PeerAvatar> players;
	
	/*A double index hash map is used for the projectiles, with the first index being the
	 * peer ID of the parent of the projectile, and the second index being the ID of the projectile.
	 * This allows multiple projectiles with the same ID, but different parent nodes.
	 */
    private HashMap<Integer, HashMap<Integer,Projectile>> projectiles;
    private int currentProjectileID;
    private final int maxProjectileID = 1000000;
	
	public GameStateManager(){
		players = new HashMap<Integer,PeerAvatar>();
		projectiles = new HashMap<Integer, HashMap<Integer,Projectile>>();
		currentProjectileID = 1;
		
	}
	
	public HashMap<Integer,PeerAvatar> getPlayerMap(){
		return players;
	}
	public int spawnProjectile(Integer parentID, Point2D.Double dest){
		Point2D.Double start;
		synchronized(players){
			if(players.containsKey(parentID)){
				start = players.get(parentID).getPosition();
			}
			else return -1;
		}
			int retval;
		synchronized(projectiles){			
			retval = currentProjectileID;
			if(projectiles.containsKey(parentID)){
				projectiles.get(parentID).put(currentProjectileID, new Projectile(this,currentProjectileID,parentID,start,dest));
			}
			else{
				projectiles.put(parentID, new HashMap<Integer,Projectile>());
				projectiles.get(parentID).put(currentProjectileID, new Projectile(this,currentProjectileID,parentID,start,dest));
			}
			
			currentProjectileID++;
			if(currentProjectileID > maxProjectileID)currentProjectileID=1;
		}
		return retval;
		
		
	}
	
	public void spawnProjectile(Integer parentID, Integer projectileID, Point2D.Double dest, Point2D.Double start){
		boolean playerExists =false;;
		synchronized(players){
			if(players.containsKey(parentID)){
				playerExists = true;
				players.get(parentID).setPosition(start);
			}			
		}
		if(playerExists){
			synchronized(projectiles){			
				
				if(projectiles.containsKey(parentID)){
					projectiles.get(parentID).put(projectileID, new Projectile(this,projectileID,parentID,start,dest));
				}
				else{
					projectiles.put(parentID, new HashMap<Integer,Projectile>());
					projectiles.get(parentID).put(projectileID, new Projectile(this,projectileID,parentID,start,dest));
				}
			}
		}
		
		
		
	}
	
	public boolean killProjectile(Integer parentID, Integer projectileID){
		boolean returnVal = false;
		synchronized(projectiles){
			if(projectiles.containsKey(parentID)){
				if(projectiles.get(parentID).containsKey(projectileID)){
					projectiles.get(parentID).get(projectileID).setDone();
					projectiles.get(parentID).remove(projectileID);
					returnVal = true;
				}
				
			}
		}
		return returnVal;
	}
	
	public boolean killPeer(Integer peerID){
		boolean returnVal = false;
		synchronized(players){
			if(players.containsKey(peerID)){
				players.get(peerID).setDone();
				players.remove(peerID);
				returnVal = true;
			}
		}
		return returnVal;
		
	}
	
	public void addPeer(Integer peerID, Point2D.Double position){
		synchronized(players){
			if(!players.containsKey(peerID)){
				players.put(peerID, new PeerAvatar(peerID,position));
			}
		}
	}
	
	public boolean moveAvatar(Integer id, Point2D.Double dest){
		boolean returnVal = false;
		synchronized(players){
			if(players.containsKey(id)){
				players.get(id).move(dest);
				returnVal = true;
			}
		}
		return returnVal;
	}
	
	public boolean moveAvatar(Integer id, Point2D.Double start, Point2D.Double dest){	
		boolean returnVal = false;
		synchronized(players){
			if(players.containsKey(id)){
				players.get(id).move(start,dest);
				returnVal = true;
			}
		}
		return returnVal;
	}
	
	public boolean containsPeerAvatar(Integer peerID){
		return players.containsKey(peerID);
	}
	public boolean containsProjectile(Integer parentID, Integer projID){
		boolean returnVal = false;
		synchronized(projectiles){
			if(projectiles.containsKey(parentID)){
				if( projectiles.get(parentID).containsKey(projID)) returnVal = true;
			}
		}
		return returnVal;
	}
	
	public Projectile getProjectile(Integer parentID, Integer projectileID) {
		Projectile p=null;
		synchronized(projectiles){
			if(projectiles.containsKey(parentID)){
				if(projectiles.get(parentID).containsKey(projectileID)){
					p = projectiles.get(parentID).get(projectileID);
				}
				
			}
		}
		
		
		return p;
	}
	
	public PeerAvatar getPeerAvatar(Integer peerID)throws IndexOutOfBoundsException{
		PeerAvatar p;
		synchronized(players){
			if(players.containsKey(peerID)){				
				p = players.get(peerID);				
			}
			else throw new IndexOutOfBoundsException();
		}		
		return p;
	}
	
	public Collection<Drawable> getObjectsToDraw(){
		LinkedList<Drawable> list = new LinkedList<Drawable>();
		synchronized(players){
			list.addAll(players.values());
		}
		synchronized(projectiles){
			for (HashMap<Integer, Projectile> h: projectiles.values()){
				list.addAll(h.values());
			}
		}		
		return list;
	}
	
	
	
}
