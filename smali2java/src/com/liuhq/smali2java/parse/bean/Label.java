package com.liuhq.smali2java.parse.bean;

public class Label {
	
	public static final int TYPE_TAG = 0;
	public static final int TYPE_GOTO = 1;
	public static final int TYPE_RETURN = 2;
	
	private int id; //tag ��tag�б�����±�
	private int index;//: tag ��ǩ���������������е��±�
	private int type = TYPE_TAG;
	private String tag;
	private int oldIndex;//: tag ��ǩ���������������е��±�
	
	public Label(int id,int lineIndex,int type,String tag) {
		this.id = id;
		this.oldIndex = lineIndex;
		this.index = lineIndex;
		this.type = type;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getType() {
		return type;
	}

	public String getTag() {
		return tag;
	}
	
	public boolean equals(Label obj) {
		return obj.getIndex() == getIndex();
	}

	public int getOldIndex() {
		return oldIndex;
	}
}
