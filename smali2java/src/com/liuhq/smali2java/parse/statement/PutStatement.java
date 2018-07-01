package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Reference;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.bean.OutClass;

public class PutStatement extends Statement {

	private Var p1;
	private Reference ref;

	public PutStatement(String putType,Var p1, Reference ref) {
		this.p1 = p1;
		this.ref = ref;
	}

	@Override
	public ArrayList<String> execute(Method method, ClassBean clas,ArrayList<String> blockCode) {
		p1.setData(ref);
		String code = ref.toString(false) + " = " + p1.getName() +  ";";
		reuseSingleLine.set(0, code);
		return reuseSingleLine;
	}
}
