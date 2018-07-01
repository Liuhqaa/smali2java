package com.liuhq.smali2java.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.bean.Field;
import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Obj;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.bean.Type;
import com.liuhq.smali2java.bean.Data;
import com.liuhq.smali2java.parse.bean.OutClass;
import com.liuhq.smali2java.parse.statement.Statement;

public class ClassToFile {

	private static final String SPACE = " ";
	private static final String[] PROPERTIES = new String[] { "", "public", "protected", "private" };

	public static boolean parse(ClassBean classBean, String outPath) {
		File out = new File(outPath);
		File dir = out.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}

		OutClass outClass = new OutClass();
		outClass.add("");
		outClass.add("");
		outClass.add(parseClass(classBean, outClass));
		outClass.add("{");

		ArrayList<Field> fields = classBean.getFields();

		for (Field field : fields) {
			outClass.add(parseField(field, outClass));
		}

		ArrayList<Method> methods = classBean.getMethods();
		for (Method method : methods) {
			outClass.add("");
			outClass.add(parseMethod(method, outClass));
			outClass.add("{");
			ArrayList<Statement> statements = method.getStatements();
			ArrayList<String> lines = outClass.getCodes();
			for (Statement statement : statements) {
				ArrayList<String> codes = statement.execute(method,classBean,lines);
				if(codes != null && codes.size() > 0)
				{
					outClass.addLines(codes);
				}
			}
			outClass.add("}");
		}

		outClass.add("}");
		
		outClass.insertImports(classBean.getImportSet().iterator());
		writeFile(outClass.getCodes().iterator(), outPath);
		return true;
	}

	private static String parseMethod(Method method, OutClass outClass) {
		StringBuffer buffer = parse(method);
		if (!method.isConstructor()) {
			Type returnType = method.getReturnType();
			buffer.append(returnType.getClassName()).append(SPACE);
		}
		buffer.append(method.getName());
		String params = getParams(method, outClass);
		buffer.append("(").append(params).append(")");
		return buffer.toString();
	}

	private static String getParams(Method method, OutClass outClass) {
		StringBuffer buffer = new StringBuffer();
		HashMap<String, Var> params = method.getParams();
		Collection<Var> values = params.values();
		for (Var param : values) {
			if (param.isInvented()) {
				continue;
			}
			buffer.append(",").append(param.getClassName()).append(SPACE).append(param.getName());
		}
		return buffer.toString().substring(1);
	}

	private static String parseField(Field field, OutClass outClass) {
		StringBuffer buffer = parse(field);
		Type type = field.getType();
		buffer.append(type.getClassName()).append(SPACE);
		buffer.append(field.getName()).append(";");
		return buffer.toString();
	}

	private static StringBuffer parse(Obj obj) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(PROPERTIES[obj.getProperty()]);
		if (obj.getProperty() != 0) {
			buffer.append(SPACE);
		}

		if (obj.isStatic()) {
			buffer.append("static").append(SPACE);
		}
		if (obj.isFinal()) {
			buffer.append("final").append(SPACE);
		}
		if (obj.isSynthetic()) {
			buffer.append("synthetic").append(SPACE);
		}
		return buffer;
	}

	private static void writeFile(Iterator<String> iterator, String outPath) {
		File out = new File(outPath);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(out));
			while (iterator.hasNext()) {
				String line = iterator.next();
				writer.write(line);
				writer.write("\n");
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String parseClass(ClassBean classBean, OutClass outClass) {
		StringBuffer buffer = parse(classBean);
		buffer.append("class").append(SPACE);
		buffer.append(classBean.getName()).append(SPACE);

		Type superClass = classBean.getSuperClass();
		if (superClass != null) {
			buffer.append("extends ").append(superClass.getClassName()).append(SPACE);
		}
		return buffer.toString();
	}

}
