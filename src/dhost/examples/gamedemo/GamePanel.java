/*Relatively simple panel that just provides a paintComponent method that draws
 * a collection of objects representing the game state. This collection is provided by a
 * GameStateManager object.
 * 
 */
package dhost.examples.gamedemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GamePanel extends JPanel{
	
	private int prefwid, prefht;
    private GameStateManager gameStateManager;
    @SuppressWarnings("unused")
	private ScreenRefresher refresher;
    

          public GamePanel(MouseEventHandler meh,GameStateManager gsm, int pwid, int pht)
          {
        	  super();
        	  
              prefwid = pwid;
               prefht = pht;
               super.setBackground(Color.black);
               super.addMouseListener(meh);
               gameStateManager = gsm;
               refresher = new ScreenRefresher(this);
           }

          public void paintComponent (Graphics g)
           {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D)g;
               for(Drawable D : gameStateManager.getObjectsToDraw()){
            	   D.drawWith(g2d);
               }
               
           }

           public Dimension getPreferredSize()
           {
               return new Dimension(prefwid, prefht);
           }
           
           
           
           
}

