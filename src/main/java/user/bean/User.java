package user.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import mission.bean.Mission;

/**
 * 用户
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String uuid;
	private String nickname;
	private String loginToken;
	private String qqOpenid;
	private Integer currentMissionId;
	@ManyToMany
	private Set<Mission> missionSet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getQqOpenid() {
		return qqOpenid;
	}

	public void setQqOpenid(String qqOpenid) {
		this.qqOpenid = qqOpenid;
	}

	public Integer getCurrentMissionId() {
		return currentMissionId;
	}

	public void setCurrentMissionId(Integer currentMissionId) {
		this.currentMissionId = currentMissionId;
	}

	public Set<Mission> getMissionSet() {
		return missionSet;
	}

	public void setMissionSet(Set<Mission> missionSet) {
		this.missionSet = missionSet;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uuid=" + uuid + ", nickname=" + nickname + ", loginToken=" + loginToken
				+ ", qqOpenid=" + qqOpenid + ", currentMissionId=" + currentMissionId + ", missionSet=" + missionSet
				+ "]";
	}

}
