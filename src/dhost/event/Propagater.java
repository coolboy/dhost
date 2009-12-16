package dhost.event;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;

import dhost.net.MessageService;
import dhost.net.MessageSubscriber;
import dhost.net.MessageType;
import dhost.net.NetworkMessage;
import dhost.net.NetworkState;

/** analogous to dhost.state.Updater
 *  - uses the lower-level network classes (Message to propagate events to
 *  appropriate peers
 */
public class Propagater implements MessageSubscriber
{
	private int myID; // our peer ID
	private MessageService msgService;
	private MessageType SUBSCRIPTION_TYPE = MessageType.EVENT;
	private EventHandler localEventHandler; // yes, just one for now..
	private EventMonitor localEventMonitor;
	private MonitorVoteHandler monitorVoteHandler;
	private Random rand;

	NetworkState netstate; // used to get info about current network status
	
	public Propagater(NetworkState netstate, MessageService msgService)
	{
		this.netstate = netstate;
		this.msgService = msgService;
		rand = new Random();
		this.msgService.subscribe(this);
		monitorVoteHandler = new MonitorVoteHandler(this);
		myID = netstate.getLocalID();
	}
	
	// used by client to originate an event out to the network
	public boolean propagate(Event evt)
	{
		//if (evt.needsMonitor())
		//{
			// do our algorithm to determine who gets monitors, then set this
			// value in the event..
			//just set monitors for all events. if they have a monitor
			//weight of zero they wont affect the running sums and theyll
			//just get dropped by the monitors
			evt.setMonitors(assignMonitors());
			if (evt.getPeerMonitorResponsibility(myID))
			{
				System.out.println("telling local peer to monitor");
				if(localEventMonitor!=null){
					localEventMonitor.monitorEvent(evt);
				}
				else{
					System.out.println("Error: Propogator's localEventMonitor never initialized.");
				}
				
			}
		//}
		//else evt.setMonitors(new ArrayList<Integer>());
		
		
		NetworkMessage prepMsg = new NetworkMessage(myID, 0, myID, 0,
									SUBSCRIPTION_TYPE);
			
		prepMsg.setPayload(evt.toString());
		
		// use MessageService to broadcast event to all peers
		msgService.sendP2PBroadcast(prepMsg);
		
		return true;
	}
	
	// This will make use of NetworkState to find out which peers are good
	// candidates to assign monitor status to..
	// **MASSIVE HAND WAVING** :-)
	// ** note that we also need to set up some sort of voting collaboration
	// at some point along the way.. this is a separate network-level class
	// that allows agreement on generic calculated values or states..
	//just picks one random monitor, figure the netstate stuff will change anyway
	private ArrayList<Integer> assignMonitors()
	{	
		ArrayList<Integer> peerIDs = netstate.getAllPeerIDs();
		Integer monitor = peerIDs.get(rand.nextInt(peerIDs.size()));
		peerIDs.clear();
		peerIDs.add(monitor);
		// TODO Auto-generated method stub
		return peerIDs;
	}

	// handle incoming events
	private boolean receiveEvent(Event evt)
	{
		// do local stuff with the event
		if(localEventHandler!=null){
			localEventHandler.handleEvent(evt);
		}
		else{
			System.out.println("Error: Propogator's localEventHandler never initialized.");
		}
		if (evt.getPeerMonitorResponsibility(myID))
		{
			if(localEventMonitor!=null){
				localEventMonitor.monitorEvent(evt);
			}
			else{
				System.out.println("Error: Propogator's localEventMonitor never initialized.");
			}
		}
		
		return true;
	}
	
	/** 
	 * subscription method handles an incoming EVENT message
	*/
	@Override
	public void deliver(NetworkMessage message)
	{	
		// extract and de-marshall the event from message payload..
		receiveEvent(new Event(message.getPayload()));	
	}

	@Override
	public MessageType getMessageSubscriptionType() {
		return SUBSCRIPTION_TYPE;
	}
	
	public void setLocalEventHandler(EventHandler localEventHandler) {
		this.localEventHandler = localEventHandler;
		monitorVoteHandler.setLocalEventHandler(localEventHandler);
	}
	public void setLocalEventMonitor(EventMonitor localEventMonitor){
		this.localEventMonitor = localEventMonitor;
	}

	

	public MonitorVoteHandler getMonitorVoteHandler() {
		return monitorVoteHandler;
	}
	
	public int getLocalID(){
		return myID;
	}
}
