package com.curi;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
}



//public class ToDo {
//	public String name;
//	public boolean completed;
//	public int year;
//	public int month;
//	public int day;
//	public int hour;
//	public int minute;
//
//
//	public ToDo(){
//		super();
//	}
//
//	public ToDo(String name, boolean completed, int year, int month, int day,
//			int hour, int minute) {
//		super();
//		this.name = name;
//		this.completed = completed;
//		this.year = year;
//		this.month = month;
//		this.day = day;
//		this.hour = hour;
//		this.minute = minute;
//	}
//
//	@Override
//	public String toString() {
//		return "ToDo [name=" + name + ", completed=" + completed + ", year="
//				+ year + ", month=" + month + ", day=" + day + ", hour=" + hour
//				+ ", minute=" + minute + "]";
//	}
//}
