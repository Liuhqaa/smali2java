package com.liuhq.smali2java.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.liuhq.smali2java.parse.statement.Statement;

public class Method extends Obj{
	
	private boolean isConstructor = false;
	private Type returnType;
	private HashMap<String, Var> params;
	private HashMap<String, Var> locals;

	private ArrayList<Statement> statements = new ArrayList();

	public void addParam(Var param)
	{
		if(params == null)
		{
			params = new HashMap<>();
		}
		params.put(param.getRegister(), param);
	}
	
	public void addLocal(Var local)
	{
		if(locals == null)
		{
			locals = new HashMap<>();
		}
		locals.put(local.getRegister(), local);
	}
	
	public HashMap<String, Var> getParams() {
		return params;
	}

	public Var getVar(String name)
	{
		Var var = params.get(name);
		if(var == null && locals != null)
		{
			var = locals.get(name);
		}
		return var;
	}
	
	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}
	
	public void addStatement(Statement statement)
	{
		statements.add(statement);
	}

	public ArrayList<Statement> getStatements() {
		return statements;
	}

	public void setStatements(ArrayList<Statement> statements) {
		this.statements = statements;
	}
}
