package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import util.DbUtil;
import util.HibernateUtil;

/**
 * Tomcat启动时初始化
 * 
 * @author Administrator
 *
 */
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -6492012774125702399L;

	@Override
	public void init() throws ServletException {
		HibernateUtil.init();
		DbUtil.init();
		super.init();
	}

}
