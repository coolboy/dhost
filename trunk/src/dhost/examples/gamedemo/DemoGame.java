package dhost.examples.gamedemo;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;


public class DemoGame{

	Container c;
    private GameController gController;
    private DServer server;
    private final int defaultWidth = 800;
    private final int defaultHeight = 600;
    JFrame frame;
    
    
    public DemoGame(){
    	frame = new JFrame("Demo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	gController = new GameController();
    	frame.setSize(new Dimension(defaultWidth,defaultHeight));
    	c = frame.getContentPane();
    	c.add(gController.getGamePanel(),BorderLayout.CENTER);
    	frame.pack();
        frame.setVisible(true);
        server = new DServer();
        server.setLocalPeerID(0);
        server.setGameController(gController);
        gController.setServer(server);
    	gController.spawnLocalAvatar(0,new Point2D.Double(200,200));
    	gController.spawnPeerAvatar(1, new Point2D.Double(400,400));
    	gController.spawnPeerAvatar(2, new Point2D.Double(300,200));
    	
    	
    }
    public static void main(String[] args){
    	new DemoGame();
    }
	
}
