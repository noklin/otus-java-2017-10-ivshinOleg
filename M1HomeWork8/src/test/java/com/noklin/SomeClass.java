package com.noklin;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SomeClass {

	public void init(){
		booleanArray = new Boolean[]{true,true,null,null};
		stringCollection = Arrays.asList("123","123","123","123", null);
		someMap = new HashMap<>(); 
		someMap.put("a", 2);
		someMap.put("b", 3);
	}
	
	private byte byteField;
	private short shortField;
	private char charField;
	private int intField;
	private long longField;
	private float floatField;
	private double doubleField;
	private boolean booleanField;
	private Byte byteFieldW = 1;
	private Short shortFieldW = 1;
	private Character charFieldW = 1;
	private Integer intFieldW = 1;
	private Long longFieldW = 1l;
	private Float floatFieldW = 1f;
	private Double doubleFieldW = 3.14;
	private Boolean booleanFieldW = false;
	private String stringField = "some text"; 
	private Boolean[] booleanArray;
	private Collection<String> stringCollection;
	private Map<String,Integer> someMap;
	@SuppressWarnings("unused")
	private transient int transientField;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(booleanArray);
		result = prime * result + (booleanField ? 1231 : 1237);
		result = prime * result + ((booleanFieldW == null) ? 0 : booleanFieldW.hashCode());
		result = prime * result + byteField;
		result = prime * result + ((byteFieldW == null) ? 0 : byteFieldW.hashCode());
		result = prime * result + charField;
		result = prime * result + ((charFieldW == null) ? 0 : charFieldW.hashCode());
		long temp;
		temp = Double.doubleToLongBits(doubleField);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((doubleFieldW == null) ? 0 : doubleFieldW.hashCode());
		result = prime * result + Float.floatToIntBits(floatField);
		result = prime * result + ((floatFieldW == null) ? 0 : floatFieldW.hashCode());
		result = prime * result + intField;
		result = prime * result + ((intFieldW == null) ? 0 : intFieldW.hashCode());
		result = prime * result + (int) (longField ^ (longField >>> 32));
		result = prime * result + ((longFieldW == null) ? 0 : longFieldW.hashCode());
		result = prime * result + shortField;
		result = prime * result + ((shortFieldW == null) ? 0 : shortFieldW.hashCode());
		result = prime * result + ((someMap == null) ? 0 : someMap.hashCode());
		result = prime * result + ((stringCollection == null) ? 0 : stringCollection.hashCode());
		result = prime * result + ((stringField == null) ? 0 : stringField.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SomeClass other = (SomeClass) obj;
		if (!Arrays.equals(booleanArray, other.booleanArray))
			return false;
		if (booleanField != other.booleanField)
			return false;
		if (booleanFieldW == null) {
			if (other.booleanFieldW != null)
				return false;
		} else if (!booleanFieldW.equals(other.booleanFieldW))
			return false;
		if (byteField != other.byteField)
			return false;
		if (byteFieldW == null) {
			if (other.byteFieldW != null)
				return false;
		} else if (!byteFieldW.equals(other.byteFieldW))
			return false;
		if (charField != other.charField)
			return false;
		if (charFieldW == null) {
			if (other.charFieldW != null)
				return false;
		} else if (!charFieldW.equals(other.charFieldW))
			return false;
		if (Double.doubleToLongBits(doubleField) != Double.doubleToLongBits(other.doubleField))
			return false;
		if (doubleFieldW == null) {
			if (other.doubleFieldW != null)
				return false;
		} else if (!doubleFieldW.equals(other.doubleFieldW))
			return false;
		if (Float.floatToIntBits(floatField) != Float.floatToIntBits(other.floatField))
			return false;
		if (floatFieldW == null) {
			if (other.floatFieldW != null)
				return false;
		} else if (!floatFieldW.equals(other.floatFieldW))
			return false;
		if (intField != other.intField)
			return false;
		if (intFieldW == null) {
			if (other.intFieldW != null)
				return false;
		} else if (!intFieldW.equals(other.intFieldW))
			return false;
		if (longField != other.longField)
			return false;
		if (longFieldW == null) {
			if (other.longFieldW != null)
				return false;
		} else if (!longFieldW.equals(other.longFieldW))
			return false;
		if (shortField != other.shortField)
			return false;
		if (shortFieldW == null) {
			if (other.shortFieldW != null)
				return false;
		} else if (!shortFieldW.equals(other.shortFieldW))
			return false;
		if (someMap == null) {
			if (other.someMap != null)
				return false;
		} else if (!someMap.equals(other.someMap))
			return false;
		if (stringCollection == null) {
			if (other.stringCollection != null)
				return false;
		} else if (!stringCollection.equals(other.stringCollection))
			return false;
		if (stringField == null) {
			if (other.stringField != null)
				return false;
		} else if (!stringField.equals(other.stringField))
			return false;
		return true;
	}
}