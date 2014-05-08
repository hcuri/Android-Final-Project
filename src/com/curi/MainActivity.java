package com.curi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseUser;

public class MainActivity extends Activity implements OnItemClickListener{

	private EditText mTaskInput;
	private ListView mListView;
	private static TaskAdapter mAdapter;
	public static int current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Parse.initialize(this, "eWwRUTH1UIfOuAjxlyE4fkrDii5AuKDWGoLvZq0w", "tfreVuLEyblkzLQN6LXL5mLtYeIrsh9yWfiVr5ql");
		ParseAnalytics.trackAppOpened(getIntent());
		ParseObject.registerSubclass(Task.class);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		mAdapter = new TaskAdapter(this, new ArrayList<Task>());

		mTaskInput = (EditText) findViewById(R.id.task_input);
		mListView = (ListView) findViewById(R.id.task_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		updateData();

		SwipeDismissListViewTouchListener touchListener =
				new SwipeDismissListViewTouchListener(
						mListView,
						new SwipeDismissListViewTouchListener.OnDismissCallback() {
							public void onDismiss(ListView listView, int[] reverseSortedPositions) {
								for (int position : reverseSortedPositions) {
									try {
										mAdapter.getItem(position).delete();
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									mAdapter.remove(mAdapter.getItem(position));
								}
								mAdapter.notifyDataSetChanged();
							}
						});
		mListView.setOnTouchListener(touchListener);
		mListView.setOnScrollListener(touchListener.makeScrollListener());

		registerForContextMenu(mListView);
	}

	public void createTask(View v) {
		if (mTaskInput.getText().length() > 0){
			Task t = new Task();
			t.setACL(new ParseACL(ParseUser.getCurrentUser()));
			t.setUser(ParseUser.getCurrentUser());
			t.setDescription(mTaskInput.getText().toString());
			t.setCompleted(false);
			t.setDueYear(0);
			t.saveEventually();
			mAdapter.insert(t, 0);
			mTaskInput.setText("");
		}
	}

	public void updateData(){
		ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<Task>() {
			@Override
			public void done(List<Task> tasks, ParseException error) {
				if(tasks != null){
					mAdapter.clear();
					for (int i = 0; i < tasks.size(); i++) {
						mAdapter.add(tasks.get(i));
					}
				}
			}
		});
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Task task = mAdapter.getItem(position);
		CheckedTextView taskDescription = (CheckedTextView) view.findViewById(R.id.task_description);

		task.setCompleted(!task.isCompleted());

		if(task.isCompleted()){
			taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			taskDescription.setChecked(true);
		}else{
			taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			taskDescription.setChecked(false);
		}

		task.saveEventually();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout: 
			ParseUser.logOut();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			return true; 
		} 
		return false; 
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Task t = mAdapter.getItem(info.position);
		current = info.position;
		switch (item.getItemId()) {
		case R.id.due_date:
			DialogFragment newFragment = new DatePickerFragment();
		    newFragment.show(getFragmentManager(), "datePicker");
			return true;
			
		case R.id.remove_due_date:
			t.setDueYear(0);
			t.saveEventually();
			mAdapter.notifyDataSetChanged();
			return true;
			
		case R.id.delete:
			try {
				t.delete();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mAdapter.remove(t);
			mAdapter.notifyDataSetChanged();
			return true;
		default:
			mAdapter.notifyDataSetChanged();
			return super.onContextItemSelected(item);
		}
	}

	public static class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			// Do something with the date chosen by the user
			Task t = mAdapter.getItem(current);
			t.setDueYear(selectedYear);
		    t.setDueMonth(selectedMonth);
		    t.setDueDay(selectedDay);
		    t.saveEventually();
		    mAdapter.notifyDataSetChanged();
			
		}
	}
}


