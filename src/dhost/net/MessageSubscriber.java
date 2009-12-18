package dhost.net;

public interface MessageSubscriber {

	public boolean subscribesToType(MessageType type);
	
	public void deliver(NetworkMessage message);

}
