package dhost.net;

import java.util.HashMap;

/**
 * Responsible for tracking the condition of the network and its connections
 *
 */
public class NetworkState
{
	public enum NetStatus {
		GOOD, INITIALIZING, FAILURE
	}

	int[] outgoingPeerIDs = null;
	private NetStatus networkStatus = NetStatus.INITIALIZING;
	private int localID;
	
	// Initialize the network state tracking stuff
	public NetworkState(NetworkMap netmap, HashMap<Integer,Peer> peers)
	{
		/* we'll want to start a connection drop detector in a new thread..
		 * this will ping persistent connections after some timeout of
		 * inactivity to make sure the remote host is still available.
		 */
		
		
		networkStatus = NetStatus.GOOD;
	}
	
	public NetStatus getNetworkStatus()
	{
		return networkStatus ;
	}
	
	/** 
	 * Return list of the Peer IDs we use for outward propagation
	 * 
	 * @return a list of peers we are persistently connected and propagate to
	 */
	public int[] getOutgoingPeerIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLocalID() {
		return localID;
	}

}
