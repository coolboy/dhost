package dhost.net;

import java.net.Socket;

public class ConnectionHandler implements Runnable
{
	private NetworkState netstate;
	private Socket socket;

	public ConnectionHandler(Socket socket, NetworkState netstate) {
		this.socket = socket;
		this.netstate = netstate;
	}
	
	public void run()
	{
		netstate.handleNewSocket(socket);
	}

}
