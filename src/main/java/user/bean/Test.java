package user.bean;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class Test {

	public static void main(String[] args) throws MalformedURLException, IOException {
		String qqAvatarUrl = "http://thirdqq.qlogo.cn/qqapp/1104455702/A85FF41E29368A49AFA24C9062B6F7BC/100";
		String filepath = "C:\\Users\\Administrator\\Desktop\\caonima.png";
		FileUtils.copyURLToFile(new URL(qqAvatarUrl), new File(filepath));
	}

}
