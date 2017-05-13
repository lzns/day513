package com.dzf.serviceImpl;

import java.sql.SQLException;
import java.util.List;

import com.dzf.dao.CategoryDao;
import com.dzf.dao.ProductDao;
import com.dzf.domain.Category;
import com.dzf.service.CategoryService;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.C3P0Utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CategoryServiceImpl implements CategoryService {

	/**
	 * 查询所有的分类
	 */
	@Override
	public List<Category> get() throws Exception {
		//创建缓冲管理器
		CacheManager cache = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		
		//获取指定的缓存
		Cache cache2 = cache.getCache("categoryCache");
		//获取缓存中的数据
		Element element = cache2.get("clist");
		//判断数据
		List<Category> list=null;
		if(element==null){
			//去数据中获取数据
			CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");
			list =dao.get();
			System.out.println("没有数据，已经去数据库中获取");
			//将数据放入到缓存中
			cache2.put(new Element("clist", list));
		}else{
			System.out.println("有数据，直接在缓存中获取");
			list = (List<Category>) element.getObjectValue();
		}
		
		return list;
	}

	/**
	 * 添加分类
	 */
	@Override
	public void add(Category category) throws Exception {
		CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");
		dao.add(category);
		//添加成功后要清理缓存
		//1.获取缓冲管理器
		CacheManager create = CacheManager.create();
		//2.获取指定名称的缓存
		Cache cache = create.getCache("categoryCache");
		//3.删除里面的元素
		cache.removeAll();
	}

	/**
	 * 删除指定的分类通过cid
	 * @throws Exception 
	 */
	@Override
	public void delete(String cid) throws Exception {
		try {
			//开启事务
			C3P0Utils.startTransAction();
			//将product中相对应的分类的ID置为null
			ProductDao pdao = (ProductDao) BeanFactory.getBean("ProductDao");
			pdao.setNull(cid);
			//删除category中的分类
			CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");
			dao.delete(cid);
			//关闭事务
			C3P0Utils.commitAndClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//事务回滚
		C3P0Utils.rollBackAndClose();
			throw e;
		}
		//清空缓存
		CacheManager create = CacheManager.create();
		Cache cache = create.getCache("categoryCache");
		cache.removeAll();
		
	}

	/**
	 * 通过cid查询单个分类
	 */
	@Override
	public Category getById(String cid) throws Exception {
		CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");
		
		return dao.getById(cid);
	}

	/**
	 * 通过cname 和 cid 完成更新单个分类操作
	 */
	@Override
	public void update(String cid, String cname) throws Exception {
		CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");
		dao.update(cid,cname);
		//更新缓存
		CacheManager create = CacheManager.create();
		Cache cache = create.getCache("categoryCache");
		cache.removeAll();
	}
}
