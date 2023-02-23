package com.shoppingcart.admin.product;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Product;


@Service
@Transactional
public class ProductService {
	@Autowired
	ProductRepository repo;

	public static final int PRODUCTS_PER_PAGE = 4;

	public List<Product> listAll() {
		return (List<Product>) repo.findAll(Sort.by("id").ascending());
	}
	
	public Page<Product> listByPage(int pageNum,String sortField,String sortDir,String keyword,
			Integer categoryId){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1,  PRODUCTS_PER_PAGE,sort);
		Page<Product> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);//find all by keyword & categoryId
			} else {
				page = repo.findAll(keyword, pageable);//find all by keyword
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);//find all by categoryId
			} else {		
				page = repo.findAll(pageable);//find all
			}
		}
		
		return page;
	}

	public Product saveCategory(Product product) {

		return repo.save(product);
	}

	public void deleteCategoryById(Integer id) {
		repo.deleteById(id);
	}

	public Product findById(Integer id) {
		return repo.findById(id).get();
	}

	public void updateStatus(Integer id, boolean enabled) {
		repo.updateStatus(id, enabled);
	}

	public Page<Product> listPerPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);
	}
	
	public void deleteProductById(Integer id) {
		repo.deleteById(id);
	}
	
	public Product save(Product product) {
		boolean isUpdatingUser = (product.getId() != null);
		
		if(isUpdatingUser) {
			Product existingUser  = repo.findById(product.getId()).get();	
		}
		return repo.save(product);
	}

}
