package com.delta.hello;


import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	int counter =0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void Scramble(View v){
		
		counter=counter+1;
		
		//using the id of hello world! we make the text view handle hello
		TextView hello =(TextView)findViewById(R.id.textView2);
		
		// we get the string stored as value at this before mentioned position
	    String str = hello.getText().toString();
	    
	    Log.i("TEST - string  to be scrambled",str);
	    
	     //METHOD 1 - fail 
	     //REASON:: I got gibrish which is the Class name followed by the memory address of the object -this comes when i print the string from
	     // the char list using tostring
	    //FIX: http://stackoverflow.com/questions/13505274/java-println-with-char-array-gives-gibberish
	    
	    //METHOD 2
	   
	    
	    //store the string that is right now being displayed, and that which is going to be scrambled as a Character List!!
	    char[] charlist = str.toCharArray();
	    
	    //this is A WAY to convert the char list back into a string PROPERLY, unlike in the last method which failed
	    //String array=Arrays.toString(charlist);
	    //Log.i("TEST - array  to be scrambled",array);
	    
	    // using linear congruential generator, we use some start or "seed" number which ideally is "genuinely unpredictable", and which in practice is "unpredictable enough".
	 		Random r = new Random();
	 		
	    //a number between [65,80) inclusive of 65 exclusive of 80
	 	//this is as the .nextInt(N) function gives us a number between 0(inclusive) and N(exclusive)
	 		int value = r.nextInt(12 - 0) + 0;
	 		
	 	// now temp1 and temp2 are the characters which are to be switched	
	 		char temp1 =charlist[value];
	    

			// and we swap 2 characters
			if(value==11){
				
				char temp2 = charlist[value-1];
				charlist[value]=temp2;
				charlist[value-1]=temp1;
			}
			else{
				
				char temp2 = charlist[value+1];
				charlist[value]=temp2;
				charlist[value+1]=temp1;
			}
	    
	    
		//we make this toast to inform the user we have scrambled the text
		
		//this defines where this toast should be displayed.. i used the getApplicationContext because it associates with the application 
			//where as the Activity context is associated with the activity and could possibly be destroyed many times as the activity is destroyed during screen orientation changes and such.
			//by the activity context i mean 'this' which can also be used
			//and we dont use getBaseContext() as it is used to access context of a class which is not an activity
			// SOURCES:http://stackoverflow.com/questions/1026973/android-whats-the-difference-between-the-various-methods-to-get-a-context
		Context context = getApplicationContext();
		// we define the text to be displayed
		CharSequence text = "Hello World! has been SCRAMBLED "+counter+" number of times";
		//we define the duration the toast should be visible
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
			
		//now we get the string that has to replace the previous one
		String n =new String(charlist);
		
		//now using the "hello" handle we replace the previous text
		hello.setText(n);
		
		
		
	}

}
