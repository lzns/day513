package com.dzf.serviceImpl;

import java.util.List;

import com.dzf.dao.ProductDao;
import com.dzf.daoImpl.ProductDaoImpl;
import com.dzf.domain.PageBean;
import com.dzf.domain.Product;
import com.dzf.service.ProductService;
import com.dzf.utils.BeanFactory;

public class ProductServiceImpl implements ProductService {

	/**
	 * 查询热门的商品
	 */
	@Override
	public List<Product> findHotProduct() throws Exception {
		ProductDao  dao= new ProductDaoImpl();
		return dao.findHotProduct();
	}

	/**
	 * 查询最新商品
	 */
	@Override
	public List<Product> findNewProduct() throws Exception {
		ProductDao  dao= new ProductDaoImpl();
		return dao.findNewProduct();
	}

	/**
	 * 查询商品
	 */
	@Override
	public Product getById(String pid) throws Exception {
		ProductDao  dao= new ProductDaoImpl();
		return dao. getById( pid);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PageBean<Product> getProductByPageBean(int currPage, int pageSize, String cid) throws Exception {
		ProductDao  dao= new ProductDaoImpl();
		//获取总条数
		int totalCount = dao.getCount(cid);
		//获取内容
		List<Product> list = dao.getProudct(currPage,pageSize,cid);
		return new PageBean<>(list,currPage,pageSize,totalCount);
	}

	/**
	 * 查询所有的商品
	 */
	@Override
	public List<Product> findAll() throws Exception {
		ProductDao dao = (ProductDao) BeanFactory.getBean("ProductDao");
	
		return dao.findAll();
	}

	/**
	 * 更新商品
	 */
	@Override
	public void update(Product p) throws Exception {
		ProductDao dao = (ProductDao) BeanFactory.getBean("ProductDao");
		dao.update(p);
	}

	/**
	 * 删除指定的商品
	 */
	@Override
	public void deleteById(String pid) throws Exception {
		ProductDao dao = (ProductDao) BeanFactory.getBean("ProductDao");
		dao.deleteById(pid);
	}

	/**
	 * 添加商品
	 */
	@Override
	public void add(Product p) throws Exception {
		ProductDao dao = (ProductDao) BeanFactory.getBean("ProductDao");
		dao.add(p);
	}

	/**
	 * 分页查询所有的商品
	 */
	@Override
	public PageBean<Product> getProductByPageBean(int currPage, int pageSize) throws Exception {
		ProductDao  dao= new ProductDaoImpl();
		//获取总条数
		int totalCount = dao.getCount();
		//获取内容
		List<Product> list = dao.getProudct(currPage,pageSize);
		
		return new PageBean<>(list, currPage, pageSize, totalCount);
	}

}
