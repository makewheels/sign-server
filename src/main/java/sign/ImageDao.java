package sign;

import org.hibernate.Session;
import org.hibernate.query.Query;

import sign.bean.Image;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class ImageDao {

	public Image getImageByUuid(String uuid) {
		Session session = HibernateUtil.getSession();
		Query<Image> query = session.createQuery("from Image where uuid=?1");
		query.setParameter(1, uuid);
		Image image = query.uniqueResult();
		session.close();
		return image;
	}

}
