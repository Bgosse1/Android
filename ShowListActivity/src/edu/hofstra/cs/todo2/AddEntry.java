package edu.hofstra.cs.todo2;





import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddEntry extends Activity {
	int numClick = 1;
	int priorityNum = 1;
	EditText text;
	Button add;
	Button priority;
	Button ListOrder;
	ToDoList myEntries;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_entry);
		myEntries = new ToDoList(this);  //This is the data manager
		text = (EditText) findViewById(R.id.editText1);
		add = (Button) findViewById(R.id.button1);
		priority = (Button) findViewById(R.id.button2);
		ListOrder = (Button) findViewById(R.id.button2);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String entry_text = text.getText().toString();
				if ( entry_text.trim().length() > 0){
					myEntries.add(entry_text,priorityNum);//Ask the data manager (model) to add
					finish(); 
				}
			}
		});
		
		ListOrder.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				
			}
		});
		
		priority.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				//final ListView listview = (ListView) findViewById(R.id.listView1);
				//String[] values = new String[] {"1 (High)","2","3","4","5 (Low)"};
				//Toast.makeText(AddEntry.this, "You lose!", Toast.LENGTH_LONG).show();
				//int defaultPriority = 3;
				//int numClick = 0;
				numClick++;
				//for (int i = 0; i <= 5; i++){
					if(numClick == 6){
						numClick = 1;
						priorityNum = 1;
					}
					if(numClick == 1){
						priorityNum = 1;
						text.setTextColor(Color.BLACK);
						Toast.makeText(AddEntry.this, "Priority set to 1", Toast.LENGTH_SHORT).show();
					}
					if(numClick == 2){
						priorityNum = 2;
						text.setTextColor(Color.BLUE);
						Toast.makeText(AddEntry.this, "Priority set to 2", Toast.LENGTH_SHORT).show();
					}
					if(numClick == 3){
						priorityNum = 3;
						text.setTextColor(Color.GREEN);
						Toast.makeText(AddEntry.this, "Priority set to 3", Toast.LENGTH_SHORT).show();
					}
					if(numClick == 4){
						priorityNum = 4;
						text.setTextColor(Color.YELLOW);
						Toast.makeText(AddEntry.this, "Priority set to 4", Toast.LENGTH_SHORT).show();
					}
					if(numClick == 5){
						priorityNum = 5;
						text.setTextColor(Color.RED);
						Toast.makeText(AddEntry.this, "Priority set to 5", Toast.LENGTH_SHORT).show();
					}
			}
				
			//}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_entry, menu);
		return true;
	}

}
