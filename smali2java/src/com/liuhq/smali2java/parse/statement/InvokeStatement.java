package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Type;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.bean.OutClass;
import com.sun.org.apache.xpath.internal.operations.VariableSafeAbsRef;

public class InvokeStatement extends Statement {
	
	private String methodName;
	private Type classType;
	private ArrayList<Var> vars;
	private Var obj;
	private Type returnType;

	/**
	 * 
	 * invoke-static 是类静态方法的调用，编译时，静态确定的；
	 * invoke-virtual 虚方法调用，调用的方法运行时确认实际调用，和实例引用的实际对象有关，动态确认的，一般是带有修饰符protected或public的方法；
	 * invoke-direct 没有被覆盖方法的调用，即不用动态根据实例所引用的调用，编译时，静态确认的，一般是private或<init>方法；
	 * invoke-super 直接调用父类的虚方法，编译时，静态确认的。
	 * invokeinterface 调用接口方法，调用的方法运行时确认实际调用，即会在运行时才确定一个实现此接口的对象。
	 * 
	 * 
	 * **/

	public InvokeStatement(String invokeType, ArrayList<Var> vars, Type classType, String methodName, ArrayList<Type> types,
			Type returnType) {
		this.vars = vars;
		this.obj = vars.get(vars.size()-1);
		this.classType = classType;
		this.methodName = methodName;
		this.returnType = returnType;
	}

	@Override
	public ArrayList<String> execute(Method method,ClassBean clas,ArrayList<String> blockCode) {
		String code = "";
		if(methodName.equals("<init>"))
		{
			if(classType.equals(clas.getSuperClass()))
			{
				code = "super(" + getParams(method) +");";
			}
		}else
		{
			code = obj.getName() + "." + methodName + "(" + getParams(method) + ");";
		}
		reuseSingleLine.set(0, code);
		return reuseSingleLine;
	}

	private String getParams(Method method) {
		
		return "";
	}

	public Type getReturnType() {
		return returnType;
	}
	
}
