package com.shoppingcart.admin.product.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Product;

public class ProductCsvExporter extends AbstractExporter {
	public void export(List<Product> listProducts,HttpServletResponse response) throws IOException{
		super.setResponseHeader(response, "text/csv", ".csv", "products_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID Product","Product Name","Main Image"};
		String[] fieldMapping = {"id","name","mainImage"};
		csvWriter.writeHeader(csvHeader);
		
		for (Product product : listProducts) {
			csvWriter.write(product,fieldMapping);
		}
		csvWriter.close();
	}
}
