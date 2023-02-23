package com.shoppingcart.admin.brand;

public class CategoryDTO {
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public CategoryDTO(Integer id) {
		super();
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CategoryDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public CategoryDTO() {

	}
	
}