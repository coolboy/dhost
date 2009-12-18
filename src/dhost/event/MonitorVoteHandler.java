/*Class that communicates with local event monitors, the propagater, and the messageservice.
 * It subscribes to VOTE and VOTE_ACK messages, and is the only class that generates them.
 * All events generated by monitors go through this class. A vote is essentially an event
 * that is generated in the process of monitoring another event. 
 * 
 * 
 */



package dhost.event;

import java.util.HashMap;
import java.util.LinkedList;

import dhost.net.MessageService;
import dhost.net.MessageSubscriber;
import dhost.net.MessageType;
import dhost.net.NetworkMessage;

public class MonitorVoteHandler implements MessageSubscriber, Runnable{
	private Propagater propagater;
	private MessageService messageService;
	EventHandler localEventHandler;
	private MessageType SUBSCRIPTION_TYPE1 = MessageType.VOTE;
	private MessageType SUBSCRIPTION_TYPE2 = MessageType.VOTE_ACK;
	private int localVotesCommitted;
	private int votesReceived;
	private int voteAcksSent;
	private int votesSent;
	private int voteAcksReceived;
	private HashMap<String,Event> eventMap;
	private LinkedList<Vote> voteConfirmQueue;
	private LinkedList<Vote> voteAckQueue;
	private static final long VOTE_TIMEOUT = 500;//milliseconds
	
	
	public MonitorVoteHandler(Propagater p, MessageService m){
		propagater = p;
		messageService =m;		
		eventMap = new HashMap<String, Event>();
		voteConfirmQueue = new LinkedList<Vote>();
		voteAckQueue = new LinkedList<Vote>();
		m.subscribe(this);
		localVotesCommitted=0;
		voteAcksSent=0;
		votesSent = 0;
		new Thread(this).start();
	}
	
	public void run(){
		LinkedList<Vote> removeList = new LinkedList<Vote>();
		try{
			Thread.sleep(30);
			removeList.clear();
			synchronized(this){
				for(Vote v: voteConfirmQueue){
					if(v.isExpired()){
						if(eventMap.containsKey(v.getVoteID())){
							eventMap.remove(v.getVoteID());
						}
						removeList.add(v);					
					}
				}
				for(Vote v : removeList){
					voteConfirmQueue.remove(v);
				}
				
			}
			removeList.clear();
			synchronized(this){
				for(Vote v: voteAckQueue){
					if(v.isExpired()){
						
						propagater.propagate(eventMap.get(v.getVoteID()));
						if(localEventHandler!=null){
							localEventHandler.handleEvent(eventMap.get(v.getVoteID()));		
						}
						else{
							System.out.println("Error: MonitorVoteHandler's LocalEventHandler never set.");
						}
						eventMap.remove(v.getVoteID());
						removeList.add(v);					
					}
				}
				for(Vote v:removeList){
					voteConfirmQueue.remove(v);
				}
			}
			
		}
		catch(InterruptedException e) {}	
	
	}
	
	public void setLocalEventHandler(EventHandler localEventHandler){
		this.localEventHandler = localEventHandler;
	}
	
	public boolean subscribesToType(MessageType type){
		return (type==SUBSCRIPTION_TYPE1||type==SUBSCRIPTION_TYPE2);
	}
	
	//this is where we would make a new networkMessage of VOTE type
	//and send it to the 
	public void handleVote(Event originalEvent, Event event){
		if(!originalEvent.getMonitors().isEmpty()){
			Vote newVote = new Vote(getVoteID(originalEvent, event), VOTE_TIMEOUT);
			if(originalEvent.getMonitors().get(0).equals(propagater.getLocalID())){
				synchronized(this){
					boolean containedVote = false;
					for(Vote v: voteConfirmQueue){
						if(v.getVoteID().equals(newVote.getVoteID())){
							localVotesCommitted++;
							propagater.propagate(event);
							if(localEventHandler!=null){
								localEventHandler.handleEvent(event);		
							}
							else{
								System.out.println("Error: MonitorVoteHandler's LocalEventHandler never set.");
							}
							voteConfirmQueue.remove(v);
							containedVote=true;
							break;
						}
					}
					if(!containedVote){
						voteConfirmQueue.add(newVote);
						eventMap.put(newVote.getVoteID(),event);
					}
				}				
			}
			
			//else if this node is not voter 0
			else{
				NetworkMessage prepMsg = new NetworkMessage(
						propagater.getLocalID(), originalEvent.getMonitors().get(0), 
						propagater.getLocalID(), 0, MessageType.VOTE);
				
				prepMsg.setPayload(newVote.getVoteID());
				
				messageService.sendMessage(prepMsg);
				synchronized(this){
					eventMap.put(newVote.getVoteID(),event);				
					voteAckQueue.add(newVote);
				}
				votesSent++;
				//System.out.println("done handling vote from local client: "+newVote.getVoteID());
			}
		}
		messageService.sendStat(2,0);
	}
	
	public void deliver(NetworkMessage message){
		String voteID = message.getPayload();
		if(message.getType()==MessageType.VOTE){
			NetworkMessage prepMsg = new NetworkMessage(
					propagater.getLocalID(), message.getOriginPeerID(), 
					propagater.getLocalID(), 0, MessageType.VOTE_ACK);
			
			prepMsg.setPayload(voteID);			
			messageService.sendMessage(prepMsg);
			synchronized(this){
				boolean containedVote = false;
				Vote newVote = new Vote(voteID, VOTE_TIMEOUT);
				for(Vote v: voteConfirmQueue){
					if(v.getVoteID().equals(voteID)){
						if(eventMap.containsKey(voteID)){
							localVotesCommitted++;
							votesReceived++;
							voteAcksSent++;
							propagater.propagate(eventMap.get(voteID));
							if(localEventHandler!=null){
								localEventHandler.handleEvent(eventMap.get(voteID));		
							}
							else{
								System.out.println("Error: MonitorVoteHandler's LocalEventHandler never set.");
							}
							eventMap.remove(voteID);
							voteConfirmQueue.remove(v);
							containedVote=true;
							break;
						}
					}
				}
				
				if(!containedVote){
					voteConfirmQueue.add(newVote);
				}
			}
		}
		else if(message.getType()==MessageType.VOTE_ACK){
	
			synchronized(this){
				for(Vote v: voteAckQueue){
					if(v.getVoteID().equals(message.getPayload())){
						voteAcksReceived++;
						voteAckQueue.remove(v);
						eventMap.remove(v.getVoteID());
						break;
					}
				}
			}
		}
		
	}
	
	
	private String getVoteID(Event originalEvent, Event monitorEvent){
		String voteHash = monitorEvent.getAppEvent().getVoteHash();
		return ""+originalEvent.toString()+voteHash;
		
	}
	
	public String getStatus(){
		return "Local votes committed: "+localVotesCommitted + "  Votes Recieved: "+
			votesReceived + " Vote Acks Sent: "+ voteAcksSent+"\n Votes Sent: "+votesSent+
			" Vote Acks Received: "+ voteAcksReceived+"\n";
	}
}
