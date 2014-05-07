package com.curi;

import java.util.Vector;

public class List {
	public String name;
	public String color;
	public Vector<Task> tasks;

	public List(){
		super();
	}

	public List(String name, String color){
		super();
		this.name=name;
		this.color=color;
	}

	@Override
	public String toString(){
		return name + "\n" + color + "\n" ;
	}
}
