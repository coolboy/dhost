package dhost.state;

import dhost.net.MessageService;
import dhost.net.MessageSubscriber;
import dhost.net.MessageType;
import dhost.net.NetworkMessage;
import dhost.net.NetworkState;


/** 
 * Abstracts all networking details so that the user only needs to request what
 * state cell needs to be updated. 
 *
 */
public class Updater implements MessageSubscriber
{
	private int myID; // our peer ID
	private MessageService msgService;
	private GameState gamestate;  // reference to local game state 
	private static final MessageType SUBSCRIPTION_TYPE = MessageType.STATECHANGE;
	NetworkState netstate; // used to get info about current network status
	
	public Updater(NetworkState netstate, MessageService msgService)
	{
		this.netstate = netstate;
		this.msgService = msgService;
		
		myID = netstate.getLocalID();
	}
	
	/**
	 * Request that any changes made to the specified Cell are sent out to all
	 * other peers in the network. 
	 * 
	 * @param cell the cell we wish to propagate local updates for
	 * 
	 * @returns whether the update successfully propagated (delivery is not
	 *  guaranteed however!)  If no updates were needed, simply return true
	 */
	public boolean sendUpdate(Cell cell)
	{
		// See if anything has actually changed in this cell
		if (cell.hasUpdates())
		{
			// get the changes
			CellChange stateChange = cell.getStateChange();
			
			// send changes out to the network
			int[] outgoingIDs = netstate.getOutgoingPeerIDs();			
			NetworkMessage prepMsg;
			
			// Send a state change message to the appropriate peers 
			for(int i : outgoingIDs)
			{
				prepMsg = new NetworkMessage(
					myID, myID, i, 0, SUBSCRIPTION_TYPE);
				
				// set serialized state change as the message payload
				prepMsg.setPayload(stateChange.toString());
				
				msgService.sendMessage(prepMsg);
			}
		}
		
		return true;
	}
	
	/**
	 * Handles incoming updates. This will result in updating our own state and
	 * also potentially propagating changes outward to further nodes.
	 * 
	 * @param change a CellChange object
	 * @return whether all updates and propagations succeeded
	 */
	private boolean handleUpdate(CellChange change)
	{
		
		// update our local game state with the CellChange request
		gamestate.update(change);
		
		// propagate change if needed...
		
		
		return true;
	}


	/** 
	 * Handles an incoming STATECHANGE NetworkMessage 	
	*/
	@Override
	public void deliver(NetworkMessage message)
	{
		// use NetworkMap to see if we need further propagation..
		
		// then hand off for further processing
		handleUpdate(new CellChange(message.getPayload()));
		
	}

	@Override
	public boolean subscribesToType(MessageType type) {
		return type==SUBSCRIPTION_TYPE;
	}

}
