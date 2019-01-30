package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static Session session;

	static {
		Configuration configuration = new Configuration().configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();
	}

	public static Session getSession() {
		return session;
	}

	/**
	 * 根据逐渐查对象
	 * 
	 * @param clazz
	 * @param primaryKey
	 * @return
	 */
	public static <T> T getObjectById(Class<T> clazz, Object primaryKey) {
		return session.find(clazz, primaryKey);
	}

	/**
	 * 保存对象
	 * 
	 * @param object
	 */
	public static void save(Object object) {
		session.save(object);
	}

	/**
	 * 更新对象
	 * 
	 * @param object
	 */
	public static void update(Object object) {
		Transaction transaction = session.beginTransaction();
		session.update(object);
		transaction.commit();
	}

}