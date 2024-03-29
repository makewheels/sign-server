package app;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import app.bean.OpenAppLog;
import user.UserDao;
import user.bean.User;
import util.Constants;
import util.HibernateUtil;
import util.QcloudCosUtil;
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
					// 联系人
				} else if (type.equals("contacts")) {
					reportContacts(request, response);
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
		openAppLog.setPosition(request.getParameter("position"));
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
	 * 上报联系人
	 * 
	 * @param request
	 * @param response
	 */
	private void reportContacts(HttpServletRequest request, HttpServletResponse response) {
		String phoneContacts = request.getParameter("phoneContacts");
		String simContacts = request.getParameter("simContacts");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		System.out.println("phone contacts: userId = " + userId);
		System.out.println(phoneContacts);
		System.out.println("sim contacts: userId = " + userId);
		System.out.println(simContacts);
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis);
		String phoneKey = "/contacts/" + userId + "/" + userId + "_phone_" + currentTimeMillis + ".json";
		String simKey = "/contacts/" + userId + "/" + userId + "_sim_" + currentTimeMillis + ".json";
		// 保存到本地
		File phoneFile = new File(Constants.ROOT_PATH + phoneKey);
		File simFile = new File(Constants.ROOT_PATH + simKey);
		try {
			FileUtils.writeStringToFile(phoneFile, phoneContacts, "utf-8");
			FileUtils.writeStringToFile(simFile, simContacts, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 保存到对象存储
		QcloudCosUtil.saveToQcloud(phoneFile, phoneKey);
		QcloudCosUtil.saveToQcloud(simFile, simKey);
	}

	/**
	 * 检查更新
	 * 
	 * @param request
	 * @param response
	 */
	private void checkUpdate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		// 比较版本，如果相等，不需要更新
//		String clientVersion = request.getParameter("version");
//		if (clientVersion.equals(Constants.APP_VERSION) == true) {
//			map.put("needUpdate", false);
//			// 如果不相等，告知安装包下载地址
//		} else {
//			map.put("needUpdate", true);
//			map.put("url", "");
//		}

		// 不验证更新
		map.put("needUpdate", false);

		map.put("latestVersion", Constants.APP_VERSION);
		ResponseUtil.writeJson(response, map);
	}

}
