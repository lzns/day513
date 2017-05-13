package com.dzf.servlet;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzf.domain.Cart;
import com.dzf.domain.CartItem;
import com.dzf.domain.Order;
import com.dzf.domain.OrderItem;
import com.dzf.domain.PageBean;
import com.dzf.domain.User;
import com.dzf.service.OrderService;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.PaymentUtil;
import com.dzf.utils.UUIDUtils;

/**
 * 订单模块
 */
public class OrderServlet extends BaseServlet {

	/**
	 * 生成订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 0.判断用户是否登录
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			request.setAttribute("msg", "请您先登录");
			return "/jsp/msg.jsp";
		}
		// 封装数据order
		Order order = new Order();
		// 1.设置id
		order.setOid(UUIDUtils.getId());
		// 2.设置订单生成的时间
		order.setOrdertime(new Date());
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		// 3.设置订单的总价
		order.setTotal(cart.getTotalMoney());
		// 4.设置订单的订单项
		/**
		 * 订单项的属性
		 */
		// 遍历购物车项
		List<OrderItem> item = order.getItems();
		Collection<CartItem> items = cart.getItems();
		for (CartItem cartItem : items) {
			OrderItem oi = new OrderItem();
			// 订单项的 itemid
			oi.setItemid(UUIDUtils.getCode());
			// 订单项的count
			oi.setCount(cartItem.getCount());
			// 设置小计
			oi.setSubtotal(cartItem.getSubtotal());
			// 设商品
			oi.setProduct(cartItem.getProduct());
			// 设置订单
			oi.setOrder(order);

			// 将订单项添加到集合中去
			item.add(oi);
		}

		// 5.设置用户
		order.setUser(user);
		// 调用service完成将订单插入到数据库中去
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		service.add(order);

		// 将order放入到session中，重定向到order-infor.jsp zhogn
		request.setAttribute("order", order);
		// 将购物车清空
		cart.clear();
		return "/jsp/order_info.jsp";
	}

	/**
	 * 查询订单列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取参数uid
		User user = (User) request.getSession().getAttribute("user");
		String uid = user.getUid();
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		int pageSize = 3;
		// 调用service
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		PageBean<Order> pagebean = service.findAll(uid, currPage, pageSize);
		// 将pagebean放回到request域中
		request.setAttribute("page", pagebean);

		return "/jsp/order_list.jsp";
	}

	/**
	 * 查询单个订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取参数
		String oid = request.getParameter("oid");
		// 调用service
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		Order order = service.getById(oid);
		// 将pagebean放回到request域中
		request.setAttribute("order", order);
		return "/jsp/order_info.jsp";
	}

	/**
	 * 付款
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取参数
		String oid = request.getParameter("oid");
		// 调用service 完成
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		Order order = service.getById(oid);

		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");
		String name = request.getParameter("name");
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		// 将数据库中数据进行更新
		service.update(order);

		// 组织发送支付公司需要哪些数据
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = order.getTotal().toString();
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		// 发送给第三方
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);

		response.sendRedirect(sb.toString());

		return null;

	}

	public String callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 身份校验 --- 判断是不是支付公司通知你
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");

		// 自己对上面数据进行加密 --- 比较支付公司发过来hamc
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid,
				r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 响应数据有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				System.out.println("111");
				request.setAttribute("msg", "您的订单号为:" + r6_Order + ",金额为:" + r3_Amt + "已经支付成功,等待发货~~");

			} else if (r9_BType.equals("2")) {
				// 服务器点对点 --- 支付公司通知你
				System.out.println("付款成功！222");
				// 修改订单状态 为已付款
				// 回复支付公司
				response.getWriter().print("success");
			}

			// 修改订单状态
			OrderService s = (OrderService) BeanFactory.getBean("OrderService");
			Order order = s.getById(r6_Order);
			order.setState(1);
			
			s.update(order);

		} else {
			// 数据无效
			System.out.println("数据被篡改！");
		}

		return "/jsp/msg.jsp";

	}
	/**
	 * 确认收货
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取参数
		String oid = request.getParameter("oid");
		String state = "3";
		//调用service完成更新操作
		OrderService service = (OrderService) BeanFactory.getBean("OrderService");
		service.updateState(oid, state);
		//页面重定向到订单展示页面
		response.sendRedirect(request.getContextPath()+"/order?method=findAll&currPage=1");
		return null;
	}
}
