package com.shoppingcart.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	@Query("Update Product c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateStatus(Integer id, boolean enabled);

	Long countById(Integer id);
	
	@Query("Select c FROM Product c where c.name LIKE %?1% OR c.alias LIKE %?1%")
	public Page<Product> findAll(String keyword,Pageable pageable);
	
	@Query("SELECT c FROM Product c WHERE c.name LIKE %?1%")
	public Page<Product> search(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%")	
	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, 
			Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%) AND "
			+ "(p.name LIKE %?3% " 
			+ "OR p.shortDescription LIKE %?3% "
			+ "OR p.fullDescription LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, 
			String keyword, Pageable pageable);
}
