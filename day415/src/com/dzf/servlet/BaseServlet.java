package com.dzf.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有具体实体类servlet的父类
 */
public class BaseServlet extends HttpServlet {

	/**
	 * 重写service方法。执行自己的业务逻辑
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class clazz = this.getClass();
			//System.out.println(clazz);
			// 获取方法
			String m = request.getParameter("method");
			
		//	System.out.println(m);
			if(m==null){
				m="index";
			}
			Method method = clazz.getMethod(m, HttpServletRequest.class, HttpServletResponse.class);
			//利用反射的技术，我们来让方法执行
			String str = (String) method.invoke(this,request,response);
			if(str!=null){
				request.getRequestDispatcher(str).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 让每个servlet都有个index方法！
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception 
	 */
	public String index(HttpServletRequest request, HttpServletResponse response)
			throws  Exception {
		return "/jsp/index.jsp";
	}
}
