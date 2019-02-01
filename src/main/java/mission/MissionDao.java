package mission;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.hibernate.Session;
import org.hibernate.query.Query;

import mission.bean.Mission;
import mission.bean.UserMission;
import util.DbUtil;
import util.HibernateUtil;

@SuppressWarnings("unchecked")
public class MissionDao {

	/**
	 * 根据uuid查mission
	 * 
	 * @param uuid
	 * @return
	 */
	public Mission findMissionByUuid(String uuid) {
		Session session = HibernateUtil.getSession();
		Query<Mission> query = session.createQuery("from Mission where uuid=?1");
		query.setParameter(1, uuid);
		Mission mission = query.uniqueResult();
		session.close();
		return mission;
	}

	/**
	 * 保存中间表新数据
	 * 
	 * @param userId
	 * @param missionId
	 */
	public void saveUserMission(Integer userId, Integer missionId) {
		QueryRunner queryRunner = DbUtil.getQueryRunner();
		try {
			queryRunner.update("insert into user_mission (userId,missionId,createTime) values(?,?,?)", userId,
					missionId, new Date());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户和任务的id，查中间表
	 * 
	 * @param userId
	 * @param missionId
	 * @return
	 */
	public UserMission findUserMissionByIds(Integer userId, Integer missionId) {
		QueryRunner queryRunner = DbUtil.getQueryRunner();
		try {
			return queryRunner.query("select * from user_mission where userId=? and missionId=?",
					new BeanHandler<UserMission>(UserMission.class), userId, missionId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
