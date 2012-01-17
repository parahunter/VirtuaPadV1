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
    int serverPort;
    InetAddress serverAddress;
    private clientState state;
    private char id;
    
    private Handler handler = new Handler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	    	
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        tw = new TextView(this);
        state = clientState.disconnected;
    	serverPort = 9999;
    	serverAddress = null;
    	
		/*try
    	{
			serverAddress = InetAddress.getByName("192.168.1.100");
    		
        	//udpSocket = new DatagramSocket();
        	
        	tcpSocket = new Socket(serverAddress, serverPort);
        	
    	}
    	catch(SocketException e)
    	{
    		Log.e("socket", e.getMessage(), e);
    	}
    	catch(UnknownHostException e)
    	{
    		Log.e("host", "blah2", e);
    	} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                
        setContentView(tw);
        
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