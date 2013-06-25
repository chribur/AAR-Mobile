/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aar.android;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 */
public class StepDetector implements SensorEventListener
{
    private final static String TAG = "StepDetector";
	private PDSensorManager mPDSensorManager; 

    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;
    
//    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();
    
    public StepDetector(PDSensorManager pdSensorManager) {
		mPDSensorManager = pdSensorManager;
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }
    
    public void setSensitivity(float sensitivity) {
        mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }
    
//    public void addStepListener(StepListener sl) {
//        mStepListeners.add(sl);
//    }
    
//    @SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor; 
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;
                    
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {

                    	// Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);
                            
                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                calculatePace();
                                

//                                for (StepListener stepListener : mStepListeners) {
//                                    stepListener.onStep();
//                                }
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }
    
    
    int mCounter = 0;
    
    private long mLastStepTime = 0;
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    private int mLastStepDeltasIndex = 0;
    private long mPace = 0;
    
    
    /** Desired pace, adjusted by the user */
    int mDesiredPace;
    
    public void calculatePace(){
    	  long thisStepTime = System.currentTimeMillis();
          mCounter ++;
          Log.i(TAG, "im here ");

          // Calculate pace based on last x steps
          if (mLastStepTime > 0) {
              long delta = thisStepTime - mLastStepTime;
              
              mLastStepDeltas[mLastStepDeltasIndex] = delta;
              mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;
              
              long sum = 0;
              boolean isMeaningfull = true;
              for (int i = 0; i < mLastStepDeltas.length; i++) {
                  if (mLastStepDeltas[i] < 0) {
                      isMeaningfull = false;
                      break;
                  }
                  sum += mLastStepDeltas[i];
              }
              if (isMeaningfull && sum > 0) {
                  long avg = sum / mLastStepDeltas.length;
                  mPace = 60*1000 / avg;                  
              }
              else {
                  mPace = -1;
              }
          }
          mLastStepTime = thisStepTime;
          
          Log.i(TAG, String.valueOf(mPace));

          
          
  		//hier senden an puredata
          mPDSensorManager.sendEvent(new PDSensorEvent("batterylevel", String.valueOf(mPace)));

    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}