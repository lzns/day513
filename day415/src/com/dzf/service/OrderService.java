package com.dzf.service;

import java.util.List;

import com.dzf.domain.Order;
import com.dzf.domain.PageBean;

public interface OrderService {

	void add(Order order)throws Exception;

	PageBean<Order> findAll(String uid, int currPage, int pageSize)throws Exception;

	Order getById(String oid)throws Exception;

	void update(Order order)throws Exception;

	List<Order> findAll(String state)throws Exception;

	void updateState(String oid, String state)throws Exception;


}
