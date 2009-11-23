package dhost.net;

import java.net.*;

public class Peer implements Comparable<Peer>
{
	private static final int defaultPort = 44777;
	private Integer peerID;
	private int portNumber;
	InetAddress InetAddr;
	//variable for bandwidth score? latency score? hardware score?

	//construct peer on just an IP address, in string form
	public Peer(String peerIPAddr) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerIPAddr);
		portNumber = defaultPort;
		peerID = new Integer(-1);
	}

	//construct on IP, ID...
	public Peer(String peerIPAddr, int id) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerIPAddr);
		peerID = new Integer(id);
		portNumber = defaultPort;
	}

	//construct on IP, port, and ID
	public Peer(String peerIPAddr, int id, int port) throws UnknownHostException
	{
		InetAddr = InetAddress.getByName(peerIPAddr);
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

	public int compareTo(Peer anotherPeer)
	{    
		int anotherPeerID = anotherPeer.getID();
		if (this.peerID<anotherPeerID)
			return -1;
		else if (this.peerID > anotherPeerID)
			return 1;
		else return 0;
	}
}