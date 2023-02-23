package com.shoppingcart.admin.entity;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends IdBaseEntity{
	@Column(length = 255, nullable = false, unique = true)
	private String name;

	@Column(length = 255, nullable = false)
	private String alias;
	
	@Column(length = 512, nullable = false, name ="short_description")
	private String shortDescription;
	
	@Column(length = 4096, nullable = false, name="full_description")
	private String fullDescription;
	
	@Column(nullable = false, name="main_image")
	private String mainImage;
	
	@Column(nullable = false, updatable= false, name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	private boolean enabled;
	
	@Column(name="in_stock")
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	@Column(name="discount_percent")
	private float discountPercent;
	
	private float length;
	
	private float width;
	
	private float height;
	
	private float weight;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	    
//	@ManyToOne
//	@JoinTable(name = "products_brands", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
//	private Set<Brand> brands = new HashSet<>();
//	
//	@ManyToOne
//	@JoinTable(name = "products_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
//	private Set<Category> categories = new HashSet<>();
	

	public Product() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Transient
	public String getPhotosImagePath() {
		if(id == null || mainImage == null) return"/images/default-user.png";
		return "/product-photos/" + this.id + "/" + this.mainImage;
	}
	
	
	
	
	
	
																				
	
}
