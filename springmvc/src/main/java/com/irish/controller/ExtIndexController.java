package com.irish.controller;

import com.irish.extspringmvc.extannotation.ExtController;
import com.irish.extspringmvc.extannotation.ExtRequestMapping;

@ExtController
@ExtRequestMapping("/ext")
public class ExtIndexController {
	
	@ExtRequestMapping("/test")
	public String test() {
		System.out.println("手写springmvc框架...");
		return "index";
	}

}
