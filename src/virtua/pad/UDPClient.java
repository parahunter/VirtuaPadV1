package virtua.pad;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

public class UDPClient implements Runnable {
	
	InetAddress serverAddress;
	
	int serverPort = 40000;
	
	public UDPClient ( InetAddress address, int port) {
		serverPort = port;
		serverAddress = address;
	}
	
	public void run() {
		//Log.v("progress", "start of thread");
		try {
			
			// Create UDP socket
			DatagramSocket socket = new DatagramSocket();
				
				

			
			// Data to send
			String msgString = "Message from Alexander's phone, again: ";
			int counter = 1;
			
			while(true) {
				byte[] buf = (msgString + counter).getBytes();
				
				// Create the UDP packet with destination
				DatagramPacket packet = new DatagramPacket(buf,buf.length,
						serverAddress,serverPort);
				
				Log.v("packet message", "trying to send packet");
				// Send of the packet
				socket.send(packet);
				Thread.sleep(10);
				counter += 1;
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
