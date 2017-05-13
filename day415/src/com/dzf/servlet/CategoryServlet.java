package com.dzf.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.Category;
import com.dzf.service.CategoryService;
import com.dzf.serviceImpl.CategoryServiceImpl;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.JsonUtil;

import net.sf.json.util.JSONUtils;

/**
 * 商品分类实体类
 */
public class CategoryServlet extends BaseServlet {
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//到数据库中查询数据并将数据返回
		//查询分类信息
		CategoryService cate1 = new CategoryServiceImpl();
		CategoryService cate=(CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> clist = cate.get();
		//将数据写回到首页：
//		request.setAttribute("clist", clist);
		//将数据转为json数据格式，
		String json = JsonUtil.list2json(clist);
		//设置编码
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(json);
		return null;
	}
}
