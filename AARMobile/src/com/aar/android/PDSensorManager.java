package com.aar.android;

import org.puredata.core.PdBase;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

public class PDSensorManager {
	private SensorManager mSensorManager; 
	private PDOrientationSensor orientationSensor;
	private PDBatterySensor batterySensor;
	private PDTimeSensor timeSensor;
	private StepDetector stepDetector;
	
	public PDSensorManager(Activity activity, Context context) {
		orientationSensor = new PDOrientationSensor(this, context);
//		batterySensor = new PDBatterySensor(this, context);
//		timeSensor = new PDTimeSensor(this, activity);
		stepDetector = new StepDetector(this);
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(stepDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
		
	}

	public void sendEvent(PDSensorEvent sensorEvent) {
		String eventName = sensorEvent.getName();

		if (eventName.equals("pitch_angle")) {
			PdBase.sendFloat("pitch_angle",
					Float.valueOf(sensorEvent.getValue()));
//			Log.i("Pitch", sensorEvent.getValue());
		} else if (eventName.equals("roll_angle")) {
			PdBase.sendFloat("roll_angle",
					Float.valueOf(sensorEvent.getValue()));
		} else if (eventName.equals("batterylevel")) {
			PdBase.sendFloat("batterylevel",
					Float.valueOf(sensorEvent.getValue()));
		}

	}

}
