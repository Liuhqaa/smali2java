package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.bean.OutClass;

public class ReturnStatement extends Statement {

	private String returnType;

	public ReturnStatement(String returnType,Var var) {
		this.returnType = returnType;
	}
	
	@Override
	public ArrayList<String> execute(Method method, ClassBean clas,ArrayList<String> blockCode) {
		String result = "";
		if(returnType.equals("return-void"))
		{
			if(method.getReturnType().isVoid())
			{
				result = "return ;";
			}else
			{
				result = "return x;";
			}
		}
		reuseSingleLine.set(0, result);
		return reuseSingleLine;
	}

}
