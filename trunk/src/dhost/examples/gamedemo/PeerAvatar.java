package dhost.examples.gamedemo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class PeerAvatar{
	private final double RADIUS = (double)15;
	private Ellipse2D.Double ellipse;
	private double deltax;
	private double deltay;
	private Point2D.Double destPoint;
	private final double speed = 2;
	private boolean moving;
	private Integer myID;
	private Color myColor;
	
	private AvatarMover mover;
	
	public PeerAvatar(Integer _myID, Point2D.Double p){
		myID = _myID;
		myColor = Color.blue;
		ellipse = new Ellipse2D.Double(p.getX()-RADIUS,p.getY()-RADIUS,2*RADIUS,2*RADIUS);
		mover = new AvatarMover(this);
		moving =false;
	}
	
	public void setPosition(Point2D.Double p){
		ellipse.setFrame(p.getX(),p.getY(),RADIUS*2,RADIUS*2);	
	}
	
	
	public void drawWith(Graphics2D g){
		
		g.setColor(myColor);
		g.fill(ellipse);
		
	}
	
	public void setColor(Color c){
		myColor =c;
	}
	public synchronized void  updatePosition(){
		Point2D.Double p  = new Point2D.Double(ellipse.getX()+(deltax*speed),ellipse.getY()+(deltay*speed));
		setPosition(p);
		if(p.distance(destPoint)< 5){
			moving=false;
		}
		
	}
	public boolean doneMoving(){
		return !moving;
	}
	public void setDone(){
		moving=false;
	}
	
	public Point2D.Double getPosition(){
		return new Point2D.Double(ellipse.getX()+RADIUS, ellipse.getY()+RADIUS);
	}
	
	public boolean intersects(Rectangle2D rect){
		return ellipse.intersects(rect);
	}
	
	
		
	
	public synchronized void move(Point2D.Double start, Point2D.Double dest){
		
		moving =true;
		
			setPosition(start);
			destPoint=new Point2D.Double(dest.getX()-RADIUS,dest.getY()-RADIUS);
			deltax = destPoint.getX()-start.getX()  ;
			deltay = destPoint.getY()-start.getY()  ;
			deltax = deltax / start.distance(dest);
			deltay = deltay / start.distance(dest);
			mover.move();
			try{
				Thread.sleep(5);
   	 		}
   	 		catch (InterruptedException e) {}			
			
		
	}
	public synchronized void move(Point2D.Double dest){
		
		moving =true;
		Point2D.Double start = new Point2D.Double(ellipse.getX(),ellipse.getY());
		destPoint=new Point2D.Double(dest.getX()-RADIUS,dest.getY()-RADIUS);
		deltax = destPoint.getX()-start.getX()  ;
		deltay = destPoint.getY()-start.getY()  ;
		deltax = deltax / start.distance(dest);
		deltay = deltay / start.distance(dest);
		mover.move();
		try{
			Thread.sleep(5);
	 		}
	 		catch (InterruptedException e) {}
	}
	
	public Integer getID(){
		return myID;
	}
}
