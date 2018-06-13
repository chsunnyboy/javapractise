package com.thread;

import java.io.File;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {

		File file=new File(".");
        System.out.println(file.exists());
        System.out.println(file.isDirectory());
        System.out.println(file.getAbsolutePath().toString());
        
        //û�в���
        File fileCreateByNo=new File("");
        System.out.println("fileCreateByNo=="+fileCreateByNo);
        System.out.println("fileCreateByNo=="+fileCreateByNo.getCanonicalPath());
        System.out.println("-----------------------------");
        //һ����Ĳ���
        File fileCreateByPoint=new File(".");
        System.out.println("fileCreateByPoint=="+fileCreateByPoint);
        System.out.println("fileCreateByPoint=="+fileCreateByPoint.getCanonicalPath());
        System.out.println("-----------------------------");
        //������Ĳ���
        File fileTwoPoint = new File("..");  
        System.out.println("fileTwoPoint=="+fileTwoPoint);
        System.out.println("fileTwoPoint=="+fileTwoPoint.getCanonicalPath());
        System.out.println("-----------------------------");
        //һ��������б�ߵĲ���
        File filePLL = new File(".\\");  
        System.out.println("filePLL=="+filePLL);
        System.out.println("filePLL=="+filePLL.getCanonicalPath());
        System.out.println("-----------------------------");
        //��ǰ����Ŀ¼
        String currentWorkPath=System.getProperty("user.dir");
        System.out.println("currentWorkPath=="+currentWorkPath);
        
	}

}
