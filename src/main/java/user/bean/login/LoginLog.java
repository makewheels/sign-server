package user.bean.login;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 登录日志
 * 
 * @author Administrator
 *
 */

@Entity
public class LoginLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;// 用户id
	private Date time;
	private String ip;
	@Column(length = 2000)
	private String userAgent;
	@Column(length = 1000)
	private String deviceJson;
	private String loginToken;// 本次登录使用的loginToken
	private String refreshLoginToken;// 下次刷新之后要用的loginToken
	private String type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getDeviceJson() {
		return deviceJson;
	}

	public void setDeviceJson(String deviceJson) {
		this.deviceJson = deviceJson;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getRefreshLoginToken() {
		return refreshLoginToken;
	}

	public void setRefreshLoginToken(String refreshLoginToken) {
		this.refreshLoginToken = refreshLoginToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
