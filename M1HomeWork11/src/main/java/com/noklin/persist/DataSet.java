package com.noklin.persist;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_SET")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DATA_TYPE", discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class DataSet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id = -1;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public final static String ID = "id"; 
	public final static long EMPTY_ID = -1; 
	public final static Class<?> ID_TYPE = long.class;
	
}