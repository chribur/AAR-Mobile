package com.aar.android;

import org.puredata.core.PdBase;

import android.content.Context;
import android.util.Log;

public class PDSensorManager {

	private PDOrientationSensor orientationSensor;
	
	public PDSensorManager(Context context) {
		orientationSensor = new PDOrientationSensor(this, context);
	}

	public void sendEvent(PDSensorEvent sensorEvent) {
		String eventName = sensorEvent.getName();

		if (eventName.equals("pitch_angle")) {
			PdBase.sendFloat("pitch_angle",
					Float.valueOf(sensorEvent.getValue()));
			Log.i("Pitch", sensorEvent.getValue());
		} else if (eventName.equals("roll_angle")) {
			PdBase.sendFloat("roll_angle",
					Float.valueOf(sensorEvent.getValue()));
		}

	}

}
