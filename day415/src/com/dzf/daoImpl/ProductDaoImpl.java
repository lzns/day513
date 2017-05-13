package com.dzf.daoImpl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.dzf.dao.ProductDao;
import com.dzf.domain.Product;
import com.dzf.utils.C3P0Utils;

public class ProductDaoImpl implements ProductDao{
	
	/**
	 * 查询热门商品
	 */
	@Override
	public List<Product> findHotProduct() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product where is_hot = 1 order by pid limit 9";
		return qr.query(sql,new BeanListHandler<>(Product.class));
	}

	/**
	 * 查询最新商品
	 */
	@Override
	public List<Product> findNewProduct() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product  order by pdate limit 9";
		return qr.query(sql,new BeanListHandler<>(Product.class));
	}

	/**
	 * 查询单个商品
	 */
	@Override
	public Product getById(String pid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product  where pid = ? limit 1";
		return qr.query(sql, new BeanHandler<>(Product.class),pid);
	}

	/**
	 * 查询对应类的总条数
	 */
	@Override
	public int getCount(String cid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select count(*) from product where cid = ?";
		
		return ((Long)qr.query(sql, new ScalarHandler(),cid)).intValue();
	}

	/**
	 * 查询对应分类的商品
	 */
	@Override
	public List<Product> getProudct(int currPage, int pageSize, String cid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product where cid = ? limit ?,?";
		return qr.query(sql,new BeanListHandler<>(Product.class),cid,(currPage-1)*pageSize,pageSize);
	}

	/**
	 * 更新商品的分类，为删除分类做准备
	 */
	@Override
	public void setNull(String cid) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql="update product set cid= ? where cid=?";
		qr.update(C3P0Utils.getConnection(),sql,null,cid);
	}

	/**
	 * 查询所有的商品
	 */
	@Override
	public List<Product> findAll() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product order by pdate desc";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	/**
	 * 更新商品
	 */
	@Override
	public void update(Product p) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "update product set pname=?,market_price=?,shop_price=?,pimage=?,pdate=?"
				+ ",is_hot=?,pdesc=?,pflag=?,cid=? where pid= ?";
		qr.update(sql,p.getPname(),p.getMarket_price(),p.getShop_price(),p.getPimage(),p.getPdate(),p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCategory().getCid(),p.getPid());
	}

	/**
	 * 删除指定的商品通过pid
	 */
	@Override
	public void deleteById(String pid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "delete from product where pid = ?";
		qr.update(sql,pid);
	}
	/**
	 * 添加商品
	 */
	@Override
	public void add(Product p) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		qr.update(sql,p.getPid(),p.getPname(),p.getMarket_price(),p.getShop_price(),p.getPimage(),p.getPdate(),p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCategory().getCid());
	}

	/**
	 * 查询商品的总条数
	 */
	@Override
	public int getCount() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select count(*) from product ";
		return ((Long)qr.query(sql, new ScalarHandler())).intValue();
	}

	/**
	 * 分页查询所有的商品
	 */
	@Override
	public List<Product> getProudct(int currPage, int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from product order by pdate limit ?,?";
		return qr.query(sql, new BeanListHandler<>(Product.class),(currPage-1)*pageSize,pageSize);
	}

}
