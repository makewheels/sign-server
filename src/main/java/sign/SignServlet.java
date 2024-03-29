package sign;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSON;

import mission.bean.Mission;
import sign.bean.Image;
import sign.bean.Record;
import sign.bean.SignLog;
import sign.bean.SignPushLog;
import sign.bean.baiduaip.Result;
import sign.bean.position.Position;
import sign.bean.responseclient.ClientSignLogDetail;
import sign.bean.responseclient.ClientSignLogList;
import user.UserDao;
import user.bean.User;
import util.BaiduAipUtil;
import util.Constants;
import util.HibernateUtil;
import util.PushUtil;
import util.ResponseUtil;

public class SignServlet extends HttpServlet {
	private static final long serialVersionUID = -412797394793347434L;

	private UserDao userDao = new UserDao();
	private SignLogDao signLogDao = new SignLogDao();
	private ImageDao imageDao = new ImageDao();
	private RecordDao recordDao = new RecordDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (StringUtils.isNotEmpty(method)) {
			// 获取当前签到状态
			if (method.equals("getCurrentSignState")) {
				getCurrentSignState(request, response);
				// 执行签到
			} else if (method.equals("doSign")) {
				doSign(request, response);
				// 查询签到记录列表
			} else if (method.equals("getSignList")) {
				getSignList(request, response);
				// 查询单个签到记录详情
			} else if (true) {
				getSignDetail(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 补零
	 * 
	 * @param num
	 * @return
	 */
	private String addZero(int num) {
		if (num <= 9) {
			return "0" + num;
		} else {
			return num + "";
		}
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
		Mission mission = HibernateUtil.findObjectById(Mission.class, user.getCurrentMissionId());
		Integer missionId = mission.getId();
		// 当前任务名
		String currentMissionName = mission.getName();
		int startHour = mission.getStartHour();
		int startMinute = mission.getStartMinute();
		int endHour = mission.getEndHour();
		int endMinute = mission.getEndMinute();
		// 签到时间范围
		String timeRange = addZero(startHour) + ":" + addZero(startMinute) + "-" + addZero(endHour) + ":"
				+ addZero(endMinute);
		// 我的签到状态
		String mySignedStateString = "今日签到状态";
		// 现在的时间
		LocalTime now = LocalTime.now();
		// 开始时间
		LocalTime start = LocalTime.of(startHour, startMinute);
		// 截止时间
		LocalTime end = LocalTime.of(endHour, endMinute); // 比较时间
		// 该任务今天所有在时间范围内的签到记录
		List<SignLog> signLogList = signLogDao.findTodayInTimeRange(missionId);
		// 先看该用户今天签到了没有
		boolean isTodaySigned = false;
		for (SignLog signLog : signLogList) {
			if (signLog.getUserId() == user.getId()) {
				mySignedStateString = "今日签到已完成";
				isTodaySigned = true;
				break;
			}
		}
		// 如果今天还没签到
		if (isTodaySigned == false) {
			// 如果还没到开始时间
			if (now.isBefore(start)) {
				mySignedStateString = "签到尚未开始";
				// 如果晚于开始时间
			} else if (now.isBefore(end)) {
				mySignedStateString = "一日之计在于晨，正是签到好时机";
				// 如果晚于截止时间
			} else {
				mySignedStateString = "Oops，过截止时间喽，下次要早哦";
			}
		}
		Set<Integer> signedUserSet = new HashSet<>();
		for (SignLog signLog : signLogList) {
			signedUserSet.add(signLog.getUserId());
		}
		// 已签到人数
		Integer signedCount = signedUserSet.size();
		Map<String, Object> map = new HashMap<>();
		map.put("currentMissionName", currentMissionName);
		map.put("timeRange", timeRange);
		map.put("signedCount", signedCount);
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
		// 找用户的当前任务
		Mission mission = HibernateUtil.findObjectById(Mission.class, user.getCurrentMissionId());
		// 根据文件uuid找id
		Image image = imageDao.getImageByUuid(request.getParameter("imageUuid"));
		Record record = recordDao.getRecordByUuid(request.getParameter("recordUuid"));
		SignLog signLog = new SignLog();
		signLog.setUuid(UUID.randomUUID().toString());
		signLog.setUserId(userId);
		signLog.setMissionId(mission.getId());
		// 设置时间范围
		int startHour = mission.getStartHour();
		int startMinute = mission.getStartMinute();
		int endHour = mission.getEndHour();
		int endMinute = mission.getEndMinute();
		signLog.setStartHour(startHour);
		signLog.setStartMinute(startMinute);
		signLog.setEndHour(endHour);
		signLog.setEndMinute(endMinute);
		// 看是否在时间范围内
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), startHour,
				startMinute);
		LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), endHour, endMinute);
		signLog.setInTimeRange(now.isAfter(start) && now.isBefore(end));
		signLog.setIp(request.getRemoteAddr());
		signLog.setTime(new Date());
		signLog.setImageId(image.getId());
		signLog.setRecordId(record.getId());
		signLog.setPosition(request.getParameter("position"));
		// 保存签到记录
		HibernateUtil.save(signLog);
		Integer signLogId = signLog.getId();
		// 更新文件的签到记录id
		image.setSignLogId(signLogId);
		HibernateUtil.update(image);
		record.setSignLogId(signLogId);
		HibernateUtil.update(record);

		// 回写客户端
		Map<String, String> map = new HashMap<>();
		map.put("state", "ok");
		ResponseUtil.writeJson(response, map);

		// 再开线程异步完成签到后续工作
		new Thread() {
			public void run() {
				// 语音识别
				try {
					String resultJson = BaiduAipUtil.recognizeAudio(record.getAbsolutePath());
					record.setAudioRecognitionJson(resultJson);
					Result result = JSON.parseObject(resultJson, Result.class);
					record.setAudioRecognitionResult(result.getResult().get(0));
					// 更新语音识别结果
					HibernateUtil.update(record);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 推送消息通知其它人
				List<User> userList = userDao.findUserByCurrentMissionId(user.getCurrentMissionId());
				// 遍历用户通知
				for (User eachUser : userList) {
					String pushClientId = eachUser.getPushClientId();
					// 不是当前签到用户
					if (eachUser.getId() != userId && pushClientId != null) {
						String title = user.getNickname() + " 已签到";
						String text = "签到时间：" + DateFormatUtils.format(signLog.getTime(), "yyyy-MM-dd hh:mm:ss");
						Map<String, Object> resultMap = PushUtil.pushByClientId(pushClientId, title, text,
								user.getAvatarUrl());
						SignPushLog signPushLog = new SignPushLog();
						signPushLog.setPushUserId(eachUser.getId());
						signPushLog.setSignLogId(signLog.getId());
						signPushLog.setPushClientId(pushClientId);
						signPushLog.setTime(new Date());
						signPushLog.setTitle(title);
						signPushLog.setText(text);
						signPushLog.setResponseJson(JSON.toJSONString(resultMap));
						HibernateUtil.save(signPushLog);
					}
				}
			};
		}.start();
	}

	/**
	 * 查询签到记录
	 * 
	 * @param request
	 * @param response
	 */
	private void getSignList(HttpServletRequest request, HttpServletResponse response) {
		// 查询条件
		// 查谁
		String who = request.getParameter("who");
		// 有效性
		String valid = request.getParameter("valid");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		Integer currentMissionId = user.getCurrentMissionId();
		List<ClientSignLogList> result = new ArrayList<>();
		// 执行查询
		List<SignLog> findList = signLogDao.findByCondition(currentMissionId, who, userId, valid);
		for (SignLog signLog : findList) {
			ClientSignLogList signLogList = new ClientSignLogList();
			signLogList.setSignLogUuid(signLog.getUuid());
			// 签到的用户
			User signUser = HibernateUtil.findObjectById(User.class, signLog.getUserId());
			// 头像
			signLogList.setAvatarUrl(signUser.getAvatarUrl());
			signLogList.setNickname(signUser.getNickname());
			signLogList.setSignTime(DateFormatUtils.format(signLog.getTime(), "yyyy-MM-dd HH:mm:ss"));
			signLogList.setValid(signLog.getInTimeRange());
			result.add(signLogList);
		}
		ResponseUtil.writeJson(response, result);
	}

	/**
	 * 单个签到记录详情页
	 * 
	 * @param request
	 * @param response
	 */
	private void getSignDetail(HttpServletRequest request, HttpServletResponse response) {
		String signUuid = request.getParameter("signLogUuid");
		SignLog signLog = signLogDao.findSignLogByUuid(signUuid);
		ClientSignLogDetail clientSignLogDetail = new ClientSignLogDetail();
		clientSignLogDetail.setSignLogUuid(signUuid);
		Image image = HibernateUtil.findObjectById(Image.class, signLog.getImageId());
		clientSignLogDetail.setImageUrl(image.getUrl());
		Record record = HibernateUtil.findObjectById(Record.class, signLog.getRecordId());
		clientSignLogDetail.setRecordUrl(record.getUrl());
		clientSignLogDetail.setPosition(JSON.parseObject(signLog.getPosition(), Position.class).getAddresses());
		clientSignLogDetail.setSignTime(DateFormatUtils.format(signLog.getTime(), Constants.DATE_FORMAT_PATTERN));
		clientSignLogDetail.setStartHour(signLog.getStartHour());
		clientSignLogDetail.setStartMinute(signLog.getStartMinute());
		clientSignLogDetail.setEndHour(signLog.getEndHour());
		clientSignLogDetail.setEndMinute(signLog.getEndMinute());
		clientSignLogDetail.setInTimeRange(signLog.getInTimeRange());
		Mission mission = HibernateUtil.findObjectById(Mission.class, signLog.getMissionId());
		clientSignLogDetail.setMissionName(mission.getName());
		User user = HibernateUtil.findObjectById(User.class, signLog.getUserId());
		clientSignLogDetail.setNickname(user.getNickname());
		clientSignLogDetail.setAvatarUrl(user.getAvatarUrl());
		ResponseUtil.writeJson(response, clientSignLogDetail);
	}

}
