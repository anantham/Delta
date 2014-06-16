package com.delta.deltatimer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
	
	//the handler which is used to add runnable's to the message queue, this happens on a seperate thread, created here
	private Handler handler = new Handler();
	
	//the handle onto the listview
	ListView list;
	
	//the list of lap lengths stored as arrays
    ArrayList<String> finallist=new ArrayList<String>();

    //the no of laps recorded can be kept count of
    int laps=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer_screen);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//so we establish the handle on to the list view inside oncreate
		list = (ListView)findViewById(android.R.id.list);
		
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
		
		//timer is paused
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
		
		// we stop the timer
		pause(v);
		// we set a lap to be stored
		laps++;
		
		// made a new adapter with this updated list
		//this refers to the activity context
		//android.R.layout.simple_list_item_1 simple_list_item_1 is the layout in android.R.layout, (xml resource)
		//finallist is a string array for the lap durations (data array)
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,finallist);
		
		//attaching the adapter to the list view
		// Construct the data source
		ArrayList<time> arrayOfUsers = new ArrayList<time>();
		//adding to the list
		String lap=Integer.toString(laps);
		// Add item to adapter
		time newUser = new time(lap, currenttime.toString());
		// Create the adapter to convert the array to views
		TimeAdapter adapter = new TimeAdapter(this, arrayOfUsers);
		// Attach the adapter to a ListView
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		
		adapter.add(newUser);

		//UNABLE TO SET THE LIST WITH THIS ADAPTER
		//cause  of crash in reset button!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
		//list.setAdapter(adapter); 
	
		// we reset the values of all the timer variables to default
		starttime =0L;
		milisecondstime =0L;
		buffertime=0L;
		newtime=0L;
		currenttime.setText("0:00:000");
		
		//and now we resume counting, timer restarts
		start(v);
	}
	
	//for us to use a Custom ArrayAdapter
	//we need to first define the model
	public class time {
		public String lapno;
		public String timertime;
		
		public time(String lapno, String timertime){
			//we set the default values for both the strings inside the constructor
			this.lapno="0";
			this.timertime="0:00:000";
		}
		//We can now create a custom listview of user objects by subclassing ArrayAdapter, 
		//describing how to translate the object into a view within that class and then using it like any other adapter (ie like the ArrayAdapter<String>)
	}
	//once we have DEFINED a model
	// we now CONSTRUCT the model -?-
	//and now define the adapter
	public class TimeAdapter extends ArrayAdapter<time> {
	    public TimeAdapter(Context context, ArrayList<time> users) {
	       super(context, R.layout.fragment_timer_screen, users);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
	       time user = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_timer_screen, parent, false);
	       }
	       // Lookup view for data population
	       TextView tvHome = (TextView) convertView.findViewById(R.id.textView1);
	       tvHome.setText(user.timertime);
	       // Return the completed view to render on screen
	       return convertView;
	   }
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



}
