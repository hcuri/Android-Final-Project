package com.curi;

public class ToDo {
	public String name;
	public boolean completed;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;


	public ToDo(){
		super();
	}

	public ToDo(String name, boolean completed, int year, int month, int day,
			int hour, int minute) {
		super();
		this.name = name;
		this.completed = completed;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	@Override
	public String toString() {
		return "ToDo [name=" + name + ", completed=" + completed + ", year="
				+ year + ", month=" + month + ", day=" + day + ", hour=" + hour
				+ ", minute=" + minute + "]";
	}
}
