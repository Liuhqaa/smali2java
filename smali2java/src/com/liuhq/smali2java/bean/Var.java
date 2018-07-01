package com.liuhq.smali2java.bean;

public class Var{
	
	private boolean invented = false;//虚拟变量 仅供P0 这虚拟的this 参数使用
	
	private String register;
	
	private Type type;
	
	private String name;
	
	private Data data;
	
	public Var(String register) {
		this(register,register);
	}
	
	public Var(String register,String name) {
		this.register = register;
		this.name = name;
	}
	
	public Var(String register,Type type) {
		this.register = register;
		this.name = register;
		this.type = type;
	}

	public Var(String register, String name,Type type,  Data data,boolean invented) {
		this.invented = invented;
		this.register = register;
		this.type = type;
		this.name = name;
		this.data = data;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}
	
	public boolean isInvented() {
		return invented;
	}

	public void setInvented(boolean invented) {
		this.invented = invented;
	}

	public String getClassName() {
		return type.getClassName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data.clone();
		this.type = data.getType();
	}

	public String toString(boolean isRef) {
		if(data == null || !isRef)
		{
			return name;
		}
		return data.toString(isRef);
	}
	
	public Var clone()
	{
		return new Var(register,name,type,data,invented);
	}

	public boolean isObject() {
		return type.isObject();
	}

	public boolean isBoolean() {
		return type.isBoolean();
	}
}
