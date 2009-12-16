package dhost.event;

import java.util.ArrayList;

import dhost.net.MessageSubscriber;
import dhost.net.MessageType;
import dhost.net.NetworkMessage;

public class MonitorVoteHandler implements MessageSubscriber{
	Propagater propagater;
	EventHandler localEventHandler;
	private MessageType SUBSCRIPTION_TYPE = MessageType.VOTE;
	
	public MonitorVoteHandler(Propagater p){
		propagater = p;
	}
	
	public void setLocalEventHandler(EventHandler localEventHandler){
		this.localEventHandler = localEventHandler;
	}
	
	public MessageType getType(){
		return SUBSCRIPTION_TYPE;
	}
	
	//this is where we would make a new networkMessage of VOTE type
	//and send it to the 
	public void handleVote(ArrayList<Integer> monitors, Event event){
		if(!monitors.isEmpty()){
			if(monitors.get(0).equals(propagater.getLocalID())){
				propagater.propagate(event);
				if(localEventHandler!=null){
					localEventHandler.handleEvent(event);		
				}
				else{
					System.out.println("Error: MonitorVoteHandler's LocalEventHandler never set.");
				}
			}
		}
	}
	
	public void deliver(NetworkMessage message){
		
	}
}
