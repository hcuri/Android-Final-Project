package com.curi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

public class MainActivity extends Activity implements OnItemClickListener{

	private EditText mTaskInput;
	private ListView mListView;
	private TaskAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Parse.initialize(this, "eWwRUTH1UIfOuAjxlyE4fkrDii5AuKDWGoLvZq0w", "tfreVuLEyblkzLQN6LXL5mLtYeIrsh9yWfiVr5ql");
		ParseAnalytics.trackAppOpened(getIntent());
		ParseObject.registerSubclass(Task.class);

		mTaskInput = (EditText) findViewById(R.id.task_input);
		mListView = (ListView) findViewById(R.id.task_list);

		mAdapter = new TaskAdapter(this, new ArrayList<Task>());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

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


		updateData();
	}

	public void createTask(View v) {
		if (mTaskInput.getText().length() > 0){
			Task t = new Task();
			t.setDescription(mTaskInput.getText().toString());
			t.setCompleted(false);
			t.saveEventually();
			mTaskInput.setText("");
			mAdapter.insert(t, 0);
		}
	}

	public void updateData(){
		ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<Task>() { 

			@Override
			public void done(java.util.List<Task> tasks, ParseException e) {
				if(tasks != null){
					mAdapter.clear();
					mAdapter.addAll(tasks);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
