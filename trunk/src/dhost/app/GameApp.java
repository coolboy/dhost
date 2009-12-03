package dhost.app;

/* NOTE: GameApp is a generic game interface usable by either a GUI
 * client or the simulator.  It initializes the game irrespective of any
 * available GUI implementation so that we can actually run the game
 * headless for use by the simulator.
 */  
public interface GameApp
{
	// Start the game logic (ie. may be headless for simulation)
	public void startGame();
	
	// Generate random move (for use by simulator)
	public void doRandomMove();
	
	// Get a string describing the status of the game app (for debugging)
	public String getStatus();
	
	/**
	 *  Initialize and return the GUI element to be wrapped by the containing
	 *  DHost Swing user interface.  Note that we can start the game logic and
	 *  add the GUI later.  These are asynchronous of course. The game GUI gets
	 *  on the Swing thread after initialization.
	 */
	// public SomeWrappedSwingElementType initGUI();
	
}
