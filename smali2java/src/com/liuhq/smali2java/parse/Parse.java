package com.liuhq.smali2java.parse;

import java.util.ArrayList;
import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Field;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Obj;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.bean.Path;
import com.liuhq.smali2java.bean.Reference;
import com.liuhq.smali2java.bean.Type;
import com.liuhq.smali2java.bean.Data;
import com.liuhq.smali2java.parse.bean.Line;
import com.liuhq.smali2java.parse.bean.MethodInfo;
import com.liuhq.smali2java.parse.bean.OutClass;
import com.liuhq.smali2java.parse.bean.SmaliInfo;
import com.liuhq.smali2java.parse.statement.ConstStatement;
import com.liuhq.smali2java.parse.statement.GetStatement;
import com.liuhq.smali2java.parse.statement.IfStatement;
import com.liuhq.smali2java.parse.statement.InvokeStatement;
import com.liuhq.smali2java.parse.statement.MoveResultStatement;
import com.liuhq.smali2java.parse.statement.PutStatement;
import com.liuhq.smali2java.parse.statement.ReturnStatement;
import com.liuhq.smali2java.parse.statement.Statement;
import com.liuhq.smali2java.parse.statement.StatementGroup;

public class Parse {

	public static ClassBean parseSmali(String samliPath) {
		SmaliInfo smali = new SmaliInfo(samliPath);
		return smali2Class(smali);
	}

	private static ClassBean smali2Class(SmaliInfo smali) {
		ClassBean clas = new ClassBean();
		ArrayList<Statement> statements = smali.getStatements();
		for (Statement statement : statements) {
			if (statement instanceof StatementGroup) {
				parseBlock((StatementGroup) statement, clas);
			} else {
				parseLine(statement, clas);
			}
		}
		return clas;
	}

	private static void parseBlock(StatementGroup statementGroup, ClassBean clas) {
		switch (statementGroup.getType()) {
		case ".method":
			Method method = parseMethod(statementGroup, clas);
			clas.addMethod(method);
			break;
		case ".annotation":
			parseAnnotation(statementGroup, clas);
			break;
		case ".field":
			Field field = parseField(statementGroup, clas);
			clas.addField(field);
			break;
		default:
			System.out.println("parseBlock: " + statementGroup.getHeadLine());
			clas.parseError = true;
			break;
		}
	}

	private static Field parseField(StatementGroup statementGroup, ClassBean clas) {
		Field field = new Field();
		Statement head = statementGroup.removeHead();
		initField(head, field, clas);
		ArrayList<Statement> lines = statementGroup.getStatements();
		if (!lines.get(0).startsWith(".annotation") || !lines.get(lines.size() - 1).equals(".end annotation")) {
			System.out.println("parseField: " + head);
			clas.parseError = true;
			return field;
		}
		StatementGroup annoBlock = new StatementGroup(lines);
		parseAnnotation(annoBlock, field);
		return field;
	}

	private static boolean parseAnnotation(StatementGroup block, Obj obj) {
		// ArrayList<String> lines = block.getLines();
		// printArray(lines);
		return true;
	}

	private static Method parseMethod(StatementGroup block, ClassBean clas) {
		Method method = new Method();
		initMethod(block.getHeadLine(), method, clas);
		MethodInfo methodInfo = new MethodInfo(block);
		StatementGroup statementGroup = parseStatementGroup(clas, method, methodInfo, block, 1);
		method.setStatements(statementGroup.getStatements());
		return method;
	}

	private static IfStatement parseIfStatement(ClassBean clas, Method method, MethodInfo methodInfo, int index) {
		IfStatement ifStatement = methodInfo.getIfStatement(index,method);
		StatementGroup ifBlock = parseStatementGroup(clas, method, methodInfo, ifStatement.getIfBlock(), 0);
		StatementGroup elBlock = parseStatementGroup(clas, method, methodInfo, ifStatement.getElBlock(), 0);
		ifStatement.setIfBlock(ifBlock);
		ifStatement.setElBlock(elBlock);
		return ifStatement;
	}

	private static StatementGroup parseStatementGroup(ClassBean clas, Method method, MethodInfo methodInfo,
			StatementGroup statementGroup, int startIndex) {
		if (statementGroup.isEmpty()) {
			return statementGroup;
		}
		StatementGroup result = new StatementGroup();
		ArrayList<Statement> statements = statementGroup.getStatements();
		int size = statements.size();
		for (int i = startIndex; i < size; i++) {
			Statement line = statements.get(i);
			Statement statement = null;
			if (line.startsWith("return")) {
				String[] arr = line.split(" ");
				result.addStatement(new ReturnStatement(arr[0], null));
				break;
			} else if (line.startsWith(":")) {
				continue;
			} else if (line.startsWith("if-")) {
				IfStatement ifState = parseIfStatement(clas, method, methodInfo, line.getIndex());
				i = getEndIndex(methodInfo, ifState.getEndIndex(), statements);
				statement = ifState;
			} else {
				statement = parseStatement(line.toString(), method, clas);
			}
			if (statement != null) {
				result.addStatement(statement);
			}
		}
		return result;
	}

	private static int getEndIndex(MethodInfo methodInfo, int endIndex, ArrayList<Statement> statements) {
		int size = statements.size();
		for (int i = 0; i < size; i++) {
			Statement statement = statements.get(i);
			if (statement.getIndex() == endIndex) {
				return i;
			}
		}
		return size;
	}

	private static Statement parseStatement(String line, Method method, ClassBean clas) {
		String[] arr = line.split(" ");
		Statement statement = null;
		switch (arr[0]) {
		case "sput":
		case "sput-boolean":
		case "sput-byte":
		case "sput-char":
		case "sput-short":
		case "sput-wide":
		case "sput-object":
		case "aput":
		case "aput-boolean":
		case "aput-byte":
		case "aput-char":
		case "aput-short":
		case "aput-wide":
		case "aput-object":
		case "iput":
		case "iput-boolean":
		case "iput-byte":
		case "iput-char":
		case "iput-short":
		case "iput-wide":
		case "iput-object":
			statement = parsePut(arr, method, clas);
			break;
		case "aget":
		case "aget-boolean":
		case "aget-byte":
		case "aget-char":
		case "aget-short":
		case "aget-wide":
		case "aget-object":
		case "sget":
		case "sget-boolean":
		case "sget-byte":
		case "sget-char":
		case "sget-short":
		case "sget-wide":
		case "sget-object":
		case "iget":
		case "iget-boolean":
		case "iget-byte":
		case "iget-char":
		case "iget-short":
		case "iget-wide":
		case "iget-object":
			statement = parseGet(arr, method, clas);
			break;
		case "invoke-super":
		case "invoke-super/range":
		case "invoke-interface":
		case "invoke-interface/range":
		case "invoke-direct":
		case "invoke-direct/range":
		case "invoke-virtual":
		case "invoke-virtual/range":
		case "invoke-static":
		case "invoke-static/range":
			statement = parseInvoke(line, method, clas);
			break;
		case "new-instance":
			break;
		case "rsub-int":
		case "rsub-int/lit8":
		case "sub-int":
		case "sub-int/2addr":
		case "sub-long":
		case "sub-long/2addr":
		case "sub-float":
		case "sub-float/2addr":
		case "sub-double":
		case "sub-double/2addr":
			break;
		case "add-int":
		case "add-int/lit8":
		case "add-int/lit16":
		case "add-int/2addr":
		case "add-long":
		case "add-long/2addr":
		case "add-float":
		case "add-float/2addr":
		case "add-double":
		case "add-double/2addr":
			break;
		case "div-int":
		case "div-int/lit8":
		case "div-int/lit16":
		case "div-int/2addr":
		case "div-long":
		case "div-long/2addr":
		case "div-float":
		case "div-float/2addr":
		case "div-double":
		case "div-double/2addr":
			break;
		case "mul-int":
		case "mul-int/lit8":
		case "mul-int/lit16":
		case "mul-int/2addr":
		case "mul-long":
		case "mul-long/2addr":
		case "mul-float":
		case "mul-float/2addr":
		case "mul-double":
		case "mul-double/2addr":
			break;
		case "and-int":
		case "and-int/lit8":
		case "and-int/lit16":
		case "and-int/2addr":
		case "and-long":
		case "and-long/2addr":
			break;

		case "shr-int":
		case "shr-int/lit8":
		case "shr-int/2addr":
		case "shr-long":
		case "shr-long/2addr":
		case "ushr-int":
		case "ushr-int/lit8":
		case "ushr-int/2addr":
		case "ushr-long":
		case "ushr-long/2addr":
			break;
		case "shl-int":
		case "shl-int/lit8":
		case "shl-int/2addr":
		case "shl-long":
		case "shl-long/2addr":
			break;
		case "or-int":
		case "or-int/lit8":
		case "or-int/lit16":
		case "or-int/2addr":
		case "or-long":
		case "or-long/2addr":
		case "xor-int":
		case "xor-int/lit8":
		case "xor-int/lit16":
		case "xor-int/2addr":
		case "xor-long":
		case "xor-long/2addr":

			break;
		case "move":
		case "move/from16":
		case "move-wide":
		case "move-wide/from16":
		case "move-object":
		case "move-object/from16":
		case "move-exception":
			break;
		case "move-result":
		case "move-result-wide":
		case "move-result-object":
			statement = parseMoveResult(arr, method, clas);
			break;
		case "const":
		case "const/4":
		case "const/16":
		case "const/high16":
		case "const-wide":
		case "const-wide/16":
		case "const-wide/high16":
		case "const-wide/32":
		case "const-string":
		case "const-string/jumbo":
		case "const-class":
			statement = parseConst(arr, method, clas);
			break;

		case "cmp-long":
		case "cmpl-float":
		case "cmpg-float":
		case "cmpl-double":
		case "cmpg-double":
			break;
		case "neg-int":
		case "neg-long":
		case "neg-float":
		case "neg-double":
			break;
		case "rem-int":
		case "rem-int/lit8":
		case "rem-int/lit16":
		case "rem-int/2addr":
		case "rem-long":
		case "rem-long/2addr":
		case "rem-float":
		case "rem-float/2addr":
		case "rem-double":
		case "rem-double/2addr":
			break;
		case "instance-of":
			break;
		case "array-length":
		case "new-array":
		case "filled-new-array":
		case "fill-array-data":
			break;
		case "check-cast":
			break;
		case "throw":
		case ".catch":
		case ".catchall":
			break;
		case "int-to-byte":
		case "int-to-char":
		case "int-to-short":
		case "int-to-long":
		case "int-to-float":
		case "int-to-double":
		case "long-to-int":
		case "long-to-float":
		case "long-to-double":
		case "float-to-int":
		case "float-to-long":
		case "float-to-double":
		case "double-to-int":
		case "double-to-long":
		case "double-to-float":
			break;
		case ".local":
			break;
		case ".locals":
			parseLocals(arr, method, clas);
			break;
		case ".end":
		case ".restart":
			break;
		case "goto":
		case "goto/16":
		case "goto/32":
			parseGoto("", method);
			break;
		case ".prologue":
		case ".line":
			break;
		case "return":
		case "return-void":
		case "return-wide":
		case "return-object":
			break;

		case "monitor-enter":
		case "monitor-exit":
			break;

		case ".param":
			break;
		case "packed-switch":
		case ".packed-switch":
		case "sparse-switch":
		case ".sparse-switch":
			break;
		case "nop":
			break;
		case "if-eq":
		case "if-eqz":
		case "if-ne":
		case "if-nez":
		case "if-le":
		case "if-lez":
		case "if-ge":
		case "if-gez":
		case "if-lt":
		case "if-ltz":
		case "if-gt":
		case "if-gtz":
			parseIf(arr, method, clas);
			break;
		default:
			if (!arr[0].startsWith(":")) {
				System.out.println("parseStatement : " + arr[0]);
				clas.parseError = true;
			}
			break;
		}

		return statement;
	}

	private static Statement parseMoveResult(String[] arr, Method method, ClassBean clas) {
		MoveResultStatement statement = new MoveResultStatement(arr[0],method.getVar(arr[1]));
		return statement;
	}

	private static Statement parseGet(String[] arr, Method method, ClassBean clas) {
		Var p1 = method.getVar(endSubStr(arr[1]));
		Reference ref = new Reference(method.getVar(endSubStr(arr[2])), arr[3], clas);
		GetStatement statement = new GetStatement(arr[0], p1, ref );
		return statement;
	}

	private static String endSubStr(String str) {
		return str.substring(0, str.length() -1);
	}

	private static Statement parseInvoke(String line, Method method, ClassBean clas) {
		line = line.replace("},", "}");
		String[] arr = line.split("[{}]");
		if (arr.length != 3) {
			System.out.println("parseInvoke: " + arr[1]);
		}
		String params = arr[1];
		String[] strVars = params.split(", ");
		ArrayList<Var> vars = new ArrayList<>(strVars.length);
		for (String str : strVars) {
			vars.add(method.getVar(str));
		}
		String[] tmpe = arr[2].trim().split("[()]");
		String[] invokes = tmpe[0].split("->");
		Type classType = new Type(invokes[0],clas);
		String methodName = invokes[1];
		ArrayList<Type> types = getParamTypes(tmpe[1],clas);
		Type returnType = new Type(tmpe[2],clas);
		return new InvokeStatement(arr[0].trim(), vars, classType, methodName, types, returnType);
	}

	private static Statement parseConst(String[] arr, Method method, ClassBean clas) {
		if (arr.length != 3) {
			return null;
		}
		Var var = method.getVar(endSubStr(arr[1]));
		String value = arr[2];
		return new ConstStatement(arr[0], var, value);
	}

	private static PutStatement parsePut(String[] arr, Method method, ClassBean clas) {
		if (arr.length != 4) {
			System.out.println("parsePut: " + arr[3]);
		}
		Reference ref = new Reference(method.getVar(endSubStr(arr[2])),arr[3],clas);
		return new PutStatement(arr[0], method.getVar(endSubStr(arr[1])),ref);

	}

	private static void parseLocals(String[] arr, Method method, ClassBean clas) {
		int count = Integer.valueOf(arr[1]);
		for (int i = 0; i < count; i++) {
			method.addLocal(new Var("v" + Integer.toString(i)));
		}
	}

	private static boolean parseGoto(String str, Method method) {
		return false;
	}

	private static boolean parseIf(String[] str, Method method, ClassBean clas) {

		return false;
	}

	private static void initMethod(Statement head, Method method, ClassBean clas) {
		String[] hArray = head.split("[()]");
		if (hArray.length != 3) {
			System.out.println("initMethod: " + head);
			clas.parseError = true;
			return;
		}
		String[] arr = hArray[0].split(" ");
		int end = arr.length - 1;
		for (int i = 1; i < end; i++) {
			if (parseObj(arr[i], method)) {
				continue;
			}
			switch (arr[i]) {
			case "constructor":
				method.setConstructor(true);
				break;
			default:
				System.out.println("initMethod : " + head);
				clas.parseError = true;
				break;
			}
		}
		int pIndex = 0;
		if (!method.isStatic()) {
			Var var = new Var("p" + pIndex++, "this");
			var.setInvented(true);
			method.addParam(var);
		}

		method.setName(method.isConstructor() ? clas.getName() : arr[end]);
		String params = hArray[1];
		ArrayList<Type> types = getParamTypes(params,clas);
		for (Type type : types) {
			method.addParam(new Var("p" + pIndex++, type));
		}
		method.setReturnType(new Type(hArray[2],clas));
	}

	private static ArrayList<Type> getParamTypes(String params,ClassBean clas) {
		if (params == null || params.length() == 0) {
			return new ArrayList<>(0);
		}
		ArrayList<Type> types = new ArrayList<>();
		int endIndex;
		while (params.length() > 0) {
			endIndex = 1;
			if (params.startsWith("[")) {
				if (params.charAt(1) == 'L') {
					endIndex = params.indexOf(';') + 1;
				} else {
					endIndex = 2;
				}
			} else if (params.startsWith("L")) {
				endIndex = params.indexOf(';') + 1;
			}

			String type = params.substring(0, endIndex);
			if (endIndex == params.length()) {
				params = "";
			} else {
				params = params.substring(endIndex);
			}
			types.add(new Type(type,clas));
		}
		return types;
	}

	private static void parseLine(Statement line, ClassBean clas) {
		String str = line.getContent();
		String[] words = str.split(" ");
		String action = words[0];
		switch (action) {
		case ".field":
			Field field = new Field();
			initField(line, field, clas);
			clas.addField(field);
			break;
		case ".class":
			parseClass(str, clas);
			break;
		case ".super":
			parseSuper(str, clas);
			break;
		case ".source":
			parseSource(str, clas);
			break;
		case ".implements":
			parseImplements(str, clas);
			break;
		default:
			System.out.println("parseLine: " + str);
			clas.parseError = true;
			break;
		}
	}

	private static void parseSuper(String str, ClassBean clas) {
		String[] arr = str.split(" ");
		if (arr.length != 2) {
			System.out.println("parseSuper:" + str);
			clas.parseError = true;
		}
		clas.setSuperClass(new Type(arr[1],clas));
	}

	private static void parseSource(String str, ClassBean clas) {
		String[] arr = str.split(" ");
		if (arr.length != 2) {
			System.out.println("parseSource:" + str);
			clas.parseError = true;
		}
	}

	private static void parseImplements(String str, ClassBean clas) {
		String[] arr = str.split(" ");
		if (arr.length != 2) {
			System.out.println("parseImplements:" + str);
			clas.parseError = true;
		}
		clas.addimplements(new Path(arr[1],clas));
	}

	private static void parseClass(String str, ClassBean clas) {
		String[] arr = str.split(" ");
		int end = arr.length - 1;
		for (int i = 1; i < end; i++) {
			if (parseObj(arr[i], clas)) {
				continue;
			}
			switch (arr[i]) {
			case "interface":
				break;
			case "abstract":
				break;
			case "annotation":
				break;
			case "enum":
				break;
			default:
				System.out.println("parseClass : " + str);
				clas.parseError = true;
				break;
			}
		}
		clas.setCurrent(new Type(arr[end],clas));
	}

	private static void initField(Statement str, Field field, ClassBean clas) {
		String[] arr = null;
		int index = str.indexOf('=');
		if (index >= 0) {
			arr = str.substring(0, index - 1).split(" ");
		} else {
			arr = str.split(" ");
		}

		int end = arr.length - 1;
		for (int i = 1; i < end; i++) {
			if (parseObj(arr[i], field)) {
				continue;
			}
			switch (arr[i]) {
			case "enum":
				break;
			case "volatile":
				break;
			case "transient":
				break;
			default:
				System.out.println("initField : " + str);
				clas.parseError = true;
				break;
			}
		}

		String[] temp = arr[end].split(":");
		if (temp.length == 2) {
			field.setName(temp[0]);
			field.setVar(new Data(new Type(temp[1],clas)));
		} else {
			System.out.println("initField : " + str);
			clas.parseError = true;
		}

	}

	private static boolean parseObj(String str, Obj obj) {
		boolean result = true;
		switch (str) {
		case "public":
		case "private":
		case "protected":
			obj.setProperty(str);
			break;
		case "static":
			obj.setStatic(true);
			break;
		case "final":
			obj.setFinal(true);
			break;
		case "synthetic":
			obj.setSynthetic(true);
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	private static void printArray(ArrayList<String> lines) {
		for (String line : lines) {
			System.out.println(line);
		}
	}
}
