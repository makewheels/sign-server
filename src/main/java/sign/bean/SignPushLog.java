package sign.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 签到完成后，向该任务其他成员推送的记录
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "sign_push_log")
public class SignPushLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer pushUserId;
	private Integer signLogId;
	private String pushClientId;
	private Date time;
	private String title;
	private String text;
	@Column(length = 1000)
	private String responseJson;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPushUserId() {
		return pushUserId;
	}

	public void setPushUserId(Integer pushUserId) {
		this.pushUserId = pushUserId;
	}

	public Integer getSignLogId() {
		return signLogId;
	}

	public void setSignLogId(Integer signLogId) {
		this.signLogId = signLogId;
	}

	public String getPushClientId() {
		return pushClientId;
	}

	public void setPushClientId(String pushClientId) {
		this.pushClientId = pushClientId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

}