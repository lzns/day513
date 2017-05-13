package com.dzf.service;

import java.util.List;

import com.dzf.domain.PageBean;
import com.dzf.domain.Product;

public interface ProductService {

	List<Product> findHotProduct() throws Exception;

	List<Product> findNewProduct()throws Exception;

	Product getById(String pid)throws Exception;

	PageBean<Product> getProductByPageBean(int currPage, int pageSize, String cid)throws Exception;

	List<Product> findAll()throws Exception;

	void update(Product p)throws Exception;

	void deleteById(String pid)throws Exception;

	void add(Product p)throws Exception;

	PageBean<Product> getProductByPageBean(int currPage, int pageSize)throws Exception;

}
