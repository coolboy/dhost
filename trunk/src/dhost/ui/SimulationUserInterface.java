package dhost.ui;

import java.util.Random;


public class SimulationUserInterface extends ClientUserInterface {

	private int delay;
	
	// delay is the max # milliseconds (random) before creating a new request
	public SimulationUserInterface(int delay)
	{
		this.delay = delay;
		doInterface();
	}
	
	
	@Override
	public void doInterface()
	{	
		Random r = new Random();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("starting simulation");
		
		// wait for init to avoid message flood
		try {
			Thread.sleep(r.nextInt(2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			// wait a random length of time.
			if (delay > 0)
			{
				try {
					Thread.sleep(r.nextInt(delay));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println("performing game move..");
			// GameMove move = generateRandomGameMove();
			// game.update(move);

		}

	}

}
