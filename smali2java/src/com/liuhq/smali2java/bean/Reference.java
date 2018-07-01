package com.liuhq.smali2java.bean;

public class Reference extends Data{

	private Var obj;
	private Type objType;
	private String dataName;

	public Reference(Var var, String str,ClassBean classBean) {
		this.obj = var;
		String[] split = str.split("->");
		objType = new Type(split[0],classBean);
		
		String[] arr = split[1].split(":");
		dataName = arr[0];
		setType(new Type(arr[1],classBean));
	}

	public Reference(Var obj, Type objType, String dataName) {
		this.obj = obj;
		this.objType = objType;
		this.dataName = dataName;
	}

	
	public String toString(boolean isRef) {
		return obj.toString(isRef)+ "." + dataName;
	}
	
	@Override
	public Data clone() {
		Reference ref = new Reference(obj.clone(),objType,dataName);
		ref.setType(getType());
		ref.setValue(getValue());
		return ref;
	}
}
