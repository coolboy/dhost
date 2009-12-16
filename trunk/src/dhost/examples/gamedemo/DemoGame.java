package dhost.examples.gamedemo;
import javax.swing.*;

import dhost.app.GameApp;

import java.awt.*;


public class DemoGame{

	Container c;
    
    private final int defaultWidth = 800;
    private final int defaultHeight = 600;
    GameApp gameApp;
    JFrame frame;
    
    
    public DemoGame(GameApp gApp){
    	gameApp = gApp;
    	frame = new JFrame("Demo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	frame.setSize(new Dimension(defaultWidth,defaultHeight));
    	c = frame.getContentPane();
    	c.add(gameApp.getPanel(),BorderLayout.CENTER);
    	frame.pack();
        frame.setVisible(true);
       
        while(frame.isVisible()){
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	
    }
    
	
}
