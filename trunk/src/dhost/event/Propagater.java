package dhost.event;

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
	
	// handle incoming events
	private boolean receiveEvent(Event evt)
	{
		
		// do local stuff with the event
		
		// propagate further if indicated
		
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
}
