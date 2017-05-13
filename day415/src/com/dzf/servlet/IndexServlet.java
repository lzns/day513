package com.dzf.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.Product;
import com.dzf.service.ProductService;
import com.dzf.serviceImpl.ProductServiceImpl;



/**
 * 首页实体类
 */
public class IndexServlet extends BaseServlet {
	/**
	 * 首页上需要展示的内容
	 */
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取热门商品和最新商品
		ProductService product = new ProductServiceImpl();
		List<Product> hList = product.findHotProduct();
		List<Product> nList =product.findNewProduct();
		
		//将数据转发
		request.setAttribute("hList", hList);
		request.setAttribute("nList", nList);
		
		return "/jsp/index.jsp";
	}
	
}
