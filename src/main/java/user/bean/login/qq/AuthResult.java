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
public class AuthResult {

	private int ret;
	private String openid;
	private String access_token;
	private String pay_token;
	private long expires_in;
	private String pf;
	private String pfkey;
	private String msg;
	private int login_cost;
	private int query_authority_cost;
	private int authority_cost;

	public void setRet(int ret) {
		this.ret = ret;
	}

	public int getRet() {
		return ret;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setPay_token(String pay_token) {
		this.pay_token = pay_token;
	}

	public String getPay_token() {
		return pay_token;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPf() {
		return pf;
	}

	public void setPfkey(String pfkey) {
		this.pfkey = pfkey;
	}

	public String getPfkey() {
		return pfkey;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setLogin_cost(int login_cost) {
		this.login_cost = login_cost;
	}

	public int getLogin_cost() {
		return login_cost;
	}

	public void setQuery_authority_cost(int query_authority_cost) {
		this.query_authority_cost = query_authority_cost;
	}

	public int getQuery_authority_cost() {
		return query_authority_cost;
	}

	public void setAuthority_cost(int authority_cost) {
		this.authority_cost = authority_cost;
	}

	public int getAuthority_cost() {
		return authority_cost;
	}

}