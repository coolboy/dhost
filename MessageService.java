package dhost.net;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class MessageService
{
	private MessageServer ms;
	private int localID;
	private ArrayList<MessageSubscriber> _subscribers;
	// HashMap<Integer,Peer> peers;
	
	boolean statsEnabled = false;
	private String statsServerIP;
	private int statsServerPort;
	private int messageDelay;
	private NetworkState netstate;
	
	public MessageService(int messageDelay, NetworkState netstate)
	{	
		this.localID = netstate.getLocalID();
		this.messageDelay = messageDelay;
		this.netstate = netstate;
		_subscribers = new ArrayList<MessageSubscriber>();
		
		// Instantiate socket server
		ms = new MessageServer(netstate);
		new Thread(ms).start();
	}
	
	public void enableStatServer(String IPAddress, int port)
	{
		statsEnabled = true;
		statsServerIP = IPAddress;
		statsServerPort = port;
	}

	/**
	 * Send a message directly to a remote peer (no P2P routing)
	 */
	/*
	 * - This will actually ask netstate for a connection to use, then pass the
	 * message to that connection (where it will be enqueued)
	 * - netstate will actually create a new connection if it has to
	 * - the new connection will need to open up its own socket to the dest.  
	 * 
	 */
	// TODO cleanup comments after refactoring complete
	public boolean sendMessage(NetworkMessage message)
	{
		Connection con;
		
		// Optional delay (we use this for debugging / watching output)
		if (messageDelay > 0)
		{
			try {
				Thread.sleep(messageDelay);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		/* Ask our NetworkState for a Connection to use..
		 * - Need to see if the message has a NextHopPeer set, in which case
		 * we will send the message not to its final destination but rather
		 * forward it to this NextHopPeer instead
		 * 
		 * To be clear, NextHopPeer is one of our persistent connections
		 * in the current NetworkMap implementation, so there's nothing funny
		 * going on here.. we just need to preserve the real destination for
		 * unicasting through the P2P network map.
		 */
		if (message.hasForwardingPeer())
			con = netstate.getConnection(message.getForwardingPeerID());
		else
			con = netstate.getConnection(message.getDestinationPeerID());
		
		con.send(message);
		
		// Log message generated statistic
		/*
		if (message.getType() != MessageType.INIT)
		{
			sendStat(1,1);
		}
		*/
		
		return true;
	}
	
	/**
	 * Broadcast a message to all nodes via P2P network routing
	 * @param message NetworkMessage to send.  Note that any destination
	 * peer ID value in the will be ignored because we set it here instead..
	 */
	// Note: this is used only for initial message sending, not forwarding later
	public void sendP2PBroadcast(NetworkMessage message)
	{
		Vector<Integer> outgoingIDs = netstate.getOutgoingPeerIDs();
		
		for(int i : outgoingIDs)
		{
			message.setDestinationPeerID(i);
			sendMessage(message);
		}
	}
	
	/**
	 * Unicast a message to a single node via P2P network routing
	 * @param message network message to send
	 */
	public void sendP2PUnicast(NetworkMessage message)
	{
		message.setForwardingPeerID(
				netstate.getNextHop(message.getDestinationPeerID()));
		
		sendMessage(message);
	}
	
	/**
	 * Log a statistic with the stats server
	 * @param statTypeID 1 = Send State Upd, 2 = Receive State Upd, 3 = Other
	 * @param numericValue related to this stat
	 */
	// TODO: This could be re-written to use the new Connection system
	public void sendStat(int statTypeID, long numericValue)
	{
		if (statsEnabled)
		{
			PrintWriter output = null;
			
			try
			{
				// Establish connection
				InetAddress addr = InetAddress.getByName(statsServerIP);
				Socket socket = new Socket(addr, statsServerPort);

				output = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())),true);

				// Encode the stat in wire format and send it
				output.println(localID + "," + statTypeID + "," + numericValue);
			}
			catch(Exception e)
			{
				System.out.println("Problem sending stat");
				e.printStackTrace();
			}
			finally
			{
				if (output != null)
					output.close();
			}
		}
	}
	
	public void shutdown()
	{
		ms.shutdown();
	}

	// Receives a message (from a Connection) and processes it
	public void receiveMessage(NetworkMessage message)
	{
		// for debugging what state data is being pushed around..
		// TODO: need a debug flag to shut this noise off!
		/*
		if (message.getPayload().length() > 0)
			System.out.println("received " + message.toString());
		
		if (message.getSourcePeerID() == myID)
			System.out.println("Error: received message with own source ID!");
		*/
		
		
		// Check if this is a unicast P2P message but not at final destination
		if (message.hasForwardingPeer() && 
				message.getDestinationPeerID() != localID)
		{
			sendMessage(message);
		}
		else
		{
			// Check if this message needs broadcasted further (events, etc.)
			if (message.getType() == MessageType.EVENT)
				// || message.getType() == MessageType.INIT) ???
			{
				// Get vector or peers we need to continue broadcast to (if any)
				Vector<Integer> bcast = netstate.getBroadcastPeers(
						message.getSourcePeerID(),
						message.getOriginPeerID());

				message.setSourcePeerID(localID);

				for(int i : bcast)
				{
					message.setDestinationPeerID(i);
					sendMessage(message);
				}
			}

			// Deliver the message to all subscribers of the correct type
			for (MessageSubscriber s : _subscribers)
			{
				if (s.getMessageSubscriptionType() == message.getType())
					s.deliver(message);
			}
		}
	}

	public void subscribe(MessageSubscriber subscriber)
	{
		_subscribers.add(subscriber);	
	}
	
	public void unsubscribe(MessageSubscriber subscriber)
	{
		_subscribers.remove(subscriber);	
	}

	public boolean isRunning() {
		return ms.isRunning();
	}
}
