package sign.bean.baiduaip;

/**
  * Copyright 2019 bejson.com 
  */
import java.util.List;

/**
 * Auto-generated: 2019-02-09 22:50:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

	private List<String> result;
	private String err_msg;
	private String sn;
	private String corpus_no;
	private int err_no;

	public void setResult(List<String> result) {
		this.result = result;
	}

	public List<String> getResult() {
		return result;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSn() {
		return sn;
	}

	public void setCorpus_no(String corpus_no) {
		this.corpus_no = corpus_no;
	}

	public String getCorpus_no() {
		return corpus_no;
	}

	public void setErr_no(int err_no) {
		this.err_no = err_no;
	}

	public int getErr_no() {
		return err_no;
	}

}
