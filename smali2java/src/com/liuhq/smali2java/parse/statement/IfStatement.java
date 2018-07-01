package com.liuhq.smali2java.parse.statement;

import java.util.ArrayList;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.bean.Label;
import com.liuhq.smali2java.parse.bean.OutClass;

public class IfStatement extends Statement {

	private Label endLabel;
	private String ifType;
	private Var v1;
	private Var v2;
	private StatementGroup ifBlock;
	private StatementGroup elBlock;

	public IfStatement(String ifType, Var v1, Var v2, StatementGroup ifBlock, StatementGroup elBlock) {

		this.ifType = ifType;
		this.v1 = v1;
		this.v2 = v2;
		this.ifBlock = ifBlock;
		this.elBlock = elBlock;
	}

	/**
	 * 
	 * "if-eq vA, vB, :cond_**" 如果vA等于vB则跳转到:cond_** "if-ne vA, vB, :cond_**"
	 * 如果vA不等于vB则跳转到:cond_** "if-lt vA, vB, :cond_**" 如果vA小于vB则跳转到:cond_** "if-ge
	 * vA, vB, :cond_**" 如果vA大于等于vB则跳转到:cond_** "if-gt vA, vB, :cond_**"
	 * 如果vA大于vB则跳转到:cond_** "if-le vA, vB, :cond_**" 如果vA小于等于vB则跳转到:cond_** "if-eqz
	 * vA, :cond_**" 如果vA等于0则跳转到:cond_** "if-nez vA, :cond_**" 如果vA不等于0则跳转到:cond_**
	 * "if-ltz vA, :cond_**" 如果vA小于0则跳转到:cond_** "if-gez vA, :cond_**"
	 * 如果vA大于等于0则跳转到:cond_** "if-gtz vA, :cond_**" 如果vA大于0则跳转到:cond_** "if-lez vA,
	 * :cond_**" 如果vA小于等于0则跳转到:cond_
	 * 
	 * @return **
	 * 
	 **/

	// isDir = true 正向逻辑
	// isDir = false 反向逻辑
	private String getIfType(boolean isDir) {
		String result = "";
		switch (ifType) {
		case "if-eq":
		case "if-eqz":
			result = isDir ? "=" : "!=";
			break;
		case "if-ne":
		case "if-nez":
			result = isDir ? "!=" : "=";
			break;
		case "if-le":
		case "if-lez":
			result = isDir ? "<=" : ">";
			break;
		case "if-ge":
		case "if-gez":
			result = isDir ? ">=" : "<";
			break;
		case "if-lt":
		case "if-ltz":
			result = isDir ? "<" : ">=";
			break;
		case "if-gt":
		case "if-gtz":
			result = isDir ? ">" : "<=";
			break;
		default:
			System.out.println("error --------getIfType : " + ifType);
			break;
		}
		return result;
	}

	public void setEndLabel(Label endLabel) {
		this.endLabel = endLabel;
	}

	public Label getEndLabel() {
		return endLabel;
	}

	public int getEndIndex() {
		return endLabel.getOldIndex();
	}

	public StatementGroup getIfBlock() {
		return ifBlock;
	}

	public StatementGroup getElBlock() {
		return elBlock;
	}

	public void setIfBlock(StatementGroup ifBlock) {
		this.ifBlock = ifBlock;
	}

	public void setElBlock(StatementGroup elBlock) {
		this.elBlock = elBlock;
	}

	@Override
	public ArrayList<String> execute(Method method, ClassBean classBean, ArrayList<String> blockCode) {

		ArrayList<String> codes = new ArrayList<String>();
		if (ifBlock == null || ifBlock.isEmpty()) {
			codes.add("if(" + getContent(true) + ") {");
			codes.addAll(elBlock.execute(method, classBean, codes));
			codes.add("}");
			return codes;
		}

		codes.add("if(" + getContent(false) + ") {");
		codes.addAll(ifBlock.execute(method, classBean, codes));
		if (elBlock == null || elBlock.isEmpty()) {
			codes.add("}");
		} else {
			codes.add("} else {");
			codes.addAll(elBlock.execute(method, classBean, codes));
			codes.add("}");
		}
		return codes;
	}

	private String getContent(boolean isDir) {
		String var1 = v1.getName();
		String var2 = null;
		if (v2 == null) {
			if(v1.isBoolean())
			{
				var2 = "flase";
			}else
			{
				var2 = v1.isObject() ? "null" : "0";
			}
		} else {
			var2 = v2.getName();
		}
		return var1 + " " + getIfType(isDir) + " " + var2;
	}
}
