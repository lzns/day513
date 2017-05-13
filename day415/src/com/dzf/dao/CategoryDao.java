package com.dzf.dao;

import java.sql.SQLException;
import java.util.List;

import com.dzf.domain.Category;

public interface CategoryDao {

	List<Category> get() throws Exception ;

	void add(Category category)throws Exception ;

	void delete(String cid)throws Exception ;

	Category getById(String cid)throws Exception ;

	void update(String cid, String cname)throws Exception ;


}
