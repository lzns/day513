package com.dzf.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.PageBean;
import com.dzf.domain.Product;
import com.dzf.service.ProductService;
import com.dzf.serviceImpl.ProductServiceImpl;
import com.dzf.utils.CookUtils;

/**
 * 商品实体类
 * 
 * @author adminstrtor
 *
 */
public class ProductServlet extends BaseServlet {

	/**
	 * 查询单个商品的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 根据商品的id查询商品的信息
		String pid = request.getParameter("pid");
		ProductService service = new ProductServiceImpl();
		Product product = null;
		try {
			product = service.getById(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 扩展,浏览记录
		Cookie cookie = CookUtils.getCookieByName("ProductList", request.getCookies());
		String buff = "";
		String imgPath=product.getPimage();
		
		if (cookie == null) {
			buff = imgPath;
		} else {
			List<String> asList = Arrays.asList(cookie.getValue().split("-"));
			LinkedList<String> list = new LinkedList<>(asList);
			if (list.contains(imgPath)) {
				list.remove(imgPath);
				list.addFirst(imgPath);
			} else {
				if (list.size() >= 3) {
					list.removeLast();
					list.addFirst(imgPath);
				} else {
					list.addFirst(imgPath);
				}
			}
			for (String string : list) {
				buff += string + "-";
			}
			buff = buff.substring(0, buff.length() - 1);
		}
		Cookie cooki = new Cookie("ProductList", buff);
		cooki.setMaxAge(3600 * 24);
		cooki.setPath(request.getContextPath() + "/");
		response.addCookie(cooki);

		request.setAttribute("bean", product);
		return "/jsp/product_info.jsp";
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String getProductByPageBean(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取商品分类的id
		String cid = request.getParameter("cid");
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		int pageSize = 12;

		ProductService service = new ProductServiceImpl();
		PageBean<Product> pagebean = null;
		try {
			pagebean = service.getProductByPageBean(currPage, pageSize, cid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("bean", pagebean);
		return "/jsp/product_list.jsp";
	}

}
