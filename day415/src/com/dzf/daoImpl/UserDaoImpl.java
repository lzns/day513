package com.dzf.daoImpl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.dzf.dao.UserDao;
import com.dzf.domain.User;
import com.dzf.utils.C3P0Utils;

public class UserDaoImpl implements UserDao {

	/**
	 * 用户注册
	 * @throws SQLException 
	 */
	@Override
	public void register(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		qr.update(sql, user.getUid(),user.getUsername(),user.getPassword(),
				user.getName(),user.getEmail(),user.getTelephone(),
				user.getBirthday(),user.getSex(),user.getState(),user.getCode());
		
	}

	/**
	 * 用户激活，修改用户的状态
	 */
	@Override
	public int udpateUserStatu(String code) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "update user set state=? where code =?";
		return qr.update(sql, 1,code);
	}

	/**
	 * 用户登录
	 */
	@Override
	public User loginByUsernameAndPassword(String username, String password) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.GetDataSource());
		String sql = "select * from user where username=? and password =? limit 1";
		return qr.query(sql, new BeanHandler<>(User.class), username,password);
	}

}
