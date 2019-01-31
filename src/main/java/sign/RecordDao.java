package sign;

import org.hibernate.Session;
import org.hibernate.query.Query;

import sign.bean.Record;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class RecordDao {
	private Session session = HibernateUtil.getSession();

	public Record getRecordByUuid(String uuid) {
		Query<Record> query = session.createQuery("from Record where uuid=?1");
		query.setParameter(1, uuid);
		return query.uniqueResult();
	}

}
