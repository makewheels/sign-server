package util;

import com.baidu.aip.speech.AipSpeech;

/**
 * 百度语音识别
 * 
 * @author Administrator
 *
 */
public class BaiduAipUtil {

	private static String APP_ID = "15538967";
	private static String API_KEY = "B3nebm3xGQXQTtsGUpmym1lO";
	private static String SECRET_KEY = "RHCbDOGZ9j1XKRPQdHYVaCxkd3NUkoUq";
	private static AipSpeech client;

	static {
		client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
	}

	/**
	 * 识别本地录音文件
	 * 
	 * @param filepath
	 * @return
	 */
	public static String recognizeAudio(String filepath) {
		String suffix = filepath.substring(filepath.lastIndexOf(".") + 1);
		return client.asr(filepath, suffix, 16000, null).toString();
	}

	public static void main(String[] args) {
		String filepath = "C:\\mysoftware\\tomcatFiles\\sign-app\\sign\\record\\2\\"
				+ "09c58cbe-94b6-4f65-abc7-2fb1a5500673.amr";
		String recognizeAudio = recognizeAudio(filepath);
		System.out.println(recognizeAudio);
	}

}
