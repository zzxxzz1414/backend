package com.shoppingcart.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.Product;
import com.shoppingcart.admin.product.export.ProductCsvExporter;
import com.shoppingcart.admin.security.ShoppingUserDetails;

@Controller
public class ProductController {
	
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;


	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId) {

		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();

		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("listCategories", listCategories);

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "products/products";
	}

	@GetMapping("/products/new")
	public String createNew(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTittle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);
		return "products/product_form";
	}
	

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShoppingUserDetails logger)
			throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			Product savedUser = productService.save(product);

			String uploadDir = "product-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			
			productService.save(product);
			}
		return defaultRedirectURL;
		}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		productService.deleteProductById(id);

		redirectAttributes.addFlashAttribute("message", "Deleted Product Id: " + id);

		return defaultRedirectURL;
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
		
			Model model, Product product) throws ProductNotFoundException {
		product = productService.findById(id);

		model.addAttribute("product", product);
		model.addAttribute("pageTittle", "Edit Product");

		redirectAttributes.addFlashAttribute("message", "Editted Id: " + id);

		return defaultRedirectURL;
	}

	@GetMapping("/products/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Product> listProducts = productService.listAll();
		ProductCsvExporter productCsvExporter = new ProductCsvExporter();
		productCsvExporter.export(listProducts, response);
	}
	
	

	
}
