package com.mgcompass.app;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private SensorManager sensorManager;
	private ImageView compassImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		compassImg=(ImageView)findViewById(R.id.arrow_img);
		
		sensorManager=(SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
		
		Sensor magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		sensorManager.registerListener(listener,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(listener, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
	}

	
	
	
	private SensorEventListener listener=new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		float[] a=new float[3];
		float[] b=new float[3];
		
		private float lastRotateDegree;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				a=event.values.clone();
			}else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
				b=event.values.clone();
				
			}
					
			float []R=new float[9];
			float []values=new float[3];
			
			SensorManager.getRotationMatrix(R, null, a, b);
			SensorManager.getOrientation(R, values);
			
			
			float rotateDegree=-(float)Math.toDegrees(values[0]);
			if(Math.abs(rotateDegree-lastRotateDegree)>1){
				RotateAnimation animation=new RotateAnimation(lastRotateDegree,rotateDegree,Animation.RELATIVE_TO_SELF
						,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
				animation.setFillAfter(true);
				compassImg.startAnimation(animation);
				lastRotateDegree=rotateDegree;
			}         
		}	
	};
		
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(sensorManager!=null){
			sensorManager.unregisterListener(listener);
		}
	  }
   }
