package com.exportexcel.poi;


import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {
	
	public static void main(String[] args) throws Exception {
		File file=new File("C:\\Users\\Administrator\\Desktop\\export.xlsx");
		FileOutputStream fis=new FileOutputStream(file,true);
		
		long t1 = System.currentTimeMillis();
		System.out.println("��ʼ����excel:"+t1);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		SXSSFWorkbook workbook2=new SXSSFWorkbook(1000);  // ��ֹ�ڴ����
        // ����һ�����
    	XSSFSheet sheet = workbook.createSheet();
		// ������������
    	//XSSFRow row = sheet.createRow(1);
        String[] array ={"���","Ӧ�ս��","ʵ�ս��"};   
        XSSFDrawing patr = sheet.createDrawingPatriarch();
        XSSFCellStyle style2 = workbook.createCellStyle();
        XSSFFont font2 = workbook.createFont();  
        font2.setColor(XSSFFont.COLOR_NORMAL);  
        font2.setFontName("����");
        font2.setFontHeightInPoints((short) 11);
        style2.setFont(font2);
        DataFormat format = workbook.createDataFormat();    
        style2.setDataFormat(format.getFormat("@"));
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        for(int j=0;j<15;j++){
        	XSSFRow row = sheet.createRow(j);
        	for (int i = 0; i < array.length; i++) {
    			XSSFCell cell = row.createCell(i);
    			XSSFRichTextString text = new XSSFRichTextString(array[i]);
    			cell.setCellValue(text);
    			XSSFCellStyle style = workbook.createCellStyle();
    			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
    	        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
    	        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
    	        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
    	        XSSFFont font = workbook.createFont();
    			font.setFontName("����");
    			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
    			font.setFontHeightInPoints((short) 11);
    			font.setColor(XSSFFont.COLOR_NORMAL);
    			style.setFont(font);
    			cell.setCellStyle(style);
    		}
        }
		
		workbook.write(fis);
		workbook.close();
		long t2 = System.currentTimeMillis();
		System.out.println("��������excel:"+t2);
	}
}
