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
	 * invoke-static ���ྲ̬�����ĵ��ã�����ʱ����̬ȷ���ģ�
	 * invoke-virtual �鷽�����ã����õķ�������ʱȷ��ʵ�ʵ��ã���ʵ�����õ�ʵ�ʶ����йأ���̬ȷ�ϵģ�һ���Ǵ������η�protected��public�ķ�����
	 * invoke-direct û�б����Ƿ����ĵ��ã������ö�̬����ʵ�������õĵ��ã�����ʱ����̬ȷ�ϵģ�һ����private��<init>������
	 * invoke-super ֱ�ӵ��ø�����鷽��������ʱ����̬ȷ�ϵġ�
	 * invokeinterface ���ýӿڷ��������õķ�������ʱȷ��ʵ�ʵ��ã�����������ʱ��ȷ��һ��ʵ�ִ˽ӿڵĶ���
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
