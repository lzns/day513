package com.dzf.serviceImpl;

import com.dzf.dao.UserDao;
import com.dzf.daoImpl.UserDaoImpl;
import com.dzf.domain.User;
import com.dzf.service.UserService;
import com.dzf.utils.MailUtils;

public class UserServiceImpl implements UserService {

	/**
	 * 用户注册
	 */
	@Override
	public void register(User user) throws Exception {
		UserDao dao = new UserDaoImpl();
		dao.register(user);
		//放送邮件
		
		String emailMsg="<a href='http://localhost:8080/day415/user?method=active&code="+user.getCode()+"'>请点击激活</a>";
		MailUtils.sendMail(user.getEmail(), emailMsg);
	}

	/**
	 * 用户激活
	 */
	@Override
	public int updateUserStatu(String code) throws Exception {
		UserDao dao = new UserDaoImpl();
		return dao.udpateUserStatu(code);
	}

	/**
	 * 用户登录
	 */
	@Override
	public User login(String username, String password) throws Exception {
		UserDao dao = new UserDaoImpl();
		return dao.loginByUsernameAndPassword(username,password);
		
	}

}
