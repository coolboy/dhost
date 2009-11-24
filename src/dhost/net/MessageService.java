package dhost.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MessageService
{
	private MessageServer ms;
	private int myID;
	private ArrayList<MessageSubscriber> _subscribers;
	HashMap<Integer,Peer> peers;
	
	boolean statsEnabled = false;
	private String statsServerIP;
	private int statsServerPort;
	private int messageDelay;
	
	// Message send retry options
	private static final int MAXRETRY = 7;
	
	public MessageService(int myID, HashMap<Integer,Peer> peers,
							int messageDelay)
	{	
		this.peers = peers;
		this.myID = myID;
		int myPort = peers.get(myID).getPort();
		this.messageDelay = messageDelay;
		_subscribers = new ArrayList<MessageSubscriber>();
		
		// Instantiate socket server
		ms = new MessageServer(myPort,this);
		new Thread(ms).start();
	}
	
	public void enableStatServer(String IPAddress, int port)
	{
		statsEnabled = true;
		statsServerIP = IPAddress;
		statsServerPort = port;
	}

	/*
	 * returns whether message send was successful
	 */
	public boolean sendMessage(NetworkMessage message)
	{
		int numTries = 0;
		boolean sendSuccess = false;
		Peer destPeer = peers.get(message.getDestinationPeerID());
		InetAddress destAddr = destPeer.getAddr();
		int destPort = destPeer.getPort();
		
		PrintWriter output = null;

		// Optional delay (we use this for debugging / watching output)
		if (messageDelay > 0)
		{
			try {
				Thread.sleep(messageDelay);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		// Attempt to send the message until a certain failure limit is reached
		while (!sendSuccess && numTries < MAXRETRY)
		{
			try
			{	
				// Establish connection
				Socket socket = new Socket(destAddr, destPort);

				output = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())),true);

				// Encode the message in wire format and send it
				output.println(message.encodeWireFormat());

				// Log message generated statistic
				if (message.getRequestType() !=
					NetworkMessage.MessageType.INIT)
				{
					sendStat(1,1);
				}
				
				sendSuccess = true;
			}
			catch(IOException e)
			{
				if (numTries < MAXRETRY)
				{
					System.out.println("Message send unsuccessful. Retrying..");
					
					// sleep, doubling length of time each failure
					try {
						Thread.sleep((long)(300*Math.pow(2,numTries/2)));
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					numTries++;
				}	
			}
			finally
			{
				if (output != null)
					output.close();
			}
		}
		if (numTries == MAXRETRY)
		{
			System.out.println("Send Error: Failed to send message after " +
					MAXRETRY + " retries.");
		}
		
		return sendSuccess;
	}
	
	/**
	 * Log a statistic with the stats server
	 * @param statTypeID 1 = Send State Upd, 2 = Receive State Upd, 3 = Other
	 * @param numericValue related to this stat
	 * 
	 */
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
				output.println(myID + "," + statTypeID + "," + numericValue);
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

	public void receiveMessage(NetworkMessage message)
	{
		// for debugging what state data is being pushed around..
		// TODO: need a debug flag to shut this noise off!
		if (message.getPayload().length() > 0)
			System.out.println("received " + message.toString());
		
		if (message.getDestinationPeerID() != myID)
			System.out.println("Error: received message for wrong peer!");
		
		if (message.getSourcePeerID() == myID)
			System.out.println("Error: received message with own source ID!");
		
		// Deliver the message to all subscribers
		for (MessageSubscriber s : _subscribers)
		{
			s.deliver(message);
		}
		
	}

	public void subscribe(MessageSubscriber subscriber)
	{
		_subscribers.add(subscriber);	
	}

	public boolean isRunning() {
		return ms.isRunning();
	}
	

}
