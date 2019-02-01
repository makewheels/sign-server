package util;

import org.apache.commons.dbutils.QueryRunner;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Apache commons dbutils
 * 
 * @author Administrator
 *
 */
public class DbUtil {
	private static QueryRunner queryRunner;

	/**
	 * 初始化
	 */
	public static void init() {
		if (queryRunner == null) {
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setUrl("jdbc:mysql:///sign_app?serverTimezone=Asia/Shanghai");
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUsername("root");
			dataSource.setPassword("mysqlmima123");
			queryRunner = new QueryRunner(dataSource);
		}
	}

	public static QueryRunner getQueryRunner() {
		if (queryRunner == null) {
			init();
		}
		return queryRunner;
	}

}
