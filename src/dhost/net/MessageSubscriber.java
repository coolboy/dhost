package dhost.net;

public interface MessageSubscriber {

	public MessageType getType();
	
	public void deliver(NetworkMessage message);

}
