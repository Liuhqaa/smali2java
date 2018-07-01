package com.liuhq.smali2java.bean;

public class Data{
	
	private Type type;
	
	private String value;
	
	public Data() {
	}
	
	public Data(Type type) {
		this.type = type;
	}

	public Data(Type type, String value) {
		this.type = type;
		this.value = value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public String getStrValue()
	{
		return value;
	}
	
	public boolean getBooleanValue()
	{
		return true;
	}
	
	public int getIntValue()
	{
		return 0;
	}
	
	public float getFloatValue()
	{
		return 0;
	}

	public double getDoubleValue()
	{
		return 0;
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getClassName() {
		return type.getClassName();
	}

	public Data clone() {
		Data data = new Data (type,value);
		return data;
	}
	
	public String toString(boolean isRef) {
		return value;
	}

	public boolean isObject() {
		return type.isObject();
	}
}
