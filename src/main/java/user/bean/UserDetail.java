package user.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户详情
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "user_detail")
public class UserDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer userId;// 用户id
	private String registIp;// 注册ip
	private Date registTime;// 注册时间
	@Column(length = 1000)
	private String registUserAgent;// 注册ua
	private String registType;// 注册方式
	@Column(length = 2000)
	private String qqAuthJson;// qq登录结果
	private String avatarAbsolutePath;// 头像本地绝对地址
	private String avatarRelativePath;// 头像本地相对地址
	private String avatarObjectStorgeKey;// 头像对象存储key

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

	public String getRegistIp() {
		return registIp;
	}

	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}

	public String getRegistUserAgent() {
		return registUserAgent;
	}

	public void setRegistUserAgent(String registUserAgent) {
		this.registUserAgent = registUserAgent;
	}

	public String getRegistType() {
		return registType;
	}

	public void setRegistType(String registType) {
		this.registType = registType;
	}

	public String getQqAuthJson() {
		return qqAuthJson;
	}

	public void setQqAuthJson(String qqAuthJson) {
		this.qqAuthJson = qqAuthJson;
	}

	public String getAvatarAbsolutePath() {
		return avatarAbsolutePath;
	}

	public void setAvatarAbsolutePath(String avatarAbsolutePath) {
		this.avatarAbsolutePath = avatarAbsolutePath;
	}

	public String getAvatarRelativePath() {
		return avatarRelativePath;
	}

	public void setAvatarRelativePath(String avatarRelativePath) {
		this.avatarRelativePath = avatarRelativePath;
	}

	public String getAvatarObjectStorgeKey() {
		return avatarObjectStorgeKey;
	}

	public void setAvatarObjectStorgeKey(String avatarObjectStorgeKey) {
		this.avatarObjectStorgeKey = avatarObjectStorgeKey;
	}

}