package com.liuhq.smali2java.bean;

import java.util.ArrayList;

public class Obj {
	
	//访问修饰符

	public static final int PROPERTY_DEFAULT = 0;
	public static final int PROPERTY_PUBLIC = 1;
	public static final int PROPERTY_PROTECTED = 2;
	public static final int PROPERTY_PRIVATE = 3;
	
	private boolean isStatic = false;
	
	private boolean isFinal = false;
	
	private boolean isSynthetic = false;//自动生成
	
	private int property = PROPERTY_DEFAULT;//访问修饰符
	
	private static final  ArrayList<Annotation> EMPTY = new ArrayList<>(0);

	private ArrayList<Annotation> annotations;
	
	private String name;

	
	public void addAnnotation(Annotation annotation)
	{
		if(annotations == null)
		{
			annotations = new ArrayList<>();
		}
		annotations.add(annotation);
	}

	public ArrayList<Annotation> getAnnotations() {
		return annotations == null ?EMPTY:annotations;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isSynthetic() {
		return isSynthetic;
	}

	public void setSynthetic(boolean isSynthetic) {
		this.isSynthetic = isSynthetic;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}
	
	public void setProperty(String property) {
		int pro = 0;
		switch (property) {
		case "public":
			pro = PROPERTY_PUBLIC;
			break;
		case "protected":
			pro = PROPERTY_PROTECTED;
			break;
		case "private":
			pro = PROPERTY_PRIVATE;
			break;
		default:
			pro = PROPERTY_DEFAULT;
			break;
		}
		this.property = pro;
	}
}
