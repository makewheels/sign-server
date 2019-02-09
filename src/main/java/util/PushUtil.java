package util;

import java.util.Map;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;

/**
 * 个推推送
 * 
 * @author Administrator
 *
 */
public class PushUtil {
	private static String appId = "FrBpkWfXDX6EwqVWOfimY4";
	private static String appKey = "M7bdxptHq27qS0G8P6gBk3";
	private static String masterSecret = "nsX3cbFuIb84SXteSk8mq7";

	private static IGtPush push;

	static {
		push = new IGtPush(appKey, masterSecret);
	}

	/**
	 * 获取用户在线状态
	 * 
	 * @param clientId
	 * @return
	 */
	public static boolean getOnlineStatus(String clientId) {
		IQueryResult clientIdStatus = push.getClientIdStatus(appId, clientId);
		if (clientIdStatus.getResponse().get("result").equals("Online")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据clientId推送到单个用户
	 * 
	 * @param clientId
	 * @param title
	 * @param text
	 * @param logoUrl
	 * @return
	 */
	public static Map<String, Object> pushByClientId(String clientId, String title, String text, String logoUrl) {
		NotificationTemplate notificationTemplate = new NotificationTemplate();
		notificationTemplate.setAppId(appId);
		notificationTemplate.setAppkey(appKey);
		notificationTemplate.setTransmissionType(1);
		Style0 style = new Style0();
		style.setTitle(title);
		style.setText(text);
		style.setLogoUrl(logoUrl);
		notificationTemplate.setStyle(style);
		SingleMessage singleMessage = new SingleMessage();
		singleMessage.setData(notificationTemplate);
		Target target = new Target();
		target.setAppId(appId);
		target.setClientId(clientId);
		IPushResult result = push.pushMessageToSingle(singleMessage, target);
		return result.getResponse();
	}

}
