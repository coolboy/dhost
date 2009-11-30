package dhost.state;

import java.net.UnknownHostException;
import java.util.ArrayList;

import dhost.net.MessageService;
import dhost.net.MessageSubscriber;
import dhost.net.NetworkMessage;
import dhost.net.Peer;


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
			
			// TODO:  How are we distributing updates exactly?
			// - Find out which peers we need to send updates to
			// - Get (vector) paths to these peers?
			ArrayList<Peer> peers = new ArrayList<Peer>();
			
			// bogus test list of peers to send updates to
			try {
				peers.add(new Peer("localhost", 1));
				peers.add(new Peer("localhost", 2));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// Send a state change message to the appropriate peers 
			for(Peer p : peers)
			{
				NetworkMessage msg = new NetworkMessage(
						myID, p.getID(), 0, NetworkMessage.MessageType.BROADCAST);
				
				// set serialized state change as the message payload
				msg.setPayload(stateChange.toString());
				
				// we may need to set a vector on the NetworkMessage.. discuss
			}
			
		}
		
		return true;
	}
	
	
	private boolean networkSend()
	{
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
	 * Handles an incoming NetworkMessage of type BROADCAST
	 * 	
	*/
	@Override
	public void deliver(NetworkMessage message)
	{
		// shouldn't need this check..
		if (message.getRequestType() == NetworkMessage.MessageType.BROADCAST)
		{
			handleUpdate(new CellChange(message.getPayload()));
		}
		
	}

}
