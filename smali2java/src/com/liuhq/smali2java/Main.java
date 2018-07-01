package com.liuhq.smali2java;

import java.io.File;

import com.liuhq.smali2java.bean.ClassBean;
import com.liuhq.smali2java.parse.ClassToFile;
import com.liuhq.smali2java.parse.Parse;

public class Main {

	static boolean isDebug = true;
	static long fileCount = 0;
	
	static boolean error = false;
	
	public static void main(String[] args) {
		ClassBean bean = Parse.parseSmali("E:\\workspace\\java\\test\\aaac.smali");
		
		String outPath = "E:\\\\workspace\\\\java\\\\test\\\\aaac.java";
		ClassToFile.parse(bean, outPath);
		
//		if(isDebug)
//		{
//			parseApp("E:\\workspace\\java\\map\\mobileqq");
//		}else
//		{
//			parseAllApp();
//		}

		
	}

	private static void parseAllApp() {
		String src = "E:\\workspace\\java\\map";
		File srcFile = new File(src);
		File[] list = srcFile.listFiles();
		for (File f : list) {
			if(error)
			{
				break;
			}
			if (f.isDirectory()) {
				parseApp(f.getAbsolutePath());
			}
		}
	}

	private static void parseApp(String src) {
		error = false;
		File srcFile = new File(src);
		File[] list = srcFile.listFiles();
		fileCount = -1;
		for (File f : list) {
			if(error)
			{
				break;
			}
			if (f.isDirectory() && f.getName().startsWith("smali")) {
				parseSrcPackage(f);
				if(isDebug)
				{
					break;
				}
			}
		}
		System.out.println(srcFile.getName() + "文件总数：" + fileCount);
	}

	private static void parseSrcPackage(File root) {
		File[] list = root.listFiles();
		for (File file : list) {
			if(error)
			{
				break;
			}
			if(file.isFile())
			{
				ClassBean classBean = Parse.parseSmali(file.getAbsolutePath());
				error = classBean.parseError;
				fileCount ++;
				if(error)
				{
					System.out.println(file.getAbsolutePath());
					break;
				}
			}else
			{
				parseSrcPackage(file);
			}
		}
	}
}
