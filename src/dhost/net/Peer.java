package dhost.net;

import java.net.*;

public class Peer implements Comparable<Peer>
{
	private static final int DEFAULT_PORT = 44777;
	private Integer peerID;
	private int portNumber;
	InetAddress InetAddr;
	// variable for bandwidth score? latency score? hardware score?

	/** construct peer on just an IP address, in string form
	 *  @deprecated This is useless as Peer always requires a real ID. */
	public Peer(String peerIPAddr) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerIPAddr);
		portNumber = DEFAULT_PORT;
		peerID = new Integer(-1);//represents null
	}

	// Construct on IP address and PeerID. Use default port.
	public Peer(String peerIPAddr, int id) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerIPAddr);
		peerID = new Integer(id);
		portNumber = DEFAULT_PORT;
	}

	// Standard constructor: construct on Address, Port, and PeerID
	public Peer(String peerAddress, int id, int port) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerAddress);
		portNumber = port;
		peerID = new Integer(id);
	}

	public void setPort(int port)
	{
		portNumber = port;
	}

	public void setID(int i)
	{
		peerID = i;
	}

	public int getID()
	{
		return peerID;
	}
	
	public int getPort()
	{
		return portNumber;
	}
	
	public InetAddress getAddr()
	{
		return InetAddr;
	}

	/** compare is by Peer ID */
	public int compareTo(Peer anotherPeer)
	{    
		int anotherPeerID = anotherPeer.getID();
		if (this.peerID < anotherPeerID)
			return -1;
		else if (this.peerID > anotherPeerID)
			return 1;
		else return 0;
	}
}