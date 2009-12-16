package dhost.net;

/** Message Types:
 * <li>STATECHANGE	state change replication message
 * <li>EVENT		event replication message<br\>
 * <li>NETCHANGE	network change replication message (ex. dropped peer)
 * <li>STATESYNC	state sync message (currently unused)
 * <li>RESOLVE		state resolve request/reply (currently unused)
 * <li>INIT			peer initialization message
 * <li>PING			connection test
 */
public enum MessageType
{
	STATECHANGE, EVENT, NETCHANGE, STATESYNC, RESOLVE, INIT, PING
}
