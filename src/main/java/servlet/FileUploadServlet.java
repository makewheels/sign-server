package servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import sign.bean.Image;
import sign.bean.Record;
import user.bean.User;
import util.HibernateUtil;
import util.QcloudCosUtil;
import util.ResponseUtil;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -5695627878375367368L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			// 签到文件
			if (type.equals("sign")) {
				String content = request.getParameter("content");
				// 照片
				if (content.equals("image")) {
					signImage(request, response);
					// 录音
				} else if (content.equals("record")) {
					signRecord(request, response);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 上传签到的图片
	 * 
	 * @param request
	 * @param response
	 */
	private void signImage(HttpServletRequest request, HttpServletResponse response) {
		Image image = new Image();
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getId();
		try {
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> list = upload.parseRequest(request);
			FileItem fileItem = list.get(0);
			if (fileItem.isFormField() == false) {
				String originalFilename = fileItem.getFieldName();
				String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
				String filename = UUID.randomUUID().toString() + "." + extension;
				image.setFilename(filename);
				image.setExtension(extension);
				image.setOriginalFilename(originalFilename);
				image.setSize(fileItem.getSize());
				image.setContentType(fileItem.getContentType());
				try {
					InputStream inputStream = fileItem.getInputStream();
					String relativePath = "/WEB-INF/file/sign/image/" + userId + "/" + filename;
					image.setRelativePath(relativePath);
					File file = new File(getServletContext().getRealPath(relativePath));
					image.setAbsolutePath(file.getPath());
					File folder = file.getParentFile();
					if (folder.exists() == false) {
						folder.mkdirs();
					}
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					IOUtils.copy(inputStream, fileOutputStream);
					String objectStorageKey = "/sign/image/" + user.getUuid() + "/" + UUID.randomUUID().toString() + "."
							+ extension;
					String url = QcloudCosUtil.saveToQcloud(file, objectStorageKey);
					image.setObjectStorageKey(objectStorageKey);
					image.setUrl(url);
					BufferedImage bufferedImage = ImageIO.read(file);
					image.setWidth(bufferedImage.getWidth());
					image.setHeight(bufferedImage.getHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			image.setUserId(userId);
			image.setMissionId(user.getCurrentMissionId());
			String uuid = UUID.randomUUID().toString();
			image.setUuid(uuid);
			image.setIp(request.getRemoteAddr());
			image.setCreateTime(new Date());
			HibernateUtil.save(image);
			Map<String, String> map = new HashMap<>();
			map.put("fileUuid", uuid);
			ResponseUtil.writeJson(response, map);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传签到录音文件
	 * 
	 * @param request
	 * @param response
	 */
	private void signRecord(HttpServletRequest request, HttpServletResponse response) {
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		Record record = new Record();
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getId();
		try {
			List<FileItem> list = upload.parseRequest(request);
			FileItem fileItem = list.get(0);
			if (fileItem.isFormField() == false) {
				String originalFilename = fileItem.getFieldName();
				String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
				String filename = UUID.randomUUID().toString() + "." + extension;
				record.setFilename(filename);
				record.setExtension(extension);
				record.setOriginalFilename(originalFilename);
				record.setSize(fileItem.getSize());
				record.setContentType(fileItem.getContentType());
				try {
					InputStream inputStream = fileItem.getInputStream();
					String relativePath = "/WEB-INF/file/sign/record/" + userId + "/" + filename;
					record.setRelativePath(relativePath);
					File file = new File(getServletContext().getRealPath(relativePath));
					record.setAbsolutePath(file.getPath());
					File folder = file.getParentFile();
					if (folder.exists() == false) {
						folder.mkdirs();
					}
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					IOUtils.copy(inputStream, fileOutputStream);
					String objectStorageKey = "/sign/record/" + user.getUuid() + "/" + UUID.randomUUID().toString()
							+ "." + extension;
					String url = QcloudCosUtil.saveToQcloud(file, objectStorageKey);
					record.setObjectStorageKey(objectStorageKey);
					record.setUrl(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			record.setUserId(userId);
			record.setMissionId(user.getCurrentMissionId());
			String uuid = UUID.randomUUID().toString();
			record.setUuid(uuid);
			record.setIp(request.getRemoteAddr());
			record.setCreateTime(new Date());
			HibernateUtil.save(record);
			Map<String, String> map = new HashMap<>();
			map.put("fileUuid", uuid);
			ResponseUtil.writeJson(response, map);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
