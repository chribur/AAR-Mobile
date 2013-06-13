package com.aar.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class PDBatterySensor extends BroadcastReceiver{

	private PDSensorManager mPDSensorManager; 
    private int absLevel = -1;
	private int scale = -1;
	private int level = -1;
	private int status = -1;
    private boolean isCharging = false;
    private boolean isFull = false;
	    

	public PDBatterySensor(PDSensorManager pdSensorManager, Context context) {
		mPDSensorManager = pdSensorManager;
		
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(this, batteryLevelFilter);
	}
	
	public void onReceive(Context context, Intent intent) {		
		
		if (absLevel != intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)){
			
			absLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);	
	        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	        
	        if((absLevel != -1) && (scale != -1)){
	        	level = (absLevel * 100) / scale;
			    mPDSensorManager.sendEvent(new PDSensorEvent("batterylevel", Float.toString(level)));
	        } else {
	        	// vllt schon vor erstem intent wert senden, damit geräusch überhaupt erzeigt werden kann
//			    mPDSensorManager.sendEvent(new PDSensorEvent("batterylevel", Float.toString(level)));
	        }
		}
		
		
//       
//		if (status != intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)){
//			 status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//		     isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
//
//		     if (isFull != (status == BatteryManager.BATTERY_STATUS_FULL))
//		    	 isFull = status == BatteryManager.BATTERY_STATUS_FULL;
//		}
       
	}

	    

	
}
