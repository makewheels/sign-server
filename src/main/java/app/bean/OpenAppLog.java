package app.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 打开应用日志
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "open_app_log")
public class OpenAppLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;
	@Column(length = 2000)
	private String deviceJson;
	private String ip;
	private Date time;
	@Column(length = 2000)
	private String userAgent;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDeviceJson() {
		return deviceJson;
	}

	public void setDeviceJson(String deviceJson) {
		this.deviceJson = deviceJson;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
