package sign.bean.responseclient;

/**
 * 返回给客户端的单个签到详情
 * 
 * @author Administrator
 *
 */
public class ClientSignLogDetail {
	private String signLogUuid;// 签到记录uuid
	private String imageUrl;// 图片
	private String recordUrl;// 录音
	private String position;// 签到位置
	private String signTime;// 签到时间
	private Integer startHour;// 签到时的任务开始时间
	private Integer startMinute;
	private Integer endHour;// 签到时的任务结束时间
	private Integer endMinute;
	private Boolean inTimeRange;// 是否在规定的签到时间内
	private String missionName;// 任务名
	private String nickname;// 昵称
	private String avatarUrl;// 头像
	public String getSignLogUuid() {
		return signLogUuid;
	}
	public void setSignLogUuid(String signLogUuid) {
		this.signLogUuid = signLogUuid;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getRecordUrl() {
		return recordUrl;
	}
	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public Integer getStartHour() {
		return startHour;
	}
	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}
	public Integer getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
	}
	public Integer getEndHour() {
		return endHour;
	}
	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}
	public Integer getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(Integer endMinute) {
		this.endMinute = endMinute;
	}
	public Boolean getInTimeRange() {
		return inTimeRange;
	}
	public void setInTimeRange(Boolean inTimeRange) {
		this.inTimeRange = inTimeRange;
	}
	public String getMissionName() {
		return missionName;
	}
	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}