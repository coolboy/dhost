package dhost.net;

import java.io.*;
import java.net.*;

/* A trivial multi-threaded socket server
 * All it does is wait for new socket connections and spins off
 * ConnectionHandler threads.. */
// TODO: rename.. Message makes no sense now
public class MessageServer implements Runnable
{
	public final int PORT;
	
	private boolean shutdown = false;
	private NetworkState netstate;
	private ServerSocket listener;
	
	public void shutdown()
	{
		shutdown = true;
		try {
			listener.close();
		} catch (SocketException e) {
			System.out.println("Closing message listener..");
		} catch (IOException e) {
		}
	}
	
	public MessageServer(NetworkState netstate)
	{
		this.PORT = netstate.getLocalPort();
		this.netstate = netstate;
	}

	public void run()
	{
		try 
		{
			listener = new ServerSocket(PORT);
			Socket newSocket;
			System.out.println("Opened message listener on port: " + PORT);

			while(!shutdown)
			{
				// Blocks until a connection occurs, then creates new thread
				newSocket = listener.accept();
				if (newSocket != null)
				{
					ConnectionHandler ch =
						new ConnectionHandler(newSocket,netstate);
					Thread t = new Thread(ch);
					t.start();
				}
			}
		}
		catch (SocketException ioe) {
			System.out.println("Closing socket..");
		}
		catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
		finally
		{
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		return !listener.isClosed();
	}
}


