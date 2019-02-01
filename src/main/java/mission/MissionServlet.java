package mission;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import mission.bean.Mission;
import mission.bean.UserMission;
import user.bean.User;
import util.HibernateUtil;
import util.ResponseUtil;

public class MissionServlet extends HttpServlet {
	private static final long serialVersionUID = -1555833866094477766L;

	private MissionDao missionDao = new MissionDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (StringUtils.isNotEmpty(method)) {
			// 获取当前任务
			if (method.equals("getCurrent")) {
				getCurrentMission(request, response);
				// 新建第一个任务
			} else if (method.equals("new")) {
				newMission(request, response);
				// 保存任务
			} else if (method.equals("save")) {
				saveMission(request, response);
				// 加入一个已有的任务
			} else if (method.equals("join")) {
				joinMission(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取当前任务
	 * 
	 * @param request
	 * @param response
	 */
	private void getCurrentMission(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");
		User findUser = HibernateUtil.findObjectById(User.class, currentUser.getId());
		Integer currentMissionId = findUser.getCurrentMissionId();
		Map<String, Integer> map = new HashMap<>();
		map.put("currentMissionId", currentMissionId);
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 新建第一个任务
	 * 
	 * @param request
	 * @param response
	 */
	private void newMission(HttpServletRequest request, HttpServletResponse response) {
		String missionName = request.getParameter("missionName");
		int startHour = Integer.parseInt(request.getParameter("startHour"));
		int startMinute = Integer.parseInt(request.getParameter("startMinute"));
		int endHour = Integer.parseInt(request.getParameter("endHour"));
		int endMinute = Integer.parseInt(request.getParameter("endMinute"));
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getId();
		Mission mission = new Mission();
		mission.setCreateUserId(userId);
		mission.setCreateTime(new Date());
		mission.setUuid(getUuid());
		mission.setName(missionName);
		mission.setStartHour(startHour);
		mission.setStartMinute(startMinute);
		mission.setEndHour(endHour);
		mission.setEndMinute(endMinute);
		// 保存新任务
		HibernateUtil.save(mission);
		Integer missionId = mission.getId();
		// 更新用户当前任务id
		user.setCurrentMissionId(missionId);
		HibernateUtil.update(user);
		// 在中间表插入数据
		missionDao.saveUserMission(userId, missionId);
		Map<String, String> map = new HashMap<>();
		map.put("status", "ok");
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 保存一个任务
	 * 
	 * @param request
	 * @param response
	 */
	private void saveMission(HttpServletRequest request, HttpServletResponse response) {
		String missionName = request.getParameter("missionName");
		int startHour = Integer.parseInt(request.getParameter("startHour"));
		int startMinute = Integer.parseInt(request.getParameter("startMinute"));
		int endHour = Integer.parseInt(request.getParameter("endHour"));
		int endMinute = Integer.parseInt(request.getParameter("endMinute"));
		User user = (User) request.getSession().getAttribute("user");
		Mission mission = new Mission();
		mission.setCreateUserId(user.getId());
		mission.setCreateTime(new Date());
		mission.setUuid(getUuid());
		mission.setName(missionName);
		mission.setStartHour(startHour);
		mission.setStartMinute(startMinute);
		mission.setEndHour(endHour);
		mission.setEndMinute(endMinute);
		HibernateUtil.save(mission);
		Map<String, String> map = new HashMap<>();
		map.put("status", "ok");
		map.put("uuid", mission.getUuid());
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 加入一个已有任务
	 * 
	 * @param request
	 * @param response
	 */
	private void joinMission(HttpServletRequest request, HttpServletResponse response) {
		String missionUuid = request.getParameter("missionUuid");
		Mission mission = missionDao.findMissionByUuid(missionUuid);
		Map<String, String> map = new HashMap<>();
		// 查无此任务
		if (mission == null) {
			// 回写错误
			map.put("status", "error");
			ResponseUtil.writeJson(response, map);
			return;
		} else {
			Integer missionId = mission.getId();
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			// 更新user
			user.setCurrentMissionId(missionId);
			HibernateUtil.update(user);
			// 更新中间表
			// 如果没有这个记录，保存
			// 如果已经有这个记录了，什么都不做
			Integer userId = user.getId();
			UserMission findUserMission = missionDao.findUserMissionByIds(userId, missionId);
			if (findUserMission == null) {
				UserMission userMission = new UserMission();
				userMission.setUserId(userId);
				userMission.setMissionId(missionId);
				userMission.setCreateTime(new Date());
				HibernateUtil.save(userMission);
			}
			// 回写
			map.put("status", "ok");
			map.put("missionName", mission.getName());
			ResponseUtil.writeJson(response, map);
		}
	}
}
