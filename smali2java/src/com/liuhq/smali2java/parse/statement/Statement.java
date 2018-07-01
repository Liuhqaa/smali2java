package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;

public class Statement {
	
	public static final ArrayList<String> reuseSingleLine = new ArrayList<>(1);
	static {
		reuseSingleLine.add("");
	}
	
	private Statement lastStatement;
	private String content;
	private int index;//当前语句在方法块中的下标
	
	public Statement() {
		
	}

	public Statement(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String geType() {
		return content.split(" ",2)[0];
	}

	public Statement getLastStatement() {
		return lastStatement;
	}

	public void setLastStatement(Statement lastStatement) {
		this.lastStatement = lastStatement;
	}

	public boolean startsWith(String prefix) {
		return content.startsWith(prefix);
	}
	
	@Override
	public String toString() {
		return content;
	}

	public String[] split(String regex) {
		return content.split(regex);
	}
	
	public boolean equals(Statement statement) {
		return toString().equals(statement.toString());
	}

	public String substring(int beginIndex) {
		return content.substring(beginIndex);
	}
	
	public String substring(int beginIndex, int endIndex) {
		return content.substring(beginIndex, endIndex);
	}

	public int indexOf(char ch) {
		return content.indexOf(ch);
	}

	public ArrayList<String> execute(Method method,ClassBean classBean,ArrayList<String> blockCode) {
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int line) {
		this.index = line;
	}

}
