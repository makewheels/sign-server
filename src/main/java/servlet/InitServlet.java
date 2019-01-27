package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import util.HibernateUtil;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -6492012774125702399L;

	@Override
	public void init() throws ServletException {
		HibernateUtil.getSession();
		super.init();
	}

}
