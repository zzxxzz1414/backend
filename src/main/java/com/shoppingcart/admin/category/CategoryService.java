package com.shoppingcart.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;

@Service
@Transactional
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	public static final int ROOT_CATEGORIES_PER_PAGE = 4;

	public List<Category> listAll() {
		return (List<Category>) categoryRepo.findAll(Sort.by("id").ascending());
	}

	public Category saveCategory(Category category) {

		return categoryRepo.save(category);
	}

	public void deleteCategoryById(Integer id) {
		categoryRepo.deleteById(id);
	}

	public Category findById(Integer id) {
		return categoryRepo.findById(id).get();
	}

	public void updateStatus(Integer id, boolean enabled) {
		categoryRepo.updateStatus(id, enabled);
	}

	public Page<Category> listPerPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

		if (keyword != null) {
			return categoryRepo.findAll(keyword, pageable);
		}

		return categoryRepo.findAll(pageable);
	}
	
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword) {

		Sort sort = Sort.by("name");
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);
		
		Page<Category> pageCategories = null;
		

		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = categoryRepo.search(keyword, pageable);
		} else {
			pageCategories = categoryRepo.findRootCategories(pageable);
		}

		List<Category> rootCategories = pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		if(keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for(Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			return searchResult;
		} else {
			return listHierarchicalCategories(rootCategories);
		}
	}
	
	public List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for(Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for(Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
		}
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
		}
	}

	public List<Category> listCategoriesUsedInForm() {
	List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepo.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			categoriesUsedInForm.add(category.copyIdAndName(category));

			Set<Category> children = category.getChildren();

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

				listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
			}
		}
		return categoriesUsedInForm;
	}

	public void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		//Set<Category> children = sortSubCategories(parent.getChildren());
		Set<Category> children = parent.getChildren();
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),name));
			
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

}
