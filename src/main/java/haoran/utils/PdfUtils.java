package haoran.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * 
 * 
 * PdfUtils
 * 
 * 2017年5月16日 下午5:06:01
 * @author lei
 * @version 1.0.0
 *
 */
public class PdfUtils {

	private static  Logger logger = Logger.getLogger(PdfUtils.class);
	
//	private static String openOfficePath = "C:\\Program Files (x86)\\OpenOffice 4";  //openoffice软件的安装路径  
	
	/**
	 * 
	 * generateImage:
	 * 适用:将pdf的第一页转换成图片格式
	 * @param inputPath 需要生成缩略图的pdf文件的完整路径
	 * @param outPath  生成缩略图存放的完整路径
	 * @exception 
	 * @since  1.0.0
	 */
	public static synchronized void generateImage(String inputPath, String outPath) {
		Document document = null;
		
		try {
			float rotation = 0f;
			//缩略图显示倍数，1表示不缩放，0.5表示缩小到50%
			float zoom = 0.8f;
			
			document = new Document();
			document.setFile(inputPath);
			
			BufferedImage image = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
			Iterator<ImageWriter> iterator  = ImageIO.getImageWritersBySuffix("jpg");
			ImageWriter writer = (ImageWriter) iterator.next();
			
			File outputFile = new File(outPath);
			if (!outputFile.getParentFile().exists()) {    
                outputFile.getParentFile().mkdirs();    
            }   
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			ImageOutputStream outImage = ImageIO.createImageOutputStream(outputStream);
			
			writer.setOutput(outImage);
			writer.write(new IIOImage(image, null, null));
		} catch (Exception e) {
			logger.warn(e);
			e.printStackTrace();
		} 
	}
	
	/** 
     * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice下载地址为 
     * http://www.openoffice.org/ 
     *  
     * <pre> 
     * 方法示例: 
     * String sourcePath = "F:\\office\\source.doc"; 
     * String destFile = "F:\\pdf\\dest.pdf"; 
     * Converter.office2PDF(sourcePath, destFile); 
     * </pre> 
     *  
     * @param sourceFile 
     *            源文件, 绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc, 
     *            .docx, .xls, .xlsx, .ppt, .pptx等. 示例: F:\\office\\source.doc 
     * @param destFile 
     *            目标文件. 绝对路径. 示例: F:\\pdf\\dest.pdf 
     * @return 操作成功与否的提示信息. 如果返回 -1, 表示找不到源文件, 或url.properties配置错误; 如果返回 0, 
     *         则表示操作成功; 返回1, 则表示转换失败 
     */  
    public static synchronized int office2PDF(String sourceFile, String destFile) {  
    	try {   
    		 //如果是txt文件则先将txt后缀改成odt
    		if (sourceFile.endsWith(".txt")) {
			StringBuffer odtPath = new StringBuffer(sourceFile.substring(0, sourceFile.lastIndexOf(".")));
			odtPath.append(".odt");
			FileUtils.copy(sourceFile, odtPath.toString());
			sourceFile = odtPath.toString();
    		}
    		// 源文件目录
            File inputFile = new File(sourceFile);    
            if (!inputFile.exists()) {    
                return -1;// 找不到源文件, 则返回-1    
            }    
    
            // 如果目标路径不存在, 则新建该路径    
            File outputFile = new File(destFile);    
            if (!outputFile.getParentFile().exists()) {    
                outputFile.getParentFile().mkdirs();    
            }    
    
            String OpenOffice_HOME = ConfigUtils.getConfig("openOfficePath");//这里是OpenOffice的安装目录    
            // 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'    
            if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {    
                OpenOffice_HOME += "\\";    
            }    
            // 启动OpenOffice的服务    
            String command = OpenOffice_HOME    
                    + "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";    
            Process pro = Runtime.getRuntime().exec(command);    
            
            // 连接openoffice服务
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(    
                    "127.0.0.1", 8100);    
            connection.connect();    
    
            // 转换word到pdf  
            DocumentConverter converter = new OpenOfficeDocumentConverter(    
                    connection);    
            converter.convert(inputFile, outputFile);    
    
            // 关闭连接  
            connection.disconnect();    
            // 关闭OpenOffice服务的进程    
            pro.destroy();     
    
            return 0;    
        } catch (FileNotFoundException e) {    
            e.printStackTrace();    
            return -1;    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        return 1;    
    }  
    
    /**
     * 
     * txtToPdf:利用itexpdf.jar将text文件转换成pdf文件
     * 适用:需要将txt文件转换成pdf文件的需求
     * @param sourceFilePath  txt文件的绝对地址
     * @param destFilePath    保存pdf文件的地址
     * @throws DocumentException
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public static synchronized void txtToPdf(String sourceFilePath, String destFilePath) throws DocumentException, IOException {
    	com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 80, 80, 30, 30);
    	PdfWriter.getInstance(document, new FileOutputStream(destFilePath));
    	document.open();
    	/*三个参数的意思：
    	 * 第一个参数就是采用的字体，此处使用itext-asian自带的字体
    	 * 第二个参数是横向打印的意思
    	 * 第三个参数是是否把字体嵌入到pdf文档中
    	 * */
    	BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//    	BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);  //使用微软自带的字体，但是如果没有该字体的情况下会出错
    	Font font = new Font(baseFont, 12, Font.NORMAL);
    	BufferedReader reader = null;
    	try {
//    		reader = new BufferedReader(new FileReader(sourceFilePath)); // 这样的写法会导致读取中文文件的时候出现乱码
	    	reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath), "gb2312"));
	    	String line = null;
	    	Paragraph t = null;
	    	while((line = reader.readLine()) != null) {
	    		t = new Paragraph(line, font);
	    		t.setAlignment(Element.ALIGN_LEFT);
	    		t.setLeading(20.0f);
	    		document.add(t);
	    	}
    	} catch (Exception e) {
    		logger.error("目标文件不存在,或者不可读");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();	
			}
			if (document != null) {
				document.close();	
			}
		}
    }
    
    /**
     * 
     * htmlToPdf:
     * 适用:将html文件转换成pdf
     * @param sourceFilePath 文件的绝对地址
     * @param destFilePath 保存pdf文件的地址
     * @throws DocumentException
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public static synchronized void htmlToPdf(String sourceFilePath, String destFilePath) throws DocumentException, IOException {
    	com.itextpdf.text.Document document = new com.itextpdf.text.Document();
    	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFilePath));
    	document.open();
    	XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new FileInputStream(sourceFilePath),Charset.forName("UTF-8"));
    	document.close();
    }
    
    public static String getSize(Long size) {
    	//如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义  
        if (size < 1024) {  
//        	return String.valueOf(new BigDecimal(size).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
            return String.valueOf(size) + "B";  
        } else {  
            size = size / 1024;  
        }  
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位  
        //因为还没有到达要使用另一个单位的时候  
        //接下去以此类推  
        if (size < 1024) {  
            return String.valueOf(size) + "KB";  
        } else {  
            size = size / 1024;  
        }  
        if (size < 1024) {  
            //因为如果以MB为单位的话，要保留最后1位小数，  
            //因此，把此数乘以100之后再取余  
            size = size * 100;  
            return String.valueOf((size / 100)) + "."  
                    + String.valueOf((size % 100)) + "MB";  
        } else {  
            //否则如果要以GB为单位的，先除于1024再作同样的处理  
            size = size * 100 / 1024;  
            return String.valueOf((size / 100)) + "."  
                    + String.valueOf((size % 100)) + "GB";  
        }  
    }
    
    /**
     * 
     * imageToPdf:
     * 适用:利用iText将图片文件按照一定的比例转换成pdf文件
     * @param sourceFilePath 源文件地址
     * @param destFilePath   转换pdf文件存放地址
     * @exception 
     * @since  1.0.0
     */
    public static synchronized void imageToPdf(String sourceFilePath, String destFilePath) {
    		//创建一个文档对象
    		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
    		try {
    			 //定义输出文件的位置  
				PdfWriter .getInstance(document, new FileOutputStream(destFilePath));
				//开启文档  
				document.open();
				
				//原来的图片的路径
				Image image = Image.getInstance(sourceFilePath);
				
				//获得图片的高度和宽度
				float height = image.getHeight();
				float width = image.getWidth();
				System.out.println("height:"+height);
				System.out.println("width:"+width);
				
				//统一按照宽度压缩
				int percent = getPercent2(height, width);
				 //设置图片居中显示
				image.setAlignment(Image.MIDDLE);
				//按百分比显示图片的比例
				image.scalePercent(percent);
				//向文档中加入图片 
				document.add(image);
				//关闭文档并释放资源  
				document.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    
    /**
     * 第一种解决方案
     * 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
     * @param h
     * @param w
     * @return
     */
    public static int getPercent(float h,float w)
    {
        int p=0;
        float p2=0.0f;
        if(h>w)
        {
            p2=297/h*100;
        }
        else
        {
            p2=210/w*100;
        }
        p=Math.round(p2);
        return p;
    }
    
    /**
     * 第二种解决方案，统一按照宽度压缩
     * 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
     * @param args
     */
    public static int getPercent2(float h,float w)
    {
        int p=0;
        float p2=0.0f;
        p2=530/w*100;
        p=Math.round(p2);
        return p;
    }
    
    /**
     * 
     * pdfToSwf:
     * 适用:利用SWFTools工具将pdf转换成swf
     * @param sourceFilePath PDF文件存放路径（包括文件名）
     * @param destFilePath  swf文件存放路径（包括文件名）
     * @exception 
     * @since  1.0.0
     */
    public static synchronized void pdfToSwf(String sourceFilePath, String destFilePath) {
    	 //文件路径
    	String filePath = destFilePath.substring(0, destFilePath.lastIndexOf("\\"));
    	//文件名，不带后缀
    	String fileName = destFilePath.substring(filePath.length()+1, destFilePath.lastIndexOf("."));
    	
    	  // 如果目标路径不存在, 则新建该路径    
        File outputFile = new File(destFilePath);    
        if (!outputFile.getParentFile().exists()) {    
            outputFile.getParentFile().mkdirs();    
        }    
    	
    	System.out.println("filePath:"+filePath);
    	System.out.println("fileName:"+fileName);
    	try {
			String exePath = ConfigUtils.getConfig("SWFToolsPath");
			Process process = null;
			
			if (isWindowsSystem()) {
					//如果是windows系统
					//命令行命令   有些pdf转成swf之后不能在flexpaper中显示，是版本的问题，必须要swf9版本以上 所以加上-T 9 
					//swf在flexpaper中搜索内容不能高亮，则需要添加 -f
	    		  String cmd = exePath + " \"" + sourceFilePath + "\" -o \"" + filePath + "/" + fileName + ".swf\" -T 9 -f";
	    		  System.out.println("cmd:"+cmd);
	    		  process = Runtime.getRuntime().exec(cmd);
			} else {
				//如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
	            String[] cmd = new String[3];
	            cmd[0] = exePath;
	            cmd[1] = filePath;
	            cmd[2] = filePath + "/" + fileName + ".swf";
	            //Runtime执行后返回创建的进程对象
	            process = Runtime.getRuntime().exec(cmd);
			}
			
			//非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
	        new DoOutput(process.getInputStream()).start();
	        new DoOutput(process.getErrorStream()).start();
	        
	        //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
	        process.waitFor();
		} catch (IOException e) {
			logger.info("配置文件中没有该地址的配置");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public static boolean isWindowsSystem(){
    	String osName = System.getProperty("os.name");
    	return osName.toLowerCase().indexOf("windows") >= 0 ? true : false;
    }
    
    /**
     * 多线程内部类
     * 读取转换时cmd进程的标准输出流和错误输出流，这样做是因为如果不读取流，进程将死锁
     * DoOutput
     * 
     * 2017年6月8日 下午5:04:36
     * @author lei
     * @version 1.0.0
     *
     */
   private static class DoOutput extends Thread {
       public InputStream is;
     
       //构造方法
        public DoOutput(InputStream is) {
           this.is = is;
       }
     
       public void run() {
           BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
           String str = null;
           try {
               //这里并没有对流的内容进行处理，只是读了一遍
                 while ((str = br.readLine()) != null);
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               if (br != null) {
                   try {
                       br.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
   } 
    
    
    
	
	public static void main(String[] args) throws DocumentException, IOException {
		String sourceFilePath = "C:\\Users\\lei\\Desktop\\（国省检）软件需求规格说明书.pdf";
		String destFilePath = "C:\\Users\\lei\\Desktop\\（国省检）软件需求规格说明书.swf";
//		imageToPdf(sourceFilePath, destFilePath);
//		office2PDF(sourceFilePath, destFilePath);
		pdfToSwf(sourceFilePath, destFilePath);
		System.out.println("success.....");
	}
}
