package com.csc184.tictactoe;

import java.util.ArrayList;
import com.csc184.tictactoe.R;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	Button[] buttons = new Button[9];
	Button Play, button1, button2, button3, button4, button5, button6, button7, button8, button9;
	EditText DialPhone;
	String Receiver;
	AlertDialog.Builder Send_B, Request_B;
	RadioGroup radio;
	String[] test = new String[9];
	final SmsManager ManagerResponse = SmsManager.getDefault();

	@Override
	protected void onCreate(Bundle savedInstancebool) 
	{
		super.onCreate(savedInstancebool);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);		
		button2 = (Button) findViewById(R.id.button2);		
		button3 = (Button) findViewById(R.id.button3);		
		button4 = (Button) findViewById(R.id.button4);		
		button5 = (Button) findViewById(R.id.button5);		
		button6 = (Button) findViewById(R.id.button6);		
		button7 = (Button) findViewById(R.id.button7);		
		button8 = (Button) findViewById(R.id.button8);		
		button9 = (Button) findViewById(R.id.button9);		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		buttons[0] = button1;
		buttons[1] = button2;
		buttons[2] = button3;
		buttons[3] = button4;
		buttons[4] = button5;
		buttons[5] = button6;
		buttons[6] = button7;
		buttons[7] = button8;
		buttons[8] = button9;
		
		Toast.makeText(MainActivity.this, "Winning Function is not Implemented", Toast.LENGTH_LONG).show();
		enableButtons(buttons, false);	
		Play = (Button) findViewById(R.id.buttonPlay);	Play.setOnClickListener(this);
		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		DialPhone = (EditText) findViewById(R.id.editPhoneNum);
		Send_B = new AlertDialog.Builder(this);
		Request_B = new AlertDialog.Builder(this);
		
		Send_B.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		
		Request_B.setPositiveButton("Yea", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enableButtons(buttons, false);
				Play.setEnabled(false);	
				Receiver = DialPhone.getText().toString();
				ManagerResponse.sendTextMessage(Receiver, null, "#0m", null, null);
				radio.check(radio.findViewById(R.id.radioO).getId());
				radio.findViewById(R.id.radioX).setEnabled(false);
			}
		});
		
		Request_B.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Receiver = DialPhone.getText().toString();
				ManagerResponse.sendTextMessage(Receiver, null, "#0t", null, null);
			}
		});
		
		Send_B.setPositiveButton("Yea", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SmsManager Manager = SmsManager.getDefault();
				Receiver = DialPhone.getText().toString();
				String seek = "#0r";
				ArrayList<String> Messages =  Manager.divideMessage(seek);
				Manager.sendMultipartTextMessage(Receiver, null, Messages, null, null);		
			}
		});
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(new BR(), filter);
	}
	
	private void enableButtons(Button[] buttons, boolean bool){
		for(Button button : buttons)
			button.setEnabled(bool);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void Movemade(int madeMove, String XorO)
	{
		SmsManager ManagerMove = SmsManager.getDefault();	//This SmsManager sends the move made by the player
		buttons[madeMove-1].setText(XorO);
		buttons[madeMove-1].setEnabled(false);
    	String message = "#" + madeMove + "" + XorO;
    	ManagerMove.sendTextMessage(Receiver,null,message,null,null);
    	enableButtons(buttons, false);
	}
	
	private void update(int madeMove, String XorO)
	{
		buttons[madeMove-1].setText(XorO);
		buttons[madeMove-1].setEnabled(false);
		enableButtons(buttons, true);
		/*if(test[0] == "X" && test[1] == "X" && test[2] == "X"){
			//Log.d("BALLS", "Winner");
			Toast.makeText(MainActivity.this, "Winner", Toast.LENGTH_LONG).show();
		}*/
	}
	
	public void onClick(View view) {
		String XorO;
		Receiver = DialPhone.getText().toString();
		if(radio.getCheckedRadioButtonId() == R.id.radioX)
			XorO = "X";
		else
			XorO = "O";
		switch (view.getId()) {
			case R.id.buttonPlay:
				Send_B.setMessage("Send a play request?");
				AlertDialog dialog = Send_B.create();
				dialog.show();
				break;
			case R.id.button1:
				Movemade(1, XorO);
		    	break;
		    case R.id.button2:
		    	Movemade(2, XorO);
		    	break;
		    case R.id.button3:
		    	Movemade(3, XorO);
		        break;
		    case R.id.button4:
		    	Movemade(4, XorO);
		        break;
		    case R.id.button5:
		    	Movemade(5, XorO);
		        break;
		    case R.id.button6:
		    	Movemade(6, XorO);
		        break;
		    case R.id.button7:
		    	Movemade(7, XorO);
		        break;
		    case R.id.button8:
		    	Movemade(8, XorO);
		        break;
		    case R.id.button9:
		    	Movemade(9, XorO);
		        break;
	      }
	  }
	
	class BR extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
				Bundle bundle = intent.getExtras();
				if(bundle != null) {
					Object[] oMessages = (Object[]) bundle.get("pdus");
					SmsMessage message;
					String message_body;
					
					for(int i = 0; i < oMessages.length; i ++)  
					{
						message = SmsMessage.createFromPdu((byte[]) oMessages[i]);					
						message_body = message.getMessageBody();	
						char firstChar = message_body.charAt(0);
						if (message_body != null && firstChar == '#') {
							DialPhone.setText(message.getOriginatingAddress());
							int position = Integer.parseInt(""+message_body.charAt(1));	//Convert 2nd char in message text to int
							
							String XorO = ""+message_body.charAt(2);	//Get 3rd char in message (the XorO played)
							//Change button text to XorO
							//String[] test = new String[9];
							
							/*if(test[1] == "X" && test[2] == "X" && test[3] == "X"){
								Toast.makeText(MainActivity.this, "Winner", Toast.LENGTH_LONG).show();
							}*/
								//Toast.makeText(MainActivity.this, XorO, Toast.LENGTH_LONG).show();
								//Toast.makeText(MainActivity.this, test[x], Toast.LENGTH_LONG).show();
							switch(position) {
								case 0:
									if(XorO.equals("r")) {	
										Request_B.setMessage("Do you want to play?");
										AlertDialog dialog = Request_B.create();
										dialog.show();
									}
									else if (XorO.equals("m")) {
										enableButtons(buttons, true);
										Play.setEnabled(false);
										radio.findViewById(R.id.radioO).setEnabled(false);
									}
									else if (XorO.equals("t")) {	
										CharSequence text = "Other player rejected";
										int duration = Toast.LENGTH_SHORT;
										Toast rejectionToast = Toast.makeText(context, text, duration);
										rejectionToast.show();
									}
									break;
								case 1:
									update(1, XorO);
									test[0] = XorO;
									Toast.makeText(MainActivity.this, test[0] + " this is 0", Toast.LENGTH_LONG).show();

									break;
								case 2:
									update(2, XorO);
									test[1] = XorO;
									Toast.makeText(MainActivity.this, test[1] + " this is 1", Toast.LENGTH_LONG).show();

									break;
								case 3:
									update(3, XorO);
									test[2] = XorO;
									Toast.makeText(MainActivity.this, test[2] + " this is 2", Toast.LENGTH_LONG).show();
									break;
								case 4:
									update(4, XorO);
									test[3] = XorO;
									Toast.makeText(MainActivity.this, test[3] + " this is 3", Toast.LENGTH_LONG).show();
									break;
								case 5:
									update(5, XorO);
									test[4] = XorO;
									Toast.makeText(MainActivity.this, test[4] + " this is 4", Toast.LENGTH_LONG).show();

									break;
								case 6:
									update(6, XorO);
									test[5] = XorO;
									Toast.makeText(MainActivity.this, test[5] + " this is 5", Toast.LENGTH_LONG).show();

									break;
								case 7:
									update(7, XorO);
									test[6] = XorO;
									Toast.makeText(MainActivity.this, test[6] + " this is 6", Toast.LENGTH_LONG).show();

									break;
								case 8:
									update(8, XorO);
									test[7] = XorO;
									Toast.makeText(MainActivity.this, test[7] + " this is 7", Toast.LENGTH_LONG).show();
									break;
								case 9:
									update(9, XorO);
									test[8] = XorO;
									Toast.makeText(MainActivity.this, test[8] + " this is 8", Toast.LENGTH_LONG).show();

									break;
							}
						}
					}						
				}
			}
		}
	}
}
