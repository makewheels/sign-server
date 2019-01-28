package util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class ResponseUtil {

	public static void writeString(HttpServletResponse response, String str) {
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeJson(HttpServletResponse response, Object object) {
		try {
			response.getWriter().write(JSON.toJSONString(object));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
