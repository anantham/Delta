package com.delta.deltatimer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class TimerScreen extends Activity{
	// so all functions inside this Activity has access to it
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer_screen);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//we need to get a handler associated with the main thread (we declared it therein the Main thread)
		handler = new Handler();
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
			for(int i=0;i<=100;i++){
				final int value=i;
				//catch any execeptions thrown by the thread when its "put to sleep"
				try{
					Thread.sleep(100); // here the UI thread (because thats *this* thread) is hung for 100 miliseconds
				} catch(InterruptedException e){ //here we are making the thread support its own interruption
					//an interrupt is used to inform the thread it should stop what its doing and it should do
					//something else
					e.printStackTrace();
					//this method of handling the interrupt is okay if interrupts happens frequently
					//but in some cases there isnt a interruption for quite some time
					//then we check using if(Thread.interrupted()){//handle the interruption}
					//keep in mind that the interrupt status flag is reset (cleared) when the status is checked
					// and it is set by the Thread.interrupt
				}
				// now here we add a runnable object into the message queue 
				// here the handler takes this object and shedules its execution
				// its execution will occur in the thread to with the handler is associated
				// we define the runnable object as an anonymous inner class
				handler.post(new Runnable(){
					@Override
					public void run(){
						bar.setProgress(value);
					}
				});
				// you may ask why use a handler, what was wrong with my previous commit, but in android we can only modify the UI elements(widgets) can only be modified in the main thread
			}
		}
		
	}

}
