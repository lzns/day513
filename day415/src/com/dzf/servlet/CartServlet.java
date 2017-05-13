package com.dzf.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dzf.domain.Cart;
import com.dzf.domain.CartItem;
import com.dzf.domain.Product;
import com.dzf.service.ProductService;
import com.dzf.utils.BeanFactory;

/**
 * 购物车模块
 */
public class CartServlet extends BaseServlet {
	public Cart getCart(HttpServletRequest request){
		Cart  cart = (Cart) request.getSession().getAttribute("cart");
		if(cart==null){
		 cart = new Cart();
		 request.getSession().setAttribute("cart", cart);
		}
		return cart;
	}
	/**
	 * 将商品添加到购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		String pid = request.getParameter("pid");
		int count= Integer.parseInt(request.getParameter("count"));
		//通过pid查询商品
		ProductService service = (ProductService) BeanFactory.getBean("ProductService") ;
		try {
			Product product = service.getById(pid);
			//购物车项拼接
			CartItem citem = new CartItem(product, count);
			//添加到购物车
			getCart(request).add(citem);
			//重定向到购物车页面
			response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	/**
	 * 删除购物车内指定的商品
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		getCart(request).remove(pid);
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getCart(request).clear();
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}

}
