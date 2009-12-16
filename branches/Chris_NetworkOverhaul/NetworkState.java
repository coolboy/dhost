package dhost.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

/**
 * Responsible for tracking the condition of the network and its connections
 * 
 * Implements MessageSubscriber because we need to watch Event messages to
 * track peer loading (see who is getting monitors assigned and what their
 * weights are..) 
 */
/* TODO: Some of this stuff should be moved to package scope since it is in
 * inappropriate outside of dhost.net
 *
 * TODO: Also, we'd probably want to factor out a ConnectionPool or something
 */
public class NetworkState implements MessageSubscriber 
{
	// INITIALIZING state will be set until all outgoingPeerIDs have running
	// network connections
	public enum NetStatus {
		GOOD, INITIALIZING, FAILURE
	}

	private Vector<Integer> outgoingPeerIDs = null;
	private HashMap<Integer, Connection> connections = null;
	private NetStatus networkStatus;
	private int localID;
	private NetworkMap netmap;
	private int localPort;
	private HashMap<Integer, Peer> peers;
	
	// Initialize the network state tracking stuff
	public NetworkState(NetworkMap netmap, HashMap<Integer,Peer> peers,
						Integer localID)
	{
		networkStatus = NetStatus.INITIALIZING;
		
		this.localID = localID;
		this.localPort = peers.get(localID).getPort();
		this.peers = peers;
		this.netmap = netmap;
		
		connections = new HashMap<Integer, Connection>();
		
		// Find out who we should be persistently connected to
		outgoingPeerIDs = netmap.
						   getDestinationVector(localID, localID, localID);
		
		// Create connection objects for any persistent peers
		// note: We don't have MessageService running yet, so we can't do any
		// actual socket creation attempts yet!
		// However, we do need to get to the point where connection attempts
		// can be handled by the messageService.. in other words, a remote
		// peer may end up initializing one of our Connection's before we get
		// around to startNetwork().. crazy, yes.
		for (int i : outgoingPeerIDs)
		{
			Connection c = new Connection(peers.get(localID),peers.get(i),true);
			connections.put(i, c);
		}
	}
	
	// Here, we will establish our persistent connections and if successful
	// set the network status to GOOD
	public void startNetwork(MessageService ms)
	{
		
		/* TODO: we'll want to start a ConnectionWatcher in a new thread..
		 * this will ping persistent connections after some timeout of
		 * inactivity to make sure the remote peer is still available.
		 * It will also reap non-persistent connections which simply timeout
		 */
	}
	
	// return list of n peers with the lowest loads
	// TODO implement
	public int[] getLowestPeerLoads(int n)
	{
		return null;
	}
	
	
	public NetStatus getNetworkStatus()
	{
		return networkStatus ;
	}
	
	
	/** 
	 * Return list of the Peer IDs we use for outward propagation
	 * 
	 * @return a list of peers we are persistently connected to
	 */
	public synchronized Vector<Integer> getOutgoingPeerIDs()
	{
		return outgoingPeerIDs;
	}

	
	/** 
	 * Drop a peer from the network.  We will have to rebuild the network map
	 * and in the process, we'll de-persist connections which no longer apply
	 * in the new mapping and start up any new ones that are absent 
	 */
	public synchronized void dropPeer(int dropPeerID)
	{	
		netmap.removePeerFromMap(dropPeerID);
		
		Vector<Integer> oldOutgoingPeers = outgoingPeerIDs;
		Vector<Integer> oldOutgoingPeersCopy =
			new Vector<Integer>(outgoingPeerIDs);
		Vector<Integer> newOutgoingPeers =
			netmap.getDestinationVector(localID, localID, localID);
		outgoingPeerIDs = new Vector<Integer>(newOutgoingPeers);
		
		/* De-persist Connections which are no longer in the list. This will
		 * cause them to be shutdown by ConnectionWatcher when they timeout.
		 */
		oldOutgoingPeers.removeAll(newOutgoingPeers);
		for (int removeID : oldOutgoingPeers)
		{
			connections.get(removeID).setPersistent(false);
		}
		
		// Add any new outgoingPeerIDs not in the old set
		// Persist any that already existed but were not persistent
		newOutgoingPeers.removeAll(oldOutgoingPeersCopy);
		for (int addID : newOutgoingPeers)
		{
			Connection c = connections.get(addID);
			if (c != null)
			{
				c.setPersistent(true);
			}
			else
			{
				c = new Connection(peers.get(localID), peers.get(addID), true);
				connections.put(addID, c);
			}
		}
		
		networkStatus = NetStatus.GOOD;
	}
	
	
	/**
	 * Given the current network state, what peer should we forward through in
	 * order to P2P route a messsage to the specified destination peer
	 * @param destID the destination Peer ID the message is intended for
	 * @return the ID of the peer we should forward through
	 */
	public int getNextHop(int destID)
	{
		return netmap.getNextHop(localID,destID);
	}

	
	/**
	 * Given the current network state, what peers should we broadcast to next
	 * @param sourceID the peer we received a broadcast message from
	 * @param originID the peer which originated the broadcast
	 * @return vector of peers we should send the broadcast out to (if any)
	 */
	public Vector<Integer> getBroadcastPeers(int sourceID, int originID)
	{
		return netmap.getDestinationVector(localID, sourceID, originID);
	}
	
	public int getLocalID() {
		return localID;
	}
	
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * Event subscription method.. this is used to watch all event messages
	 * in order to track peer loading of monitors
	 */
	// TODO: Implement!
	@Override
	public void deliver(NetworkMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageType getMessageSubscriptionType() {
		return MessageType.EVENT;
	}

	
	public void handleNewSocket(Socket socket)
	{
		boolean existing = false;
		InetAddress remoteAddr = 
			((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress();
		
		// See if any existing Connection's are for this socket's remote address
		for (Connection con : connections.values())
		{
			if (con.getRemoteAddr().equals(remoteAddr))
			{
				existing = true;
				
				// If connection isn't actively managing a socket, do so
				if (!con.isActive())
				{
					con.handleSocket(socket);
				}
				// If a socket already exists, something is wrong.. close it
				else
				{
					// Is close enough for the other side to get an IOException?
					// if not, we might have to do some sort of explicit reject
					// message sent to the other side first..
					try {
						socket.close();
					} catch (IOException e) {
						// we are rudely closing this so who cares..						
					}
				}
			}
		}
		
		// Otherwise, create a new (non-persistent) Connection for this socket
		if (!existing)
		{
			Peer findPeer = null;
			
			// ugly: search through all peers to find which one matches the addr
			for (Peer p : peers.values())
			{
				if (p.getAddr().equals(remoteAddr))
				{
					findPeer = p;
					break;
				}
			}
			
			Connection newCon = new Connection(peers.get(localID),findPeer,
												false);
			connections.put(findPeer.getID(),newCon);
			newCon.handleSocket(socket);
		}
	}

	/** Get a connection suitable to communicate with the given Peer ID
	 * @param peerID the ID of the peer we wish to communicate with
	 * @return a Connection we can send our message through
	 */
	/* This is synchronized in case we are in the middle of rebuilding
	 * the network during a peer removal operation
	 */
	public synchronized Connection getConnection(int peerID)
	{
		// Return any existing connection that matches
		if (connections.containsKey(peerID))
		{
			return connections.get(peerID);
		}
		// Otherwise, create a new (non-persistent) Connection
		else
		{
			Connection newCon = new Connection(peers.get(localID),
									peers.get(peerID),false);
			connections.put(peerID,newCon);
			return newCon;
		}
	}



}
