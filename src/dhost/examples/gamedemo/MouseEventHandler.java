package dhost.examples.gamedemo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;


public class MouseEventHandler extends MouseAdapter{
	GameController gController;
	public MouseEventHandler(GameController _gController){
		gController = _gController;
	}
	public void mousePressed(MouseEvent e){
		Point2D.Double clickPoint = new Point2D.Double((double)e.getX(),(double)e.getY());
        //System.out.println("Mouse event handled");
        if(e.getButton() == MouseEvent.BUTTON1){
        	//System.out.println("Mouse event handled");
        	gController.mouseButton1(clickPoint);
      	  
        }
        if(e.getButton()==MouseEvent.BUTTON3){
        	//System.out.println("Mouse event handled");
      	  gController.mouseButton3(clickPoint);
        }
        
 }

}
