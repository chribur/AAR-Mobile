package com.aar.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;

public class PDTimeSensor {

	private PDSensorManager mPDSensorManager; 

	protected PDTimeSensor(PDSensorManager pdSensorManager, final Activity ctx) {
		mPDSensorManager = pdSensorManager;

		new Thread(new Runnable() {
	        public void run() {
	        	while (true) {
	    			try {
	    				// checks new system time all 10 seconds
	    				ctx.runOnUiThread(new Runnable() {

							public void run() {
								updateTime();
							}
	    					
	    				})  ;
	    				Thread.sleep(10000);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		}
	        }
	    }).start();
	}
	
	private void updateTime() {
		String currentTime;
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		currentTime = formatter.format(new Date());		

		
		
		mPDSensorManager.sendEvent(new PDSensorEvent("hour", currentTime));

	}
	
}
