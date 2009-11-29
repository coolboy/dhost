package dhost.state;

//import dhost.net.NetInterface;
import dhost.ui.UIInterface;;

//this class is used for an adapter between GUI and Net
public class Updater implements SInterface{
	
	private UIInterface UIInt_;
//	private NetInterface NetInt_;
	
	@Override
	public void registerGui(UIInterface Int)
	{
		UIInt_ = Int;
	}

//	@Override
//	public void registerNet(NetInterface Int) {
//		
//	}
}
