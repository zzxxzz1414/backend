package com.shoppingcart.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.User;

@Service
@Transactional
public class BrandService {
	
	@Autowired
	BrandRepository brandRepo;

	public static final int ROOT_BRANDS_PER_PAGE = 10;

	public List<Brand> listAll() {
		return (List<Brand>) brandRepo.findAll();
	}
	
	public Page<Brand> listByPage(int pageNum,String sortField,String sortDir,String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_BRANDS_PER_PAGE,sort);
		if(keyword != null) {
			return brandRepo.findAll(keyword,pageable);
		}
		
		return brandRepo.findAll(pageable);
	}
	

	public Brand saveBrand(Brand brand) {

		return brandRepo.save(brand);
	}

	public void deleteBrandById(Integer id) {
		brandRepo.deleteById(id);
	}

	
	public Brand save(Brand brand) {
		boolean isUpdatingUser = (brand.getId() != null);
		
		if(isUpdatingUser) {
			Brand existingUser  = brandRepo.findById(brand.getId()).get();	
		}
		return brandRepo.save(brand);
	}
	
	public Brand findById(Integer id) throws BrandNotFoundException {
		try {

			return brandRepo.findById(id).get();

		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any user with ID: " + id);

		}

	}
	
	public boolean isNameUnique(Integer id, String name) {
		User userByEmail = brandRepo.getBrandByEmail(name);
		
		if(userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if(userByEmail != null) return false;
			
		}else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}


	public Page<Brand> listPerPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_BRANDS_PER_PAGE, sort);

		if (keyword != null) {
			return brandRepo.findAll(keyword, pageable);
		}

		return brandRepo.findAll(pageable);
	}
	
//	public String checkUnique(Integer id, String name) {
//		boolean isCreatingNew =(id == null || id == 0);
//		Brand brandByName = brandRepo.findByName(name);
//		
//		if(isCreatingNew) {
//			if(brandByName != null)
//				return "Duplicate";
//		} else {
//			if(brandByName != null && brandByName.getId() != id)
//				return "Duplicate";
//		}
//		return "ok";
//		
//	}
	


	
}
