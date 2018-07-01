package com.liuhq.smali2java;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.liuhq.smali2java.parse.Parse;
import com.liuhq.smali2java.parse.bean.SmaliInfo;
import com.liuhq.smali2java.parse.statement.Statement;

public class Test {
	public static void main(String[] args) {
		
		String str = "aaaa()ccccc";
		String [] array = str.split("[(,)]");
		for(String s : array)
		{
			System.out.println(s);
		}
	}

	private static void testa() {
		ArrayList<String> arr = new ArrayList<>();
		arr.add(".method public a(Lcom/tencent/av/service/QavWrapper;)V");
		arr.add(".method public a(Lcom/tencent/av/service/QavWrapper;)V");
		arr.add(".method public a(Lcom/tencent/av/service/QavWrapper;)V");
		arr.add(".end method");
		arr.add(".end method");
		arr.add(".end method");
	}
}
