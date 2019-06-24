package com.irish.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Vector;


public class ConnectionPool implements IConnectionPool {
	// 使用线程安全的集合  空闲连接集合 没有被使用的连接存放
	private List<Connection> freeConnection = new Vector<Connection>();
	// 使用线程安全的集合  活动连接集合 容器 容器正在使用的连接
	private List<Connection> activeConnection = new Vector<Connection>();
	//数据库连接配置属性
	private DbBean dbBean;
	//当前活动连接总数	
	private int curActiveConnecCount = 0;

	public ConnectionPool(DbBean dbBean) {
		// 获取配置文件信息
		this.dbBean = dbBean;
		init();
	}

	// 初始化线程池(初始化空闲线程)
	private void init() {
		if (dbBean == null) {
			return;// 注意最好抛出异常
		}
		// 1.获取初始化连接数
		for (int i = 0; i < dbBean.getInitialSize(); i++) {
			// 2.创建Connection连接
			Connection newConnection = newConnection();
			if (newConnection != null) {
				// 3.存放在freeConnection集合
				freeConnection.add(newConnection);
			}
		}

	}

	// 创建Connection连接
	private synchronized Connection newConnection() {
		try {
			Class.forName(dbBean.getDriverName());
			Connection connection = DriverManager.getConnection(dbBean.getUrl(), dbBean.getUserName(),
					dbBean.getPassword());
			curActiveConnecCount++;
			return connection;
		} catch (Exception e) {
			return null;
		}

	}

	// 调用getConnection方法 --- 获取连接
	public synchronized Connection getConnection() {

		try {
			Connection connection = null;
			// 思考：怎么知道当前创建的连接>最大连接数
			if (curActiveConnecCount < dbBean.getMaxActive()) {
				// 小于最大活动连接数
				// 1.判断空闲线程是否有数据
				if (freeConnection.size() > 0) {
					connection = freeConnection.remove(0);
				} else {
					// 创建新的连接
					connection = newConnection();
				}
				// 判断连接是否可用
				boolean available = isAvailable(connection);
				if (available) {
					// 存放在活动线程池
					activeConnection.add(connection);
					curActiveConnecCount++;
				} else {
					connection = getConnection();// 怎么使用重试？ 递归算法
				}

			} else {
				// 大于最大活动连接数，进行等待,这里是阻塞1秒后，往下执行
				wait(dbBean.getConnTimeOut());
				// 重试
				connection = getConnection();
			}
			return connection;
		} catch (Exception e) {
			return null;
		}

	}

	// 判断连接是否可用
	public boolean isAvailable(Connection connection) {
		try {
			if (connection == null || connection.isClosed()) {
				return false;
			}
		} catch (Exception e) {
		}
		return true;

	}

	// 释放连接 回收
	public synchronized void releaseConnection(Connection connection) {
		try {
			// 1.判断连接是否可用
			if (isAvailable(connection)) {
				// 2.判断空闲线程是否已满
				if (freeConnection.size() < dbBean.getMaxIdle()) {
					// 空闲线程没有满
					freeConnection.add(connection);// 回收连接
				} else {
					// 空闲线程已经满
					connection.close();
				}
				activeConnection.remove(connection);
				curActiveConnecCount--;
				notifyAll();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
