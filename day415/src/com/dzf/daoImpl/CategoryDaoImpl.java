package com.dzf.daoImpl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.dzf.dao.CategoryDao;
import com.dzf.domain.Category;
import com.dzf.utils.C3P0Utils;

public class CategoryDaoImpl implements CategoryDao{

	/**
	 * 查询商品分类
	 * @throws SQLException 
	 */
	@Override
	public List<Category> get() throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from category ";
		return qr.query(sql, new BeanListHandler<>(Category.class));
	}

	/**
	 * 添加商品分类
	 */
	@Override
	public void add(Category category) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "insert into category values(?,?)";
		qr.update(sql,category.getCid(),category.getCname());
	}

	/**
	 * 删除指定的分类，通过cid
	 */
	@Override
	public void delete(String cid) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql = "delete from category where cid = ? ";
		qr.update(C3P0Utils.getConnection(),sql,cid);
	}

	/**
	 * 通过cid查询单个分类
	 */
	@Override
	public Category getById(String cid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from category where cid = ? limit 1";
		return qr.query(sql, new BeanHandler<>(Category.class),cid);
	}

	/**
	 * 通过cname和cid完成分类的更新操作
	 */
	@Override
	public void update(String cid, String cname) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "update category set cname = ? where cid =? ";
		qr.update(sql,cname,cid);
	}


}
