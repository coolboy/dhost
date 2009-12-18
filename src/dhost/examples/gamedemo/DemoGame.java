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
    JTextArea textArea;
    long startTime;
    private JScrollPane textAreaPane;
    
    
    public DemoGame(GameApp gApp){
    	gameApp = gApp;
    	textArea = new JTextArea();
    	textArea.setEditable(false);

    	frame = new JFrame("Demo Game");
    	long printTime = 20000;//every 20 seconds
    	long counter =0;
    	long counter2 = 0;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	frame.setSize(new Dimension(defaultWidth,defaultHeight+200));
    	c = frame.getContentPane();
    	textAreaPane = new JScrollPane(textArea);
    	textAreaPane.setPreferredSize(new Dimension(800,200));
    	textArea.setLineWrap(true);

    	
    	textAreaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    	c.add(gameApp.getPanel(),BorderLayout.CENTER);
    	c.add(textAreaPane,BorderLayout.SOUTH);
    	frame.pack();
        frame.setVisible(true);
       startTime=System.currentTimeMillis();
        while(frame.isVisible()){
        	if((System.currentTimeMillis()- startTime) > printTime){
        		printTime+=20000;
        		textArea.append(gameApp.getStatus());
        		textArea.append("\n");
        		textArea.setCaretPosition(textArea.getDocument().getLength());

        	}
        	if((System.currentTimeMillis()- startTime)<10000){
        		if(counter==counter2){
        			gameApp.doRandomMove();
        			counter2+=10;
        		}
        		counter++;
        	}
        	else{
        		gameApp.doRandomMove();
        	}
        	
        	try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
    	
    	
    }
    
	
}
