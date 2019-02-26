package sign.bean.responseclient;

/**
 * 返回给客户端的签到记录列表
 * 
 * @author Administrator
 *
 */
public class ClientSignLogList {
	private String signLogUuid;// 签到uuid
	private String avatarUrl;// 头像url
	private String nickname;// 昵称
	private String signTime;// 签到时间
	private Boolean valid;// 有效性

	public String getSignLogUuid() {
		return signLogUuid;
	}

	public void setSignLogUuid(String signLogUuid) {
		this.signLogUuid = signLogUuid;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

}
