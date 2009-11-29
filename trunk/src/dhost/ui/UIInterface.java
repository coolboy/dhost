package dhost.ui;

import java.util.Vector;

/**
 * @author Cool
 *
 */
public interface UIInterface {
	//When msg arrives from the network layer
	//Updater will decode the msg to gitems and 
	//pass them to UI through "Update"
	public void Update(Vector<GraphicState> gitems);
}
