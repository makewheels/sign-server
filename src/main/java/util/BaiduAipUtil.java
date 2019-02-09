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
		String recognizeAudio = recognizeAudio("C:\\mysoftware\\tomcatFiles\\sign-app\\sign\\record\\1\\"
				+ "1ed8fdf7-e88d-4732-aa51-8c8c5e803a5f.amr");
		System.out.println(recognizeAudio);
	}

}
