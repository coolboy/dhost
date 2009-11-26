package dhost.ui;

import java.util.Collection;
import java.util.HashMap;

import dhost.net.Peer;

public abstract class ClientUserInterface {

	public void printClients(HashMap<Integer,Peer> peers)
	{
		System.out.println("List of distributed peers:");
		Collection<Peer> set = peers.values();
		
		for (Peer peer : set)
		{
			System.out.println(	peer.getID() + " " +
								peer.getAddr() + ":" +
								peer.getPort());
		}
	}

	
	// the actual user interface.. probably a loop that asks input or runs
	// a simulation.
	public abstract void doInterface();
	
	

}
