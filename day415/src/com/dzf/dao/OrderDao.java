package com.dzf.dao;

import java.util.List;

import com.dzf.domain.Order;
import com.dzf.domain.OrderItem;

public interface OrderDao {

	void add(Order order)throws Exception;

	void addItems(OrderItem orderItem)throws Exception;

	int getCount(String uid)throws Exception;

	List<Order> getList(String uid, int currPage, int pageSize)throws Exception;

	Order getById(String oid)throws Exception;

	void update(Order order)throws Exception;

	List<Order> findAll(String state)throws Exception;

	void updateState(String oid, String state)throws Exception;

}
