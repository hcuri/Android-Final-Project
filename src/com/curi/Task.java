package com.curi;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Task")
public class Task extends ParseObject{
	public Task(){
		
	}
	
	public boolean isCompleted(){
		return getBoolean("completed");
	}
	
	public void setCompleted(boolean complete){
		put("completed", complete);
	}
	
	public String getDescription(){
		return getString("description");
	}
	
	public void setDescription(String description){
		put("description", description);
	}

	public void setUser(ParseUser currentUser) {
		put("user", currentUser);
	}
	
	public void setDueYear(int year){
		put("dueYear", year);
	}
	
	public int getDueYear(){
		return getInt("dueYear");
	}
	
	public void setDueMonth(int month){
		put("dueMonth", month);
	}
	
	public int getDueMonth(){
		return getInt("dueMonth");
	}
	
	public void setDueDay(int day){
		put("dueDay", day);
	}
	
	public int getDueDay(){
		return getInt("dueDay");
	}
}
