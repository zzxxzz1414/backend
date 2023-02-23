package com.shoppingcart.admin.brand.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Brand;

public class BrandCsvExproter extends AbstractExporter{
	public void export(List<Brand> listCategories,HttpServletResponse response) throws IOException{
		super.setResponseHeader(response, "text/csv", ".csv", "brands_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID Brand","Brand Name","Logo"};
		String[] fieldMapping = {"id","name","logo"};
		csvWriter.writeHeader(csvHeader);
		
		for (Brand brand : listCategories) {
			csvWriter.write(brand,fieldMapping);
		}
		csvWriter.close();
	}/////
}
