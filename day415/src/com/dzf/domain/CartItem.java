package com.dzf.domain;

import java.io.Serializable;

/**
 * 购物车项
 * 
 * @author adminstrtor
 *
 */
public class CartItem implements Serializable{
	private Product product;
	private Integer count;
	private Double subtotal=0.0; //小计
	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getCount() {
		return count;
	}
	
	public Double getSubtotal() {
		return count*product.getShop_price();
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	public CartItem(Product product, Integer count) {
		super();
		this.product = product;
		this.count = count;
	}
	
}
