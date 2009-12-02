package dhost.examples.gamedemo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Projectile implements Runnable{
	private final double RADIUS = (double)2;
	private Ellipse2D.Double ellipse;
	private double deltax;
	private double deltay;
	private double xbound;
	private double ybound;
	private final double speed = 8;
	private boolean done=false;
	private Thread thread;
	private Integer myID;
	private Integer parentID;
	private GameEventHandler gEventHandler;
	
	
	public Projectile(GameEventHandler _gEventHandler, Integer _myID, Integer _parentID, Point2D.Double start, Point2D.Double end){
		xbound = end.getX();
		ybound = end.getY();
		ellipse = new Ellipse2D.Double(start.getX()-RADIUS,start.getY()-RADIUS,RADIUS*2,RADIUS*2);
		deltax = end.getX() - start.getX();
		deltay = end.getY() - start.getY();
		//normalize the deltas to yield 1 unit of movement
		double distance = Math.sqrt(deltax*deltax+deltay*deltay);
		deltax = deltax /distance;
		deltay = deltay/distance;
		myID = _myID;
		parentID = _parentID;
		gEventHandler = _gEventHandler;
		thread = new Thread(this);
		thread.start();
	}
	public void run(){
		double currx = ellipse.getX();
		double curry = ellipse.getY();
		while (!done){
			if(distFromDest(currx,curry)<=(double)8){
				done=true;
				gEventHandler.killProjectile(myID);
			}
			else{
				currx+=deltax*speed;
				curry+=deltay*speed;
				ellipse.setFrame(currx,curry,RADIUS*2,RADIUS*2);			
				try{
					Thread.sleep(20);
       	 		}
       	 		catch (InterruptedException e) {}
				}
		}
		
	}
	
	
	
	public void drawWith(Graphics2D g){
		
		g.setColor(Color.red);
		g.fill(ellipse);
		
	}
	
	
	
	public void setDone(){
		done =true;
		
	}
	public double distFromDest(double x, double y){
		return Math.sqrt(((x-xbound)*(x-xbound)+(y-ybound)*(y-ybound)));
	}
	
	public Integer parent(){
		return parentID;
	}
	
	public Rectangle2D getRectangle2D(){
		return ellipse.getBounds();
	}
	
	public boolean done(){
		return done;
	}
	public Integer getID(){
		return myID;
	}
}
