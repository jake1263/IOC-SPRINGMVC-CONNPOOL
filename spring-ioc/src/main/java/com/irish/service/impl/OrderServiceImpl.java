package com.irish.service.impl;

import com.irish.service.OrderService;
import com.irish.spring.extannotation.ExtService;

@ExtService
public class OrderServiceImpl implements OrderService {
	public void addOrder() {
		System.out.println("addOrder");
	}
}
