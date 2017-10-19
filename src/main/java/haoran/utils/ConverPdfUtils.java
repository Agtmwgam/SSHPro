
package haoran.utils;

import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 利用jacob将office文件转成pdf文件
 * 
 * ConverUtils
 * 
 * 2017年6月12日 上午11:17:37
 * 
 * @version 1.0.0
 * @author lei
 */
public class ConverPdfUtils {
	
	private static  Logger logger = Logger.getLogger(PdfUtils.class);

	public final static String WORDSERVER_STRING="KWPS.Application";
	public final static String PPTSERVER_STRING="KWPP.Application";
	public final static String EXECLSERVER_STRING="KET.Application";
	/*转PDF格式值*/  
	private static final int wdFormatPDF = 17;
	private static final int xlTypePDF = 0;
	private static final int ppSaveAsPDF = 32;
	
	/**
	 * 
	 * officeToPdf:
	 * 适用:
	 * @param sourceFile
	 * @param destFile
	 * @return 操作成功与否的提示信息 如果返回 -1, 表示找不到源文件;如果返回0， 则表示操作成功; 返回1, 则表示转换失败
	 * @exception 
	 * @since  1.0.0
	 */
	public static synchronized int officeToPdf(String sourceFile, String destFile) {
			File inputFile = new File(sourceFile);
			if (!inputFile.exists()) {
				return -1;// 找不到源文件, 则返回-1
			}
			// 如果目标路径不存在, 则新建该路径
			File outputFile = new File(destFile);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}
			String extentionName=getFileExtension(sourceFile);
			if(checkPowerpointType(extentionName))
			{
				ppt2pdf(sourceFile,destFile);
			} else if(checkDocType(extentionName)){
				doc2pdf(sourceFile,destFile);
			} else if(checkExcelType(extentionName)){
				excel2PDF(sourceFile,destFile);
			} else if (checkImageType(extentionName)) {
				imageToPdf(sourceFile, destFile);
			}	
			return 0;
	}
	
	protected static synchronized boolean doc2pdf(String srcFilePath, String pdfFilePath) {  
		 ActiveXComponent pptActiveXComponent=null; 
		 ActiveXComponent workbook = null; 
		try {
        	 ComThread.InitSTA();//初始化COM线程  
             pptActiveXComponent = new ActiveXComponent(WORDSERVER_STRING);//初始化exe程序  
             Variant[] openParams=new Variant[]{
            		new Variant(srcFilePath),//filePath
            		new Variant(true),
            		new Variant(true)//readOnley
             };
             workbook = pptActiveXComponent.invokeGetComponent("Documents").invokeGetComponent
            		("Open",openParams);
             workbook.invoke("SaveAs",new Variant(pdfFilePath),new Variant(wdFormatPDF));                
             return true;
		}finally{
        	 if(workbook!=null)
        	 {
        		 workbook.invoke("Close"); 
        		 workbook.safeRelease(); 
        	 }
        	 if(pptActiveXComponent!=null)
        	 {  		
        		 pptActiveXComponent.invoke("Quit"); 
        		 pptActiveXComponent.safeRelease();
        	 }
        	 ComThread.Release();  
         }
    }
	
	protected static synchronized boolean ppt2pdf(String srcFilePath, String pdfFilePath) {
		 ActiveXComponent pptActiveXComponent=null; 
		 ActiveXComponent workbook = null;  
	     boolean readonly = true;
		try {
        	 ComThread.InitSTA();//初始化COM线程  
             pptActiveXComponent = new ActiveXComponent(PPTSERVER_STRING);//初始化exe程序  
             workbook = pptActiveXComponent.invokeGetComponent("Presentations").invokeGetComponent
            		("Open",new Variant(srcFilePath),new Variant(readonly));
             workbook.invoke("SaveAs",new Variant(pdfFilePath),new Variant(ppSaveAsPDF));                
             return true;
		}finally{
        	 if(workbook!=null)
        	 {
        		 workbook.invoke("Close"); 
        		 workbook.safeRelease(); 
        	 }
        	 if(pptActiveXComponent!=null)
        	 {  		
        		 pptActiveXComponent.invoke("Quit"); 
        		 pptActiveXComponent.safeRelease();
        	 }
        	 ComThread.Release();  
         }
    } 
	 public static synchronized boolean excel2PDF(String srcFilePath,String pdfFilePath){
		ActiveXComponent et = null; 
	    Dispatch workbooks = null;  
	    Dispatch workbook = null;  
	         ComThread.InitSTA();//初始化COM线程  
	         //ComThread.InitSTA(true);  
	         try {  
	             et = new ActiveXComponent(EXECLSERVER_STRING);//初始化et.exe程序  
	             et.setProperty("Visible", new Variant(false));  
	             workbooks = et.getProperty("Workbooks").toDispatch();  
	             //workbook = Dispatch.call(workbooks, "Open", filename).toDispatch();//这一句也可以的  
	             workbook = Dispatch.invoke(workbooks,"Open",Dispatch.Method,new Object[]{srcFilePath,0,true},new int[1]).toDispatch();   
	             //Dispatch.invoke(workbook,"SaveAs",Dispatch.Method,new Object[]{pdfFilePath,xlTypePDF},new int[1]);
	             Dispatch.call(workbook,"ExportAsFixedFormat",new Object[]{xlTypePDF,pdfFilePath});
	             return true;
	         }finally{
	        	 if(workbook!=null)
	        	 {
	        		 Dispatch.call(workbook,"Close");
	        		 workbook.safeRelease(); 
	        	 }
	        	 if(et!=null)
	        	 {  		
	        		 et.invoke("Quit"); 
	        		 et.safeRelease();
	        	 }
	        	 ComThread.Release();  
	         }
	 }
	 
	 public static String getFileExtension(String fileName) {  
	        int splitIndex = fileName.lastIndexOf(".");  
	        return fileName.substring(splitIndex + 1);  
	    } 
	    
	    public static boolean checkDocType(String type){
	    	if (StringUtils.isEmpty(type)) {
				return false;
			}
	    	if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("dot") || type.equalsIgnoreCase("docx") || type.equalsIgnoreCase("dotx")|| type.equalsIgnoreCase("et")||
	    		type.equalsIgnoreCase("docm") || type.equalsIgnoreCase("dotm") || type.equalsIgnoreCase("rtf") || type.equalsIgnoreCase("wpd")|| type.equalsIgnoreCase("txt")) {
				return true;
			}else {
				return false;
			}
	    }
	    
	    public static boolean checkExcelType(String type){
	    	if (StringUtils.isEmpty(type)) {
				return false;
			}
	    	if (type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx") || type.equalsIgnoreCase("xlsm") || type.equalsIgnoreCase("xlsb")||type.equalsIgnoreCase("wps")||
	    		type.equalsIgnoreCase("xlt") || type.equalsIgnoreCase("xltx") || type.equalsIgnoreCase("xltm") || type.equalsIgnoreCase("csv")) {
				return true;
			}else {
				return false;
			}
	    }
	    
	    public static boolean checkPowerpointType(String type){
	    	if (StringUtils.isEmpty(type)) {
	    		return false;
	    	}
	    	if (type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx") || type.equalsIgnoreCase("pptm") || type.equalsIgnoreCase("pps")|| type.equalsIgnoreCase("wpt") ||
	    			type.equalsIgnoreCase("ppsx") || type.equalsIgnoreCase("ppsm") || type.equalsIgnoreCase("pot") || type.equalsIgnoreCase("potx")||type.equalsIgnoreCase("potm")) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }
	    
	    public static boolean checkImageType(String type){
	    	if (StringUtils.isEmpty(type)) {
	    		return false;
	    	}
	    	if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("png") || type.equalsIgnoreCase("bmp") || type.equalsIgnoreCase("tif")|| type.equalsIgnoreCase("gif")||
	    		type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("wbmp")) {
	    		return true;
	    	}else {
	    		return false;
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
	    
	    public static String getSize(Long size) {
	    	//如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义  
	        if (size < 1024) {  
//	        	return String.valueOf(new BigDecimal(size).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
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
	    	
	    	if (!new File(sourceFilePath).exists()) {
				logger.info("pdf源文件不存在,不能转换");
			}
	    	
	    	  // 如果目标路径不存在, 则新建该路径    
	        File outputFile = new File(destFilePath);    
	        if (!outputFile.getParentFile().exists()) {    
	            outputFile.getParentFile().mkdirs();    
	        }    
	    	
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
		    		  loadStream(process.getInputStream());
                      loadStream(process.getErrorStream());
				} else {
					//如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
		            String[] cmd = new String[3];
		            cmd[0] = exePath;
		            cmd[1] = filePath;
		            cmd[2] = filePath + "/" + fileName + ".swf";
		            //Runtime执行后返回创建的进程对象
		            process = Runtime.getRuntime().exec(cmd);
		            loadStream(process.getInputStream());
                    loadStream(process.getErrorStream());
				}
				
				//非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
//		        new DoOutput(process.getInputStream()).start();
//		        new DoOutput(process.getErrorStream()).start();
		        
		        //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
//		        process.waitFor();
			} catch (IOException e) {
				logger.info("配置文件中没有该地址的配置");
				e.printStackTrace();
			} 
	    }
	    
	    static String loadStream(InputStream in) throws IOException
	    {
	        int ptr=0;
	        in=new BufferedInputStream(in);
	        StringBuffer buffer=new StringBuffer();
	        
	        while((ptr=in.read())!=-1);
//	        {
//	            buffer.append((char)ptr);
//	        }
	        return buffer.toString();
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
	           System.out.println(str);
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
	    
	    public static void main(String[] args) {
	    	Long startTime = System.currentTimeMillis();
	    	ConverPdfUtils.officeToPdf("C:\\Users\\lei\\Desktop\\安全生命防护工程模块需求说明v1.0.doc", "C:\\Users\\lei\\Desktop\\doc111.pdf");  
//	    	ConverPdfUtils.officeToPdf("C:\\Users\\lei\\Desktop\text.doc", "C:\\Users\\lei\\Desktop\\doc.pdf");  
//	    	ConverPdfUtils.officeToPdf("C:\\Users\\lei\\Desktop\\研发部4月总结PPT.pptx", "C:\\Users\\lei\\Desktop\\pptx.pdf");  
//	    	ConverPdfUtils.officeToPdf("C:\\Users\\lei\\Desktop\\公路设施清单（汇总）.xlsx", "C:\\Users\\lei\\Desktop\\xlsx.pdf");  
//	    	ConverPdfUtils.officeToPdf("C:\\Users\\lei\\Desktop\\123.txt", "C:\\Users\\lei\\Desktop\\txt.pdf");  
//	    	ConverPdfUtils.imageToPdf("C:\\Users\\lei\\Desktop\\123.jpg", "C:\\Users\\lei\\Desktop\\jpg.pdf");  
//	    	ConverPdfUtils.imageToPdf("C:\\Users\\lei\\Desktop\\123.png", "C:\\Users\\lei\\Desktop\\png.pdf");  
//	    	ConverPdfUtils.imageToPdf("C:\\Users\\lei\\Desktop\\123.bmp", "C:\\Users\\lei\\Desktop\\bmp.pdf");  
//	    	ConverPdfUtils.imageToPdf("C:\\Users\\lei\\Desktop\\test.tif", "C:\\Users\\lei\\Desktop\\tif.pdf"); 
//	    	ConverPdfUtils.pdfToSwf("C:\\Users\\lei\\Desktop\\Redis 入门指南.pdf","C:\\Users\\lei\\Desktop\\pdf333.swf" );
//	    	Long endTime = System.currentTimeMillis();
//	    	System.out.println("花费时间:"+(endTime-startTime));
//	    	ConverPdfUtils.generateImage("C:\\Users\\lei\\Desktop\\tif.pdf","C:\\Users\\lei\\Desktop\\tif.jpg" );
//	    	ConverUtils.pdfToSwf("C:\\Users\\lei\\Desktop\\docx.pdf","C:\\Users\\lei\\Desktop\\docx.swf" );
	        System.out.println("success....");
//	        d.officeToPdf("g:\\testppt.pptx", "g:\\testppt.pdf");  
//	        d.officeToPdf("g:\\testexcel.xlsx", "g:\\testexcel.pdf");  
	    }  
}



