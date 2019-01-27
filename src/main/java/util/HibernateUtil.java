package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static Session session;

	static {
		if (session == null) {
			Configuration configuration = new Configuration().configure();
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			session = sessionFactory.openSession();
		}
	}

	public static Session getSession() {
		return session;
	}

}
