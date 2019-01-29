package mission;

import org.hibernate.Session;
import org.hibernate.query.Query;

import mission.bean.Mission;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class MissionDao {
	private Session session = HibernateUtil.getSession();

	public Mission getMissionByUuid(String uuid) {
		Query<Mission> query = session.createQuery("from Mission where uuid=?1");
		query.setParameter(1, uuid);
		return query.uniqueResult();
	}
}
