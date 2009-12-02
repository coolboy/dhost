package dhost.examples.gamedemo;

public class ScreenRefresher implements Runnable{
	Thread thread;
	GamePanel gp;
	public ScreenRefresher(GamePanel g){
		gp = g;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void run(){
		while(true){
		gp.repaint();      		
		
		try{
          Thread.sleep(20);
		}
    	catch (InterruptedException e) {}
		}
	}
}
