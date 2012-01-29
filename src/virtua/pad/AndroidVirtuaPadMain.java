package virtua.pad;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AndroidVirtuaPadMain extends Activity implements SensorEventListener, android.view.View.OnTouchListener
{
	enum clientState{disconnected, obtainingID, hasID};
	
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	private TextView tw;
	private LinearLayout layout;
	private TextView debug;
    private clientState state;
    private byte id;
    
    private int udpServerPort = 40000;
    private int tcpServerPort = 50000;
    private InetAddress serverAddress;
    private String serverName = "192.168.1.101";
    
    private float[] accData;
    private boolean shooting = false;
    
    private Thread udpThread;
    private TCPClient tcpClient;
    
    private Thread tcpThread;
    private UDPClient udpClient;
    
    private PowerManager.WakeLock wakeLock;
    
    // For detecting shakes
    private float mAccelLast;
    private float mAccelCurrent;
    private float mAccel;
    
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
	        
	        //gestureScanner = new GestureDetector(this);
	        
	        layout = new LinearLayout(this);
	        layout.setOrientation(LinearLayout.VERTICAL);
	        layout.setOnTouchListener(this);
	        
	        tw = new TextView(this);
	        tw.setText("blah blah blaaah");
	        layout.addView(tw);
	        
	        debug = new TextView(this);
	        debug.setText("VirtuaPad");
	        layout.addView(debug);
	        
	        state = clientState.disconnected;
	    	
	        tcpClient = new TCPClient(serverAddress, tcpServerPort, this);
	        tcpThread = new Thread(tcpClient);
	        tcpThread.start();
	        
	        udpClient = new UDPClient(serverAddress, udpServerPort, this);
	    	udpThread = new Thread(udpClient);		
			udpThread.start();
	    	
	        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        
	        mAccel = 0.00f;
	        mAccelCurrent = SensorManager.GRAVITY_EARTH;
	        mAccelLast = SensorManager.GRAVITY_EARTH;
	                
	        setContentView(layout);
        
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
    
    /**
     * <p>Will set the background color
     * of the entire screen.</p>
     * @param newColor
     */
    public void setColor(int newColor)
    {
    	layout.setBackgroundColor(newColor);
    }
    public void setID(byte newID)
    {
    	id = newID;
    }
    public void setDebugText(String msg) 
    {
    	debug.setText(msg);
    }
    public byte getID()
    {
    	return id;
    }
    
    protected void onStop ()
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
        
        super.onStop();
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
        
        super.finish();
    }
    
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return Array of data from the accelerometer
	 */
	public float[] getAccData()
	{
		return accData;
	}
	
	/**
	 * Tell if the phone is being shaken
	 * @return <code>true</code> if the phone is moving fast enough
	 */
	public boolean getShaking () 
	{
		return (mAccel > 2);
	}
	
	/**
	 * 
	 * @return <code>true</code> if the screen has just been touched
	 */
	public boolean getShooting()
	{
		return shooting;
	}
	
	/**
	 * For the purpose of the UDP thread resetting it to false
	 * @param newShooting
	 */
	public void setShooting(boolean newShooting)
	{
		shooting = newShooting;
	}
	
	public void onSensorChanged(SensorEvent event) 
	{
			
		for(int i = 0 ; i < accData.length ; i++)
		{
			accData[i] = event.values[i];
		}
		
    	tw.setText(event.values[0] + " " + event.values[1] + " " + event.values[2]);
    	
    	// Detect shaking
    	mAccelLast = mAccelCurrent;
    	mAccelCurrent = (float) Math.sqrt((double) (accData[0]*accData[0] + 
    			accData[1]*accData[1] + accData[2]*accData[2]));
    	float delta = mAccelCurrent - mAccelLast;
    	mAccel = mAccel * 0.9f + delta;
    	
    	//tw.setText("mAccel: " + mAccel);
	}
	
	// TOUCH DETECTION

	public boolean onTouch(View v, MotionEvent event) {
		int e = event.getAction();
		
		if(e == MotionEvent.ACTION_DOWN)
		{
			shooting = true;
		} 
		else if (e == MotionEvent.ACTION_UP)
		{
			shooting = false;
		}
		
		return true;
	}	
	
}