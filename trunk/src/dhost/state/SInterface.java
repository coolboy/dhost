package dhost.state;

//import dhost.net.NetInterface;
import dhost.ui.UIInterface;

public interface SInterface {
	//Connects UI with updater
	public void registerGui(UIInterface Int);

	//Connects updater with NET
//	public void registerNet(NetInterface Int);
}
