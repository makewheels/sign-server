package sign;

import org.hibernate.Session;
import org.hibernate.query.Query;

import sign.bean.Record;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class RecordDao {

	public Record getRecordByUuid(String uuid) {
		Session session = HibernateUtil.getSession();
		Query<Record> query = session.createQuery("from Record where uuid=?1");
		query.setParameter(1, uuid);
		Record record = query.uniqueResult();
		session.close();
		return record;
	}

}
