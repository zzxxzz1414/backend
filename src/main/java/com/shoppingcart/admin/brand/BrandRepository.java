package com.shoppingcart.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.User;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	
	@Query("select b from Brand b WHERE b.name like %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);

	Long countById(Integer id);
	
	@Query("SELECT u FROM Brand u WHERE u.name = :name")
	public User getBrandByEmail(@Param("name") String email);
	
}
