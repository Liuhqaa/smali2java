package com.liuhq.smali2java.parse.bean;

import java.util.ArrayList;
import java.util.Iterator;

public class OutClass{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7831048202006974119L;
	
	private String grade = "";
	private ArrayList<String> codes = new ArrayList<String>();
	
	public OutClass() {
	}
	
	public ArrayList<String> getCodes() {
		return codes;
	}

	public boolean add(String code) {
		
		if(code.startsWith("}"))
		{
			subGrade();
		}
		code = grade + code;
		if(code.endsWith("{")|| code.startsWith("{")) {
			addGrade();
		}
		codes.add(code);
		return true;
	}
	
	private void subGrade() {
		grade = grade.substring(0, grade.length()-1);
	}

	private void addGrade() {
		grade += "\t";
	}


	public void addLines(ArrayList<String> lines) {
		for(String line : lines)
		{
			add(line);
		}
	}
	
	public void insertImports(Iterator<String> iterator) {
		ArrayList<String> impots = new ArrayList<String>();
		while(iterator.hasNext())
		{
			impots.add("import " + iterator.next() + ";");
		}
		impots.addAll(codes);
		codes = impots;
	}

	public String endLine() {
		return codes.get(codes.size() -1 );
	}
}
