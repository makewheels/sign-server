package sign;

import org.hibernate.Session;

import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class UserDao {
	private Session session = HibernateUtil.getSession();

}
