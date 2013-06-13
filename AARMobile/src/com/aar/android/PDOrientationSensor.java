package com.aar.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class PDOrientationSensor implements SensorEventListener {

	private PDSensorManager mPDSensorManager; 
	private SensorManager mSensorManager;
	private Sensor mOrientation;

	public PDOrientationSensor(PDSensorManager pdSensorManager, Context context) {
		mPDSensorManager = pdSensorManager;
		mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
	    float pitch_angle = event.values[1];
	    float roll_angle = event.values[2];
	    
	    mPDSensorManager.sendEvent(new PDSensorEvent("pitch_angle", Float.toString(pitch_angle)));
	    mPDSensorManager.sendEvent(new PDSensorEvent("roll_angle", Float.toString(roll_angle)));
	}

}
