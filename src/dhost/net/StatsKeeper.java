package dhost.net;

import java.io.*;
import java.net.*;

// TODO:  This is initial import from old code.. needs some re-working for the
// type of stats we may collect for the Distributed Game Hosting project
//
// Ex.) We probably want to log: 
// - number of state change propagations
// - any resolve or sync operations


public class StatsKeeper 
{
	
	//TODO make port # a CLI option
	public static final int PORT = 5500;

	private static final int MESSAGES_PER_LOG_ENTRY = 10;

	private int count;
	private PrintWriter outFile;
	private BufferedReader input;
	private final String fileName = "stats.txt";
	private ServerSocket s;
	private Socket socket;
	
	private int monitorStarts = 0;
	private int votesCounted = 0;
	
	private int totalMsgSeen = 0;
	private static boolean debug = false;
	

	public StatsKeeper() throws IOException
	{
		s = new ServerSocket(PORT);
		outFile = new PrintWriter(new FileOutputStream(fileName),true);
		String logMessage;
		
		System.out.println("StatsKeeper started: \n" + s);

		logMessage = "Total Messages Seen / " +
			"Event monitors started / " + 
			"Votes counted";
		
		outFile.println(logMessage);
		System.out.println("LOG format: " + logMessage);
		
		count = 0;  // keep track of how many stats messages were received
		try 
		{
			while(true) 
			{	
				// Blocks until a connection occurs:
				socket = s.accept();
				try 
				{
					input =
						new BufferedReader(
						new InputStreamReader(
						socket.getInputStream()));
					
					/* parse the stat message we just received
					 * 
					 * format is:  clientID,statTypeID,numericValue
					 */
					String newStat = input.readLine();
					
					String[] dataLine = newStat.split(",");
					int type = Integer.parseInt(dataLine[1]);
					
					if (type == 1)
					{
						if (debug)
						System.out.println("Received monitor start stat from "
								+ dataLine[0] + " with value: " + dataLine[2]);
						monitorStarts++;
						
					}
					else if (type == 2)
					{
						if (debug)
						System.out.println("Received vote counted stat from "
								+ dataLine[0] + " with value: " + dataLine[2]);
						votesCounted++;
					}
					// Other messages received
					else if (type == 3)
					{
						if (debug)
						System.out.println("Received other message stat from " +
								dataLine[0] + " with value: " + dataLine[2]);
						
						System.out.print("*");
					}
					// String log message
					else if (type == 4)
					{
						System.out.println("Log message from " + dataLine[0]
						         + ": " + dataLine[2]);
						
					}
					
					if (type != 4)
						totalMsgSeen++;
					
					// Every MESSAGES_PER_LOG_ENTRY messages, write stats to log
					if (totalMsgSeen % MESSAGES_PER_LOG_ENTRY == 0 &&
							type == 3)
					{
						// TODO: nice formatting
						logMessage = totalMsgSeen + " / " +
									monitorStarts + " / " + 
									votesCounted;
						
						outFile.println(logMessage); // TODO: why not work?!
						outFile.flush();
						
						System.out.print("\nLOG: " + logMessage + " -> ");
					}
					

				}
				catch (Exception e1)
				{ System.out.println("Wacky problem with client" + e1); }
				finally
				{
					System.out.println("Closing connection");
					socket.close();
					outFile.close();
				}
			}
		}
		catch (Exception e2)
		{ System.out.println("Wacky problem between clients"); 
		  System.out.println("Shutting server down");
		  s.close();
		}
	}
								
	public static void main(String [] args)
	{
		if (args.length > 0 && args[0].equals("-debug"))
			debug = true;
		
		try
		{
			new StatsKeeper();
		}
		catch (IOException e)
		{
			System.out.println("OOps.. Could not start server");
		}
	}
}


