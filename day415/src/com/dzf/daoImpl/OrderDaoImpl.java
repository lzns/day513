package com.dzf.daoImpl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.dzf.dao.OrderDao;
import com.dzf.domain.Order;
import com.dzf.domain.OrderItem;
import com.dzf.domain.Product;
import com.dzf.utils.C3P0Utils;

public class OrderDaoImpl implements OrderDao {

	/**
	 * 向order中插入数据
	 */
	@Override
	public void add(Order order) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?,?,? )";
		qr.update(C3P0Utils.getConnection(), sql, order.getOid(), order.getOrdertime(), order.getTotal(),
				order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid());
	}

	/**
	 * 向orderItem表中添加数据
	 */
	@Override
	public void addItems(OrderItem orderItem) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		qr.update(C3P0Utils.getConnection(), sql, orderItem.getItemid(), orderItem.getCount(), orderItem.getSubtotal(),
				orderItem.getProduct().getPid(), orderItem.getOrder().getOid());
	}

	/**
	 * 查询总共的订单数通过uid
	 */
	@Override
	public int getCount(String uid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select count(*) from orders where uid = ? ";
		return ((Long) qr.query(sql, new ScalarHandler(), uid)).intValue();
	}

	/**
	 * 分页查询订单列表
	 */
	@Override
	public List<Order> getList(String uid, int currPage, int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from orders where uid = ? order by ordertime limit ?,?";
		List<Order> list = qr.query(sql, new BeanListHandler<>(Order.class), uid,(currPage-1)*pageSize,pageSize);
		
		for (Order order : list) {
			sql = "select * from orderitem oi,product p where oi.pid = p.pid and oi.oid = ?";
			List<Map<String, Object>> map = qr.query(sql, new MapListHandler(),order.getOid());
			
			//封装order
			for (Map<String, Object> map2 : map) {
				//封装商品
				Product p = new Product();
				BeanUtils.populate(p, map2);
				//封装orderItem
				OrderItem oi = new OrderItem();
				BeanUtils.populate(oi, map2);
				oi.setProduct(p);
				//封装order中的list<OrderItem>
				order.getItems().add(oi);
			}
		}

		return list;
	}

	/**
	 * 根据订单的id查询一条订单
	 */
	@Override
	public Order getById(String oid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from orders where oid=?";
		Order order = qr.query(sql, new BeanHandler<>(Order.class),oid);
		//封装order中的orderitems
		sql="select * from orderitem oi,product p where oi.pid=p.pid and oi.oid=?";
		List<Map<String, Object>> map = qr.query(sql,new MapListHandler(),oid);
		for (Map<String, Object> map2 : map) {
			//封装product
			Product  p = new Product();
			BeanUtils.populate(p, map2);
			//封装orderItem
			OrderItem oi = new OrderItem();
			BeanUtils.populate(oi, map2);
			//封装order中的list
			oi.setProduct(p);
			order.getItems().add(oi);
		}
		return order;
	}

	/**
	 * 更新数据通过order
	 */
	@Override
	public void update(Order order) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "update orders set address= ?,state=?,telephone=?,name=? where oid = ?";
		qr.update(sql,order.getAddress(),order.getState(),order.getTelephone(),
				order.getName(),order.getOid());
	}

	/**
	 * 查询所有的订单
	 */
	@Override
	public List<Order> findAll(String state) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "";
		if(state==null||state==""){
			sql = "select * from orders order by ordertime";
			return qr.query(sql, new BeanListHandler<>(Order.class));
		}else{
			sql="select * from orders where state=? order by ordertime";
			return qr.query(sql, new BeanListHandler<>(Order.class),Integer.parseInt(state));
		}
	}

	/**
	 * 更改订单的状态
	 */
	@Override
	public void updateState(String oid, String state) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql="update orders set state=? where oid=?";
		qr.update(sql,state,oid);
	}

}
