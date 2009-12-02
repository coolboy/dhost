package dhost.examples.gamedemo;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class DemoGame{

	Container c;
    GamePanel gp;
    private HashMap<Integer,PeerAvatar> players;
    private HashMap<Integer, Projectile> projectiles;
    private MouseEventHandler mEventHandler;
    private GameController gController;
    private static int defaultWidth = 800;
    private static int defaultHeight = 600;
    JFrame frame;
    
    
    public DemoGame(){
    	frame = new JFrame("Demo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	players = new HashMap<Integer, PeerAvatar>();
    	projectiles = new HashMap<Integer, Projectile>();
    	
    	gController = new GameController(players,projectiles);
    	mEventHandler = new MouseEventHandler(gController);
    	gp = new GamePanel(mEventHandler, defaultWidth,defaultHeight);
    	gp.setPlayerMap(players);
    	gp.setProjectileMap(projectiles);
    	
    	frame.setSize(new Dimension(defaultWidth,defaultHeight));
    	c = frame.getContentPane();
    	c.add(gp,BorderLayout.CENTER);
    	frame.pack();
        frame.setVisible(true);

    	gController.spawnLocalAvatar(0,new Point2D.Double(200,200));
    	gController.spawnPeerAvatar(1, new Point2D.Double(400,400));
    	gController.spawnPeerAvatar(2, new Point2D.Double(300,200));
    	
    	
    }
    public static void main(String[] args){
    	new DemoGame();
    }
	
}
