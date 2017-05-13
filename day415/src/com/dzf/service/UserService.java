package com.dzf.service;

import com.dzf.domain.User;

public interface UserService {

	void register(User user) throws Exception;

	int updateUserStatu(String code) throws Exception;

	User login(String username, String password)throws Exception;

}
