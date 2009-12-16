package dhost.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
	HashMap<Integer,Peer> peers;
	NetworkMap netmap;
	
	// Initialize the network state tracking stuff
	public NetworkState(NetworkMap netmap, HashMap<Integer,Peer> peers)
	{
		/* we'll want to start a connection drop detector in a new thread..
		 * this will ping persistent connections after some timeout of
		 * inactivity to make sure the remote host is still available.
		 */
		this.netmap = netmap;
		this.peers = peers;
		networkStatus = NetStatus.GOOD;
	}
	public void setLocalID(int ID){
		localID =ID;
		
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
		//temp setting
		Vector<Integer> destVect = netmap.getDestinationVector(localID,localID,localID);
		int [] ids = new int[destVect.size()];
		for(int i=0;i<destVect.size();i++){
			ids[i]=destVect.get(i);
		}
		return ids;
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Integer> getAllPeerIDs(){
		ArrayList<Integer> peerIDs = new ArrayList<Integer>();
		for(Peer p: peers.values()){
			peerIDs.add(p.getID());
		}
		return peerIDs;
	}

	public int getLocalID() {
		return localID;
	}

}
