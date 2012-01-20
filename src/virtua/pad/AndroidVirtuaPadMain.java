package virtua.pad;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.hardware.*;

import java.io.IOException;
import java.net.*;

public class AndroidVirtuaPadMain extends Activity implements SensorEventListener
{
	enum clientState{disconnected, tryingToConnect, connected};
	
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	private TextView tw;
    private Socket tcpSocket;
    private clientState state;
    private byte id;
    
    private int udpServerPort = 40000;
    private int tcpServerPort = 50000;
    private int tcpCLientPort = 50000;
    private InetAddress serverAddress;
    private String serverName = "192.168.40.135";
    
    private Handler handler = new Handler();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	try 
    	{
			serverAddress = InetAddress.getByName(serverName);
    		
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.main);
	        
	        tw = new TextView(this);
	        tw.setText("blah blah blaaah");
	        
	        state = clientState.disconnected;
	    	
	        new Thread(new TCPClient(serverAddress, tcpServerPort, tcpCLientPort)).start();
	        
	    	//new Thread(new UDPClient(serverAddress, udpServerPort)).start();		
			
	        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	                
	        setContentView(tw);
        
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void onSensorChanged(SensorEvent event) 
	{
		if(state != clientState.connected)
			return;
		
    	//int packetLength = 96;
    	//byte[] data = new byte[packetLength];
    	
    	tw.setText(event.values[0] + " " + event.values[1] + " " + event.values[2]);
    	/*        
        byte[] data = 
        		
        DatagramPacket p = new DatagramPacket(message, msg_length,address,port);
        
        try 
        {
        	//Log.v("UDP send", address.toString() + " " + port);
        	socket.send(p);
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			Log.e("UDP", e.getMessage(), e);
        	e.printStackTrace();
		}*/
	}
	
	
}