package com.delta.deltatimer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class TimerScreen extends Activity{

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
	
	// this function is called at the start of the progress bar's movement 
	public void startprogress(View v){
		// get a handle onto the progress bar
		ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
		//so its progress is set to 0
		bar.setProgress(0);
		//now we create a thread, link it to a defined "task" and start it
		new Thread(new Task()).start();
	}
	
	class Task implements Runnable{
		// get a handle onto the progress bar
		ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);

		@Override  //this is the method inherited from the class Runnable
		public void run() {
			// TODO Auto-generated method stub
			/*
			for(int i=0;i<=100;i++){
				final int value=i;
				//catch any execeptions thrown by the thread when its "put to sleep"
				try{
					Thread.sleep(100);
				} catch(InterruptedException e){
					e.printStackTrace();
				}
				bar.setProgress(value);
			}
			*/
		}
		
	}

}
