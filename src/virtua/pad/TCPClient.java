package virtua.pad;

import java.net.*;
import java.io.*;

import android.util.Log;

public class TCPClient implements Runnable {
	
	private int serverPort = 50000;
	private static InetAddress serverAddress;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	
	private Socket tcpSocket;
	
	// Constructors
	public TCPClient (int port, InetAddress address) {
		serverPort = port;
		serverAddress = address;
	}
	public void run() {
		// TODO Auto-generated method stub
		setup();
		sendMessage("Hello from the TCP client!");
		
	}
	
	public void setup () {
		try {
			
			Log.d("connect","Connecting...");
			
			tcpSocket = new Socket(serverAddress,serverPort);
			
			// Get streams
			out = new ObjectOutputStream(tcpSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(tcpSocket.getInputStream());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("TCPClient",e.getStackTrace().toString());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException io) {
				Log.e("TCPClient","IO");
			}
		}
	}
	
	public void sendMessage (String msg) {
		
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}
		
	}

}
