package com.dzf.dao;

import java.sql.SQLException;

import com.dzf.domain.User;

public interface UserDao {

	void register(User user) throws Exception;

	int udpateUserStatu(String code)throws Exception;

	User loginByUsernameAndPassword(String username, String password)throws Exception;



}
