package com.shoppingcart.admin.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand extends IdBaseEntity {
	@Column(length = 45, nullable = false, unique = true)
	private String name;

	@Column(length = 128, nullable = false)
	private String logo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();
	
	public Brand(Set<Category> categories) {
		super();
		this.categories = categories;
	}


	public Set<Category> getCategories() {
		return categories;
	}


	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}


	public Brand() {
		super();
	}


	public Brand(String name, String logo) {
		super();
		this.name = name;
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	

	@Transient
	public String getLogoPath() {
		if(id == null || logo == null) return"/images/default-user.png";
		return "/brand-logos/" + this.id + "/" + this.logo;
	}

	
}
