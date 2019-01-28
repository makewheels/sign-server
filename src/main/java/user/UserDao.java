package user;

import org.hibernate.Session;
import org.hibernate.query.Query;

import user.bean.User;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class UserDao {
	private Session session = HibernateUtil.getSession();

	/**
	 * 根据QQopenid找用户
	 * 
	 * @return
	 */
	public User findUserByQQopenid(String qqOpenid) {
		Query<User> query = session.createQuery("from User where qqOpenid=?1");
		query.setParameter(1, qqOpenid);
		return query.uniqueResult();
	}

	/**
	 * 
	 * 根据loginToken找用户
	 * 
	 * @param loginToken
	 * @return
	 */
	public User findUserByLoginToken(String loginToken) {
		Query<User> query = session.createQuery("from User where loginToken=?1");
		query.setParameter(1, loginToken);
		return query.uniqueResult();
	}

}
