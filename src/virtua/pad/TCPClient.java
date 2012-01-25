package virtua.pad;

import java.net.*;
import java.io.*;

import virtua.pad.AndroidVirtuaPadMain.clientState;

import android.util.Log;

public class TCPClient implements Runnable {
	
	private int serverPort = 50000;
	private int clientPort = 50000;
	private InetAddress serverAddress;
	private InetAddress clientAddress;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	PrintWriter output;
	
	private Socket tcpSocket;
	private AndroidVirtuaPadMain mainApp;	
	
	public boolean runThread = true;
	
	// Constructors
	public TCPClient (InetAddress address, int portServer, int portClient, AndroidVirtuaPadMain main) 
	{
		serverPort = portServer;
		clientPort = portClient;
		serverAddress = address;
		mainApp = main;
		
		try 
		{
			
			clientAddress = InetAddress.getLocalHost();
			
		} 
		catch (UnknownHostException e) 
		{
			Log.e("host", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		// TODO Auto-generated method stub
		Log.d("thread","Thread started");
		
		//Socket socket;
		try
		{
			Log.d("TCP", "C: Connecting...");
			Socket socket = new Socket(serverAddress, serverPort);//, serverAddress, clientPort);
			//Socket socket = new Socket(serverAddress,serverPort);
			Log.d("TCP", "C: Connected!");
			
			String message = "IDRequest";
			
		    Log.d("TCP", "C: Sending: '" + message + "'");
		    
		    OutputStream out = socket.getOutputStream(); 
		    InputStream input = socket.getInputStream();
		    //out.flush();
		    
		    byte[] byteMsg = new byte[message.length() + 1];
		    byte[] stringBytes = message.getBytes();
		    
		    for(int i = 0 ; i < message.length() ; i++)
		    	byteMsg[i] = stringBytes[i];
		    
		    byteMsg[byteMsg.length - 1] = 0;
		    
		    out.write(byteMsg);
		    //out.flush();
		    //out.println("actual data");
		    out.flush();
		    
		    Thread.sleep(5);

		    int id = input.read();
		    mainApp.setID((byte)id);
		    mainApp.setState(clientState.hasID);
		    
		    Log.d("TCP", "response from server " + id);
		    
		    socket.shutdownOutput();
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
		
		//setup();
		//sendMessage("Hello from the TCP client!");
		
		return;
	}
                                  
		

	
	
	public void setup () 
	{
		
		
		try 
		{
			
			Log.d("connect","Connecting...");
			
			tcpSocket = new Socket(serverAddress,serverPort);
			
			// Get streams
			try
			{
				out = new ObjectOutputStream(tcpSocket.getOutputStream());
				out.flush();
				output = new PrintWriter(out);
			}
			catch (Exception e)
			{
				Log.e("outputStream", e.getMessage());				
			}
			
			in = new ObjectInputStream(tcpSocket.getInputStream());
			
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			Log.e("TCPClient",e.getMessage());
		}
		/*
		finally 
		{
			try 
			{
				tcpSocket.close();
			} 
			catch (IOException io) 
			{
				Log.e("TCPClient","IO");
			}
		}*/
	}
	
	public void sendMessage (String msg) 
	{
		
		try 
		{
			Log.d("alex", "trying to send");
			//out.writeObject(msg);
			output.println("juhhu her er android!");
			out.flush();
			//out.close();
		} 
		catch (Exception e) 
		{
			Log.e("send", e.getMessage());
		}
		
	}

}
