package com.dzf.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车
 * 
 * @author adminstrtor
 *
 */
public class Cart implements Serializable{
	// 为了使购物车项有顺序，我们使用linkedHashMap key;商品id
	private Map<String, CartItem> map = new LinkedHashMap<>();
	private Double totalMoney = 0.0;

	
	/**
	 * 获取所有的cartItem值的集合
	 * @return
	 */
	public Collection<CartItem> getItems(){
		  Collection<CartItem> values = map.values();
		return values;
	}
	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cart(Map<String, CartItem> map, Double totalMoney) {
		super();
		this.map = map;
		this.totalMoney = totalMoney;
	}

	public Map<String, CartItem> getMap() {
		return map;
	}

	public void setMap(Map<String, CartItem> map) {
		this.map = map;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	/**
	 * 将商品添加到购物车
	 * 
	 * @param item
	 */
	public void add(CartItem item) {
		// 首先判断购物车里有没有这个商品
		String pid = item.getProduct().getPid();
		if (map.containsKey(pid)) {
			// 有这个商品，只需要将数量和小计，还有总计修改
			int allCount = item.getCount() + map.get(pid).getCount();
			item.setCount(allCount);
		} 
			
		map.put(pid, item);
	

		totalMoney += item.getSubtotal();
	}

	/**
	 * 删除指定的商品
	 * 
	 * @param id
	 */
	public void remove(String id) {
		CartItem item = map.remove(id);
		totalMoney -= item.getSubtotal();
	}

	/**
	 * 清空购物车
	 */
	public void clear() {
		map.clear();
		totalMoney = 0.0;
	}

}
