package dhost.examples.gamedemo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import dhost.app.GameApp;
import dhost.event.EventHandler;
import dhost.event.Propagater;

public class DemoGameApp implements GameApp{
	
	private GameController gController;
    private DServer server;
    private Propagater propagater;
    private ArrayList<Integer> peerIDs;
    private Integer localPeerID;
    
    
    public DemoGameApp(Propagater propagater, ArrayList<Integer> peerIDs, Integer localPeerID){
    	this.propagater= propagater;
    	server = new DServer(this.propagater);
    	server.setLocalPeerID(localPeerID);
    	this.peerIDs = peerIDs;
    	this.localPeerID = localPeerID;
    }
	// Start the game logic (ie. may be headless for simulation)
	public void startGame(){
		
		Vector<Integer> peervect = new Vector<Integer>();
		peervect.addAll(peerIDs);
		gController = new GameController();
		gController.setServer(server);
		server.setGameController(gController);
		gController.spawnLocalAvatar(localPeerID,new Point2D.Double(200,200));
		for(Integer i: peerIDs){
			gController.spawnPeerAvatar(i, new Point2D.Double(200,200));
		}
	}
	
	// Generate random move (for use by simulator)
	public void doRandomMove(){
		Random rand = new Random();
		int xpos = rand.nextInt()%500;
		int ypos = rand.nextInt()%500;
		if(rand.nextInt()%1000<500){
			gController.mouseButton1(new Point2D.Double(xpos,ypos));
		}
		else{
			gController.mouseButton3(new Point2D.Double(xpos,ypos));
		}
	}
	
	// Get a string describing the status of the game app (for debugging)
	public String getStatus(){
		return "";
	}
	
	public JPanel getPanel(){
		return gController.getGamePanel();
	}
	
	public EventHandler getEventHandler(){
		return server;
	}

}
