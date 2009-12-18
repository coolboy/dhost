package dhost.net;

import java.io.*;
import java.net.*;

/* A trivial multi-threaded socket server */
public class MessageServer implements Runnable
{
	public final int PORT;
	
	private boolean shutdown = false;
	private ServerSocket listener;
	private MessageService service;
	private DatagramSocket listenSocket;
	
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
	
	public MessageServer(int port, MessageService service)
	{
		this.PORT = port;
		this.service = service;	
		try{
			listenSocket = new DatagramSocket(port);
		}
		catch(Exception e){
			System.out.println("Server socket binding failed.");
		}
	}
	
	public void processInput(String input)
	{
		// Use the wire-format convenience constructor to create a message
		// then return it to the service
		NetworkMessage msg = new NetworkMessage(input);
		service.receiveMessage(msg);
	}

	public void run()
	{
		
		byte[] buf = new byte[2000];
		DatagramPacket receivePacket = new DatagramPacket(buf,2000);
		while(true){
			try{
			listenSocket.receive(receivePacket);
			NetworkMessage msg = new NetworkMessage(new String(receivePacket.getData(),0,receivePacket.getLength()));
			service.receiveMessage(msg);
			}
			catch(Exception e){
				System.out.println("packet reception failed");
			}
			
			
		}
		/*
		try 
		{
			listener = new ServerSocket(PORT);
			Socket server;
			System.out.println("Opened message listener on port: " + PORT);

			while(!shutdown) 
			{
				// Blocks until a connection occurs, then creates new handler
				server = listener.accept();
				if (server != null)
				{
					ConnectionHandler ch = new ConnectionHandler(server,this);
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
		}*/
	}

	public boolean isRunning() {
		return !listener.isClosed();
	}
}


