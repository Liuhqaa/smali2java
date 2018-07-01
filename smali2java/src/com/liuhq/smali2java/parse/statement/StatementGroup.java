package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.Const;
import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.parse.bean.OutClass;

public class StatementGroup extends Statement{
	
	private static final  ArrayList<Statement> EMPTY_LIST = new ArrayList<>(0);
	
	public static final StatementGroup EMPTY_OBJECT = new StatementGroup(EMPTY_LIST);
	
	private String tag;
	
	private ArrayList<Statement> statementGroup;

	private Statement lastState;
	
	public StatementGroup() {
	}
	
	public StatementGroup(ArrayList<Statement> statementGroup) {
		this.statementGroup = statementGroup;
	}

	public void setStatements(ArrayList<Statement> statementGroup) {
		this.statementGroup = statementGroup;
	}
	
	public void addStatement(Statement statement)
	{
		if(statementGroup == null)
		{
			statementGroup = new ArrayList<>();
		}
		statement.setLastStatement(lastState);
		statementGroup.add(statement);
		lastState = statement;
	}

	public ArrayList<Statement> getStatements() {
		return statementGroup == null ? EMPTY_LIST : statementGroup;
	}

	public String getType() {
		if(statementGroup != null && statementGroup.size() > 0)
		{
			return statementGroup.get(0).geType();
		}
		return "";
	}

	public Statement getHeadLine() {
		if(statementGroup != null && statementGroup.size() > 0)
		{
			return statementGroup.get(0);
		}
		return null;
	}

	public Statement removeHead() {
		if(statementGroup != null && statementGroup.size() > 0)
		{
			return statementGroup.remove(0);
		}
		return null;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public static StatementGroup createBlock(ArrayList<Statement> lines, int startIndex, int endIndex) {
		ArrayList<Statement> list = new ArrayList<>(endIndex - startIndex);
		for (int i = startIndex; i < endIndex; i++) {
			Statement statement = lines.get(i);
			statement.setIndex(list.size());
			list.add(statement);
		}
		return new StatementGroup(list);
	}
	
	public boolean isEmpty()
	{
		return statementGroup == null || statementGroup.isEmpty();
	}
	
	@Override
	public ArrayList<String> execute(Method method, ClassBean classBean,ArrayList<String> blockCode) {
		ArrayList<String> result = new ArrayList<>();
		ArrayList<Statement> statements = getStatements();
		for (Statement statement : statements) {
			ArrayList<String> codes = statement.execute(method,classBean,result);
			if(codes != null && codes.size() > 0)
			{
				result.addAll(codes);
			}
		}
		return result;
	}
}
