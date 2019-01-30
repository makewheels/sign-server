package sign;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import mission.bean.Mission;
import sign.bean.SignLog;
import user.bean.User;
import util.HibernateUtil;
import util.ResponseUtil;

public class SignServlet extends HttpServlet {
	private static final long serialVersionUID = -412797394793347434L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (StringUtils.isNotEmpty(method)) {
			// 获取当前签到状态
			if (method.equals("getCurrentSignState")) {
				getCurrentSignState(request, response);
			} else if (method.equals("doSign")) {
				doSign(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 获取当前签到状态
	 * 
	 * @param request
	 * @param response
	 */
	private void getCurrentSignState(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Mission mission = HibernateUtil.getObjectById(Mission.class, user.getCurrentMissionId());
		// 当前任务名
		String currentMissionName = mission.getName();
		// 签到时间范围
		String timeRange = mission.getStartHour() + ":" + mission.getStartMinute() + "-" + mission.getEndHour() + ":"
				+ mission.getEndMinute();
		// 我的签到状态
		// TODO
		boolean mySignedStateBoolean = false;
		// TODO
		String mySignedStateString = null;
		// 已签到人数
		// TODO
		Integer signedCount = -1;
		Map<String, Object> map = new HashMap<>();
		map.put("currentMissionName", currentMissionName);
		map.put("timeRange", timeRange);
		map.put("signedCount", signedCount);
		map.put("mySignedStateBoolean", mySignedStateBoolean);
		map.put("mySignedStateString", mySignedStateString);

		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 执行签到
	 * 
	 * @param request
	 * @param response
	 */
	private void doSign(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		SignLog signLog = new SignLog();
		signLog.setUserId(userId);
		signLog.setMissionId(user.getCurrentMissionId());
		signLog.setIp(request.getRemoteAddr());
		signLog.setTime(new Date());
		HibernateUtil.save(signLog);
	}
}
