package com.noklin.persist;

public abstract class DataSet {
	private long id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public final static String ID = "id"; 
}