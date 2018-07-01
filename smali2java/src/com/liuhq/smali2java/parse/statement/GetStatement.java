package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Reference;
import com.liuhq.smali2java.bean.Var;

public class GetStatement extends Statement{

	private Var v1;
	private Reference ref;

	public GetStatement(String type, Var p1, Reference ref) {
		this.v1 = p1;
		this.ref = ref;
	}

	@Override
	public ArrayList<String> execute(Method method, ClassBean clas,ArrayList<String> blockCode) {
		
		boolean isRef = false;
		String name = v1.getName();
		Statement lastStatement = getLastStatement();
		if(lastStatement != null && lastStatement instanceof GetStatement)
		{
			GetStatement getStatement = (GetStatement)lastStatement;
			if(getStatement.getV1() == v1)
			{
				blockCode.remove(blockCode.size() -1);
				isRef = true;
			}
		}
		String code = name + " = " + ref.toString(isRef) +  ";";
		reuseSingleLine.set(0, code);
		v1.setData(ref);
		return reuseSingleLine;
	}
	
	public Var getV1() {
		return v1;
	}
}
