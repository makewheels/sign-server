package sign;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import sign.bean.SignLog;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class SignLogDao {

	/**
	 * 按任务id查今天在时间范围内的签到记录
	 * 
	 * @param uuid
	 * @return
	 */
	public List<SignLog> findTodayInTimeRange(Integer missionId) {
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
		LocalDateTime end = start.plusDays(1);
		Session session = HibernateUtil.getSession();
		Query<SignLog> query = session.createQuery(
				"from SignLog where missionId=?1 and time>=?2 and time<=?3 and inTimeRange=true order by time desc");
		query.setParameter(1, missionId);
		query.setParameter(2, new Date(start.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
		query.setParameter(3, new Date(end.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
		List<SignLog> list = query.list();
		session.close();
		return list;
	}

	/**
	 * 根据条件查询，用于签到记录的预览列表
	 * 
	 * @param currentMissionId
	 * @param who
	 * @param userId
	 * @param valid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<SignLog> findByCondition(Integer currentMissionId, String who, Integer userId, String valid) {
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(SignLog.class);
		// 任务id
		criteria.add(Restrictions.eq("missionId", currentMissionId));
		// 查谁的
		if (who.equals("my")) {
			criteria.add(Restrictions.eq("userId", userId));
		} else if (who.equals("other")) {
			criteria.add(Restrictions.ne("userId", userId));
		}
		// 有效性
		if (valid.equals("true")) {
			criteria.add(Restrictions.eq("inTimeRange", true));
		} else if (valid.equals("false")) {
			criteria.add(Restrictions.ne("inTimeRange", false));
		}
		// 按时间倒序
		criteria.addOrder(Order.desc("time"));
		List<SignLog> list = criteria.list();
		session.close();
		return list;
	}
}
