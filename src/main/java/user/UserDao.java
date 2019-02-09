package user;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import user.bean.User;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class UserDao {

	/**
	 * 根据QQopenid找用户
	 * 
	 * @return
	 */
	public User findUserByQQOpenid(String qqOpenid) {
		Session session = HibernateUtil.getSession();
		Query<User> query = session.createQuery("from User where qqOpenid=?1");
		query.setParameter(1, qqOpenid);
		User user = query.uniqueResult();
		session.close();
		return user;
	}

	/**
	 * 
	 * 根据loginToken找用户
	 * 
	 * @param loginToken
	 * @return
	 */
	public User findUserByLoginToken(String loginToken) {
		Session session = HibernateUtil.getSession();
		Query<User> query = session.createQuery("from User where loginToken=?1");
		query.setParameter(1, loginToken);
		User user = query.uniqueResult();
		session.close();
		return user;
	}

	/**
	 * 根据当前任务id找用户
	 * 
	 * @param currentMissionId
	 * @return
	 */
	public List<User> findUserByCurrentMissionId(Integer currentMissionId) {
		Session session = HibernateUtil.getSession();
		Query<User> query = session.createQuery("from User where currentMissionId=?1");
		query.setParameter(1, currentMissionId);
		List<User> list = query.list();
		session.close();
		return list;
	}
}
