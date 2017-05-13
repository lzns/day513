package com.dzf.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.internal.compiler.env.ISourceType;

import com.dzf.domain.Category;
import com.dzf.domain.PageBean;
import com.dzf.domain.Product;
import com.dzf.service.ProductService;
import com.dzf.serviceImpl.ProductServiceImpl;
import com.dzf.utils.BeanFactory;
import com.dzf.utils.UUIDUtils;
import com.dzf.utils.UploadUtils;

/**
 * 后台商品管理模块
 */
public class AdminProductServlet extends BaseServlet {
	/**
	 * 展示所有的商品
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String productShow(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取商品分类的当前页码
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		int pageSize = 8;
		ProductService service = (ProductService) BeanFactory.getBean("ProductService");
		PageBean<Product> pagebean = null;
		try {
			pagebean = service.getProductByPageBean(currPage, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("bean", pagebean);
		return "/admin/product/list.jsp";
		// 调用service层完成商品展示功能
		/*
		 * ProductService product = (ProductService)
		 * BeanFactory.getBean("ProductService"); List<Product> p =
		 * product.findAll(); request.setAttribute("product", p); return
		 * "/admin/product/list.jsp";
		 */
	}

	/**
	 * 通过pid查询商品的信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pid = request.getParameter("pid");

		// 调用service层完成商品展示功能
		ProductService product = (ProductService) BeanFactory.getBean("ProductService");
		Product p = product.getById(pid);
		request.setAttribute("product", p);
		return "/admin/product/edit.jsp";
	}

	/**
	 * 更新商品信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<>();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		List<FileItem> list = fileUpload.parseRequest(request);
		for (FileItem fileItem : list) {
			if (fileItem.isFormField()) {
				// 判断是普通的组件
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			} else {
				// 是文件上传组件
				String filename = fileItem.getName();
				// 获取文件的真实名称
				String realName = UploadUtils.getRealName(filename);
				// 获取随机名称
				String uuidName = UploadUtils.getUUIDName(realName);
				String path = this.getServletContext().getRealPath("/products/1/");
				InputStream is = fileItem.getInputStream();
				FileOutputStream out = new FileOutputStream(new File(path, uuidName));
				IOUtils.copy(is, out);
				out.close();
				is.close();
				fileItem.delete();
				map.put(fileItem.getFieldName(), "products/1/" + uuidName);
			}
		}
		// 封装数据
		Product p = new Product();
		BeanUtils.populate(p, map);
		p.setPdate(new Date());
		Category c = new Category();
		c.setCid((String) (map.get("cid")));
		p.setCategory(c);
		System.out.println(p.toString());
		// 调用service层完成商品更新的功能
		ProductService service = (ProductService) BeanFactory.getBean("ProductService");
		service.update(p);
		// 页面重定向到商品展示的页面
		response.sendRedirect(request.getContextPath() + "/adminProduct?method=productShow");
		return null;
	}

	/**
	 * 删除指定的商品，通过pid
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取参数
		String pid = request.getParameter("pid");
		// 调用service层完成删除操作
		ProductService service = (ProductService) BeanFactory.getBean("ProductService");
		service.deleteById(pid);
		response.sendRedirect(request.getContextPath() + "/adminProduct?method=productShow");
		return null;
	}

	/**
	 * 跳转到商品添加页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return "/admin/product/add.jsp";
	}

	/**
	 * 添加商品
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return "/admin/product/add.jsp";
	}

	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		List<FileItem> list = fileUpload.parseRequest(request);
		for (FileItem fileItem : list) {
			if (fileItem.isFormField()) {
				// 判断是普通的组件
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			} else {
				// 是文件上传组件
				String filename = fileItem.getName();
				// 获取文件的真实名称
				String realName = UploadUtils.getRealName(filename);
				// 获取随机名称
				String uuidName = UploadUtils.getUUIDName(realName);
				String path = this.getServletContext().getRealPath("/products/1/");
				InputStream is = fileItem.getInputStream();
				FileOutputStream out = new FileOutputStream(new File(path, uuidName));
				IOUtils.copy(is, out);
				out.close();
				is.close();
				fileItem.delete();
				map.put(fileItem.getFieldName(), "products/1/" + uuidName);
			}
		}
		// 封装数据
		Product p = new Product();
		BeanUtils.populate(p, map);
		// 设置pid
		p.setPid(UUIDUtils.getId());
		// 设置日期
		p.setPdate(new Date());
		// 设置分类
		Category c = new Category();
		c.setCid((String) (map.get("cid")));
		p.setCategory(c);
		System.out.println(p.toString());
		// 调用service层完成商品更新的功能
		ProductService service = (ProductService) BeanFactory.getBean("ProductService");
		service.add(p);
		// 页面重定向到商品展示的页面
		response.sendRedirect(request.getContextPath() + "/adminProduct?method=productShow");
		return null;
	}
}
