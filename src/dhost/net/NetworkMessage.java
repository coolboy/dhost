package dhost.net;

// TODO: This should be refactored to an implementation a generic message iface
public class NetworkMessage
{
	// TODO: better docs here
	/* Message Types:
	 * STATECHANGE	state change replication message
	 * EVENT		event replication message
	 * NETCHANGE	network change replication message (ex. dropped peer)
	 * STATESYNC	state sync message (used in re-sync new/lost peer)
	 * RESOLVE		state resolve request/reply (currently unused)
	 * INIT			peer initialization message (currently unused)
	 * 
	 */
	
	private int originPeerID;
	private int sourcePeerID;
	private int destinationPeerID;
	private long logicalClock;
	private MessageType messageType;
	private String payload; // serialized data used by some message types

	/**
	 * Standard NetworkMessage constructor
	 * 
	 * If you need an payload, use the setPayload() method after
	 * creating the NetworkMessage.
	 * 
	 * @param sourcePeerID peerID of sender of this message
	 * @param destinationPeerID peerID of recipient of this message
	 * @param logicalClock logical clock value associated with this message
	 * @param messageType type of network message. See MessageType enum
	 */
	
	// TODO: logical clock is optional for our uses, but it may be useful when
	// we get around to doing conflict resolution stuff, as a "tie breaker" 
	
	public NetworkMessage(int sourcePeerID, int destinationPeerID,
						  int originPeerID,	long logicalClock,
						  MessageType msgType)
	{
		super();
		this.destinationPeerID = destinationPeerID;
		this.sourcePeerID = sourcePeerID;
		this.originPeerID = originPeerID;
		this.logicalClock = logicalClock;
		this.messageType = msgType;
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

	public void setOriginPeerID(int originPeerID) {
		this.originPeerID = originPeerID;
	}

	public int getOriginPeerID() {
		return originPeerID;
	}

	public long getTimeOfMessage() {
		return logicalClock;
	}

	public void setTimeOfMessage(long timeOfMessage) {
		this.logicalClock = timeOfMessage;
	}
	
	public MessageType getType() {
		return messageType;
	}

	public void setType(MessageType msgType) {
		this.messageType = msgType;
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
			   originPeerID + ":" +
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
		originPeerID = Integer.parseInt(data[2]);
		logicalClock = Long.parseLong(data[3]);
		messageType = MessageType.valueOf(data[4]);
		if (data.length == 6)
			payload = data[5].trim();
		else
			payload = "";
	}
}
