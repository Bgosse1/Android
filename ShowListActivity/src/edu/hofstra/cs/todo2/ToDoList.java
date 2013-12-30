package edu.hofstra.cs.todo2;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class manages a list of things to do. It initializes the SQLite database
 * that is used to persist entries. It has methods to: 1) Enumerate all rows in
 * the database table 2) Insert a new entry into the database table 3) Mark an
 * entry as "finished"
 * 
 * @author csckzp
 * 
 */
public class ToDoList {
	SQLiteDatabase my_DB;
	MyDBHelper helper;

	public ToDoList(Context context) {
		helper = new MyDBHelper(context);
	}

	/**
	 * This method returns the rows from the "todo" table as a Java list.
	 * 
	 * @param inc_finished
	 *            Should the returned list include items that were finished
	 * @return A Java List of rows from the "todo" table.
	 */
	public ArrayList<ToDoEntry> getList(boolean inc_finished, boolean sorted) {
		my_DB = helper.getReadableDatabase();
		Cursor c;
		if (sorted)
			c = my_DB.query("todo", null, null, null, null, null, "priority");
		else
			c = my_DB.query("todo", null, null, null, null, null, null);
		ArrayList<ToDoEntry> entries = new ArrayList<ToDoEntry>();
		if (c.moveToFirst()) {
			do {
				ToDoEntry entry = new ToDoEntry();
				entry.id = c.getInt(c.getColumnIndex("_id"));
				entry.entry = c.getString(c.getColumnIndex("entry"));
				entry.finished = c.getInt(c.getColumnIndex("finished"));
				entry.priority = c.getInt(c.getColumnIndex("priority"));
				if (inc_finished || entry.finished == 0)
					entries.add(0, entry);
				c.moveToNext();
			} while (!c.isAfterLast());
		}
		ToDoEntry dummy = new ToDoEntry();
		dummy.id = -1;
		dummy.entry = "Add new entry";
		dummy.finished = 0;
		entries.add(0, dummy);
		return entries;
	}

	/**
	 * Adds an entry to the "todo" table.
	 * 
	 * @param entry
	 *            The entry to be added to the to do list.
	 */
	public void add(String entry, int priority) {
		my_DB = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("entry", entry);
		cv.put("priority", priority);
		cv.put("finished", 0);
		my_DB.insert("todo", null, cv);
	}

	/**
	 * Mark an item in the to do list as "finished"
	 * 
	 * @param currIdx
	 */
	public void markFinished(int currIdx) {
		my_DB = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("finished", 1);
		my_DB.update("todo", values, "_id=" + currIdx, null);
		
		
	}
	public void removeDB(int currIdx){
		my_DB = helper.getWritableDatabase();
		//ContentValues values = new ContentValues();
		my_DB.delete("todo", "_id="+currIdx, null);
	}
	public class MyDBHelper extends SQLiteOpenHelper {

		public MyDBHelper(Context context) {
			super(context, "ToDoDB", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table todo (_id integer primary key autoincrement not null,"
					+ "entry text, "
					+ "priority integer,"
					+ "finished integer"
					+ ")";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
