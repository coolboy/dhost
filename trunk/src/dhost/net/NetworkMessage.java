package dhost.net;

// TODO: This should be refactored to an implementation a generic message iface
public class NetworkMessage
{
	// TODO: better docs here
	/* Message Types:
	 * BROADCAST	state replication message
	 * SYNC			state sync message (used in re-sync new/lost peer)
	 * RESOLVE		state resolve request/reply, typically between two peers
	 * INIT			peer initialization message
	 * 
	 */
	
	public enum MessageType {
		BROADCAST, SYNC, RESOLVE, INIT
	}
	
	private int sourcePeerID;
	private int destinationPeerID;
	private long logicalClock;
	private MessageType messageType;
	private String payload; // serialized state update object data

	/**
	 * Standard NetworkMessage constructor
	 * 
	 * If you need an payload, use the setPayload() method after
	 * creating the NetworkMessage.
	 * 
	 * @param sourcePeerID peerID of sender of this message
	 * @param destinationPeerID peerID of recipient of this message
	 * @param logicalClock logical clock value associated with this message
	 * @param messageType type of network message. requestType is one of:
	 *  BROADCAST, SYNC, RESOLVE
	 */
	
	// TODO: logical clock is optional for our uses, but it may be useful when
	// we get around to doing conflict resolution stuff, as a "tie breaker" 
	
	public NetworkMessage(int sourcePeerID, int destinationPeerID,
								long logicalClock, MessageType requestType)
	{
		super();
		this.destinationPeerID = destinationPeerID;
		this.sourcePeerID = sourcePeerID;
		this.logicalClock = logicalClock;
		this.messageType = requestType;
		this.payload = new String();
	}

	/**
	 * Create a NetworkMessage based on de-marshalled data from wire format
	 * @param input the raw wire-format message data string 
	 */
	public NetworkMessage(String input) {
		decodeWireFormat(input);
	}

	// Getters and Setters
	public int getSourcePeerID() {
		return sourcePeerID;
	}

	public void setSourcePeerID(int peerID) {
		this.sourcePeerID = peerID;
	}

	public void setDestinationPeerID(int destinationPeerID) {
		this.destinationPeerID = destinationPeerID;
	}

	public int getDestinationPeerID() {
		return destinationPeerID;
	}

	public long getTimeOfRequest() {
		return logicalClock;
	}

	public void setTimeOfRequest(long timeOfRequest) {
		this.logicalClock = timeOfRequest;
	}
	
	public MessageType getRequestType() {
		return messageType;
	}

	public void setRequestType(MessageType requestType) {
		this.messageType = requestType;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String getPayload() {
		return this.payload;
	}
	
	public String toString()
	{
		String toString = "message " + messageType.toString() + 
						" at logical clock " + logicalClock + 
						" from peerID " + sourcePeerID;
		
		if (!payload.isEmpty())
			toString += "\nwith raw payload value: " + payload;
		
		return toString;
	}

	
	/* TODO:  This is terribly inefficient, but we can switch to a more
	 efficient wire format later if we need it */ 
	
	// Used by the message service to prepare the message for transmission
	public String encodeWireFormat()
	{	
		return sourcePeerID + ":" +
			   destinationPeerID + ":" +
			   logicalClock + ":" +
			   messageType.toString() + ":" +
			   payload.trim();
	}
	
	// TODO: validate input and return whether decoding succeeded
	private void decodeWireFormat(String input)
	{
		String[] data = input.split(":");
		sourcePeerID = Integer.parseInt(data[0]);
		destinationPeerID = Integer.parseInt(data[1]);
		logicalClock = Long.parseLong(data[2]);
		messageType = MessageType.valueOf(data[3]);
		if (data.length == 5)
			payload = data[4].trim();
		else
			payload = "";
	}
}
