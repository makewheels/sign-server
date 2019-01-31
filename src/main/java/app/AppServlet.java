package app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import app.bean.OpenAppLog;
import user.UserDao;
import user.bean.User;
import util.Constants;
import util.HibernateUtil;
import util.ResponseUtil;

/**
 * app相关servlet
 * 
 * @author Administrator
 *
 */
public class AppServlet extends HttpServlet {
	private static final long serialVersionUID = 5910053180041447357L;

	private UserDao userDao = new UserDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (StringUtils.isNotEmpty(method)) {
			// 上报
			if (method.equals("report")) {
				// 类型
				String type = request.getParameter("type");
				// 打开应用
				if (type.equals("appOpen")) {
					reportAppOpen(request, response);
				}
				// 检查更新
			} else if (method.equals("checkUpdate")) {
				checkUpdate(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 上报应用打开
	 * 
	 * @param request
	 * @param response
	 */
	private void reportAppOpen(HttpServletRequest request, HttpServletResponse response) {
		OpenAppLog openAppLog = new OpenAppLog();
		openAppLog.setDeviceJson(request.getParameter("deviceJson"));
		openAppLog.setIp(request.getRemoteAddr());
		openAppLog.setTime(new Date());
		openAppLog.setUserAgent(request.getHeader("User-Agent"));
		String loginToken = request.getParameter("loginToken");
		// 如果没有loginToken
		if (loginToken == null || loginToken.equals("null")) {
			openAppLog.setUserId(null);
			// 如果有loginToken
		} else {
			// 根据loginToken找user
			User user = userDao.findUserByLoginToken(loginToken);
			// 如果没找到
			if (user == null) {
				openAppLog.setUserId(null);
			} else {
				// 设置userId
				openAppLog.setUserId(user.getId());
			}
		}
		HibernateUtil.save(openAppLog);
	}

	/**
	 * 检查更新
	 * 
	 * @param request
	 * @param response
	 */
	private void checkUpdate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String clientVersion = request.getParameter("version");
		// 比较版本，如果相等，不需要更新
		if (Constants.APP_VERSION.equals(clientVersion) == true) {
			map.put("needUpdate", false);
			// 如果不相等，告知安装包下载地址
		} else {
			map.put("needUpdate", true);
			map.put("url", "");
		}
		map.put("latestVersion", Constants.APP_VERSION);
		ResponseUtil.writeJson(response, map);
	}

}
