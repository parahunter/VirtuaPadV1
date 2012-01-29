package virtua.pad;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;

import virtua.pad.AndroidVirtuaPadMain.clientState;

import android.util.Log;

public class UDPClient implements Runnable {
	
	private InetAddress serverAddress;
	
	private int serverPort = 40000;
	private AndroidVirtuaPadMain mainApp;
	
	public boolean runThread = true;
	
	public UDPClient ( InetAddress address, int port, AndroidVirtuaPadMain main) {
		serverPort = port;
		serverAddress = address;
		mainApp = main;
	}
	
	public void run() 
	{
		//Log.v("progress", "start of thread");
		try {
			
			// Create UDP socket
			DatagramSocket socket = new DatagramSocket();
			//OutputStream stream = new OutputStream();
			ByteArrayOutputStream stream = new ByteArrayOutputStream(0);
			DataOutputStream dataStream = new DataOutputStream(stream);
			
			while(runThread) 
			{
				if(mainApp.getState() == clientState.hasID)
				{
					stream.reset();
					
					stream.write(mainApp.getID());
						
					float[] tempAccData = mainApp.getAccData();
					
					dataStream.writeFloat(tempAccData[0]);
					dataStream.writeFloat(tempAccData[1]);
					dataStream.writeFloat(tempAccData[2]);
					
					// Tell the server if the player is shooting or not
					dataStream.writeBoolean(mainApp.getShooting());
					
					// Tell if the player is shaking the phone
					dataStream.writeBoolean(mainApp.getShaking());
										
					dataStream.flush();
					
					byte[] bytes = stream.toByteArray();
					
					Log.v("UDP", "x " + tempAccData[0] + " y " + tempAccData[1] + " z " + tempAccData[2]);
					
					// Create the UDP packet with destination
					DatagramPacket packet = new DatagramPacket(bytes,bytes.length, serverAddress,serverPort);
				
					Log.v("packet message", "trying to send packet");
					// Send of the packet
					socket.send(packet);
				}
				
				Thread.sleep(30);
			}
			
		}
		catch(SocketException e)
		{
			Log.e("udp error", e.toString());
		}
		catch (Exception e) 
		{
			Log.e("udp error", e.getMessage());
			StackTraceElement[] stackTrace= e.getStackTrace();
			for(StackTraceElement element : stackTrace )
				Log.e("udp error trace", element.toString());
			
			e.printStackTrace();
		}
		
		
	}

}
