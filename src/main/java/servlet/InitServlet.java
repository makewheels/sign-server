package servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import util.Constants;
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
		// 初始化文件存放目录
		File folder = new File(
				new File(getServletContext().getRealPath("/")).getParentFile().getParentFile().getParentFile().getPath()
						+ "/tomcatFiles" + getServletContext().getContextPath());
		if (folder.exists() == false) {
			folder.mkdirs();
		}
		Constants.ROOT_PATH = folder.getPath();
		super.init();
	}

}
