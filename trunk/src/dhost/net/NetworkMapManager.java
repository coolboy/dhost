/*was going to be a non-responsiveness detector and make splices to the network  map
 * to route around peers dropping out. Did not have time to implement.
 * 
 */

package dhost.net;

import java.util.Vector;

public class NetworkMapManager implements MessageSubscriber, Runnable {

	NetworkMap netmap;
	MessageService messageService;
	Vector<Integer> externalPeers;
	
	
	public NetworkMapManager(NetworkMap netmap, MessageService messageService){
		this.netmap = netmap;
		this.messageService = messageService;
		//this.messageService.subscribe(this);
		//externalPeers = netmap.getExternalPeers();
	}
	
	
	
	
	
	
	public boolean subscribesToType(MessageType type){
		return type == MessageType.EVENT;
	}
	
	public void deliver(NetworkMessage message){
		Integer sender = message.getSourcePeerID();
		if(!externalPeers.contains(sender)){
			synchronized(netmap){
				//netmap.addExternalPeer(sender);
				externalPeers.add(sender);
			}
		}
	}
	
	public void run()
	{
		
		
	}
	
}
