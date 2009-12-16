package dhost.net;

public interface MessageSubscriber {

	// TODO: We'd like to be able to subscribe to multiple types
	public MessageType getMessageSubscriptionType();
	
	public void deliver(NetworkMessage message);

}
