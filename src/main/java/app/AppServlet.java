package app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import app.bean.OpenAppLog;
import user.UserDao;
import user.bean.User;
import util.HibernateUtil;

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
					OpenAppLog openAppLog = new OpenAppLog();
					openAppLog.setDeviceJson(request.getParameter("deviceJson"));
					openAppLog.setIp(request.getRemoteAddr());
					openAppLog.setTime(new Date());
					openAppLog.setUserAgent(request.getHeader("User-Agent"));
					String loginToken = request.getParameter("loginToken");
					// 如果没有loginToken
					if (loginToken == null) {
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
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
