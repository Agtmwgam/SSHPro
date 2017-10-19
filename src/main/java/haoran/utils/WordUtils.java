package haoran.utils;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.field.RtfTotalPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;
import com.lowagie.text.rtf.style.RtfFont;

/**
 * 
 * word文档的相关操作
 * WordUtils
 * 
 * 2017年9月8日 上午10:59:28
 * @author zl
 * @version 1.0.0
 *
 */
public class WordUtils {

	/**
	 * 
	 * addContentToWord:
	 * 适用:主要是在word文档中添加一个表格 在下载检查表的word文档后添加文件内容目录
	 * @param wordSourcesPath 
	 * @param values 表格参数
	 * @exception 
	 * @since  1.0.0
	 */
	public static void addContentToWord(String wordSourcesPath, List<String> values) {
		
		
	}
	
	 public static void createDocContext(String file, String mainTitle, String headTitle, int[] widths, List<String> headTr, List<List<String>> contents)throws Exception{  
	        //设置纸张大小  
	        Document document = new Document(PageSize.A4);  
	        //建立一个书写器，与document对象关联  
	        RtfWriter2.getInstance(document, new FileOutputStream(file));  
	        document.open();  
	        
	        //插入标题
	        insertMainTitle(document, mainTitle, 28, Font.BOLD, Element.ALIGN_CENTER);
	        //插入表格
	        insertTable(document, headTitle, widths, headTr, contents);
	        //插入页脚  页码数
	        insertFooterPageAndTotal(document);
	        
	        document.close();  
	    } 
	 
	 /**
	  * 
	  * insertTable:
	  * 适用:向文档中插入表格
	  * @param document 文档对象
	  * @param headTitle 表格标题头内容
	  * @param widths 列所占的宽度比例数组
	  * @param headTr  表格th 内容集合
	  * @param content 表格内容集合
	  * @throws Exception 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertTable(Document document, String headTitle, int[] widths, List<String> headTr, List<List<String>> contents) throws Exception {
	        //设置中文字体  
	        BaseFont bfChinese = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		 	Table table = new Table(headTr.size());  
//	        int width[] = {5,15,20,50,5,5};//设置每列宽度比例  
	        table.setWidths(widths);  
	        table.setWidth(100);//占页面宽度比例  
	        table.setAlignment(Element.ALIGN_CENTER);//居中  
	        table.setAlignment(Element.ALIGN_MIDDLE);//垂直居中  
	        table.setAutoFillEmptyCells(true);//自动填满  
	        table.setBorderWidth(2f);//边框宽度
	        table.setBorderColor(Color.BLACK);
	        
	        //设置表格标题行
	        Font fontChinese = new Font(bfChinese,14,Font.BOLD,Color.BLACK);  
	        Cell cell = null;
	        for (String tableTh : headTr) {
				cell = new Cell(new Paragraph(tableTh,fontChinese)); 
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidth(1);
				table.addCell(cell);
			}
	        
	        int i = 1;
	        fontChinese = new Font(bfChinese,14,Font.NORMAL,Color.BLACK);  
	        //向表格中填充内容
	        for (List<String> list : contents) {
	        	//序号
	        	cell = new Cell(new Paragraph(String.valueOf(i),fontChinese)); 
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidth(1);
				table.addCell(cell);
				for (String tdContent : list) {
					cell = new Cell(new Paragraph(tdContent,fontChinese)); 
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorderWidth(1);
					table.addCell(cell);
				}
				i++;
			}
	        document.add(table);
	        document.add(new Paragraph("\n"));
	 }
	 
	 /**
	  * 
	  * insertMainTitle:
	  * 适用:插入主标题，三号黑体加粗，居中
	  * @param document 文档对象
	  * @param title 标题内容
	  * @throws DocumentException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertMainTitle(Document document,String title) throws DocumentException, IOException{
		 	RtfFont titleFont = new RtfFont("宋体_GB2312", 16, Font.BOLD, Color.BLACK);
			Paragraph paragraph = new Paragraph(title);
			paragraph.setAlignment(Element.ALIGN_CENTER);  
			paragraph.setFont(titleFont);  
			document.add(paragraph); 
		}
	 
	 /**
	  * 
	  * insertMainTitle:
	  * 适用:根据用户提供的参数插入主标题
	  * @param document
	  * @param title 标题内容
	  * @param fontSize 字体大小
	  * @param fontStyle 字体样式
	  * @param elementAlign 标题位置
	  * @throws DocumentException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertMainTitle(Document document,String title,int fontSize,
			int fontStyle,int elementAlign) throws DocumentException, IOException{
		 	RtfFont titleFont = new RtfFont("宋体_GB2312", fontSize, fontStyle, Color.BLACK);
			Paragraph paragraph = new Paragraph(title);
			paragraph.setAlignment(elementAlign);  
			paragraph.setFont(titleFont);  
			document.add(paragraph); 
		}

	 
	 /**
	  * 
	  * insertFooterPageAndTotal:
	  * 适用:向文档中插入页码  结果格式： 第 1 页 共 3 页
	  * @param document
	  * @throws DocumentException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertFooterPageAndTotal(Document document) throws DocumentException, IOException{
		 	RtfFont footerFont = new RtfFont("宋体_GB2312", 12, Font.COURIER, Color.BLACK);
			Paragraph parafooter = new Paragraph();
//			footerFont = new RtfFont("宋体_GB2312", fontsize, fontStyle, Color.BLACK);
			parafooter.setFont(new Font(footerFont));
			parafooter.add(new Phrase("第 "));
			parafooter.add(new RtfPageNumber());
			parafooter.add(new Phrase(" 页  共 "));
			parafooter.add(new RtfTotalPageNumber());
			parafooter.add(new Phrase(" 页"));

			HeaderFooter footer = new RtfHeaderFooter(parafooter);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);
		}
	 /**
	  * 
	  * insertFooterPage:
	  * 适用:向文档中插入页码  结果形式： 第 1 页
	  * @param document
	  * @throws DocumentException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertFooterPage(Document document) throws DocumentException, IOException{
		 	RtfFont footerFont = new RtfFont("宋体_GB2312", 12, Font.COURIER, Color.BLACK);
			Paragraph parafooter = new Paragraph();
			parafooter.setFont(new Font(footerFont));
			parafooter.add(new Phrase("第"));
			parafooter.add(new RtfPageNumber());
			parafooter.add(new Phrase("页"));
			HeaderFooter footer = new RtfHeaderFooter(parafooter);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);
		}
	 
	 /**
	  * 
	  * insertHeader:
	  * 适用:插入字符串形式的页眉，用来描述文档
	  * @param document 文档对象
	  * @param headerStr
	  * @throws DocumentException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static  void insertHeader(Document document,String headerStr) throws DocumentException, IOException{
		 	RtfFont headerFont = new RtfFont("仿宋_GB2312", 14, Font.BOLD, Color.BLACK);
			Paragraph paraHeader = new Paragraph();
			paraHeader.setFont(new Font(headerFont));
			paraHeader.add(new Phrase(headerStr));
			HeaderFooter header = new RtfHeaderFooter(paraHeader);
			header.setAlignment(Element.ALIGN_CENTER);
			header.setBorder(Rectangle.NO_BORDER);
			document.setHeader(header);
		}
	 /**
	  * 
	  * insertHeader:
	  * 适用:插入图片形式的页眉，由参数定义
	  * @param document 文档对象
	  * @param imgUrl 图片地址
	  * @param height 高度
	  * @throws BadElementException
	  * @throws MalformedURLException
	  * @throws IOException 
	  * @exception 
	  * @since  1.0.0
	  */
	 public static void insertHeader(Document document,String imgUrl,int height) throws BadElementException, MalformedURLException, IOException{
			Image img = Image.getInstance(imgUrl);
			img.setAlignment(Element.ALIGN_CENTER);
			img.scaleAbsolute(document.getPageSize().getWidth()*88/100, document.getPageSize().getHeight()*height/100);
			RtfHeaderFooter header = new RtfHeaderFooter(img);
			document.setHeader(header);
		}
	 
	 public static void main(String[] args) throws Exception {  
	        String file = "C:\\Users\\lei\\Desktop\\test.doc";  
//	        String pdf = "C:\\Users\\lei\\Desktop\\test.pdf";  
	        int[] widths = {8,10,15,51,8,8};
	        List<String> headTr = new ArrayList<String>();
	        headTr.add("序号");
	        headTr.add("责任者");
	        headTr.add("文号");
	        headTr.add("文件标题（内容）");
	        headTr.add("页码");
	        headTr.add("备注");
	        List<List<String>> content = new ArrayList<List<String>>();
	        try {  
	            createDocContext(file, "卷内文件目录", "", widths, headTr, content);
//	            ConverPdfUtils.officeToPdf(file, pdf);
	        } catch (DocumentException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
