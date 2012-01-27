package virtua.pad;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.hardware.*;

import java.io.IOException;
import java.net.*;

public class AndroidVirtuaPadMain extends Activity implements SensorEventListener
{
	enum clientState{disconnected, obtainingID, hasID};
	
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	private TextView tw;
    private clientState state;
    private byte id;
    
    private int udpServerPort = 40000;
    private int tcpServerPort = 50000;
    private InetAddress serverAddress;
    private String serverName = "192.168.40.135";
    
    private float[] accData;
    
    private Thread udpThread;
    private TCPClient tcpClient;
    
    private Thread tcpThread;
    private UDPClient udpClient;


    private PowerManager.WakeLock wakeLock;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    	wakeLock.acquire();
    	
    	accData = new float[3];
    	
    	try 
    	{
			serverAddress = InetAddress.getByName(serverName);
    		
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.main);
	        
	        tw = new TextView(this);
	        tw.setText("blah blah blaaah");
	        
	        state = clientState.disconnected;
	    	
	        tcpClient = new TCPClient(serverAddress, tcpServerPort, this);
	        tcpThread = new Thread(tcpClient);
	        tcpThread.start();
	        
	        udpClient = new UDPClient(serverAddress, udpServerPort, this);
	    	udpThread = new Thread(udpClient);		
			udpThread.start();
	    	
	        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	                
	        setContentView(tw);
        
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public clientState getState()
    {
    	return state;
    }
    
    public void setState(clientState newState)
    {
    	state = newState;
    }
    
    public void setID(byte newID)
    {
    	id = newID;
    }
    public byte getID()
    {
    	return id;
    }
    
    protected void onResume() {
        super.onResume();
        
        wakeLock.acquire();
        
        //udpClient.runThread = true;
        
        //udpThread.start();
        
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST );
    }
    
    protected void onPause() 
    {
    	wakeLock.release();
    	
        super.onPause();
        
        udpClient.runThread = false;
        try 
        {
			udpThread.join();
		} 
        catch (InterruptedException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mSensorManager.unregisterListener(this);
        
        //super.finish();
    }
    
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public float[] getAccData()
	{
		return accData;
	}
	
	public void onSensorChanged(SensorEvent event) 
	{
			
		for(int i = 0 ; i < accData.length ; i++)
		{
			accData[i] = event.values[i];
		}
		
    	tw.setText(event.values[0] + " " + event.values[1] + " " + event.values[2]);
    	
	}
	
	
}