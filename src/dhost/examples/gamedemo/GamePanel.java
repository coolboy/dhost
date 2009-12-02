
package dhost.examples.gamedemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GamePanel extends JPanel{
	
	private int prefwid, prefht;
    private HashMap<Integer,PeerAvatar> players;
    private HashMap<Integer, Projectile> projectiles;
    @SuppressWarnings("unused")
	private ScreenRefresher refresher;
    

          public GamePanel(MouseEventHandler meh, int pwid, int pht)
          {
        	  super();
        	  
              prefwid = pwid;
               prefht = pht;
               super.setBackground(Color.black);
               super.addMouseListener(meh);
               refresher = new ScreenRefresher(this);
           }

          public void paintComponent (Graphics g)
           {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D)g;
               synchronized(players){
               for ( PeerAvatar p : players.values()){
            	   p.drawWith(g2d);
               }}
               synchronized(projectiles){
               for(Projectile p : projectiles.values()){
            	   p.drawWith(g2d);
               }}
           }

           public Dimension getPreferredSize()
           {
               return new Dimension(prefwid, prefht);
           }
           
           
           
           public void  setPlayerMap(HashMap<Integer,PeerAvatar> m){
        	   players = m;
           }
           public void  setProjectileMap(HashMap<Integer, Projectile> m){
        	   projectiles = m;
           }
}

