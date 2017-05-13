package com.dzf.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.Category;
import com.dzf.service.CategoryService;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.UUIDUtils;

/**
 * 后台分类展示模板
 */
public class AdminCategoryServlet extends BaseServlet {
	
	/**
	 * 展示所有的分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String categoryShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService category = (CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> clist;
		try {
			clist = category.get();
			request.setAttribute("clist",clist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/admin/category/list.jsp";
	}
	/**
	 * 跳转到修改分类的页面上
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/admin/category/add.jsp";
	}
	/**
	 *通过cid获取指定的分类
	 * @throws Exception 
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取参数
		String cid = request.getParameter("cid");
		//调用service完成查询操作
		CategoryService cate = (CategoryService) BeanFactory.getBean("CategoryService");
		Category category=cate.getById(cid);
		//将对象放入到域对象中
		request.setAttribute("category", category);
		return "/admin/category/edit.jsp";
	}
	
	/**
	 * 删除指定的分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取分类的ID
		String cid = request.getParameter("cid");
		//调用service完成删除操作
		CategoryService cate = (CategoryService) BeanFactory.getBean("CategoryService");
		try {
			cate.delete(cid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//页面重定向到分类展示的页面上去
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=categoryShow");
		return null;
	}
	/**
	 *添加分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//封装cagtegory
		String cname = request.getParameter("cname");
		Category category = new Category();
		category.setCid(UUIDUtils.getId());
		category.setCname(cname);
		//调用service完成添加分类操作
		CategoryService cate = (CategoryService) BeanFactory.getBean("CategoryService");
		try {
			cate.add(category);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//页面重定向分类展示页面
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=categoryShow");
		return null;
	}
	/**
	 * 通过cid完成分类的更新操作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取参数
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		//调用service层完成分类更新操作
		CategoryService cate = (CategoryService) BeanFactory.getBean("CategoryService");
		cate.update(cid,cname);
		//页面重定向到分类展示的页面
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=categoryShow");
		return null;
	}
}
