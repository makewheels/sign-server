package sign.bean.returnclient;

/**
 * 返回给客户端的单个签到详情
 * 
 * @author Administrator
 *
 */
public class SignLogDetail {
	private String signLogUuid;// 签到记录uuid
	private String signTime;// 签到时间
	private Integer startHour;// 签到时的任务开始时间
	private Integer startMinute;
	private Integer endHour;// 签到时的任务结束时间
	private Integer endMinute;
	private String missionName;// 任务名
	private String signUserNickname;// 用户昵称
	private String missionCreatorUserNickname;// 任务创始人用户昵称
	private String signUserAvatarUrl;// 签到用户头像
	private String missionCreatorUserAvatarUrl;// 签到用户头像
	private String imageUrl;// 图片
	private String recordUrl;// 录音
	private Boolean inTimeRange;// 是否在规定的签到时间内
	private String position;// 签到位置

	public String getSignLogUuid() {
		return signLogUuid;
	}

	public void setSignLogUuid(String signLogUuid) {
		this.signLogUuid = signLogUuid;
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

	public String getMissionName() {
		return missionName;
	}

	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}

	public String getSignUserNickname() {
		return signUserNickname;
	}

	public void setSignUserNickname(String signUserNickname) {
		this.signUserNickname = signUserNickname;
	}

	public String getMissionCreatorUserNickname() {
		return missionCreatorUserNickname;
	}

	public void setMissionCreatorUserNickname(String missionCreatorUserNickname) {
		this.missionCreatorUserNickname = missionCreatorUserNickname;
	}

	public String getSignUserAvatarUrl() {
		return signUserAvatarUrl;
	}

	public void setSignUserAvatarUrl(String signUserAvatarUrl) {
		this.signUserAvatarUrl = signUserAvatarUrl;
	}

	public String getMissionCreatorUserAvatarUrl() {
		return missionCreatorUserAvatarUrl;
	}

	public void setMissionCreatorUserAvatarUrl(String missionCreatorUserAvatarUrl) {
		this.missionCreatorUserAvatarUrl = missionCreatorUserAvatarUrl;
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

	public Boolean getInTimeRange() {
		return inTimeRange;
	}

	public void setInTimeRange(Boolean inTimeRange) {
		this.inTimeRange = inTimeRange;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
