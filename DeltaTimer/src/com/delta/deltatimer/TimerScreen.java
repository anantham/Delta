package com.delta.deltatimer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TimerScreen extends Activity{
	//we use private in the following variables, as they will be used inside the TimerScreen Class

	
	//now the timer value, ie the Current Recorded Time thats going to be recorded in textview (the output format)
	TextView currenttime;
	
	//now the timer value, ie the Current Recorded Time thats going to be manipulated (the working format)
	//they are done using various variables ---
	long starttime =0L;
	long milisecondstime =0L;
	long buffertime=0L;
	long newtime=0L;
	
	//the handler which is used to add runnable's to the message queue  
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer_screen);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_timer_screen,
					container, false);
			return rootView;
		}
	}
	
	public void start(View v){
		//milliseconds since boot, not counting time spent in deep sleep 
		//this is used as a BASE, we count FROM this time
		starttime=SystemClock.uptimeMillis();
		//adds the "updateTimerThread" runnable object after "0" miliseconds of delay
		handler.postDelayed(updateTimerThread, 0);
	}
	
	public void pause(View v){
		buffertime=buffertime+milisecondstime;
		// basically removes all pending posts of "updateTimerThread" in the message queue
		handler.removeCallbacks(updateTimerThread);
	}
	//now we define the updatetimerthread - a runnable object
	private Runnable updateTimerThread = new Runnable() {
		//after the fragment has been instantiated we can safely link the handles to the respective widgets
		

		@Override
		public void run() {
			currenttime = (TextView) findViewById(R.id.textView1);
			
			milisecondstime=SystemClock.uptimeMillis()-starttime;
			
			newtime=buffertime+milisecondstime;
			
			int sec=(int)(newtime/1000);
			int mins=sec/60;
			sec=sec%60;
			int milisec=(int)(newtime%1000);
			
			currenttime.setText(""+mins+":"+ String.format("%02d", sec) + ":"+ String.format("%03d", milisec));
			
			handler.postDelayed(this, 0);
			
		}
		
		
	};



}
