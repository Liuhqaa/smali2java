package com.liuhq.smali2java.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClassBean extends Obj {
	
	private Type current;
	private Type superClass;
	private HashMap<String,Path> implementMap = new HashMap<>();
	private ArrayList<Field> fields = new ArrayList<>();
	private ArrayList<Method> methods = new ArrayList<>();	
	private HashSet<String> importSet = new HashSet<>();
	private String source;
	
	public boolean parseError = false;
	
	public void addimplements(Path path)
	{
		implementMap.put(path.getPath(), path);
	}

	public HashMap<String, Path> getImplementList() {
		return implementMap;
	}

	
	public HashSet<String> getImportSet() {
		return importSet;
	}

	public void addField(Field field) {
		fields.add(field);
	}

	public void addMethod(Method method) {
		methods.add(method);
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

	public Type getSuperClass() {
		return superClass;
	}

	public void setSuperClass(Type superClass) {
		this.superClass = superClass;
	}

	public void setCurrent(Type current) {
		this.current = current;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getName() {
		return current.getClassName();
	}

	public void addImport(String path) {
		importSet.add(path);
	}
}
