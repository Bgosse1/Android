package edu.hofstra.csc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FacultyLookupActivity extends Activity implements OnClickListener,
		OnItemSelectedListener, Runnable {

	/* User interface components */
	LinearLayout mainForm;  // A "container" for other components
	Spinner nameChoices;    // A drop-down list
	ImageView face;         // Shows images
	TextView email, phone;  // Displays textual information
	TextView status;
	Button exitBtn, viewBtn, dialBtn; // Clickable buttons

	// List of names to display in the Spinner
	String names[] = { "S. Divakaran", "S. Doboli", "J. Impagliazzo",
			"G. Kamberova", "C. Liang", "G. Ostheimer", "K. Pillaipakkamnatt" };
	
	// List of usernames to match the displayed names
	String unames[] = { "cscsyd", "cscszd", "cscjzi", "cscglk", "cscccl",
			"cscgzo", "csckzp" };
	
	// URL prefixes for retrieving images, phone numbers and email addresses
	String imgURL = "http://cs.hofstra.edu/mobile/images/",
			phnURL = "http://cs.hofstra.edu/mobile/phndata/",
			emlURL = "http://cs.hofstra.edu/mobile/emldata/";

	/**
	 * This method is called when the Activity is started.  All initialization
	 * goes here.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  //This should ALWAYS be the first line
		
		// Create the user interface
		nameChoices = new Spinner(this);
		// ArrayAdapter connects "names" array to nameChoices Spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nameChoices.setAdapter(adapter);
		
		//Set up a callback for when the user selects from Spinner (see the
		// method "onItemSelected(...)"
		nameChoices.setOnItemSelectedListener(this);

		face = new ImageView(this);
		email = new TextView(this);
		phone = new TextView(this);

		exitBtn = new Button(this);
		exitBtn.setText("Exit");
		viewBtn = new Button(this);
		viewBtn.setText("View");
		dialBtn = new Button(this);
		dialBtn.setText("Dial");
		
		//Create a subcontainer for the buttons
		LinearLayout miniForm = new LinearLayout(this);
		miniForm.setOrientation(LinearLayout.HORIZONTAL);
		miniForm.setGravity(Gravity.CENTER);
		
		// Add the buttons to subcontainer
		miniForm.addView(exitBtn);
		miniForm.addView(viewBtn);
		miniForm.addView(dialBtn);
		
		status = new TextView(this);
		status.setText("Status: Idle");

		/* Create the container. */
		mainForm = new LinearLayout(this);
		mainForm.setOrientation(LinearLayout.VERTICAL);
		
		// Now add all the components
		mainForm.addView(nameChoices);
		mainForm.addView(face);
		mainForm.addView(email);
		mainForm.addView(phone);
		mainForm.addView(miniForm); //... including the subcontainer
		mainForm.addView(status);
		
		// Set up callbacks for the buttons (see the method OnClick(...)).
		exitBtn.setOnClickListener(this);
		viewBtn.setOnClickListener(this);
		dialBtn.setOnClickListener(this);
		
		// Display the container (and everything within it)
		setContentView(mainForm);
	}

	/**
	 * This method is invoked when one of the buttons is clicked.
	 */
	public void onClick(View v) {
		if (v == exitBtn)
			finish(); // Exit the app
		if (v == viewBtn) {
			// Start network activity in a new thread
			Thread t = new Thread(this);
			t.start();  // Get the thread running, see the run() method.
			viewBtn.setVisibility(View.GONE);
			enableDial(true);
		}
		if (v == dialBtn) {
			// Toast displays a short-lived message
			Toast.makeText(this, "Calling ..." , Toast.LENGTH_SHORT).show();
			
			// Create an intent (a message) to tell the system that we want to make
			// a phone call
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
			
			// Get the phone call activity going
			startActivity(intent);
			
		}
		
	}

	/**
	 * This method initiates network activity
	 */
	public void run() {
		updateStatus("Status: Connecting ...");
		try {
			lookup();  // Find information about selected faculty member
		} catch (IOException ex) {
			updateStatus("Connection error: Please try again." + ex);
		}
	}

	/**
	 * This method retrieves the photo, the phone number and an email address
	 * for the faculty member selected in the Spinner.
	 * @throws IOException
	 */
	private void lookup() throws IOException {
		HttpURLConnection myconn = null;
		InputStream istream = null;
		
		clearImage();

		setItemText(email, "");

		setItemText(phone, "");
		

		try {
			URL url;
			String uname = unames[nameChoices.getSelectedItemPosition()];
			byte[] data = new byte[256];

			/* Set up a connection for image data. */
			url = new URL(imgURL + uname + ".png");
			myconn = (HttpURLConnection) url.openConnection();
			updateStatus("Status: Getting image ...");
			istream = myconn.getInputStream();
			updateStatus("Status: Getting image ... stream open ...");

			/* Download image data into an array of bytes. */
			byte imgBuff[] = new byte[0];
			int lastPos = 0, len;
			while ((len = istream.read(data)) != -1) {
				byte[] temp = new byte[imgBuff.length + len];
				for (int q = 0; q < imgBuff.length; q++) {
					temp[q] = imgBuff[q];
				}
				for (int q = 0; q < len; q++) {
					temp[lastPos] = data[q];
					lastPos++;
				}
				imgBuff = temp;
				updateStatus("Status: Getting image ... stream open ..."
						+ imgBuff.length);
			}

			/* Create an Image object from the byte array. */
			Bitmap f_face = BitmapFactory.decodeByteArray(imgBuff, 0, imgBuff.length);
			updateStatus("Status: Image received.");
			/* Place Image object into user interface. */
			showFace(f_face);
			istream.close();

			/* Retrieve email address. */			
			String f_email = getFromWeb(emlURL + uname + ".txt", "email");
			setItemText(email, "Email: " + f_email);
			

			/* Retrieve phone number. */
			String f_phone = getFromWeb(phnURL + uname + ".txt", "phone");
			setItemText(phone, "Phone: " + f_phone);
			
		} finally {
			updateStatus("Status: Idle.");
			if (istream != null)
				istream.close();
		}
	}

	private String getFromWeb(String url_str, String what) throws IOException {
		URL url = new URL(url_str);
		HttpURLConnection myconn = (HttpURLConnection) url.openConnection();
		updateStatus("Status: Getting " + what + " ...");
		InputStream istream = myconn.getInputStream();
		byte[] data = new byte[256];
		int len = istream.read(data);
		istream.close();
		String text = new String(data, 0, len - 1);
		return text;
	}

	private void showFace(final Bitmap f_face) {
		runOnUiThread(new Runnable() {
			public void run() {
				face.setImageBitmap(f_face);
			}
		});
		
	}

	private void enableDial(final boolean b) {
		runOnUiThread(new Runnable() {
			public void run() {
				dialBtn.setEnabled(b);
			}
		});

	}
	
	private void enableView(final boolean b) {
		runOnUiThread(new Runnable() {
			public void run() {
				viewBtn.setEnabled(b);
			}
		});

	}

	private void setItemText(final TextView tv, final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				tv.setText(string);
			}
		});

	}

	private void updateStatus(final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				status.setText(string);
			}
		});
	}

	private void clearImage() {
		runOnUiThread(new Runnable() {
			public void run() {
				face.setImageResource(android.R.color.transparent);
			}
		});
	}
	
	/**
	 * What to do when a new item is selected from the Spinner.
	 */
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		 	//arg0.getItemAtPosition(arg2);
			//if (onClick(nameChoices) == new onClick())
			//dialBtn.setClickable(false);
			viewBtn.setVisibility(View.VISIBLE);
			enableDial(false);
			clearImage();
			setItemText(email, "");
			setItemText(phone, "");
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		face.setImageResource(android.R.color.transparent);

	}
}