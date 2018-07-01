package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Data;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Type;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.bean.OutClass;

public class ConstStatement extends Statement{

	private String type;
	private Var var;
	private String value;

	public ConstStatement(String type, Var var, String value) {
		this.type = type;
		this.var = var;
		this.value = value;
	}

	@Override
	public ArrayList<String> execute(Method method,ClassBean clas,ArrayList<String> blockCode) {
		var.setData(new Data(new Type(Type.TYPE_INT),value));
		return null;
	}
}
