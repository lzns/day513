package com.dzf.serviceImpl;

import java.sql.SQLException;
import java.util.List;

import com.dzf.dao.OrderDao;
import com.dzf.domain.Order;
import com.dzf.domain.OrderItem;
import com.dzf.domain.PageBean;
import com.dzf.service.OrderService;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.C3P0Utils;

public class OrderServiceImpl implements OrderService {

	/**
	 * 生成订单
	 * @throws Exception 
	 */
	@Override
	public void add(Order order) throws Exception  {
		try {
			//开启事务
			C3P0Utils.startTransAction();
			OrderDao dao =(OrderDao) BeanFactory.getBean("OrderDao");
			//向order添加一条数据
			dao.add(order);
			//向orderItem中添加n条数据
			
			//所有的订单项,向orderItem表中添加数据
			for (OrderItem orderItem : order.getItems()) {
				dao.addItems(orderItem);
			}
			//事务提交
			C3P0Utils.commitAndClose();
		} catch (SQLException e) {
			e.printStackTrace();
			C3P0Utils.rollBackAndClose();
			throw e;
		}
	}

	/**
	 * 分页查询订单列表
	 */
	@Override
	public PageBean<Order> findAll(String uid ,int currPage, int pageSize) throws Exception {
		OrderDao dao = (OrderDao) BeanFactory.getBean("OrderDao");
		//查询共有多少条数据
			int totalCount = dao.getCount(uid);
		//查询list数据
		List<Order> list = dao.getList(uid,currPage,pageSize);
		return new PageBean<>(list, currPage, pageSize, totalCount);
	}

	/**
	 * 根据订单的id查询订单
	 */
	@Override
	public Order getById(String oid) throws Exception {
		OrderDao dao = (OrderDao) BeanFactory.getBean("OrderDao");
		return dao.getById(oid);
	}

	/**
	 * 根据order更新数据库中的数据
	 */
	@Override
	public void update(Order order) throws Exception {
		
		OrderDao dao = (OrderDao) BeanFactory.getBean("OrderDao");
		dao.update(order);
	}

	/**
	 * 查询所有的订单
	 */
	@Override
	public List<Order> findAll(String state) throws Exception {
		OrderDao dao = (OrderDao) BeanFactory.getBean("OrderDao");
		return dao.findAll(state);
	}

	/**
	 * 更改订单的状态
	 */
	@Override
	public void updateState(String oid, String state) throws Exception {
		OrderDao dao = (OrderDao) BeanFactory.getBean("OrderDao");
		dao.updateState(oid,state);
	}

}
