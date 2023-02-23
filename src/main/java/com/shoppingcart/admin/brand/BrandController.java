package com.shoppingcart.admin.brand;

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
import com.shoppingcart.admin.brand.export.BrandCsvExproter;
import com.shoppingcart.admin.brand.export.BrandExcelExporter;
import com.shoppingcart.admin.brand.export.BrandPDFExporter;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.security.ShoppingUserDetails;
import com.shoppingcart.admin.user.UserService;

@Controller
public class BrandController {
	@Autowired
	BrandService service;
	@Autowired
	private CategoryService cateService;


	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();

		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String createNew(Model model) {
		List<Category> listCategories = cateService.listCategoriesUsedInForm();
		model.addAttribute("brand", new Brand());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTittle", "Create New User");
		return "brands/brand_form";
	}
	
	private String getRedirectURLtoAffectedBrand(Brand brand) {
		String firstPartOfName = brand.getName().split("@")[0];
		return "redirect:/brands/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfName;
	}

	@PostMapping("/brands/save")
	public String saveUser(Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShoppingUserDetails logger)
			throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand savedUser = service.save(brand);

			String uploadDir = "brand-logos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			
			service.save(brand);
			}
		return getRedirectURLtoAffectedBrand(brand);
		}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		service.deleteBrandById(id);

		redirectAttributes.addFlashAttribute("message", "Deleted Brand Id: " + id);

		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
		
			Model model, Brand brand) throws BrandNotFoundException {
		brand = service.findById(id);

		model.addAttribute("brand", brand);
		model.addAttribute("pageTittle", "Edit Brand");

		redirectAttributes.addFlashAttribute("message", "Editted Id: " + id);

		return "/brands/brand_form";
	}

	@GetMapping("/brands/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = service.listAll();
		BrandCsvExproter brandCsvExporter = new BrandCsvExproter();
		brandCsvExporter.export(listBrands, response);
	}
	
	@GetMapping("/brands/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = service.listAll();
		BrandExcelExporter categoryExcelExporter = new BrandExcelExporter();
		categoryExcelExporter.export(response, listBrands);
	}
	
	@GetMapping("/brands/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = service.listAll();
		BrandPDFExporter categoryPdfExporter = new BrandPDFExporter();
		categoryPdfExporter.export(response, listBrands);
	}

	
}
