package haoran.utils;

/**
 * Copyright: Copyright (c) 2002-2004
 * Company: JavaResearch(http://www.javaresearch.org)
 * 最后更新日期:2003年5月18日
 * @author Cherami
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



/**
 * 此类中封装一些常用的文件操作。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * @since  0.1
 */

public class FileUtils {
	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private FileUtils() {

	}
    
	private static Log log = LogFactory.getLog(FileUtils.class);
	/**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param file 需要修改最后访问时间的文件。
	 * @since  0.1
	 */
	public static void touch(File file) {
		long currentTime = System.currentTimeMillis();
		if (!file.exists()) {
			log.debug("file not found:" + file.getName());
			log.debug("Create a new file:" + file.getName());
			try {
				if (file.createNewFile()) {
					log.debug("Succeeded!");
				} else {
					log.debug("Create file failed!");
				}
			} catch (IOException e) {
				log.debug("Create file failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result) {
			log.debug("touch failed: " + file.getName());
		}
	}

	/**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param fileName 需要修改最后访问时间的文件的文件名。
	 * @since  0.1
	 */
	public static void touch(String fileName) {
		File file = new File(fileName);
		touch(file);
	}

	/**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param files 需要修改最后访问时间的文件数组。
	 * @since  0.1
	 */
	public static void touch(File[] files) {
		for (int i = 0; i < files.length; i++) {
			touch(files[i]);
		}
	}

	/**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param fileNames 需要修改最后访问时间的文件名数组。
	 * @since  0.1
	 */
	public static void touch(String[] fileNames) {
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * 判断指定的文件是否存在。
	 * @param fileName 要判断的文件的文件名
	 * @return 存在时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}
	
    /**
     * 判断目录是否存在，如果不存在就创建
     * 
     * @param path
     */
    public static void createFolder(String path) {
            File dirPath = new File(FileUtils.filterSpecialChart(path));
            if (!dirPath.exists()) {
                    dirPath.mkdirs();
            }
    }

	/**
	 * 创建指定的目录。
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param file 要创建的目录
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean makeDirectory(File file) {
		File parent = file.getParentFile();
		if (parent != null) {
			return parent.mkdirs();
		}
		return false;
	}

    public static boolean createDirectory(String fileName) {
        File file = new File(fileName);
        return file.mkdir();
    }
    /**
     * 判断指定的文件夹是否存在
     * @param directory
     * @return
     */
    public static boolean isDirectoryExist(String directory) {
    	return new File(directory).isDirectory();
    }

	/**
	 * 创建指定的目录。
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param fileName 要创建的目录的目录名
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean makeDirectory(String fileName) {
		File file = new File(fileName);
		return makeDirectory(file);
	}

	/**
	 * 清空指定目录中的文件。
	 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * @param directory 要清空的目录
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
	 * @since  0.1
	 */
	public static boolean emptyDirectory(File directory) {
		boolean result = true;
		File[] entries = directory.listFiles();
		if (entries!=null) {
			for (int i = 0; i < entries.length; i++) {
				if (!entries[i].delete()) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * 清空指定目录中的文件。
	 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * @param directoryName 要清空的目录的目录名
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean emptyDirectory(String directoryName) {
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

	/**
	 * 删除指定下的所有内容。
	 * @param dirName 要删除的目录的目录名
	 * @return 删除成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean deleteDirectory(String dirName) {
		return deleteDirectory(new File(dirName),false);
	}
	
	/**
	 * 
	 * deleteDirectory:
	 * 适用:删除指定目录下的所有文件和该文件夹
	 * @param dirName  要删除的目录的目录名
	 * @param flag  是否删除自身文件夹
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean deleteDirectory(String dirName, boolean flag) {
		return deleteDirectory(new File(dirName),flag);
	}

	/**
	 * 删除指定目录下的所有内容。
	 * @param dir 要删除的目录
	 * @return 删除成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean deleteDirectory(File dir,boolean flag) {
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir
					+ " is not a directory. ");
		}
		File[] entries = dir.listFiles();
		int sz = 0;
		if (entries!=null) {
		sz=entries.length;
		}
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i],true)) {
					return false;
				}
			} else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}
		if (flag && !dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * 如果不是目录删除，删除成功返回 true
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if (file.isFile()) {
			return file.delete();
		} else {
			return false;
		}
	}
	/**
	 * 删除指定文件名的文件，若是目录不删除
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return deleteFile(file);
	}
	/**
	 * 列出目录中的所有内容，包括其子目录中的内容。
	 * @param file 要列出的目录
	 * @param filter 过滤器
	 * @return 目录内容的文件数组。
	 * @since  0.1
	 */
	public static File[] listAll(File file,
			javax.swing.filechooser.FileFilter filter) {
		List<File> list = new ArrayList<File>();
		File[] files;
		if (!file.exists() || file.isFile()) {
			return new File[0];
		}
		list(list, file, filter);
		files = new File[list.size()];
		list.toArray(files);
		return files;
	}

	/**
	 * 将目录中的内容添加到列表。
	 * @param list 文件列表
	 * @param filter 过滤器
	 * @param file 目录
	 */
	private static void list(List<File> list, File file,
			javax.swing.filechooser.FileFilter filter) {
		if (filter.accept(file)) {
			list.add(file);
			if (file.isFile()) {
				return;
			}
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			if (files!=null) {
				for (int i = 0; i < files.length; i++) {
					list(list, files[i], filter);
				}
			}
		}

	}


	/**
	 * 从文件路径得到文件名。
	 * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
	 * @return 对应的文件名
	 * @since  0.4
	 */
	public static String getFileName(String filePath) {
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * 从文件名得到文件绝对路径。
	 * @param fileName 文件名
	 * @return 对应的文件路径
	 * @since  0.4
	 */
	public static String getFilePath(String fileName) {
		File file = new File(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * @param filePath 转换前的路径
	 * @return 转换后的路径
	 * @since  0.4
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

	/**
	 * 从文件名得到UNIX风格的文件绝对路径。
	 * @param fileName 文件名
	 * @return 对应的UNIX风格的文件路径
	 * @since  0.4
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName) {
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

	/**
	 * 得到文件的类型。
	 * 实际上就是得到文件名中最后一个“.”后面的部分。
	 * @param fileName 文件名
	 * @return 文件名中的类型部分
	 * @since  0.5
	 */
	public static String getTypePart(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return StringUtils.lowerCase(fileName.substring(point + 1, length));
		}
	}
	
	public static String getFileNamePart(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return StringUtils.lowerCase(fileName.substring(0,point));
		}
	}

	/**
	 * 得到文件的类型。
	 * 实际上就是得到文件名中最后一个“.”后面的部分。
	 * @param file 文件
	 * @return 文件名中的类型部分
	 * @since  0.5
	 */
	public static String getFileExtName(File file) {
		return getTypePart(file.getName());
	}

	/**
	 * 得到文件的名字部分。
	 * 实际上就是路径中的最后一个路径分隔符后的部分。
	 * @param fileName 文件名
	 * @return 文件名中的名字部分
	 * @since  0.5
	 */
	public static String getNamePart(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return fileName;
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				if (length == 1) {
					return fileName;
				} else {
					return fileName.substring(0, point);
				}
			} else {
				return fileName.substring(secondPoint + 1, point);
			}
		} else {
			return fileName.substring(point + 1);
		}
	}

	/**
	 * 得到文件名中的父路径部分。
	 * 对两种路径分隔符都有效。
	 * 不存在时返回""。
	 * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
	 * @param fileName 文件名
	 * @return 父路径，不存在或者已经是父目录时返回""
	 * @since  0.5
	 */
	public static String getPathPart(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return "";
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				return "";
			} else {
				return fileName.substring(0, secondPoint);
			}
		} else {
			return fileName.substring(0, point);
			
		}
	}

	/**
	 * 得到路径分隔符在文件路径中首次出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathIndex(String fileName) {
		int point = fileName.indexOf('/');
		if (point == -1) {
			point = fileName.indexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置后首次出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @param fromIndex 开始查找的位置
	 * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathIndex(String fileName, int fromIndex) {
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中最后出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathLastIndex(String fileName) {
		int point = fileName.lastIndexOf('/');
		if (point == -1) {
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @param fromIndex 开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathLastIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 将文件名中的类型部分去掉。
	 * @param filename 文件名
	 * @return 去掉类型部分的结果
	 * @since  0.5
	 */
	public static String trimType(String filename) {
		int index = filename.lastIndexOf(".");
		if (index != -1) {
			return filename.substring(0, index);
		} else {
			return filename;
		}
	}

	/**
	 * 得到相对路径。
	 * 文件名不是目录名的子节点时返回文件名。
	 * @param pathName 目录名
	 * @param fileName 文件名
	 * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
	 * @since  0.5
	 */
	public static String getSubpath(String pathName, String fileName) {
		int index = fileName.indexOf(pathName);
		if (index != -1) {
			return fileName.substring(index + pathName.length() + 1);
		} else {
			return fileName;
		}
	}

	/**
	 * 拷贝文件。
	 * @param fromFileName 源文件名
	 * @param toFileName 目标文件名
	 * @return 成功生成文件时返回true，否则返回false
	 * @since  0.6
	 */
	public static boolean copy(String fromFileName, String toFileName) {
		return copy(fromFileName, toFileName, false);
	}
	
	/**
	 * 拷贝文件。
	 * @param fromFileName 源文件名
	 * @param toFileName 目标文件名
	 * @param override 目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since  0.6
	 */
	public static boolean copy(String fromFileName, String toFileName,boolean override) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);
		return copy(fromFile, toFile, override);
	}

	/**
	 * 拷贝文件。
	 * @param fromFile 源文件名
	 * @param toFile 目标文件名
	 * @param override 目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since  0.6
	 */
	public static boolean copy(File fromFile, File toFile,
			boolean override) {
		try {
			if (toFile.isDirectory()) {
				toFile = new File(toFile, fromFile.getName());

			}
			org.apache.commons.io.FileUtils.copyFile(fromFile, toFile);
		} catch (Exception e) {
			 log.error(e);
			return false;
		}
		return true;
		
	}
	
	/***
	 * 
	 * 方法描述 : 
	 *
	 * copy 文件
	 * 项目名称： repair
	 * 类名： FileUtils.java
	 * 创建时间： 2013-12-30 下午02:09:43
	 * @param fromFile
	 * @param toFile
	 * @throws IOException void
	 *@exception 出错信息的描述
	 */
	public static void copy(File fromFile,File toFile) throws IOException { 
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
		        // 新建文件输入流并对它进行缓冲
		        inBuff = new BufferedInputStream(new FileInputStream(fromFile));
	
		        // 新建文件输出流并对它进行缓冲
		        outBuff = new BufferedOutputStream(new FileOutputStream(toFile));
	
		        // 缓冲数组
		        byte[] b = new byte[1024 * 5];
		        int len;
		        while ((len = inBuff.read(b)) != -1) {
		            outBuff.write(b, 0, len);
		        }
		        // 刷新此缓冲的输出流
		        outBuff.flush();
		  } finally {
		        // 关闭流
		        if (inBuff != null)
		            inBuff.close();
		        if (outBuff != null)
		            outBuff.close();
		  }

	}

	public static void writeLineToFile(String path, List<String> list) {
		FileWriter tofile;
		BufferedWriter bfw;
		try {
			tofile = new FileWriter(path, false);

			bfw = new BufferedWriter(tofile);
			for (int i = 0; i < list.size(); i++) {
				bfw.write(list.get(i));
				bfw.newLine();
			}
			bfw.flush();
			bfw.close();
		} catch (FileNotFoundException e) {
			log.debug(e);
		} catch (IOException e) {
			log.debug(e);
		}
	}

	public static void readLineFromFile(String path) {
		FileReader tofile;
		BufferedReader bfw;
		String s = "";
		try {
			tofile = new FileReader(path);
			bfw = new BufferedReader(tofile);

			while ((s = bfw.readLine()) != null) {
				log.debug(s);
				String[] s1 = s.split(",");
				for (int i = 0; i < s1.length; i++) {
					log.debug(s1[i]);
				}
			}
			bfw.close();
		} catch (FileNotFoundException e) {
			log.debug(e);
		} catch (IOException e) {
			log.debug(e);
		}
	}

	public static int getLineCountOfFile(String path) {
		FileReader tofile;
		BufferedReader bfw;
		int count=0;
		try {
			tofile = new FileReader(path);
			bfw = new BufferedReader(tofile);
			while ((bfw.readLine()) != null) {
				count++;
			}
			bfw.close();
		} catch (FileNotFoundException e) {
			log.debug(e);
		} catch (IOException e) {
			log.debug(e);
		}
		return count;
	}

	public static List<String> getLineContentOfFile(String path) {
		LinkedList<String> list=new LinkedList<String>();
		FileReader tofile;
		BufferedReader bfw;
		String s = "";
		try {
			tofile = new FileReader(path);
			bfw = new BufferedReader(tofile);
			while ((s = bfw.readLine()) != null) {
				list.add(s);
			}
			bfw.close();
		} catch (FileNotFoundException e) {
			log.debug(e);
		} catch (IOException e) {
			log.debug(e);
		}
		return list;
	}
	
	public static void addToFileByLine(String path,int line,String content) {
		List<String> list=getLineContentOfFile(path);
		int lineCount=list==null?0:list.size();
		if(line<0||line>lineCount||lineCount==0)
			return;
		list.add(line, content);
		FileWriter tofile;
		BufferedWriter bfw;
		try {
			tofile = new FileWriter(path, false);

			bfw = new BufferedWriter(tofile);
			for (int i = 0; i < list.size(); i++) {
				bfw.write(list.get(i));
				bfw.newLine();
			}
			bfw.flush();
			bfw.close();
		} catch (FileNotFoundException e) {
			log.debug(e);
		} catch (IOException e) {
			log.debug(e);
		}
	}
	
   
	/**
	 * 得到一个目录及其子目录下的所有文件
	 * @param path
	 * @return
	 */
	public static List<File> getReclusiveDirFile(String path,List<File> list){
		
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
					for (int j = 0; j < fList.length; j++) {
						if (fList[j].isDirectory()) {
							//log.debug(fList[j].getPath());
							getReclusiveDirFile(fList[j].getPath(),list); // 在getReclusiveDirFile函数里面又调用了getReclusiveDirFile函数本身
						}
					}	
				}
				if (fList!=null) {
					for (int j = 0; j < fList.length; j++) {
						if (fList[j].isFile()) {
							//log.debug(fList[j].getPath());
							list.add(fList[j]);
						}

					}
				}
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
	
	
	/**
	 * 得到一个目录及其子目录下的某种类型的文件
	 * @param path
	 * @return
	 */
	public static List<File> getReclusiveDirFile(String path,List<File> list,String fileType){
		
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
					for (int j = 0; j < fList.length; j++) {
						if (fList[j].isDirectory()) {
							//log.debug(fList[j].getPath());
							getReclusiveDirFile(fList[j].getPath(),list); // 在getReclusiveDirFile函数里面又调用了getReclusiveDirFile函数本身
						}
					}
				}
				if (fList!=null) {
					for (int j = 0; j < fList.length; j++) {

						if (fList[j].isFile()) {
							if (fileType.equalsIgnoreCase(getFileExtName(fList[j]))) {
								list.add(fList[j]);
							}
						}
					}	
				}
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
	
	
	/**
	 * 得到一个目录的所有目录
	 * @param path
	 * @return
	 */
	public static List<File> getDir(String path){
		
		LinkedList<File> list=new LinkedList<File>();
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
					for (int j = 0; j < fList.length; j++) {
						if (fList[j].isDirectory()) {
							list.add(fList[j]);
						}
					}
				}
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
	/**
	 * 得到一个目录的所有文件
	 * @param path
	 * @return
	 */
	public static List<File> getDirFile(String path){
		
		LinkedList<File> list=new LinkedList<File>();
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isFile()) {
						list.add(fList[j]);
					}
				}
			 }
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
	
	/**
	 * 得到一个目录的所有文件
	 * @param path
	 * @return
	 */
	public static List<String> getDirFileName(String path){
		
		LinkedList<String> list=new LinkedList<String>();
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isFile()) {
						list.add(fList[j].getName());
					}
				}
				}
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
	/**
	 * 得到指定目录下指定文件类型的所有文件
	 * @param path
	 * @param fileType
	 * @return
	 */
	public static List<File> getDirFileByFileType(String path, String fileType) {
		List<File> list=new LinkedList<File>();
		try {
			File f = new File(FileUtils.filterSpecialChart(path));
			if (f.isDirectory()) {
				File[] fList = f.listFiles();
				if (fList!=null) {
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isFile()) {
						if (fileType.equalsIgnoreCase(getFileExtName(fList[j]))) {
							list.add(fList[j]);
						}
					}
				}
			 }
			}
		} catch (Exception e) {
			log.debug("Error： " + e);
		}
		return list;
	}
		  
	    /**  
	     * 递归查找文件  
	     * @param baseDirName  查找的文件夹路径  
	     * @param targetFileName  需要查找的文件名  
	     * @param fileList  查找到的文件集合  
	     */  
	    public static void findFiles(String baseDirName, String targetFileName, List<File> fileList) {   
	        /**  
	         * 算法简述：  
	         * 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，  
	         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。  
	         * 队列不空，重复上述操作，队列为空，程序结束，返回结果。  
	         */  
	        String tempName = null;   
	        //判断目录是否存在   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	        } else {   
	        	String[] filelist = baseDir.list(filter);    
	        	if (filelist!=null) {
	            for (int i = 0; i < filelist.length; i++) {   
	                File readfile = new File(baseDirName + "\\" + filelist[i]);   
	                if(!readfile.isDirectory()) {   
	                    tempName =  readfile.getName();    
	                    if (wildcardMatch(targetFileName, tempName)) {   
	                    	FileUtils.copy(readfile.getAbsoluteFile(), new File("F:\\photo\\cxd\\"), true);
	                        //匹配成功，将文件名添加到结果集   
	                        fileList.add(readfile.getAbsoluteFile());    
	                    }   
	                } else if(readfile.isDirectory()){   
	                    findFiles(baseDirName + "\\" + filelist[i],targetFileName,fileList);   
	                }   
	            }   
	        	}
	        }   
	    }   
	    static FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String dirName = dir.getName();
				if(dirName.contains("PAVASSETINFO")||dirName.contains("PAVIRIINFO")||dirName.contains("PAVPOIINFO")||dirName.contains("PAVRUTINFO")
					||dirName.contains("PROJECTINFO")||dirName.contains("SPATIALINFO")){
					return false;
				}
				return true;
			}
		};
	    static FilenameFilter filter2 = new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				String dirName = dir.getName();
				if(dirName.contains("PAVIMGINFO")||dirName.contains("PAVASSETINFO")||dirName.contains("PAVIRIINFO")||dirName.contains("PAVPOIINFO")||dirName.contains("PAVRUTINFO")
					||dirName.contains("PROJECTINFO")||dirName.contains("SPATIALINFO")){
					return false;
				}
				return true;
			}
		};
	    /**  
	     * 递归查找文件夹  
	     * @param baseDirName  查找的文件夹路径  
	     * @param targetFileName  需要查找的文件名  
	     * @param fileList  查找到的文件集合  
	     */  
	    public static  Boolean findFilePaths(String baseDirName, String targetFileName,  List<File> resultList) {   
	        /**  
	         * 算法简述：  
	         * 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，  
	         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。  
	         * 队列不空，重复上述操作，队列为空，程序结束，返回结果。  
	         */  
	        String tempName = null;   
	        //判断目录是否存在   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	        } else {   

	            String[] filelist = baseDir.list(filter2);  
	            if (filelist!=null) {
	            	for (int i = 0; i < filelist.length; i++) {   
		                File readfile = new File(baseDirName + "\\" + filelist[i]);   
		                if(readfile.isDirectory()) {   
		                    tempName =  readfile.getName();    
		                    if (wildcardMatch(targetFileName, tempName)) {   
		                    	resultList.add(readfile);
		                    	return true;
		                    } else {
		                    	findFilePaths(baseDirName + "\\" + filelist[i], targetFileName, resultList);
		                    }
		                }  
		            } 
				}
	        }   
	        return false;
	    } 
	    /**  
	     * 通配符匹配  
	     * @param pattern    通配符模式  
	     * @param str    待匹配的字符串  
	     * @return    匹配成功则返回true，否则返回false  
	     */  
	    private static boolean wildcardMatch(String pattern, String str) {   
	        int patternLength = pattern.length();   
	        int strLength = str.length();   
	        int strIndex = 0;   
	        char ch;   
	        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {   
	            ch = pattern.charAt(patternIndex);   
	            if (ch == '*') {   
	                //通配符星号*表示可以匹配任意多个字符   
	                while (strIndex < strLength) {   
	                    if (wildcardMatch(pattern.substring(patternIndex + 1),   
	                            str.substring(strIndex))) {   
	                        return true;   
	                    }   
	                    strIndex++;   
	                }   
	            } else if (ch == '?') {   
	                //通配符问号?表示匹配任意一个字符   
	                strIndex++;   
	                if (strIndex > strLength) {   
	                    //表示str中已经没有字符匹配?了。   
	                    return false;   
	                }   
	            } else {   
	                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {   
	                    return false;   
	                }   
	                strIndex++;   
	            }   
	        }   
	        return (strIndex == strLength);   
	    }   

		  /******
		   * 
		   * getTempPath:
		   * 适用:得到临时目录和导出目录
		   * @return 
		   * @exception 
		   * @since  1.0.0
		   */
		  public static String getTempPath(String fileType){
				//获取系统的临时目录，同时判断文件是否存在
				String systempath=System.getProperty("java.io.tmpdir")+"\\ssjc\\";
				File file=new File(systempath);
				if(!file.exists()){
					file.mkdir();
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
				String filename=sdf.format(new Date())+new SecureRandom().nextInt(10);
				//导出路径
				String outputPath =systempath +filename+fileType;
				
				return outputPath;
		  }
	    
		  /******
		   * 
		   * getTempPath:
		   * 适用:得到临时目录和导出目录
		   * @return fileName :加一个文件夹做辨识
		   * @exception 
		   * @since  1.0.0
		   */
		  public static String getTempPath2(String folderName){
				//获取系统的临时目录，同时判断文件是否存在
				String systempath=System.getProperty("java.io.tmpdir")+"\\repair";
				File file=new File(systempath);
				if(!file.exists()){
					file.mkdir();
				}
				//导出路径
				String outputPath =systempath +"\\"+folderName;
				
				return outputPath;
		  } 
		  
		  /**
		   * 
		   * getFileMkdirPath:
		   * 适用:得到临时目录下的一个保存文件的文件夹路径 便于拷贝文件用
		   * @return 
		   * @exception 
		   * @since  1.0.0
		   */
		  public static String getFileMkdirPath(String filePath){
			  //临时目录加上一个默认文件夹及其一个当前时间生成的文件夹做辨别
			  String systemPath = System.getProperty("java.io.tmpdir")+"zipTemp\\"+System.currentTimeMillis()+"\\"+filePath+"\\";
			  File file=new File(systemPath);
			  if(!file.exists()){
				  file.mkdirs();
			  }
			  return systemPath;
		  }  
		/***
		 * 	  
		 * 方法描述 : 
		 *判断图片是否纯在 ，不存在用notfoundimg替代
		 *
		 * 项目名称： repair
		 * 类名： FileUtils.java
		 * 创建时间： 2013-12-17 下午02:24:25
		 * @param notfoundImg
		 * @param imgPath void
		 *@exception 出错信息的描述
		 */
		public static String getImgPath(String notfoundImgPath, String imgPath){
			File file=new File(imgPath);
			if(file.exists()){
				return imgPath;
			}
			return notfoundImgPath;
		}	  
		  
	
		/**
		 * 获取文件上传路径
		 * @return
		 */
		public static String getUploadPath(){
			String p = "upload\\" + 
				DateUtils.getCurrentStrDate() + "\\";
			return p;
		}
		/**
		 * 文件大小
		 * @param fileS
		 * @return
		 */
		public static String formetFileSize(long fileS) {//转换文件大小
	        DecimalFormat df = new DecimalFormat("#.00");
	        String fileSizeString = "";
	        if (fileS < 1024) {
	            fileSizeString = df.format((double) fileS) + "B";
	        } else if (fileS < 1048576) {
	            fileSizeString = df.format((double) fileS / 1024) + "K";
	        } else if (fileS < 1073741824) {
	            fileSizeString = df.format((double) fileS / 1048576) + "M";
	        } else {
	            fileSizeString = df.format((double) fileS / 1073741824) + "G";
	        }
	        return fileSizeString;
	    }
		
		 /***
	     * 	
	     * getRandomName:
	     * 适用:得到一个随机的名字
	     * @return 
	     * @exception 
	     * @since  1.0.0
	     */
		  public static String getRandomName(){
			  SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
			  String filename=sdf.format(new Date())+new SecureRandom().nextInt(1000);
			  return filename;
			  
		  }
		  
	/**
	 * 
	 * downFile: 适用:springMVC下载附件
	 * 
	 * @param sourceFilePath
	 *            附件地址
	 * @param fileName
	 *            下载附件的名称包含格式 eg：123.txt
	 * @return
	 * @throws IOException
	 * @exception
	 * @since 1.0.0
	 */
	public static ResponseEntity<byte[]> downFile(String sourceFilePath,
			String fileName) throws IOException {
			 	File file = new File(sourceFilePath);
				HttpHeaders headers = new HttpHeaders();
				//下载显示的文件名，解决中文名称乱码问题  
				String downloadFileName = new String(fileName.getBytes("GBK"),"ISO8859-1");
				//通知浏览器以attachment（下载方式）打开图片
				headers.setContentDispositionFormData("attachment", downloadFileName);
				 //application/octet-stream ： 二进制流数据（最常见的文件下载）。
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				//HttpStatus.CREATED (201) IE对http1.1 不支持201状态码  所以用了ok 200
				return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(file), headers, HttpStatus.OK); 
	}
		  
		  public static String filterSpecialChart(String path){
			  if (path==null) {
				return "";
			}else {
				if (path.lastIndexOf(".")!=-1) {
					String subStr = path.substring(path.lastIndexOf(".")+1, path.length());
					if ("exe".equals(subStr)||"dll".equals(subStr)||"dat".equals(subStr)||"bat".equals(subStr)||"lnk".equals(subStr)||"html".equals(subStr)||"jsp".equals(subStr)) {
						return path.substring(0, path.lastIndexOf(".")+1)+subStr+"_";
					}else {
						return path;
					}
				}else {
					return path;
				}
			}
		  }
	   
	    public static void main(String[] args) throws IOException {
	    	
	    	
	    }

}
