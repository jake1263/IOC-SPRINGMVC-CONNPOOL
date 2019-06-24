package com.irish.spring.ext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.irish.spring.extannotation.ExtAutowire;
import com.irish.spring.extannotation.ExtService;
import com.irish.utils.ClassUtil;

// 手写SpringIOC 注解版本
public class ExtWebApplicationContext {
	
	// 扫包的范围
	private String packageName;

	// springbean容器
	private ConcurrentHashMap<String, Object> beans = null;

	public ExtWebApplicationContext(String packageName) throws Exception {
		beans = new ConcurrentHashMap<String, Object>();
		this.packageName = packageName;
		initBeans();
		initEntryField();
	}

	// 初始化属性
	private void initEntryField() throws Exception {
		// 1.遍历所有的bean容器对象
		for (Entry<String, Object> entry : beans.entrySet()) {
			// 2.判断属性上面是否有加注解
			Object bean = entry.getValue();
			attriAssign(bean);
		}

	}

	public Object getBean(String beanId) throws Exception {
		if (StringUtils.isEmpty(beanId)) {
			throw new Exception("beanId参数不能为空");
		}
		Object object = beans.get(beanId);
		return object;
	}

	// 初始化对象
	public Object newInstance(Class<?> classInfo)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return classInfo.newInstance();
	}

	// 初始化对象
	public void initBeans() throws Exception {
		// 1.使用java的反射机制扫包,获取当前包下所有的类
		List<Class<?>> classes = ClassUtil.getClasses(packageName);
		// 2.判断类上是否存在注入bean的注解
		ConcurrentHashMap<String, Object> classExisAnnotation = findClassExisAnnotation(classes);
		if (classExisAnnotation == null || classExisAnnotation.isEmpty()) {
			throw new Exception("该包下没有任何类加上注解");
		}

	}

	// 判断类上是否存在注入bean的注解
	public ConcurrentHashMap<String, Object> findClassExisAnnotation(List<Class<?>> classes) throws Exception {
		for (Class<?> classInfo : classes) {
			// 判断类上是否有注解
			ExtService annotation = classInfo.getAnnotation(ExtService.class);
			if (annotation != null) {
				// 获取当前类名
				String className = classInfo.getSimpleName();
				// 将当前类名变为小写
				String beanId = toLowerCaseFirstOne(className);
				Object newInstance = newInstance(classInfo);
				beans.put(beanId, newInstance);
			}
		}
		return beans;
	}

	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	// 依赖注入注解原理
	public void attriAssign(Object object) throws Exception {

		// 1.使用反射机制,获取当前类的所有属性
		Class<? extends Object> classInfo = object.getClass();
		Field[] declaredFields = classInfo.getDeclaredFields();
		
		// 2.判断当前类属性是否存在注解
		for (Field field : declaredFields) {
			ExtAutowire extResource = field.getAnnotation(ExtAutowire.class);
			if (extResource != null) {
				// 获取属性名称
				String beanId = field.getName();
				Object bean = getBean(beanId);
				if (bean != null) {
					// 3.默认使用属性名称，查找bean容器对象 1参数 当前对象 2参数给属性赋值
					field.setAccessible(true); // 允许访问私有属性
					field.set(object, bean);
				}

			}
		}

	}

}
