package dhost.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dhost.net.*;

public class Client
{
	private static String GAME_DB; // game state on disk? (maybe not use..)
	private static String PEERLIST_DB;
	
	private static int mode = 0;
	private static int localPeerID = 0;
	// private static int networkScheme = 0;  // right now only one scheme..
	private static String statServerIP = "";
	private static int statServerPort = 0;
	private static float readRatio = 0;
	private static int messageDelay = 0;
	private static int simDelay = 0;
	
	private HashMap<Integer,Peer> peers;
	
	public Client()
	{	
		// Read in the list of (known) peers
		peers = getPeersList(PEERLIST_DB);
		
		// Game code only needs to know peer IDs, not IPs/ports, so separate it
		ArrayList<Integer> peerIDs = getPeerIDs(peers);
		
		// Initialize the local messaging service
		MessageService messageSvc = new MessageService(localPeerID,peers,
									messageDelay);
		
		// Enable stats server
		messageSvc.enableStatServer(statServerIP, statServerPort);
		
		
		// TODO: finish impl
		// Initialize the requested distributed network structure 
		// ..in case we want to try multiple implementations someday..
		// PeerNetworkScheme scheme = null;
		
		// if (networkScheme == 1)
			// default network scheme..
		//	scheme = new NetworkMap(peers);
		
		
		//
		// Initialize any game state here (disk persistent, etc.)
		//
		

		// Initialize the desired user interface
		// TODO: implement these
		if (mode == 1)
			// new SwingUserInterface();
		if (mode == 2)
			// new SimulationUserInterface(readRatio,simDelay);
		
		messageSvc.shutdown();
		System.out.println("Exiting local client..");
		
		// wait for the message service to shut down..
		// TODO: this is funky.. fix it!
		while(messageSvc.isRunning())
		{
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}

	
	private ArrayList<Integer> getPeerIDs(
								HashMap<Integer, Peer> peers)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (Peer peer : peers.values())
		{
			ids.add(peer.getID());
		}
		return ids;
	}


	// TODO: Move this to its own class
	// Load the list of peers from a CSV file
	// CSV columns are:  Peer ID, IP Address, Port
	private HashMap<Integer, Peer> getPeersList(String fileName)
	{
		HashMap<Integer, Peer> parsedPeers = 
			new HashMap<Integer,Peer>();
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			in.readLine(); // skip the first line, it's always the header

			// Read all lines from file
			String curLine;
			String[] dataLine;
			
			while((curLine = in.readLine())!=null)
			{
				dataLine = curLine.split(",");
				String peerAddr = dataLine[0];
				int peerID = Integer.parseInt(dataLine[1]);
				int peerPort = Integer.parseInt(dataLine[2]);
				
				Peer thisPeer = new Peer(peerAddr,peerID,peerPort);
				parsedPeers.put(peerID, thisPeer);
			}
		}
		catch(IOException ioe)
		{
			System.out.println("I/O Exception. Missing or invalid peers list.");
		}
		
		return parsedPeers;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		boolean hasMode = false;
		boolean hasPeerID = false;
		boolean hasScheme = true; // unused.. one default value
		boolean hasStatServer = false;
		boolean hasMessageDelay = false;
		boolean hasDBFile = false;
		boolean hasPeerListPath = false;
		boolean hasSimDelay = false;
		
		boolean badArgumentValue = false;
		
		
		// Get the program arguments and process accordingly
		for (int c = 0; c < args.length; c++)
		{
			if (args[c].equals("-gamestate"))
			{
				if (args.length-1 > c) {
					if (!args[c+1].startsWith("-"))
					{
						hasDBFile = true;
						GAME_DB = args[c+1].trim();
					}
				}
			}
			
			else if (args[c].equals("-peerlist"))
			{
				if (args.length-1 > c) {
					if (!args[c+1].startsWith("-"))
					{
						hasPeerListPath = true;
						PEERLIST_DB = args[c+1].trim();
					}
				}
			}
			
			else if (args[c].equals("-mode"))
			{
				if (args.length-1 > c) {
					if (!args[c+1].startsWith("-"))
					{
						hasMode = true;				
						if (args[c+1].equals("swing"))
							mode = 1;
						else if (args[c+1].equals("simulator"))
							mode = 2;
						else
							badArgumentValue = true;
					}
				}
			}

			else if (args[c].equals("-peerid"))
			{
				if (args.length-1 > c) {
					localPeerID = Integer.parseInt(args[c+1]);
					if (localPeerID > 0)
						hasPeerID = true;
				}
			}
			
			else if (args[c].equals("-scheme"))
			{
				if (args.length-1 > c) {
					if (!args[c+1].startsWith("-"))
					{
						hasScheme = true;
						/*
						if (args[c+1].equals("PeerGroupsNetwork"))
							networkScheme = 1;
						else if (args[c+1].equals("blah"))
							networkScheme = 2;
						else
							badArgumentValue = true;
						*/
					}
				}
			}
			
			else if (args[c].equals("-statserver"))
			{
				if (args.length-1 > c) {
					String[] data = args[c+1].split(":");
					statServerIP = data[0];
					statServerPort = Integer.parseInt(data[1]);
					if (statServerPort > 1024 && statServerPort < 65536)
						hasStatServer = true;
				}
			}
			
			else if (args[c].equals("-messagedelay"))
			{
				if (args.length-1 > c) {
					messageDelay = Integer.parseInt(args[c+1]);
					if (messageDelay > 0)
						hasMessageDelay = true;
				}
			}
			
			else if (args[c].equals("-simdelay"))
			{
				if (args.length-1 > c) {
					simDelay = Integer.parseInt(args[c+1]);
					if (simDelay >= 0)
						hasSimDelay = true;
				}
			}
		}
		
		
		if (!hasDBFile || !hasPeerListPath ||
				!hasMode || !hasPeerID || !hasScheme || !hasStatServer ||
				(mode == 2 && !hasSimDelay) ||
				badArgumentValue)
		{	
			System.out.println(
					"Bad program arguments. Expected are:\n" +
					"-gamestate [path to game state database]\n" +
					"-peerlist [path to peer list]\n" +
					"-mode [swing|simulator]\n" +
					"-peerid [integer peer ID]\n" +
					"-statserver [address]:[port]\n" +
					"-simdelay [milliseconds]  (needed for simulator)\n" +
					"-messagedelay [milliseconds]  (for testing)");
		}
		else
			new Client();
	}
}
