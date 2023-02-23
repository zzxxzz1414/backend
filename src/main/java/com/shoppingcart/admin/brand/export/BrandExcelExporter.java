package com.shoppingcart.admin.brand.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Brand;

public class BrandExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public BrandExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	private void writeHeadLine() {
		sheet = workbook.createSheet("Brands");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row,0,"Brand ID",cellStyle);
		createCell(row,1,"Brand Name",cellStyle);
		createCell(row,2,"Logo",cellStyle);
		
	}
	private void createCell(XSSFRow row,int columnIndex,Object value,CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		if(value instanceof Integer) {
			cell.setCellValue ((Integer) value);
		}else if(value instanceof Boolean) {
			cell.setCellValue ((Boolean) value);
		}else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	
	public void export(HttpServletResponse response,List<Brand> listBrands) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "brands");
		
		writeHeadLine();
		writeDataLine(listBrands);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	private void writeDataLine(List<Brand> listBrands) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for(Brand brand: listBrands) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			
			createCell(row, columnIndex++, brand.getId(), cellStyle);
			createCell(row, columnIndex++, brand.getName(), cellStyle);
			createCell(row, columnIndex++, brand.getLogo(), cellStyle);
			
		}
	}
}
