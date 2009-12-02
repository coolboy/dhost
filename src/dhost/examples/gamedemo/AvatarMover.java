package dhost.examples.gamedemo;




public class AvatarMover implements Runnable{
	private PeerAvatar peerAva;
	int moveNumber;
	public AvatarMover(PeerAvatar ava){
		peerAva = ava;
		moveNumber = 0;
	}
	
	public void run(){
		
		int myMoveNum;
		synchronized(this){
			myMoveNum=moveNumber;
			moveNumber++;
			if(moveNumber>1000)moveNumber=1;
		}
		boolean done = false;
		while(!done){
			
				if(myMoveNum!=moveNumber-1)done=true;
				else{
					if(peerAva.doneMoving())done=true;
					else{
						peerAva.updatePosition();
					}								
				}
			
			try{
				Thread.sleep(20);
		 	}
		 		catch (InterruptedException e) {}
		}
	}
	public void move(){
		new Thread(this).start();
	}
}
