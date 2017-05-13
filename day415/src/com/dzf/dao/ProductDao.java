package com.dzf.dao;

import java.util.List;

import com.dzf.domain.Product;

public interface ProductDao {

	List<Product> findHotProduct() throws Exception;

	List<Product> findNewProduct()throws Exception;

	Product getById(String pid)throws Exception;

	int getCount(String cid)throws Exception;

	List<Product> getProudct(int currPage, int pageSize, String cid)throws Exception;

	void setNull(String cid)throws Exception;

	List<Product> findAll()throws Exception;

	void update(Product p)throws Exception;

	void deleteById(String pid)throws Exception;

	void add(Product p)throws Exception;

	int getCount()throws Exception;

	List<Product> getProudct(int currPage, int pageSize)throws Exception;

}
