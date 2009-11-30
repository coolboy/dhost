package dhost.net;

// TODO: There should probably be types of subscribers for routing purposes
public interface MessageSubscriber {

	public void deliver(NetworkMessage message);

}
