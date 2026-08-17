package sign;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
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
	public List<SignLog> findByCondition(Integer currentMissionId, String who, Integer userId, String valid) {
		Session session = HibernateUtil.getSession();
		StringBuilder hql = new StringBuilder("from SignLog where missionId = :missionId");
		// 查谁的
		if (who.equals("my")) {
			hql.append(" and userId = :userId");
		} else if (who.equals("other")) {
			hql.append(" and userId <> :userId");
		}
		// 有效性
		if (valid.equals("true")) {
			hql.append(" and inTimeRange = true");
		} else if (valid.equals("false")) {
			hql.append(" and inTimeRange <> false");
		}
		// 按时间倒序
		hql.append(" order by time desc");
		Query<SignLog> query = session.createQuery(hql.toString(), SignLog.class);
		query.setParameter("missionId", currentMissionId);
		if (who.equals("my") || who.equals("other")) {
			query.setParameter("userId", userId);
		}
		List<SignLog> list = query.list();
		session.close();
		return list;
	}

	/**
	 * 根据uuid找签到记录
	 * 
	 * @param signUuid
	 */
	public SignLog findSignLogByUuid(String signUuid) {
		Session session = HibernateUtil.getSession();
		Query<SignLog> query = session.createQuery("from SignLog where uuid=?1");
		query.setParameter(1, signUuid);
		SignLog signLog = query.uniqueResult();
		session.close();
		return signLog;
	}
}
