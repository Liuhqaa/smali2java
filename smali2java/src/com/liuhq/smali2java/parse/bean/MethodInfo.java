package com.liuhq.smali2java.parse.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.liuhq.smali2java.bean.Method;
import com.liuhq.smali2java.bean.Var;
import com.liuhq.smali2java.parse.statement.IfStatement;
import com.liuhq.smali2java.parse.statement.Statement;
import com.liuhq.smali2java.parse.statement.StatementGroup;

public class MethodInfo {

	private HashMap<String, Label> tagMap;
	private ArrayList<Label> infoList;
	private ArrayList<Statement> lines;
	private Label returnLabel;

	public MethodInfo(StatementGroup statementGroup) {
		this.lines = statementGroup.getStatements();
		initData(lines);
	}

	private void initData(ArrayList<Statement> lines) {
		tagMap = new HashMap<>();
		infoList = new ArrayList<>();
		int size = lines.size();

		for (int i = 0; i < size; i++) {
			Statement line = lines.get(i);
			if (line.startsWith(":")) {
				addInfo(new Label(infoList.size(), line.getIndex(), Label.TYPE_TAG, line.substring(1)));
			} else if (line.startsWith("goto")) {
				addInfo(new Label(infoList.size(), line.getIndex(), Label.TYPE_GOTO, line.split(":")[1]));
			} else if (line.startsWith("return")) {
				returnLabel = new Label(infoList.size(), line.getIndex(), Label.TYPE_RETURN, "return");
				addInfo(returnLabel);
			}
		}

		// 所有相邻的标签他们的行数都等于最大的那个标签的行数
		for (int i = infoList.size() - 2; i >= 0; i--) {
			if (infoList.get(i).getType() == Label.TYPE_TAG && infoList.get(i + 1).getType() == Label.TYPE_TAG) {
				infoList.get(i).setIndex(infoList.get(i + 1).getIndex());
			}
		}

	}

	private void addInfo(Label info) {
		infoList.add(info);
		if (info.getType() == Label.TYPE_TAG) {
			tagMap.put(info.getTag(), info);
		}
	}

	private Label getEndLabel(int ifIndex, String tag) {
		Label elseTag_first = getTagByName(tag);
		Label ifTag_first = null;
		int ifI = ifIndex;
		int elI = 0;
		Label ifTag = null, elTag = null;
		while ((ifTag = findTag(ifI)) != ifTag_first) {
			if (ifTag_first == null) {
				ifTag_first = ifTag;
			}
			if (ifTag.equals(elseTag_first)) {
				return geteMaxIndex(ifTag);
			}
			elI = elseTag_first.getIndex();
			while ((elTag = findTag(elI)) != elseTag_first) {
				if (ifTag.equals(elTag)) {
					return geteMaxIndex(ifTag);
				}
				elI = elTag.getIndex();
			}
			ifI = ifTag.getIndex();
		}
		return null;
	}

	private Label geteMaxIndex(Label ifTag) {
		if (ifTag.getId() == infoList.size() -1) {
			return ifTag;
		}
		Label result = ifTag;
		for (int i = ifTag.getId() + 1; i < infoList.size(); i++) {
			if (ifTag.equals(infoList.get(i))) {
				result = infoList.get(i);
			}else
			{
				break;
			}
		}
		return result;
	}

	private Label findTag(int startLine) {
		int size = infoList.size();
		boolean[] arr = new boolean[size];
		for (int i = 0; i < size; i++) {
			arr[i] = false;
		}

		Label result = null;
		boolean isStart = true;
		for (int i = 0; i < size; i++) {
			Label info = infoList.get(i);
			if (isStart && info.getIndex() <= startLine) {
				continue;
			}
			isStart = false;

			if (arr[i]) {
				break;
			}
			arr[i] = true;
			int type = info.getType();
			if (type == Label.TYPE_TAG) {
				result = info;
				break;
			} else if (type == Label.TYPE_GOTO) {
				Label goTag = getTagByName(info.getTag());
				i = Math.max(goTag.getId() - 1, 0);
			} else {
				break;
			}
		}
		return result;
	}

	private Label getTagByName(String tag) {
		return tagMap.get(tag);
	}

	public Label getReturnLabel() {
		return returnLabel;
	}

	public StatementGroup getBlock(int start, int endIndex) {
		StatementGroup statementGroup = new StatementGroup();
		int size = lines.size();
		for (int i = start; i < size; i++) {
			if (endIndex == i) {
				break;
			}
			Statement line = lines.get(i);
			if(line.startsWith("return"))
			{
				break;
			}
			if (line.startsWith("goto")) {
				String tag = line.split(" ")[1].substring(1);
				i = getTagByName(tag).getOldIndex();
			} else if (line.startsWith(":")|| line.startsWith(".")) {
				continue;
			} else {
				statementGroup.addStatement(line);
			}
		}
		return statementGroup;
	}

	public IfStatement getIfStatement(int i, Method method) {
		Statement line = lines.get(i);
		String tag = line.substring(line.indexOf(':') + 1);
		Label endLabel = getEndLabel(i, tag);
		int endIndex = endLabel.getOldIndex();
		StatementGroup ifBlock = getBlock(i + 1, endIndex);
		Label elLabel = getTagByName(tag);
		StatementGroup elBlock = getBlock(elLabel.getOldIndex() + 1, endIndex);
		String[] arr = line.split(" ");
		String type = arr[0];
		Var v1 = method.getVar(endSubStr(arr[1]));
		Var v2 = null;
		if(arr.length > 3)
		{
			v2 = method.getVar(endSubStr(arr[2]));
		}
		IfStatement statement = new IfStatement(type,v1,v2 , ifBlock, elBlock);
		statement.setEndLabel(endLabel);
		return statement;
	}

	private static String endSubStr(String str) {
		return str.substring(0, str.length() -1);
	}
}
