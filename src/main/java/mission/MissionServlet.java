package mission;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import mission.bean.Mission;
import user.UserDao;
import user.bean.User;
import util.HibernateUtil;
import util.ResponseUtil;

public class MissionServlet extends HttpServlet {
	private static final long serialVersionUID = -1555833866094477766L;

	private UserDao userDao = new UserDao();
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
		User findUser = userDao.findUserById(currentUser.getId());
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
		Mission mission = new Mission();
		mission.setCreateUserId(user.getId());
		mission.setCreateTime(new Date());
		mission.setUuid(getUuid());
		mission.setName(missionName);
		mission.setStartHour(startHour);
		mission.setStartMinute(startMinute);
		mission.setEndHour(endHour);
		mission.setEndMInute(endMinute);
		Set<User> userSet = mission.getUserSet();
		if (userSet == null) {
			userSet = new HashSet<>();
		}
		userSet.add(user);
		mission.setUserSet(userSet);
		HibernateUtil.save(mission);
		user.setCurrentMissionId(mission.getId());
		Set<Mission> missionSet = user.getMissionSet();
		if (missionSet == null) {
			missionSet = new HashSet<>();
		}
		missionSet.add(mission);
		user.setMissionSet(missionSet);
		HibernateUtil.update(user);
		Map<String, String> map = new HashMap<>();
		map.put("state", "ok");
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 保存一个新任务
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
		mission.setEndMInute(endMinute);
		HibernateUtil.save(mission);
		Map<String, String> map = new HashMap<>();
		map.put("state", "ok");
		map.put("uuid", mission.getUuid());
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 加入一个已有的任务
	 * 
	 * @param request
	 * @param response
	 */
	private void joinMission(HttpServletRequest request, HttpServletResponse response) {
		String missionUuid = request.getParameter("missionUuid");
		Mission mission = missionDao.getMissionByUuid(missionUuid);
		Map<String, String> map = new HashMap<>();
		// 查无此任务
		if (mission == null) {
			// 回写错误
			map.put("state", "error");
			ResponseUtil.writeJson(response, map);
			return;
		} else {
			// 更新user
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			user.setCurrentMissionId(mission.getId());
			session.setAttribute("user", user);
			Set<Mission> missionSet = user.getMissionSet();
			if (missionSet == null) {
				missionSet = new HashSet<>();
			}
			missionSet.add(mission);
			user.setMissionSet(missionSet);
			HibernateUtil.update(user);
			// 更新mission
			Set<User> userSet = mission.getUserSet();
			if (userSet == null) {
				userSet = new HashSet<>();
			}
			userSet.add(user);
			mission.setUserSet(userSet);
			HibernateUtil.update(mission);
			// 回写
			map.put("state", "ok");
			map.put("missionName", mission.getName());
			ResponseUtil.writeJson(response, map);
		}
	}
}
