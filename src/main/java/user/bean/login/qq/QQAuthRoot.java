/**
  * Copyright 2019 bejson.com 
  */
package user.bean.login.qq;

/**
 * Auto-generated: 2019-01-28 9:49:37
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class QQAuthRoot {

	private String id;
	private String description;
	private AuthResult authResult;
	private UserInfo userInfo;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setAuthResult(AuthResult authResult) {
		this.authResult = authResult;
	}

	public AuthResult getAuthResult() {
		return authResult;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

}