package edu.hofstra.cs.todo2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowListActivity extends Activity {

	ToDoList myEntries;  //Data manager (model)
	boolean inc_finished; //Should we include "finished" entries?
	boolean sorted;
	ListView l_view; //Is what we show the items in.
	AlertDialog.Builder builder; //Is for asking if the user wants to mark an item as "finished"
	int currIdx; //Tracks the "_id" of the item that the user has tapped
	ArrayList<ToDoEntry> entries;  //A Java list that corresponds to items in the table
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_list);
		
		myEntries = new ToDoList(this);
		sorted = false;
		inc_finished = false;  //Assume the user doesn't want to see completed items
		l_view = (ListView) findViewById(R.id.listView1);
		l_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if ( arg2 == 0){  //User has tapped the first entry?
					Intent i = new Intent(ShowListActivity.this, AddEntry.class);
					startActivity(i);
				}
				else{  //User has tapped some other entry
					currIdx = entries.get(arg2).id;
					builder.setMessage("Mark item as completed?");
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
		
		builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myEntries.markFinished(currIdx);
				showList();
			}
		});
		builder.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showList();
			}
		});
		builder.setNeutralButton("Remove from DB", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myEntries.removeDB(currIdx);
				showList();
			}
		});
	}
 
	public void onResume(){
		super.onResume();
		showList();
	}
	
	private void showList() {
		entries = myEntries.getList(inc_finished,sorted);
		//entries = myEntries.getList(sorted);
		ArrayAdapter<ToDoEntry> adapter;
		//adapter = new ArrayAdapter<ToDoEntry>(this, android.R.layout.simple_list_item_1, entries);
		//adapter = new ArrayAdapter<ToDoEntry>(this, R.layout.listitem, entries);
		adapter = new MyArrayAdapter<ToDoEntry>(this, android.R.layout.simple_list_item_1, entries);
		l_view.setAdapter(adapter);		
	}

	/**
	 * This was automatically provided by Eclipse.  All I did was the
	 * edit the resource called activity_show_list in the "menu" folder.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_list, menu);
		return true;
	}

	/**
	 * I had to add this method.  This method is a callback--it is 
	 * invoked by the Android framework when the user has selected an
	 * item from the menu.  The "item" parameter tells you which item
	 * was selected by the user.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//See if the selected item's title matches the string for completion
		if ( item.getTitle().equals(getString(R.string.completed))){
			inc_finished = ! inc_finished; //Toggle the inc_finished boolean
			item.setChecked(inc_finished); //Update the check status for the menu item
			showList(); //Update the list again
		}
		if(item.getTitle().equals(getString(R.string.sorted))){
			sorted = ! sorted; 
			item.setChecked(sorted); 
			showList(); 
		}
		return true;
	}

}
