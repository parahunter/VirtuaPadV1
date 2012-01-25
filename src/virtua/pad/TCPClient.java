package virtua.pad;

import java.net.*;
import java.io.*;

import virtua.pad.AndroidVirtuaPadMain.clientState;

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
		// TODO Auto-generated method stub
		Log.d("thread","Thread started");
		
		try
		{
			// Set up the connection
			Log.d("TCP", "C: Connecting...");
			socket = new Socket(serverAddress, serverPort);
			
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
		    mainApp.setID((byte)id);
		    mainApp.setState(clientState.hasID);
		    
		    Log.d("TCP", "response from server " + id);
		    
		    // Clean up
		    out.close();
		    input.close();
		    
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
			Log.e("TCP", e.getMessage());
			StackTraceElement[] stackTrace= e.getStackTrace();
			for(StackTraceElement element : stackTrace )
				Log.e("tcp error trace", element.toString());
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
