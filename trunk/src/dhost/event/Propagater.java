package dhost.event;

import java.util.ArrayList;

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

	NetworkState netstate; // used to get info about current network status
	
	public Propagater(NetworkState netstate, MessageService msgService)
	{
		this.netstate = netstate;
		this.msgService = msgService;
		
		myID = netstate.getLocalID();
	}
	
	// used by client to originate an event out to the network
	public boolean propagate(Event evt)
	{
		if (evt.needsMonitor())
		{
			// do our algorithm to determine who gets monitors, then set this
			// value in the event..
			
			evt.setMonitors(assignMonitors());
		}
		
		int[] outgoingIDs = netstate.getOutgoingPeerIDs();
		NetworkMessage prepMsg;
		
		for(int i : outgoingIDs)
		{
			prepMsg = new NetworkMessage(
					myID, myID, i, 0, SUBSCRIPTION_TYPE);
			
			prepMsg.setPayload(evt.toString());
			
			msgService.sendMessage(prepMsg);
		}
		
		return true;
	}
	
	// This will make use of NetworkState to find out which peers are good
	// candidates to assign monitor status to..
	// **MASSIVE HAND WAVING** :-)
	// ** note that we also need to set up some sort of voting collaboration
	// at some point along the way.. this is a separate network-level class
	// that allows agreement on generic calculated values or states..
	private ArrayList<Integer> assignMonitors()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// handle incoming events
	private boolean receiveEvent(Event evt)
	{
		// do local stuff with the event
		localEventHandler.handleEvent(evt);
		
		return true;
	}
	
	
	/** 
	 * Handles an incoming NetworkMessage of type EVENT
	*/
	@Override
	public void deliver(NetworkMessage message)
	{
		// use NetworkMap to see if we need further propagation..
		
		// then hand off for further processing
		// extract and de-marshall the event from message payload..
		receiveEvent(new Event(message.getPayload()));	
		
	}

	@Override
	public MessageType getType() {
		return SUBSCRIPTION_TYPE;
	}
	
	public void setLocalEventHandler(EventHandler localEventHandler) {
		this.localEventHandler = localEventHandler;
	}
}
