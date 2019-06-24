package com.irish.service.impl;

import com.irish.service.OrderService;
import com.irish.service.UserService;
import com.irish.spring.extannotation.ExtAutowire;
import com.irish.spring.extannotation.ExtService;

//user 服务层
@ExtService
public class UserServiceImpl implements UserService {

	@ExtAutowire
	private OrderService orderServiceImpl;

	public void add() {
		orderServiceImpl.addOrder();
		System.out.println("使用java反射机制初始化对象..");
	}

}
