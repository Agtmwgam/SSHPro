
/**
 * 
 */
package haoran.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ibm.icu.util.Calendar;
import haoran.entity.app.Attachment;
import haoran.security.SpringSecurityUtils;
import haoran.service.system.SystemManageService;
import haoran.utils.encode.EncodeUtils;
import haoran.utils.spring.SpringContextHolder;
import haoran.utils.web.ServletUtils;

/**
 * @author 631wj
 * 
 */
public class UpDownFileUtils {

	private static Log log = LogFactory.getLog(UpDownFileUtils.class);
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param attachment
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(HttpServletRequest request,Attachment attachment)
	throws IOException {
		return deleteFile(getAppRootPath(request), attachment);
	}
	
	
	public static Attachment getAttachmentByFile(MultipartFile multipartFile, Attachment attachment, String parentPath) throws Exception {
		SystemManageService systemManageService = SpringContextHolder.getBean("systemManagerServiceImpl");
		String rootPath = ConfigUtils.getConfig("attachment.path"); //获取上传附件的根目录
		String systemPdfPath = ConfigUtils.getConfig("pdf.path"); //获取转换pdf文件的根目录 
		String systemScaleImagePath = ConfigUtils.getConfig("scaleImage.path"); //获取缩略图文件的根目录 
		String systemSWFPath = ConfigUtils.getConfig("swfFile.path"); //获取swf文件的根目录 
		
		// 得到文件后缀名
		final String fileExtension = FileUtils.getTypePart(multipartFile.getOriginalFilename());
		//得到文件的名字
		String fileName = FileUtils.getFileNamePart(multipartFile.getOriginalFilename());
		// 设置文件全路径
		StringBuilder fileNameBuffer = new StringBuilder();
		fileNameBuffer.append(parentPath).append(Constants.FILE_SEP).append(DateUtils.getCurrentStrDate()).append(Constants.FILE_SEP);
		StringBuilder filePath  = new StringBuilder();
		
		Long random = MathsUtils.getRandom();
		//重新命名文件名（文件名+随机数）
		filePath.append(fileName).append(random).append(".").append(fileExtension);
		fileNameBuffer.append(filePath);
		
		String fileMD5 =  FileMD5Utils.getFileMD5String(multipartFile);
		
		//通过文件的md5码去匹配上传的文件是否已经存在
		List<Attachment> list = systemManageService.getAttachmentBy("fileMD5",fileMD5);
		if (list != null && list.size()>0) { //如果文件已经存在，即不需要写入该文件，引用该文件即可
			Attachment existAttachment = list.get(0);
			//赋值已有附件地址
			attachment.setFullPath(existAttachment.getFullPath());
			//赋值已有附件的pdf地址
			attachment.setPdfPath(existAttachment.getPdfPath());
			//赋值已有附件的缩略图地址
			attachment.setScaleImagePath(existAttachment.getScaleImagePath());
			//赋值已有swf附件的地址
			attachment.setSwfPath(existAttachment.getSwfPath());
		} else {
			File file = new File(rootPath, fileNameBuffer.toString());
			FileUtils.makeDirectory(file);
			attachment.setFullPath(fileNameBuffer.toString());
			
			/*将文件写入服务器、转换成pdf、生成缩略图均应用多线程来完成，后续完成*/
			multipartFile.transferTo(file); //上传文件到服务器指定位置
			String pdfPath = random+".pdf";
			String imagePath = random+".jpg";
			String swfPath = random+".swf";
			final String sourceFilePath = rootPath+fileNameBuffer.toString(); //服务器指定地址
			final String destFilePath = systemPdfPath+pdfPath;    //转成pdf文件存放地址
			final String imageFilePath = systemScaleImagePath+imagePath;  //生成图片文件的缩略图地址
			final String swfFilePath = systemSWFPath+swfPath;  //转换swf文件存放地址
			
			new Thread(){
				public void run(){
					try {
						fileToPdfOrScaleImage(fileExtension, sourceFilePath, destFilePath, imageFilePath, swfFilePath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			//设置附件的pdf地址
			//设置附件的缩略图地址
			if ("zip".equals(fileExtension)) {
				attachment.setScaleImagePath(Constants.ZIP_DEFAULT_IMAGE);
			}else {
				attachment.setScaleImagePath(Constants.OTHER_DEFAULT_IMAGE);
			}
			if ("pdf".equals(fileExtension)) {
				attachment.setPdfPath(fileNameBuffer.toString());
				attachment.setScaleImagePath(imagePath);
				attachment.setSwfPath(swfPath);
			} else if (ConverPdfUtils.checkDocType(fileExtension)||ConverPdfUtils.checkExcelType(fileExtension)||ConverPdfUtils.checkPowerpointType(fileExtension)||ConverPdfUtils.checkImageType(fileExtension)) {
				attachment.setPdfPath(pdfPath);
				attachment.setScaleImagePath(imagePath);
				attachment.setSwfPath(swfPath);
			} 
//			else if (checkImageType(fileExtension)) {
//				attachment.setPdfPath(pdfPath);
//				attachment.setScaleImagePath(imagePath);
//				attachment.setSwfPath(swfPath);
//			}
			
		}
		
		/*将文件属性值赋值给对象*/
		attachment.setFileMD5(fileMD5);
		attachment.setFileName(FileUtils.getNamePart(multipartFile.getOriginalFilename()));
		attachment.setUpfileFileName(FileUtils.getNamePart(multipartFile.getOriginalFilename()));
		attachment.setSize(ConverPdfUtils.getSize(multipartFile.getSize()));
		attachment.setType(fileExtension);
		attachment.setOrgId(UserUtils.getUser().getOrg().getId());
		if (attachment.getFileTime() == null) { 
			attachment.setFileTime(DateUtils.getCurrentDate());
		}
		if (StringUtils.isEmpty(attachment.getYear())) {
			attachment.setYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		}
		attachment.setUpfileContentType(multipartFile.getContentType());
		attachment.setUploadPerson(SpringSecurityUtils.getCurrentName());
		attachment.setPath(filePath.toString());
		return attachment;
	}
	
	/**
	 * 
	 * fileToPdfOrScaleImage:
	 * 适用:1.将上传到服务器的文件，根据文件格式，判断是否需要转换成pdf文件
	 *     2.若是.txt .doc .docx .xls .xlsx .ppt .pptx img 均需要转换成pdf文件，接着生成pdf首页的缩略图 最后生成swf文件
	 *     3.若是pdf文件，直接生成缩略图，且pdf文件地址和fullpath地址一样 
	 *     4.若是图片格式(.bmp、.jpg、.jpeg、.gif、.png、.TIF)，则生成图片的缩略图
	 *     5.若文件格式是上述没有提到的格式，则不需要转pdf  pdf文件地址为空，且缩略图地址为默认的缩略图地址
	 * @param fileExtension  文件类型
	 * @param sourceFilePath  源文件的地址
	 * @param destFilePath   目标pdf文件地址
	 * @param imagePath   目标缩略图文件地址
	 * @param swfFilePath  目标swf文件地址
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void fileToPdfOrScaleImage(String fileExtension, String sourceFilePath, String destFilePath, String imagePath, String swfFilePath) throws Exception {
		fileExtension = StringUtils.lowerCase(fileExtension);
		if (new File(sourceFilePath).exists()) {
			 ConverPdfUtils.officeToPdf(sourceFilePath, destFilePath); //转pdf文件  
			 if (ConverPdfUtils.checkImageType(fileExtension)) { 
				 if ("tif".equalsIgnoreCase(fileExtension)) {
					 ConverPdfUtils.generateImage(destFilePath, imagePath);
				} else {
					ScaleImageUtils.resize(Constants.DEFAULT_SCALE, imagePath, new File(sourceFilePath)); //生成缩略图
				}
				 ConverPdfUtils.pdfToSwf(destFilePath, swfFilePath);  //pdf文件转swf文件
			} else if ("pdf".equalsIgnoreCase(fileExtension)) {
				ConverPdfUtils.generateImage(sourceFilePath, imagePath);
				ConverPdfUtils.pdfToSwf(sourceFilePath, swfFilePath);
			} else if (ConverPdfUtils.checkDocType(fileExtension)||ConverPdfUtils.checkExcelType(fileExtension)||ConverPdfUtils.checkPowerpointType(fileExtension)) {
				ConverPdfUtils.generateImage(destFilePath, imagePath);
				ConverPdfUtils.pdfToSwf(destFilePath, swfFilePath);
			}
		} else {
			log.info("源文件不存在,不能转换");
		}
	}
	
	/**
	 * 
	 * checkImageType:
	 * 适用:判断文件的格式是否为图片格式
	 * @param type  文件格式
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean checkImageType(String type) {
		if (StringUtils.isEmpty(type)) {
			return false;
		} else if ("bmp".equals(type) || "jpg".equals(type) || "gif".equals(type) || "png".equals(type) || "tif".equals(type)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * checkNeedToPdf:
	 * 适用:判断文件格式是否需要转换成pdf文件
	 * @param type  文件格式
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean checkNeedToPdf(String type) {
		if (StringUtils.isEmpty(type)) {
			return false;
		} else if ("doc".equals(type) || "docx".equals(type) || "xls".equals(type) || "xlsx".equals(type) || "ppt".equals(type) || "pptx".equals(type) ||"txt".equals(type)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param attachment
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(String rootPath,Attachment attachment)
	throws IOException {
		// 得到文件的存储实际全路径
		StringBuffer filePath = new StringBuffer();
		filePath.append(rootPath).append(
				attachment.getFullPath());
		File file = new File(filePath.toString());
			// 文件已经不存在
		  if(!file.isFile()){
			  return  true;
		  }
		  else{
			  return file.delete();
		  }
		
	}
	
	/**
	 * fileUpload:上传文件到某个地址下
	 * 适用:
	 * @param file 文件
	 * @param path 上传文件的地址
	 * @return 返回上传文件后文件名
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	*/
	public static String fileUpload(CommonsMultipartFile file,String path)
			throws IOException {
		 //用来检测程序运行时间
		//获取输出流
		long date = new Date().getTime();
		OutputStream os = new FileOutputStream(path
				+ date + file.getOriginalFilename());
		//获取输入流 CommonsMultipartFile 中可以直接得到文件的流
		InputStream in = file.getInputStream();
		int temp;
		//一个一个字节的读取并写入
		while ((temp = in.read()) != -1) {
			os.write(temp);
		}
		path=(date + file.getOriginalFilename());
		os.flush();
		os.close();
		in.close();
		return path;
	}
	
	
	public static String fileUpload(MultipartFile multipartFile, String bannerPath) throws Exception {
		if (StringUtils.isEmpty(bannerPath)) {
			return "";
		}
		String fileName = haoran.utils.StringUtils.getUUID()+multipartFile.getOriginalFilename();
		File file = new File(bannerPath, fileName);
		FileUtils.makeDirectory(file);
		/*将文件写入服务器、转换成pdf、生成缩略图均应用多线程来完成，后续完成*/
		multipartFile.transferTo(file); //上传文件到服务器指定位置
		return fileName;
	}

	
	/**
	 * 下载附件表中存储的附件文件
	 * 
	 * @param request
	 * @param response
	 * @param attachment  附件表中的附件
	 * @return
	 * @throws IOException
	 */
	public static boolean downloadFile(HttpServletRequest request,
			HttpServletResponse response, Attachment attachment)
			throws IOException {
		InputStream fileInput = null;
		OutputStream filetoClient = null;
		try {
			// 得到文件的存储实际全路径
			StringBuffer filePath = new StringBuffer();
			filePath.append(getAppRootPath(request)).append(
					attachment.getFullPath());
			// 判断文件是否存在
			File file = new File(filePath.toString());
			// 文件类型
			String upfileContentType = attachment.getUpfileContentType();
			// 下载的文件名
			StringBuffer downloadFileName = new StringBuffer();
			if (!file.isFile()) {
				file = new File(getAppRootPath(request)
						+ Constants.CANNOT_FOUND_FILE_PATH);
				upfileContentType = ServletUtils.IMAGE_TYPE;
				downloadFileName.append("文件不存在.gif");
			} else {
				// 设置下载的文件名
				if (StringUtils.isEmpty(attachment.getFileName()))
					// 这是上传时的文件名
					downloadFileName.append(attachment.getUpfileFileName());
				else {
					// 这是以用户填写的文件名作为下载
					downloadFileName.append(attachment.getFileName());
					downloadFileName.append(".").append(
							getFileExtension(attachment.getUpfileFileName()));
				}
				// 流的形式
				if (StringUtils.isEmpty(upfileContentType)) {
					upfileContentType = ServletUtils.STREAM_TYPE;
				}
			}
			// 设置文件下载头
			ServletUtils.setFileDownloadHeader(response, downloadFileName.toString());
			response.setContentType(upfileContentType);
			// 文件输入流
			fileInput = new BufferedInputStream(new FileInputStream(file));
			// 输出流
			filetoClient = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[4096];
			int len = 0;
			// 写入输出流
			while ((len = fileInput.read(buffer, 0, buffer.length)) != -1) {
				filetoClient.write(buffer, 0, len);
			}
		} catch (IOException e) {
			log.error("can not download", e);
			return false;
		} finally {
			try {
				if (fileInput != null) {
					fileInput.close();
				}
				fileInput = null;

				if (filetoClient != null) {
					filetoClient.close();
				}
				filetoClient = null;
			} catch (IOException e) {
				log.error("can not download", e);
				return false;
			}
		}

		return true;

	}
	/**
	 * 查看附件表中存储的附件图片
	 * 
	 * @param request
	 * @param response
	 * @param attachment
	 * @return
	 * @throws IOException
	 */
	public static boolean downloadImageFile(HttpServletRequest request,
			HttpServletResponse response, Attachment attachment)
			throws IOException {
		InputStream fileInput = null;
		OutputStream filetoClient = null;
		java.util.Date date = new java.util.Date();  
		response.setDateHeader("Last-Modified",date.getTime());  
		response.setDateHeader("Expires",date.getTime()+10000);  
		response.setHeader("Cache-Control", "public"); 
		try {
			// 设置下载的文件名
			StringBuffer downloadFileName = new StringBuffer();
			if (StringUtils.isEmpty(attachment.getUpfileFileName())){
				// 这是上传时的文件名
				downloadFileName.append(attachment.getFileName());
			    downloadFileName.append(".").append(attachment.getType());
			}
			else {
				downloadFileName.append(attachment.getUpfileFileName());//
				
			}
			response.reset();
			// 设置文件下载头
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ URLEncoder.encode(downloadFileName.toString(), "UTF-8")
					+ "\"");
			String contentType = attachment.getUpfileContentType();
			if(StringUtils.isEmpty(contentType)){
				response.setContentType(ServletUtils.STREAM_TYPE);
			}
			// 得到文件的存储实际全路径
			StringBuffer filePath = new StringBuffer();
			//filePath.append(getAppRootPath(request)).append(attachment.getFullPath());
			Properties props = PropertiesUtils.loadProperties("application.properties");
			String full = props.getProperty("file.path");
			filePath.append(full).append(attachment.getFullPath());
			File imageFile = new File(filePath.toString());
			  if(!imageFile.isFile()){
				  imageFile =  new File(getAppRootPath(request)+Constants.CANNOT_FOUND_IMAGE_PATH);  
			  }
			// 文件输入流
			  try {
			    fileInput = new BufferedInputStream(new FileInputStream(imageFile));
			    // 输出流
				filetoClient = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[4096];
				int len = 0;
				// 写入输出流
				while ((len = fileInput.read(buffer, 0, buffer.length)) != -1) {
					filetoClient.write(buffer, 0, len);
				}
			} catch (Exception e) {
				
			}
			
		} catch (IOException e) {
			log.error("can not download", e);
			return false;
		} finally {
			try {
				if (fileInput != null) {
					fileInput.close();
				}
				fileInput = null;

				if (filetoClient != null) {
					filetoClient.close();
				}
				filetoClient = null;
			} catch (IOException e) {
				log.error("can not download", e);
				return false;
			}
		}

		return true;

	}
	
	/**
	 * 下载模版导出excel文件
	 * downloadFileByInputStream:
	 * 适用:  要与  ExcelHelper.createWritableWorkbook(OutputStream out,String templatePath) 配合使用
	 * @param filetoClient
	 * @param response
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean downloadExcelFile(WritableWorkbook wwb ,HttpServletResponse response, String fileName) throws IOException {
		OutputStream filetoClient = null;
			try {
				ServletUtils.setFileDownloadHeader(response, fileName);
				response.setContentType(ServletUtils.EXCEL_TYPE);
				filetoClient = response.getOutputStream();
			} catch (IOException e) {
				log.error("can not download");
				//e.printStackTrace();
				//response.sendError(405, "File cannot found!");
				return false;
			} finally {
				try {
					wwb.write();
					wwb.close();
				} catch (WriteException e1) {
					log.error("can not download"+e1);
					//response.sendError(405, "download error!");
				}
				try {
					if (filetoClient != null) {
						filetoClient.flush();
						filetoClient.close();
					}
					filetoClient =null;
				} catch (IOException e) {
					log.error("can not download");
					//e.printStackTrace();
					//response.sendError(405, "download error!");
					return false;
				} 
			}
			return true;
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean downloadFileByInputStream(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		return	downloadFileByInputStream(request, response, file,null);
	}
	
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean downloadFileByInputStream(HttpServletRequest request, HttpServletResponse response, File file,String downName) throws IOException {
		if (file!=null) {
			InputStream fileInput = null;
			OutputStream filetoClient = null;
			try {
				if (!file.isFile()) {
					file = new File(getAppRootPath(request)
							+ Constants.CANNOT_FOUND_FILE_PATH);
					response.setContentType(ServletUtils.IMAGE_TYPE);
				} 
				response.setContentType(ServletUtils.STREAM_TYPE);
				if(StringUtils.isNotEmpty(downName)){
					ServletUtils.setFileDownloadHeader(response, downName+"."+getFileExtension(file.getName()));
				} else {
					ServletUtils.setFileDownloadHeader(response, file.getName());
				}
				fileInput = new FileInputStream(file);
				filetoClient = response.getOutputStream();
				byte[] buffer = new byte[1024*8];
				int len=0;
				// 写入输出流
				while((len = fileInput.read(buffer)) != -1){
					filetoClient.write(buffer, 0, len);	
				}
				filetoClient.flush();
			} catch (IOException e) {
				log.error("can not download");
				//response.sendError(405, "File cannot found!");
				return false;
			} finally {
				try {
					if (fileInput != null) {
						fileInput.close();
					}
					fileInput = null;
					if (filetoClient != null) {
						filetoClient.close();
					}
					filetoClient =null;
				} catch (IOException e) {
					log.error("can not download");
					//response.sendError(405, "download error!");
					return false;
				} 
			}
			return true;
		} else {
			log.error(" file is null");
			//response.sendError(405, "File cannot found!");
			return false;
		}
	}
	

	/**
	 * 将文件存入服务器指定文件夹
	 * 
	 * @param item
	 *            file信息
	 * @param rootPath
	 *            根目录
	 * @return filePath 文件的路径/除根目录
	 * @throws FileCannotUploadException
	 */
	public static void processUploadedFile(String rootPath,Attachment attachment) throws Exception {
		File upFile = attachment.getUpfile();
		// 得到文件后缀名
		String fileExtension = getFileExtension(attachment.getUpfileFileName());
		// 产生随机数
//		Random random = new Random();
		// 设置文件全路径
		StringBuilder fileNameBuffer = new StringBuilder();
		
		fileNameBuffer.append(FileUtils.getUploadPath()).append(attachment.getTableName()).append(
				Constants.FILE_SEP).append(attachment.getTableField()).append(
						Constants.FILE_SEP).append(attachment.getUpfileContentType()).append(
								Constants.FILE_SEP);
		StringBuilder filePath  = new StringBuilder();
		filePath.append(MathsUtils.getRandom()).append(".").append(fileExtension);
		fileNameBuffer.append(filePath);
		String fullPath = fileNameBuffer.toString();
		File uploadedFile = new File(FileUtils.filterSpecialChart(rootPath), FileUtils.filterSpecialChart(fullPath));
		FileUtils.makeDirectory(uploadedFile);
		FileUtils.copy(upFile, uploadedFile,Boolean.TRUE);
		attachment.setPath(filePath.toString());
		attachment.setFullPath(fullPath);
	}
	
//	/**
//	 * 将照片的缩略图存入服务器指定文件夹
//	 * @param rootPath 
//	 * @return filePath 文件的路径/除根目录
//	 * @throws FileCannotUploadException
//	 */
//	public static void processUploadedThumbnail(String rootPath,Attachment attachment) throws FileCannotUploadException {
//		  try {
//			File upFile = attachment.getUpfile();
//			// 得到文件后缀名
//			String fileExtension = getFileExtension(attachment.getUpfileFileName());
//			// 产生随机数
//			Random random = new Random();
//			// 设置文件全路径
//			StringBuilder fileNameBuffer = new StringBuilder();
//			
//			fileNameBuffer.append(FileUtils.getUploadPath()).append(attachment.getTableName()).append(
//					Constants.FILE_SEP).append(attachment.getTableField()).append(
//							Constants.FILE_SEP).append(attachment.getUpfileContentType()).append(
//									Constants.FILE_SEP).append(Constants.THUMBNAIL_PATH);
//			StringBuilder filePath  = new StringBuilder();
//			filePath.append(MathsUtils.getRandom()).append(".").append(fileExtension);
//			fileNameBuffer.append(filePath);
//			String fullPath = fileNameBuffer.toString();
//			File uploadedFile = new File(FileUtils.filterSpecialChart(rootPath), FileUtils.filterSpecialChart(fullPath));
//			FileUtils.makeDirectory(uploadedFile);
//			FileUtils.copy(upFile, uploadedFile,Boolean.TRUE);
//			Thumbnails.of(upFile).size(50, 50).toFile(uploadedFile); 
//			attachment.setPath(filePath.toString());
//			attachment.setFullPath(fullPath);
//		  } catch (Exception e) {
//				 log.error(e);
//		  }
//	}
	
	/**
	 * 得到根目录
	 * 
	 * @param request
	 * @return
	 */
	public static String getAppRootPath(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		StringBuffer rootPath = new StringBuffer();
		rootPath.append(session.getServletContext().getRealPath("/"));
		return rootPath.toString();
	}
	

	/**判断不允许上传的格式*/ 
	
	public static boolean islegality(String fileName){
	  String fileExt = getFileExtension(fileName);
	  fileExt = fileExt.toLowerCase();
	  if(fileExt.endsWith("exe") || fileExt.endsWith("bat") || fileExt.endsWith("jsp") || fileExt.endsWith("class") || fileExt.endsWith("dll") || fileExt.endsWith("msi") || fileExt.endsWith("com") || fileExt.endsWith("sys") || fileExt.endsWith("jar")){
		  
		  return false;
	  }
	  return true;
	}
	/**
	 * 得到上传文件的后缀名
	 * 
	 * @param item
	 * @return
	 */
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		String extension = "";
		if (fileName != null) {
			extension = FilenameUtils.getExtension(fileName);
		}
		return extension;

	}
	/**
	 * 得到上传文件的后缀名
	 * 
	 * @param item
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		String extension = "";
		if (fileName != null) {
			extension = FilenameUtils.getExtension(fileName);
		}
		return extension;

	}
	/**
	 * 
	 * imageToBasic64:
	 * 适用:// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param imgFilePath
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String imageToBasic64(String imgFilePath) {
			byte[] data = null;
			// 读取图片字节数组
			try {
				if(!FileUtils.isFileExist(imgFilePath)){
					return null;
				}
				InputStream in = new FileInputStream(imgFilePath);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 对字节数组Base64编码
			return EncodeUtils.base64Encode(data);
		}
//		public static void byte2image(byte[] data,String path){
//		    if(data.length<3||path.equals("")) return;
//		    try{
//			    FileImageOutputStream imageOutput = new FileImageOutputStream(new File(FileUtils.filterSpecialChart(path)));
//			    imageOutput.write(data, 0, data.length);
//			    imageOutput.close();
//		    } catch(Exception ex) {
//		      ex.printStackTrace();
//		    }
//		}
//		public static void byte2file(byte[] data,String path){
//		    if(data.length<3||path.equals("")) return;
//		    try{
//		    	FileOutputStream imageOutput = new FileOutputStream(new File(FileUtils.filterSpecialChart(path)));
//			    imageOutput.write(data, 0, data.length);
//			    imageOutput.close();
//		    } catch(Exception ex) {
//		      ex.printStackTrace();
//		    }
//		}
		/** 
	     * 文件转化为字节数组 
	     *  
	     * @param file 
	     * @return 
	     */  
	    public static byte[] getBytesFromFile(File file) {  
	        byte[] ret = null;  
	        try {  
	            if (file == null) {  
	                // log.error("helper:the file is null!");  
	                return null;  
	            }  
	            FileInputStream in = new FileInputStream(file);  
	            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);  
	            byte[] b = new byte[4096];  
	            int n;  
	            while ((n = in.read(b)) != -1) {  
	                out.write(b, 0, n);  
	            }  
	            in.close();  
	            out.close();  
	            ret = out.toByteArray();  
	        } catch (IOException e) {
	             log.error("helper:get bytes from file process error!");  
	            e.printStackTrace();  
	        }  
	        return ret;  
	    } 
	    /**
	     * 直接在浏览器打开文件 但是必须是图片或者二进制文件 .txt , .log等等
	     * @param request
	     * @param response
	     * @param file
	     * @param fileName
	     * @return
	     */
	    public static boolean openInBrowser(HttpServletRequest request,HttpServletResponse response,File file,String fileName){
	    	if(file!=null){
	    	InputStream inputStream=null;
	    	OutputStream outputStream=null;
	    	try {
	    		response.setContentType("text/plain;charset=utf-8");
				String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
				response.setHeader("Content-Disposition,inline", "fileName="+encodedfileName);
				inputStream=new FileInputStream(file);
				outputStream = response.getOutputStream();
				byte[] buffer = new byte[1024*8];
				int k = 0;
				while((k=inputStream.read(buffer))!=-1){
					outputStream.write(buffer, 0, k);
				}
				outputStream.flush();
				return true;
			} catch (Exception e) {
				log.error("打开文件失败");
				e.printStackTrace();
				return false;
			}finally{
				try {
					if (inputStream != null) {
						inputStream.close();
					}
					inputStream = null;

					if (outputStream != null) {
						outputStream.close();
					}
					outputStream = null;
				} catch (IOException e) {
					log.error("can not download", e);
					return false;
				};
			}
	    }
	    else{
	    	log.error("文件不存在");
	    	return false;
	    }
	    }	
	    
	    /**
	     * 直接在浏览器打开pdf
	     * @param request
	     * @param response
	     * @param file
	     * @param fileName
	     * @return
	     */
	    public static boolean openPDF(HttpServletRequest request,HttpServletResponse response,File file,String fileName){
	    	if(file!=null){
	    	InputStream inputStream=null;
	    	OutputStream outputStream=null;
	    	try {
	    		response.setContentType("application/pdf;charset=utf-8");
				String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
				response.setHeader("Content-Disposition", "inline;filename="+encodedfileName);
				inputStream=new FileInputStream(file);
				outputStream = response.getOutputStream();
				byte[] buffer = new byte[1024*8];
				int k = 0;
				while((k=inputStream.read(buffer))!=-1){
					outputStream.write(buffer, 0, k);
				}
				outputStream.flush();
				return true;
			} catch (Exception e) {
				log.error("打开文件失败");
				e.printStackTrace();
				return false;
			}finally{
				try {
					if (inputStream != null) {
						inputStream.close();
					}
					inputStream = null;

					if (outputStream != null) {
						outputStream.close();
					}
					outputStream = null;
				} catch (IOException e) {
					log.error("can not download", e);
					return false;
				};
			}
	    }
	    else{
	    	log.error("文件不存在");
	    	return false;
	    }
	    }
	    
	    /**
		 * 将pdf文件转化成swf文件 
		 * @param fileName 文件的绝对路径
		 * @param destPath 目标路径
		 * @return -1：源文件不存在,-2:格式不正确,-3：发生异常,0:转化成功 
		 * 
		 */
		public static int ConvertPdfToSwf(String fileName,String destPath){
			String destName = "",fileExt = "";
			StringBuffer command = new StringBuffer();
			fileExt = getFileExtension(fileName).toLowerCase();  //文件后缀名
			try{
				File file = new File(fileName);
				if(!file.exists()){//判断源文件是否存在
					return -1;
				}else if(!fileExt.equals("pdf")){//判断文件是否是pdf格式的文件
					return -2;
				}
				else{
					String swftoolsPath = "D://SWFTools";//获取pdf转swf工具的路径
					if(!swftoolsPath.substring(swftoolsPath.length()-1, swftoolsPath.length()).equals("//")){
						swftoolsPath = swftoolsPath+"//";    //在目录后加 "/"
					}
					if(!destPath.substring(destPath.length()-1, destPath.length()).equals("//")){
						destPath = destPath+"//";    //在目录后加 "/"
					}
					File destFile = new File(destPath);
					if(!destFile.exists()){//目标文件路径如果不存在，则创建目录
						destFile.mkdirs();
					}
					destName = file.getName().substring(0, file.getName().length()-4)+".swf";//目标文件名称
					File dest = new File(destPath+destName);
					if(dest.exists()){//目标文件如果不存在，则不用创建   直接获取
						return 0;
					}else {
						command.append(swftoolsPath).append("pdf2swf.exe ").append(fileName).append(" -o ").append(destPath).append(destName).append(" -T 9 -f");
						Process pro = Runtime.getRuntime().exec(command.toString());
						BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
						while(buffer.readLine()!=null);
						return pro.exitValue();
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				return -3;
			}
		}
	    
//		public static void main(String[] args) throws IOException {
//			String images =imageToBasic64("F:\\photo\\000264-1-201207040244510624-0000374.jpg");
//			FileOutputStream out;
//			try {
//				out = new FileOutputStream("E:\\ee.jpg");
//				out.write(EncodeUtils.base64Decode(images));
//				out.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			
//			
//		}
	
}
