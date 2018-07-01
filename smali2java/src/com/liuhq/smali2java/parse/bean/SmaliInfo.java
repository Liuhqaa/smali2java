package com.liuhq.smali2java.parse.bean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.liuhq.smali2java.parse.statement.Statement;
import com.liuhq.smali2java.parse.statement.StatementGroup;

public class SmaliInfo extends StatementGroup {
	
	public SmaliInfo(String samliPath) {
		ArrayList<Statement> lines = getLines(samliPath);
		initData(lines);
	}
	
	private void initData(ArrayList<Statement> lines) {
		// 倒序解析
		int nested = 0;
		String tag = "";
		boolean isbody = false;
		int endIndex = 0;
		Statement endLine = null;
		for (int i = lines.size() - 1; i >= 0; i--) {
			Statement tempStatement = lines.get(i);
			if (isbody) {
				if (tempStatement.startsWith(tag)) {
					if (--nested <= 0) {
						isbody = false;
						StatementGroup statement = StatementGroup.createBlock(lines, i, endIndex);
						if (i > 0 && lines.get(i - 1).startsWith(":")) {
							statement.setTag(lines.get(i - 1).toString());
						}
						addStatement(statement);
					}
				} else if (tempStatement.startsWith(".end") && tempStatement.equals(endLine)) {
					++nested;
				}
				continue;
			}

			if (tempStatement.startsWith(".end annotation") || tempStatement.startsWith(".end array-data")
					|| tempStatement.startsWith(".end method") || tempStatement.startsWith(".end field")) {
				isbody = true;
				++nested;
				endLine = tempStatement;
				tag = "." + tempStatement.split(" ")[1];
				endIndex = i;
			} else {
				addStatement(tempStatement);
			}
		}
		// 倒序调整到正序
		ArrayList<Statement> codes = getStatements();
		ArrayList<Statement> statements = new ArrayList<>(codes.size());
		int size = codes.size();
		for (int i = size - 1; i >= 0; i--) {
			statements.add(codes.get(i));
		}
		setStatements(statements);
	}
	
	private  ArrayList<Statement> getLines(String file) {
		ArrayList<Statement> lines = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!line.equals("") && !line.startsWith("#")) {
					lines.add(new Statement(line.trim()));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

}
