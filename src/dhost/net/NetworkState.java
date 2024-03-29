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
	MessageService messageService;
	NetworkMapManager netmapManager;
	private int localID;
	HashMap<Integer,Peer> peers;
	NetworkMap netmap;
	
	// Initialize the network state tracking stuff
	public NetworkState(HashMap<Integer,Peer> peers,MessageService messageService)
	{
		/* we'll want to start a connection drop detector in a new thread..
		 * this will ping persistent connections after some timeout of
		 * inactivity to make sure the remote host is still available.
		 */
		this.messageService = messageService;
		this.peers = peers;
		this.netmap = new NetworkMap(this.peers.values());
		networkStatus = NetStatus.GOOD;
		netmapManager = new NetworkMapManager(this.netmap,this.messageService);
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
	
	public Vector<Integer> getBroadcastDestinationVector(Integer thisPeerID, 
			Integer  precedingHopPeerID, Integer msgOriginPeerID)
	{
		return netmap.getDestinationVector(thisPeerID,precedingHopPeerID,msgOriginPeerID);			
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
