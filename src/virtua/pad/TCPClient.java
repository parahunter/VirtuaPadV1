package virtua.pad;

import java.net.*;
import java.io.*;

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
	
	// Constructors
	public TCPClient (InetAddress address, int portServer, int portClient) {
		serverPort = portServer;
		clientPort = portClient;
		serverAddress = address;
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
		
		try
		{
			Log.d("TCP", "C: Connecting...");
			Socket socket = new Socket(serverAddress, serverPort);//, serverAddress, clientPort);
			//Socket socket = new Socket(serverAddress,serverPort);
			Log.d("TCP", "C: Connected!");
			
			String message = "Hello from Client";
		
			try 
			{
			    Log.d("TCP", "C: Sending: '" + message + "'");
			
			    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),false);
			    InputStream input = socket.getInputStream();
			    //out.flush();
			    
			    out.println(message);
			    //out.flush();
			    out.println("actual data");
			    out.flush();
			    
			   
			    //Thread.sleep(5);
			    
			    int id = input.read();
			    Log.d("response", id + " so?");
			    
			    Log.d("TCP", "C: Sent.");
			
				Log.d("TCP", "C: Done.");
			} 
			catch(Exception e) 
			{
				//Log.e
				Log.e("TCP","inner try" + e.getMessage());
		    } 
			finally 
			{
				socket.close();
		    }
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
