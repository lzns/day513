package com.dzf.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.Order;
import com.dzf.domain.OrderItem;
import com.dzf.service.OrderService;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.JsonUtil;

/**
 * 后台订单管理模块
 */
public class AdminOrderServlet extends BaseServlet {
	/**
	 * 查询所有的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String findByState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String state = request.getParameter("state");
		//调用service完成查询所有订单操作
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		List<Order> list = service.findAll(state);
//		请求转发
		request.setAttribute("list", list);
		return "/admin/order/list.jsp";
	}
	/**
	 * 查看订单详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDetailById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取oid
		String oid = request.getParameter("oid");
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		List<OrderItem> items = service.getById(oid).getItems();
		String json = JsonUtil.list2json(items);
		response.getWriter().println(json);
		return null;
	}
	/**
	 * 更改订单的状态
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取参数
		String oid = request.getParameter("oid");
		String state = request.getParameter("state");
		//调用service完成更新订单状态操作
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		service.updateState(oid,state);
		//页面重定向到订单展示页面上去
		response.sendRedirect(request.getContextPath()+"/adminOrder?method=findByState&state="+state);
		return null;
	}
}
