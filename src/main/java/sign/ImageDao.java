package sign;

import org.hibernate.Session;
import org.hibernate.query.Query;

import sign.bean.Image;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class ImageDao {
	private Session session = HibernateUtil.getSession();

	public Image getImageByUuid(String uuid) {
		Query<Image> query = session.createQuery("from Image where uuid=?1");
		query.setParameter(1, uuid);
		return query.uniqueResult();
	}
}
