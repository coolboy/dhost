package dhost.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
	private Socket socket;
	private String message;
	private MessageServer server;

	public ConnectionHandler(Socket socket, MessageServer messageServer) {
		this.socket = socket;
		this.server = messageServer;
	}
	
	public void run()
	{	
		try
		{
			BufferedReader input = 
				new BufferedReader(
				new InputStreamReader(
				socket.getInputStream()));

			message = input.readLine();
			
			server.processInput(message);
			
			socket.close();
		}
		catch (IOException ioe)
		{
	        System.out.println("IOException on socket listener: " + ioe);
	        ioe.printStackTrace();
		}
	}

}
