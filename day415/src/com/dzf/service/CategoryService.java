package com.dzf.service;

import java.util.List;

import com.dzf.domain.Category;

public interface CategoryService {

	List<Category> get() throws Exception;

	void add(Category category)throws Exception;

	void delete(String cid)throws Exception;

	Category getById(String cid)throws Exception;

	void update(String cid, String cname)throws Exception;

}
