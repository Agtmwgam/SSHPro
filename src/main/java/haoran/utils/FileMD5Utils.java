/**
 * <p>Copyright (c) 2017 深圳市鹏途交通科技有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 	         </p>
 * <p>创建者   :	lei 
 * 
 * <p>描   述  :   FileMD5Utils.java for com.pengtu.gsj.utils    </p>
 * 
 * <p>最后修改 : $: 2017年5月11日-上午9:27:11 v 1.0.0	 lei   $ </p>
 * 
 */

package haoran.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * FileMD5Utils
 * 
 * 2017年5月11日 上午9:27:11
 * 
 * @version 1.0.0
 * @author lei 用MD5验证上传文件的完整性
 * 
 */
public class FileMD5Utils {

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	protected static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			System.err.println(FileMD5Utils.class.getName()
					+ "初始化失败，MessageDigest不支持MD5Util。");
			e.printStackTrace();
		}
	}

	/**
	 * 生成字符串的md5校验值
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * 判断字符串的md5校验码是否与一个已知的md5码相匹配
	 * 
	 * @param password
	 *            要校验的字符串
	 * @param md5PwdStr
	 *            已知的md5校验码
	 * @return
	 */
	public static boolean checkPassword(String md5, String md5PwdStr) {
		return md5.equals(md5PwdStr);
	}

	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[8192];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, numRead);
		}
		fis.close();
		return bufferToHex(messageDigest.digest());
	}
	
	public static String getFileMD5String(MultipartFile multipartFile) throws IOException {
		InputStream fis;
		fis = multipartFile.getInputStream();
		byte[] buffer = new byte[8192];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, numRead);
		}
		fis.close();
		return bufferToHex(messageDigest.digest());
	}	
	

	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bufferToHex(messageDigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
												// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();

		File file = new File("C:/Users/lei/Desktop/1.txt");
		String md5 = getFileMD5String(file);

		// 文件名不同，内容相同；
		File file2 = new File("C:/Users/lei/Desktop/2.txt");
		String md52 = getFileMD5String(file2);

		// 文件名不同，内容不同；
		File file3 = new File("C:/Users/lei/Desktop/3.txt");
		String md53 = getFileMD5String(file3);

		// 测试压缩包
		File fileZip = new File("C:/Users/lei/Desktop/1.zip");
		String md5Zip = getFileMD5String(fileZip);

		// 测试压缩包
		File fileZip2 = new File("C:/Users/lei/Desktop/2.zip");
		String md5Zip2 = getFileMD5String(fileZip2);
		
		// 测试word 与pdf文件内容相同
		File fileDoc = new File("C:/Users/lei/Desktop/1.doc");
		String md5Doc = getFileMD5String(fileDoc);
		
		// 测试pdf 与word内容相同
		File filePdf = new File("C:/Users/lei/Desktop/1.pdf");
		String md5Pdf = getFileMD5String(filePdf);

		System.out.println("MD5:" + md5);
		System.out.println("MD5:" + md52);
		System.out.println("MD5:" + md53);
		System.out.println("MD5:" + md5Zip);
		System.out.println("MD5:" + md5Zip2);
		System.out.println("MD5:" + md5Doc);
		System.out.println("MD5:" + md5Pdf);
		System.out.println("两个文件名不同，内同相同" + checkPassword(md5, md52));
		System.out.println("文件名不同，内容不同" + checkPassword(md5, md53));
		System.out.println("测试压缩包,内容不同" + checkPassword(md5Zip, md5Zip2));
		System.out.println("内容相同，格式不一样" + checkPassword(md5Doc, md5Pdf));

		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000)
				+ "s");
	}
}
