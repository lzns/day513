package com.dzf.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.dzf.constant.Constant;
import com.dzf.conventer.MyConventer;
import com.dzf.domain.User;
import com.dzf.service.UserService;
import com.dzf.serviceImpl.UserServiceImpl;
import com.dzf.utils.MD5Utils;
import com.dzf.utils.UUIDUtils;

/**
 * 客户实体类
 * 
 * @author adminstrtor
 *
 */
public class UserServlet extends BaseServlet {
	/**
	 * 跳转到注册页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String registerUI(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		return "/jsp/register.jsp";
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 封装数据
		// 数据转换器，将字符串转换为日期类型,注册converter
		ConvertUtils.register(new MyConventer(), Date.class);

		Map<String, String[]> map = request.getParameterMap();
		User user = new User();
		try {
			BeanUtils.populate(user, map);
			// 设置用户的uid
			user.setUid(UUIDUtils.getId());
			// 设置激活码
			user.setCode(UUIDUtils.getCode());
			// 将密码进行加密
			user.setPassword(MD5Utils.md5(user.getPassword()));
			// 调用service完成注册操作
			UserService service = new UserServiceImpl();
			service.register(user);
			request.setAttribute("msg", "用户已完成经注册，请去邮箱激活");
			return "/jsp/msg.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 用户激活
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取激活码
		String code = request.getParameter("code");
		// 根据激活码去修改用户的状态
		UserService service = new UserServiceImpl();
		int i = service.updateUserStatu(code);
		if (i == 1) {
			request.setAttribute("msg", "激活成功");
		} else {
			request.setAttribute("msg", "激活失败");
		}
		return "/jsp/msg.jsp";
	}

	/**
	 * 跳转到登录界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loginUI(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		return "/jsp/login.jsp";
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null || username.trim().length() <= 0 || password.trim().length() <= 0) {
			request.setAttribute("msg", "用户和密码不能为null");
		} else {

			// 将密码进行加密
			password = MD5Utils.md5(password);
			// 调用service完成登录操作
			UserService service = new UserServiceImpl();
			User user = service.login(username, password);
			if (user != null) {
				if (Constant.USER_IS_ACTIVE == user.getState()) {
					// 直接跳转到首页 并显示谁谁已经登录 并将用户放入到session中，重定向到首页
					request.getSession().setAttribute("user", user);

					// 判断用户是够勾选了记住用户名，记住密码，自动登录，
					//如果用户选择le记住用户名，将用户名写入到cookie中，让后通过response返回给浏览器，
					//通过el表达式的内置对象。cookie。cookie的名字，value，获取名字
					//判断用户是选择下次自动登录，如果是，创建一个cookie，用来存用户名和密码，在过滤器中实现这种操作
					// 自动登录使用的是过滤器，过滤不是登录页面和注册的请求，每当请求来的时候，获得指定的cookie，
					// 如果cookie不为null。去数据查查询这个用户，返回一个user,放入到session中，
					response.sendRedirect(request.getContextPath() + "/");
					return null;
				} else {
					// 告诉其账户未激活
					request.setAttribute("msg", "登录失败，请先激活");
				}
			} else {
				// 要是为null 说明此用户不存在，告诉让其先注册
				request.setAttribute("msg", "用户和密码不匹配");
			}
		}
		return "/jsp/login.jsp";
	}

	/**
	 * 用户退出
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.invalidate();
		// 页面重定向
		response.sendRedirect(request.getContextPath() + "/");
		return null;
	}
}
