package user;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSON;

import user.bean.User;
import user.bean.UserDetail;
import user.bean.login.LoginLog;
import user.bean.login.qq.QQAuthRoot;
import util.Constants;
import util.HibernateUtil;
import util.QcloudCosUtil;
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
				// 用户信息
			} else if (method.equals("getUserInfo")) {
				getUserInfo(request, response);
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
		User user = userDao.findUserByQQOpenid(qqOpenid);
		String loginToken = RandomStringUtils.randomAlphanumeric(50);
		String pushClientId = request.getParameter("pushClientId");
		// 先注册或更新loginToken，再登录
		// 是否是新用户
		boolean isNewUser = false;
		// 如果没有此QQ用户，注册新用户
		if (user == null) {
			isNewUser = true;
			user = new User();
			user.setNickname(qqAuthRoot.getUserInfo().getNickname());
			user.setLoginToken(loginToken);
			user.setQqOpenid(qqOpenid);
			user.setUuid(UUID.randomUUID().toString());
			user.setPushClientId(pushClientId);
			// QQ头像
			String qqAvatarUrl = qqAuthRoot.getUserInfo().getFigureurl_qq_2();
			HibernateUtil.save(user);
			// 下载头像
			String avatarRelativePath = "/avatar/" + user.getId() + ".png";
			String avatarAbsolutePath = Constants.ROOT_PATH + avatarRelativePath;
			File avatarFile = new File(avatarAbsolutePath);
			try {
				FileUtils.copyURLToFile(new URL(qqAvatarUrl), avatarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String avatarObjectStorgeKey = "/avatar/" + UUID.randomUUID().toString() + ".png";
			String avatarUrl = QcloudCosUtil.saveToQcloud(avatarFile, avatarObjectStorgeKey);
			user.setAvatarUrl(avatarUrl);
			// 更新user头像url
			HibernateUtil.update(user);
			UserDetail userDetail = new UserDetail();
			userDetail.setUserId(user.getId());
			userDetail.setRegistIp(request.getRemoteAddr());
			userDetail.setRegistTime(new Date());
			userDetail.setRegistType("qq");
			userDetail.setRegistUserAgent(request.getHeader("User-Agent"));
			userDetail.setQqAuthJson(authJson);
			userDetail.setAvatarAbsolutePath(avatarAbsolutePath);
			userDetail.setAvatarRelativePath(avatarRelativePath);
			userDetail.setAvatarObjectStorgeKey(avatarObjectStorgeKey);
			HibernateUtil.save(userDetail);
		} else {
			// 如果已经存在此QQ用户
			user.setLoginToken(loginToken);
			// 更新推送clientId
			user.setPushClientId(pushClientId);
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
		Map<String, Object> map = new HashMap<>();
		map.put("loginToken", loginToken);
		// 如果是新用户，上报通讯录
		if (isNewUser) {
			map.put("reportContacts", true);
		} else {
			map.put("reportContacts", false);
		}
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

	/**
	 * 用户信息
	 * 
	 * @param request
	 * @param response
	 */
	private void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		UserDetail userDetail = userDao.findUserDetailByUserId(user.getId());
		// 回写
		Map<String, String> map = new HashMap<>();
		map.put("avatarUrl", user.getAvatarUrl());
		map.put("nickname", user.getNickname());
		map.put("registTime", DateFormatUtils.format(userDetail.getRegistTime(), Constants.DATE_FORMAT_PATTERN));
		ResponseUtil.writeJson(response, map);
	}

}
