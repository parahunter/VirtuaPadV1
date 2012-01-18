package virtua.pad;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient implements Runnable {
	
	String SERVERIP = "127.0.0.1";
	int SERVERPORT = 40000;

	@Override
	public void run() {
		
		try {
			// Get server name
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			System.out.println("C: Connecting...");
			
			// Create UDP socket
			DatagramSocket socket = new DatagramSocket();
			
			// Data to send
			String msgString = "Message number ";
			int counter = 1;
			
			while(true) {
				byte[] buf = (msgString + counter).getBytes();
				
				// Create the UDP packet with destination
				DatagramPacket packet = new DatagramPacket(buf,buf.length,
						serverAddr,SERVERPORT);
				
				// Send of the packet
				socket.send(packet);
				Thread.sleep(1000);
				counter += 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
