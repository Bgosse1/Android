package edu.hofstra.cs.todo2;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter<T> extends ArrayAdapter<T>{

	public MyArrayAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if ( convertView == null){
			LayoutInflater inflater;
			inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.listitem, null);
		}
		
		TextView t = (TextView) convertView.findViewById(R.id.text1);
		t.setText(getItem(position).toString());
		t.setTextColor(Color.BLACK);
		ToDoEntry e = (ToDoEntry) getItem(position);
		if ( e.priority == 1){
			t.setTextColor(Color.BLACK);
		}
		if ( e.priority == 2){
			t.setTextColor(Color.BLUE);
		}
		if ( e.priority == 3){
			t.setTextColor(Color.GREEN);
		}
		if ( e.priority == 4){
			t.setTextColor(Color.YELLOW);
		}
		if ( e.priority == 5){
			t.setTextColor(Color.RED);
		}
		//else
		//{
			//t.setTextColor(Color.BLACK);
		//}
		return convertView;
	}

}
