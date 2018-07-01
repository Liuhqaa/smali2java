package com.liuhq.smali2java.bean;

public class Type {
	
	private static final String[] TYPE_NAMES = new String[] {"void","boolean","byte","short","char","int","long","float","double","Object"};

	public static final int TYPE_VOID = 0;
	public static final int TYPE_BOOLEAN = 1;//1 的值是ture 0 值是false
	public static final int TYPE_BYTE = 2;
	public static final int TYPE_SHORT = 3;
	public static final int TYPE_CHAR = 4;
	public static final int TYPE_INT = 5;
	public static final int TYPE_LONG = 6;
	public static final int TYPE_FLOAT = 7;
	public static final int TYPE_DOUBLE = 8;
	public static final int TYPE_OBJ = 9;
	
	private boolean isArray = false;
	
	private Path path;
	private String calssName;
	private int type = TYPE_BOOLEAN;

	public Type(int type) {
		this.type = type;
		if(type != TYPE_OBJ)
		{
			calssName = TYPE_NAMES[type];
		}
	}

	public Type(String str,ClassBean classBean) {
		if(str.startsWith("["))
		{
			isArray = true;
			str = str.substring(1);
		}
		
		if (str.startsWith("L") && str.endsWith(";")) {
			this.type = TYPE_OBJ;
			this.path = new Path(str,classBean);
			calssName = path.getClassName();
		} else {
			this.type = getType(str);
			calssName = TYPE_NAMES[type];
		}
	}

	private int getType(String str) {
		int type = 0;
		switch (str.toUpperCase()) {
		case "V":
			type = TYPE_VOID;
			break;
		case "Z":
			type = TYPE_BOOLEAN;
			break;
		case "B":
			type = TYPE_BYTE;
			break;
		case "S":
			type = TYPE_SHORT;
			break;
		case "C":
			type = TYPE_CHAR;
			break;
		case "I":
			type = TYPE_INT;
			break;
		case "J":
			type = TYPE_LONG;
			break;
		case "F":
			type = TYPE_FLOAT;
			break;
		case "D":
			type = TYPE_DOUBLE;
			break;
		default:
			type = TYPE_VOID;
			break;
		}
		return type;
	}

	public String getClassName() {
		return calssName;
	}

	public Path getPath() {
		return path;
	}
	
	public String getPathName()
	{
		return path.getPath();
	}

	public boolean isObject() {
		return type == TYPE_OBJ;
	}

	public boolean isVoid() {
		return type == TYPE_VOID;
	}
	
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
	
	@Override
	public String toString() {
		return type == TYPE_OBJ?path.getPath():calssName;
	}

	public boolean isBoolean() {
		return type == TYPE_BOOLEAN;
	}
}
