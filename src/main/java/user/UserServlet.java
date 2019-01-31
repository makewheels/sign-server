package user;

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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import user.bean.User;
import user.bean.UserDetail;
import user.bean.login.LoginLog;
import user.bean.login.qq.QQAuthRoot;
import util.HibernateUtil;
import util.ResponseUtil;

/**
 * 用户相关servlet
 * 
 * @author Administrator
 *
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 7561078664077561644L;

	private UserDao userDao = new UserDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (StringUtils.isNotEmpty(method)) {
			// 登录
			if (method.equals("login")) {
				String type = request.getParameter("type");
				// QQ登录
				if (type.equals("qq")) {
					qqLogin(request, response);
					// loginToken自动登录
				} else if (type.equals("loginToken")) {
					loginByLoginToken(request, response);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * QQ登录
	 * 
	 * @param request
	 * @param response
	 */
	private void qqLogin(HttpServletRequest request, HttpServletResponse response) {
		String authJson = request.getParameter("auth");
		QQAuthRoot qqAuthRoot = JSON.parseObject(authJson, QQAuthRoot.class);
		String qqOpenid = qqAuthRoot.getAuthResult().getOpenid();
		User user = userDao.findUserByQQopenid(qqOpenid);
		String loginToken = RandomStringUtils.randomAlphanumeric(50);
		// 先注册或更新loginToken，再登录
		// 如果没有此QQ用户，注册新用户
		if (user == null) {
			user = new User();
			user.setNickname(qqAuthRoot.getUserInfo().getNickname());
			user.setLoginToken(loginToken);
			user.setQqOpenid(qqOpenid);
			user.setUuid(UUID.randomUUID().toString());
			HibernateUtil.save(user);
			UserDetail userDetail = new UserDetail();
			userDetail.setUserId(user.getId());
			userDetail.setRegistIp(request.getRemoteAddr());
			userDetail.setRegistTime(new Date());
			userDetail.setRegistType("qq");
			userDetail.setRegistUserAgent(request.getHeader("User-Agent"));
			userDetail.setQqAuthJson(authJson);
			HibernateUtil.save(userDetail);
		} else {
			// 如果已经存在此QQ用户
			user.setLoginToken(loginToken);
			// 更新loginToken
			HibernateUtil.update(user);
		}
		// 登录
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		LoginLog loginLog = new LoginLog();
		loginLog.setDeviceJson(request.getParameter("deviceJson"));
		loginLog.setIp(request.getRemoteAddr());
		loginLog.setLoginToken(loginToken);
		loginLog.setRefreshLoginToken(loginToken);
		loginLog.setTime(new Date());
		loginLog.setType("qq");
		loginLog.setUserAgent(request.getHeader("User-Agent"));
		loginLog.setUserId(user.getId());
		HibernateUtil.save(loginLog);
		// 回写
		Map<String, String> map = new HashMap<>();
		map.put("loginToken", loginToken);
		ResponseUtil.writeJson(response, map);
	}

	/**
	 * 使用loginToken自动登录
	 * 
	 * @param request
	 * @param response
	 */
	private void loginByLoginToken(HttpServletRequest request, HttpServletResponse response) {
		String loginToken = request.getParameter("loginToken");
		User user = userDao.findUserByLoginToken(loginToken);
		// 如果找不到用户，登录失败
		if (user == null) {
			try {
				response.sendError(401, "toLogin");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// 正常执行登录
		// 刷新loginToken
		String refreshLoginToken = RandomStringUtils.randomAlphanumeric(50);
		user.setLoginToken(refreshLoginToken);
		System.out.println(user);
		HibernateUtil.update(user);
		// 登录日志
		LoginLog loginLog = new LoginLog();
		loginLog.setDeviceJson(request.getParameter("deviceJson"));
		loginLog.setIp(request.getRemoteAddr());
		loginLog.setLoginToken(loginToken);
		loginLog.setRefreshLoginToken(refreshLoginToken);
		loginLog.setTime(new Date());
		loginLog.setType("loginToken");
		loginLog.setUserAgent(request.getHeader("User-Agent"));
		loginLog.setUserId(user.getId());
		HibernateUtil.save(loginLog);
		// 存到session中
		request.getSession().setAttribute("user", user);
		// 回写
		Map<String, String> map = new HashMap<>();
		map.put("loginToken", refreshLoginToken);
		ResponseUtil.writeJson(response, map);
	}
}
