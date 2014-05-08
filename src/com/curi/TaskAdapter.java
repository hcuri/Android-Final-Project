package com.curi;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;


public class TaskAdapter extends ArrayAdapter<Task> {
	  private Context mContext;
	  private List<Task> mTasks;

	  public TaskAdapter(Context context, List<Task> objects) {
	      super(context, R.layout.task_row_item, objects);
	      this.mContext = context;
	      this.mTasks = objects;
	  }

	  public View getView(int position, View convertView, ViewGroup parent){
	      if(convertView == null){
	          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
	          convertView = mLayoutInflater.inflate(R.layout.task_row_item, null);
	      }

	      Task task = mTasks.get(position);

	      CheckedTextView descriptionView = (CheckedTextView) convertView.findViewById(R.id.task_description);
	      TextView	dateView = (TextView) convertView.findViewById(R.id.task_date);

	      descriptionView.setText(task.getDescription());
	      dateView.setVisibility(View.GONE);
	      
	      if(task.getDueYear()>2000){
	    	  dateView.setVisibility(View.VISIBLE);
	    	  dateView.setText(new StringBuilder().append(task.getDueMonth() + 1)
	   			   .append("-").append(task.getDueDay()).append("-").append(task.getDueYear())
	   			   .append(" "));
	      }

	      if(task.isCompleted()){
	          descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	          descriptionView.setChecked(true);
	      }else{
	          descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
	          descriptionView.setChecked(false);
	      }

	      return convertView;
	  }

	}