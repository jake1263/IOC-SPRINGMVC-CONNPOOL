package com.irish;

import com.irish.service.UserService;
import com.irish.spring.ext.ExtWebApplicationContext;

public class Test {

	public static void main(String[] args) throws Exception {
		ExtWebApplicationContext app = new ExtWebApplicationContext("com.irish.service.impl");
		UserService userService = (UserService) app.getBean("userServiceImpl");
		userService.add();
		System.out.println(userService);
	}

}
