package virtua.pad;

import java.net.*;
import java.io.*;

import virtua.pad.AndroidVirtuaPadMain.clientState;

import android.graphics.Color;
import android.util.Log;

public class TCPClient implements Runnable {
	
	private int serverPort;
	private InetAddress serverAddress;	
	private Socket socket;
	
	private OutputStream out;
	private InputStream input;
	private String message;
	
	private AndroidVirtuaPadMain mainApp;
	
	public boolean runThread = true; // Currently not in use?
	
	// Constructor
	public TCPClient (InetAddress address, int portServer, AndroidVirtuaPadMain main) 
	{
		serverPort = portServer;
		serverAddress = address;
		mainApp = main;
	}
	
	public void run() 
	{
		Log.d("thread","Thread started");
		
		try
		{
			// Set up the connection
			Log.d("TCP", "C: Connecting...");
			
			// Address and port in one object!!!
			SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverPort);
			// Unconnected socket :(
			socket = new Socket();
			// Connect, but with a timeout
			socket.connect(socketAddress,5000);
			
			Log.d("TCP", "C: Connected!");
			
			// Request ID
			message = "IDRequest";
			Log.d("TCP", "C: Sending: '" + message + "'");
		    
		    out = socket.getOutputStream(); 
		    input = socket.getInputStream();
		    out.flush();
		    
		    sendBytes(message);
		    
		    Thread.sleep(5);

		    // Get ID from server
		    int id = input.read();
		    
		    // Get the color in RGB
		    final int r = input.read();
		    final int g = input.read();
		    final int b = input.read();
		    
		    // Get team number
		    final int team_id = input.read();
		    
		    mainApp.runOnUiThread(new Runnable ()
		    {

				public void run() {
			    	if(team_id == 1)
				    {
				    	mainApp.setTeamImage(R.drawable.rick);
				    }
				    else if(team_id == 2)
				    {
				    	mainApp.setTeamImage(R.drawable.rick2);
				    }
					
				}
		    });
		    
		    // Set color
		    mainApp.runOnUiThread(new Runnable () {

				public void run() {
					// TODO Auto-generated method stub
					Log.d("color","" + r + ", " + g + ", " + b);
					mainApp.setColor(Color.argb(255, r, g, b));
				}
		    	
		    });
		    
		    mainApp.setID((byte)id);
		    mainApp.setState(clientState.hasID);
		    
		    Log.d("TCP", "response from server " + id);
		    
		    // Clean up
		    socket.shutdownOutput();
		    socket.shutdownInput();
		    socket.close();
		    
		    Log.d("TCP", "closed connection");
		}
		catch (InterruptedException e)
		{
			return;
		}
		catch (Exception e) 
		{
			// We want the class name of the exception so we can add error handling easily.
			Log.e("TCP", e.getClass().getName());
			// Print the entire stack trace to LogCat
			StackTraceElement[] stackTrace= e.getStackTrace();
			for(StackTraceElement element : stackTrace )
			{
				Log.e("tcp error trace", element.toString());
			}
			mainApp.runOnUiThread(new Runnable () 
			{
				public void run() 
				{
					mainApp.setDebugText("Could not connect.\nPlease close app and try again.");
				}
			});
		}
		
		return;
	}
	
	/**
	 * <p>Take a String and convert it
	 * to a byte array, then send it to
	 * the server.
	 * </p>
	 * @param msg - the String to send.
	 */
	private void sendBytes (String msg) 
	{
		byte[] byteMsg = new byte[msg.length() + 1];
	    byte[] stringBytes = msg.getBytes();
	    
	    // Prevents byteMsg from being resized.
	    for(int i = 0 ; i < msg.length() ; i++)
	    	byteMsg[i] = stringBytes[i];
	    // Terminate it
	    byteMsg[byteMsg.length - 1] = 0;
		try 
		{
			out.write(byteMsg);
			out.flush();
		} 
		catch (Exception e) 
		{
			Log.e("send", e.getMessage());
		}
		
	}

}
