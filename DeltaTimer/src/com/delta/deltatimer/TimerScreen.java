package com.delta.deltatimer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
	
	//the flag variable for the timer,its value is 0 when its NOT running,and 1 when it is 
	int flag=0;
	//and the flag variable to check IF any laps have been recorded
	int lapflag=0;
	
	//the handler which is used to add runnable's to the message queue, this happens on a seperate thread, created here
	private Handler handler = new Handler();
	
	//the list of lap lengths stored as string arrays 
	ArrayList<String> arrayOflaps= new ArrayList<String>();

    //the no of laps recorded can be kept count of
    int laps=0;
    
    //the adapter
    ArrayAdapter<String> adapter;

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
	
	//function called when start button is called
	public void start(View v){
		// to make sure nothing happens if the user clicks start while the timer is running
		if(flag==1){
			return;
		}
		
		//timer is started
		flag=1;
				
		//milliseconds since boot, not counting time spent in deep sleep 
		//this is used as a BASE, we count FROM this time
		starttime=SystemClock.uptimeMillis();
		//adds the "updateTimerThread" runnable object after "0" miliseconds of delay
		//thus this ensures the time keeps getting updated
		handler.postDelayed(updateTimer, 0);
		
	}
	
	//function called when pause button is called
	public void pause(View v){
		// to make sure nothing happens if the user clicks pause while the timer is already paused
		if(flag==0){
			return;
		}
		
		//as the timer is paused
		flag=0;
		//here buffertime is the time spent while in the paused state, intially that is zero so is milisecondstime
		//but after its paused
		buffertime=buffertime+milisecondstime;
		// basically removes all pending posts of "updateTimer" in the message queue
		//ie stops the updating to the textview, currenttime
		handler.removeCallbacks(updateTimer);
	}
	
	//function called when reset button is called
	public void reset(View v){
		//in the case the timer is yet to be started, it cant be re-set unless its already set
		if(newtime==0L){
			return;
		}
		//as a lap has been recorded
		lapflag=1;
		//in the case that the timer is not running we shouldnt just start the timer
		if(flag==0){
			// we reset the values of all the timer variables to default
			starttime =0L;
			milisecondstime =0L;
			buffertime=0L;
			newtime=0L;
			currenttime.setText("0:00:000");
			// and we can just safely exit
			return;
		}
		// we stop the timer
		pause(v);
		// we increment the number of laps
		laps++;
		//get the "number of laps" as a string
		String lap=Integer.toString(laps);
		
		//we obtain the breakup of the time in min, secs, and miliseconds
		int sec=(int)(newtime/1000);
		int mins=sec/60;
		sec=sec%60;
		int milisec=(int)(newtime%1000);
		
		//we obtain the lap time in the string format
		String strtime=""+mins+":"+ String.format("%02d", sec) + ":"+ String.format("%03d", milisec);
		
		//we catanate both the nth lap no and that lap time and append it to the arrayoflaps
		arrayOflaps.add("Lap No:"+lap+"   "+strtime);
			
		//we get lv a handler onto the list point to our "list"
		ListView list = (ListView) findViewById(R.id.list);
		//we define adapter which gets the data from arrayoflaps and converts it into a diplayable format
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayOflaps){
			//here as I faced a problem with not able to see the listview elements as its text colour is by default black i use this 
			//While instantiating this adapter here, i declare this inner class to set the list's text view elements color as white

	        @Override
	        public View getView(int position, View convertView,
	                ViewGroup parent) {
	            View view =super.getView(position, convertView, parent);

	            TextView textView=(TextView) view.findViewById(android.R.id.text1);
	            
	            //doesnt have to be white, u can choose
	            textView.setTextColor(Color.WHITE);

	            return view;
	        }
	    };
	    //finally we set the adapter to the listview via the lv
		list.setAdapter(adapter);

	
		// we reset the values of all the timer variables to default
		starttime =0L;
		milisecondstime =0L;
		buffertime=0L;
		newtime=0L;
		currenttime.setText("0:00:000");
		
		//and now we resume counting, timer restarts
		start(v);
	}
	
	
	//now we define the updatetimerthread - a runnable object
	private Runnable updateTimer = new Runnable() {
		
		//this code is set to run on a thread created when the handler was created

		@Override
		public void run() {
			// sets the currenttime handler to point to textview1 so we can set the text there
		    currenttime = (TextView) findViewById(R.id.textView1);
			
			
			//this the difference between how much time actually elapsed to how much time the timer has been running (ie starttime)
			milisecondstime=SystemClock.uptimeMillis()-starttime;
			
			//but we have to consider both buffertime and milisecondstime, thus the sum of these both give us the timertime
			newtime=buffertime+milisecondstime;
			
			//we obtain the breakup of the time in min, secs, and miliseconds
			int sec=(int)(newtime/1000);
			int mins=sec/60;
			sec=sec%60;
			int milisec=(int)(newtime%1000);
			
			//stores the time in the appropriate format into currenttime
			currenttime.setText(""+mins+":"+ String.format("%02d", sec) + ":"+ String.format("%03d", milisec));
			
			//calls its self so this thread is self updating, thus continously keeping the timer updated
			handler.postDelayed(this, 0);

		}
		
		
	};
	
	// we clear the adapter and reset the no of lap to 0
	public void clear(View v){
		if(lapflag==0){
			//in the case there is no list to clear
			return;
		}
		adapter.clear();
		laps=0;
	}
	
	//we launch a intent to the home screen to exit the app
	public void exit(View v){
		//before we exit we need to STOP the damn thread as even if the application is closed the timer keeps running in the background
		handler.removeCallbacks(updateTimer);
		//then we exit
		System.exit(0);
	}



}
