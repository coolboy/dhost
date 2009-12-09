package dhost.examples.gamedemo;

import java.awt.Color;
import java.awt.geom.Point2D;


public class GameController{
    private GameStateManager gameStateManager;
    private DServer dServer;
	private GamePanel gPanel;
    private final int defaultWidth = 800;
    private final int defaultHeight = 600;
    @SuppressWarnings("unused")
	private boolean paused;
    private int localAvatarID = -1;
    private Integer currentEventID;
    
	public GameController(){
		gameStateManager = new GameStateManager();
		gPanel = new GamePanel(new MouseEventHandler(this), gameStateManager,defaultWidth,defaultHeight );
		currentEventID =new Integer(1);
		paused = false;
	}
	
	public void setServer (DServer server){
		dServer = server;
	}
	
	public GameStateManager getGameStateManager(){
		return gameStateManager;
	}
	
	public void mouseButton1(Point2D.Double clickPoint){
		boolean success=false;
		if(localAvatarID >=0){
			int thisEventID;
			DemoGameEvent event = new DemoGameEvent();
			Point2D.Double startPosition = new Point2D.Double();
			synchronized(this){
				thisEventID = currentEventID.intValue();
				currentEventID++;
			}
			synchronized(gameStateManager){
				if(gameStateManager.containsPeerAvatar(localAvatarID)){
					startPosition = gameStateManager.getPeerAvatar(localAvatarID).getPosition();
					gameStateManager.moveAvatar(new Integer(localAvatarID), clickPoint);
					success=true;
				}
			}
			if(success){
				event.setAsMoveAvaEvent(localAvatarID, thisEventID, localAvatarID, clickPoint,startPosition);
				dServer.handleEventFromClient(event);
			}
			
		}
	}
	public void mouseButton3(Point2D.Double clickPoint){
		boolean success=false;
		if(localAvatarID >=0){
			DemoGameEvent event = new DemoGameEvent();
			int thisEventID;
			int projID = 0;
			Point2D.Double parentPosition = new Point2D.Double();
   	 		synchronized(currentEventID){
   	 			thisEventID = currentEventID;
   	 			currentEventID++;
   	 		}
			System.out.println("Mouse event handled");
			synchronized(gameStateManager){
				if(gameStateManager.containsPeerAvatar(localAvatarID)){
					parentPosition = gameStateManager.getPeerAvatar(localAvatarID).getPosition();
					projID = gameStateManager. spawnProjectile(localAvatarID, clickPoint);
					success = true;
				}
			}
			if(success){
				event.setAsNewProjectileEvent(localAvatarID, thisEventID, localAvatarID,projID, clickPoint,parentPosition);
				dServer.handleEventFromClient(event);
			}
   	 		//new ProjectileCollisionMonitor(this,players,projectiles.get(projID));   	 		
		}
	}
	public void spawnLocalAvatar(Integer id, Point2D.Double position){
		if(localAvatarID<0){
			localAvatarID = id.intValue();
			synchronized(gameStateManager){
				gameStateManager.addPeer(id, position);
				gameStateManager.getPeerAvatar(id).setColor(Color.green);
			}
		}
	}
	public void spawnPeerAvatar(Integer id, Point2D.Double position){
		gameStateManager.addPeer(id, position);
	}
	 public void handleProjectilePeerCollision(Integer peerID, Integer projectileParentID, Integer projectileID){
		 
		 if(!projectileParentID.equals(peerID)){
			 DemoGameEvent event = new DemoGameEvent();
			 int thisEventID;
			 synchronized(currentEventID){
	   	 			thisEventID = currentEventID;
	   	 			currentEventID++;
	   	 		}
			 event.setAsCollisionEvent(localAvatarID,thisEventID, projectileParentID, projectileID, peerID);
			 dServer.handleEventFromClient(event);
			 gameStateManager.killPeer(peerID);
			 gameStateManager.killProjectile(projectileParentID,projectileID);
		 }
	 }
	 
	 public Integer getLocalAvatarID(){
		 return localAvatarID;
	 }
	 
	 public GamePanel getGamePanel(){
		 return gPanel;
	 }
	 
	public void handleEventFromServer(DemoGameEvent event){
		
		if(event.getType()==DemoGameEventType.NEW_AVATAR){
			spawnPeerAvatar(event.getObjectOneID(), event.getPrimaryPosition());
		}
		else if(event.getType()==DemoGameEventType.NEW_PROJECTILE){
			gameStateManager. spawnProjectile(event.getObjectOneID(), event.getObjectTwoID(),event.getPrimaryPosition());
		}
		else if(event.getType()==DemoGameEventType.MOVE_AVATAR){
			gameStateManager.moveAvatar(event.getObjectOneID(), event.getSecondaryPosition(), event.getPrimaryPosition());
		}
		else if(event.getType()==DemoGameEventType.COLLISION){
			gameStateManager.killPeer(event.getObjectThreeID());
			 gameStateManager.killProjectile(event.getObjectOneID(),event.getObjectTwoID());
		}
	}
}
