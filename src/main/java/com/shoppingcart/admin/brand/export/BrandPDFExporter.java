package com.shoppingcart.admin.brand.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

public class BrandPDFExporter extends AbstractExporter {
	public void export(HttpServletResponse response, List<Brand> listBrands) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "brands_");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		Paragraph paragraph = new Paragraph("List of Categoris", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 3.0f, 3.0f, 1.7f,1.5f});

		writeTableHeader(table);
		writeTableData(listBrands, table);

		document.add(table);
		document.close();
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(10);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("ID Category", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name Category", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Alias", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);

	}

	private void writeTableData(List<Brand> listBrands, PdfPTable table) {
		for (Brand brand : listBrands) {
			table.addCell(String.valueOf(brand.getId()));
			table.addCell(brand.getName());
			table.addCell(brand.getLogo());

		}
	}
}
