package com.liuhq.smali2java.parse.bean;

import java.util.ArrayList;

import com.liuhq.smali2java.parse.statement.Statement;
import com.liuhq.smali2java.parse.statement.StatementGroup;

public class BlockGroup  extends StatementGroup{

	private static final  ArrayList<Statement> EMPTY = new ArrayList<>(0);
	
	private ArrayList<Statement> codes;
	
	public void addCode(Statement code)
	{
		if(codes == null)
		{
			codes = new ArrayList<>();
		}
		codes.add(code);
	}

	public ArrayList<Statement> getCodes() {
		
		return codes == null ? EMPTY:codes;
	}

	public void setCodes(ArrayList<Statement> codes) {
		this.codes = codes;
	}
}
