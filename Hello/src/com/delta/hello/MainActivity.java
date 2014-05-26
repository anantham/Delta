package com.delta.hello;


import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	int counter =0;
	int i;

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
	
	public void multi(View v){
		for(i=0;i<5;i++){
			Scramble(v);
		}
	}
	
	public void Scramble(View v){
		
			
		
		counter=counter+1;
		
		
		TextView hello =(TextView)findViewById(R.id.textView2);
		
		
	    String str = hello.getText().toString();
	    
	    
	    //store the string that is right now being displayed, and that which is going to be scrambled as a Character List!!
	    char[] charlist = str.toCharArray();
	    
	 		Random r = new Random();
	 		
	    //a number between [65,80) inclusive of 65 exclusive of 80
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
	    
		Context context = getApplicationContext();
		// we define the text to be displayed
		CharSequence text = "Hello World! has been SCRAMBLED "+counter+" number of times";
		//we define the duration the toast should be visible
		int duration = Toast.LENGTH_SHORT;

		final Toast toast = Toast.makeText(context, text, duration);
		if(i==4){
			toast.show();
		}
		
		
		//now we get the string that has to replace the previous one
		String n =new String(charlist);
		
		//now using the "hello" handle we replace the previous text
		hello.setText(n);
		
		
	}
	
	

}
