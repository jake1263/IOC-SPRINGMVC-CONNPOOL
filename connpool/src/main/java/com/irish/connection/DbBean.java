package com.irish.connection;

//外部配置文件信息
public class DbBean {

	/* 链接属性 */
	private String driverName = "com.mysql.jdbc.Driver";

	private String url = "jdbc:mysql://localhost:3306/test";

	private String userName = "root";

	private String password = "root";

	private int minIdle  = 5; // 空闲最小连接数

	private int maxIdle = 10; // 空闲最大连接数

	private int initialSize = 5;// 初始化连接数

	private long connTimeOut = 1000;// 重复获得连接的频率

	private int maxActive = 100;// 最大允许的连接数

	private long connectionTimeOut = 1000 * 60 * 20;// 连接超时时间，默认20分钟

	
	public String getDriverName() {
		return driverName;
	}

	public String getUrl() {
		return url;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public long getConnTimeOut() {
		return connTimeOut;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public long getConnectionTimeOut() {
		return connectionTimeOut;
	}

	
}
