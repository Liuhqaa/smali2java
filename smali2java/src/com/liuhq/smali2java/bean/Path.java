package com.liuhq.smali2java.bean;

public class Path {

	private String path;
	private String className;

	public Path(String context,ClassBean clas) {
		context = context.substring(1,context.length()-1);
		int lastIndex = context.lastIndexOf('/');
		className = lastIndex < 0?context:context.substring(lastIndex+1);
		path = context.replace('/', '.');
		clas.addImport(path);
	}

	public String getPath() {
		return path;
	}

	public String getClassName() {
		return className;
	}
	
	@Override
	public String toString() {
		return path;
	}
}
