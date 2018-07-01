package com.liuhq.smali2java.bean;

public class Field extends Obj{

	private Data var;

	public Field() {
	}

	public Field(String name, Data var) {
		setName(name);
		this.var = var;
	}

	public Data getVar() {
		return var;
	}

	public void setVar(Data var) {
		this.var = var;
	}

	public Type getType() {
		return var.getType();
	}
	
}
