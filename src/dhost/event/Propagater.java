package dhost.event;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;

import java.util.Collections;
import java.util.HashMap;


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
	private Vector<PeerWeightTotal> monitorWeightTotals;
	private HashMap<Integer,PeerWeightTotal> peerWeightMap;
	private Random rand;

	NetworkState netstate; // used to get info about current network status
	
	public Propagater(NetworkState netstate, MessageService msgService)
	{
		this.netstate = netstate;
		this.msgService = msgService;
		rand = new Random();
		this.msgService.subscribe(this);
		monitorVoteHandler = new MonitorVoteHandler(this,msgService);
		myID = netstate.getLocalID();
		monitorWeightTotals = new Vector<PeerWeightTotal>();
		peerWeightMap = new HashMap<Integer, PeerWeightTotal>();
		ArrayList<Integer> peerIDs = netstate.getAllPeerIDs();
		PeerWeightTotal  p;
		for(int i: peerIDs){
			p = new PeerWeightTotal(i);
			monitorWeightTotals.add(p);
			peerWeightMap.put(p.getPeerID(), p);
		}
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
			evt.setMonitors(assignMonitors(evt.getMonitorWeight()));
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
		
		
			Vector<Integer> forwardingVector = netstate.getBroadcastDestinationVector(
					myID, myID, myID);
		NetworkMessage prepMsg;
		
		for(int i : forwardingVector)
		{
			prepMsg = new NetworkMessage(
					myID, i, myID, 0, SUBSCRIPTION_TYPE);
			
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
	//just picks one random monitor, figure the netstate stuff will change anyway
	private ArrayList<Integer> assignMonitors(double weight)
	{	
		//kind of an arbitrary magic number, will get rid of it eventually
		int numMonitors = 3;
		
		int currIndex;
		ArrayList<Integer> monitors = new ArrayList<Integer>();
		synchronized(monitorWeightTotals){
			if(monitorWeightTotals.size()<4){
				for(PeerWeightTotal p: monitorWeightTotals){
					monitors.add(p.getPeerID());
					p.addWeight(weight);
				}
			}
			else{
				int upperBound = (int)Math.sqrt(monitorWeightTotals.size());
				if(upperBound<3)upperBound++;//at least three monitors.
				ArrayList<Integer> monitorCandidates = new ArrayList<Integer>(upperBound);
				for(int i = 0;i<upperBound;i++){
					monitorCandidates.add(monitorWeightTotals.get(i).getPeerID());
				}
				for(int i = 0;i<numMonitors;i++){
					currIndex = rand.nextInt(monitorCandidates.size());
					monitors.add(monitorCandidates.get(currIndex));
					peerWeightMap.get(monitorCandidates.get(currIndex)).addWeight(weight);
					monitorCandidates.remove(currIndex);
				}
			}
			Collections.sort(monitorWeightTotals);
		}
		System.out.println(monitorWeightTotals);
		
		return monitors;
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
		synchronized(monitorWeightTotals){
			double monitorWeight = evt.getMonitorWeight();
			for(Integer peerID: evt.getMonitors()){
				peerWeightMap.get(peerID).addWeight(monitorWeight);
			}
			Collections.sort(monitorWeightTotals);
		}
				
		return true;
	}
	
	
	/** 
	 * Handles an incoming NetworkMessage of type EVENT
	*/
	@Override
	public void deliver(NetworkMessage message)
	{
		Vector<Integer> forwardingVector = netstate.getBroadcastDestinationVector(
				myID, message.getSourcePeerID(), message.getOriginPeerID());
		NetworkMessage prepMsg;
		
		for(int i : forwardingVector)
		{
			prepMsg = new NetworkMessage(
					myID, i, message.getOriginPeerID(), 0, SUBSCRIPTION_TYPE);
			
			prepMsg.setPayload(message.getPayload());
			
			msgService.sendMessage(prepMsg);
		}
		// use NetworkMap to see if we need further propagation..
		
		// then hand off for further processing
		// extract and de-marshall the event from message payload..
		receiveEvent(new Event(message.getPayload()));	
		
	}

	@Override
	public boolean subscribesToType(MessageType type) {
		return type==SUBSCRIPTION_TYPE;
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
