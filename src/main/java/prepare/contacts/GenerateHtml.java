package prepare.contacts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

import prepare.contacts.bean.Contact;
import prepare.contacts.bean.PhoneNumbers;
import prepare.contacts.bean.Photo;

public class GenerateHtml {

	private static String HTML_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\out.html";

	private static void write(String data) {
		try {
			FileUtils.write(new File(HTML_FILE_PATH), data, Charset.defaultCharset().name(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String contactsJsonFilePath = "C:\\Users\\Administrator\\Downloads\\" + "7_phone_1551264865631.json";
		new File(HTML_FILE_PATH).delete();
		String json = FileUtils.readFileToString(new File(contactsJsonFilePath), Charset.defaultCharset().name());
		List<Contact> contactList = JSON.parseArray(json, Contact.class);
		write("<html><head><meta charset='utf-8'></head><body text-align='center'><table border='1'>");
		for (Contact contact : contactList) {
			write("<tr>");
			write("<td>");
			List<Photo> photos = contact.getPhotos();
			if (photos != null && photos.size() != 0) {
				for (Photo photo : photos) {
					String base64 = photo.getValue();
					write("<img src='" + base64 + "'/><br>");
				}
			}
			write("</td>");
			String displayName = contact.getDisplayName();
			write("<td>" + displayName + "</td>");
			write("<td>");
			List<PhoneNumbers> phoneNumbers = contact.getPhoneNumbers();
			for (PhoneNumbers phoneNumber : phoneNumbers) {
				write(phoneNumber.getValue() + "<br>");
			}
			write("</td>");
			write("</tr>");
		}
		write("</table></body></html>");
		System.out.println("Done!");
	}

}
