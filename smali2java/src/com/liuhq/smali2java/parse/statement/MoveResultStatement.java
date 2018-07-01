package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Type;
import com.liuhq.smali2java.bean.Var;

public class MoveResultStatement extends Statement{

	private Var var;
	
	public MoveResultStatement(String type, Var var) {
		this.var = var;
	}

	@Override
	public ArrayList<String> execute(Method method, ClassBean classBean,ArrayList<String> blockCode) {
		
		String endline = blockCode.remove(blockCode.size() - 1);
		String code = var.getName() + " = " +  endline;
		Statement lastStatement = getLastStatement();
		if(lastStatement instanceof InvokeStatement)
		{
			InvokeStatement invokeStatement = (InvokeStatement)lastStatement;
			Type returnType = invokeStatement.getReturnType();
			var.setType(returnType);
		}
		reuseSingleLine.set(0, code);
		return reuseSingleLine;
	}
}
