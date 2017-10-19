package haoran.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import junit.framework.Assert;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 操作excel
 * 
 * @author
 * @time
 */
public class ExcelHelper {

//	private static final String ENCODE_WHEN_READING = "GBK";
	private static Log log = LogFactory.getLog(ExcelHelper.class);

    private static ExcelHelper excelHelper = new ExcelHelper();
    
    private ExcelHelper(){
    	
    }
	
    /**
     * 获得唯一的实例
     * @return
     */
    public synchronized static ExcelHelper getInstance(){
		if (excelHelper == null) {
			excelHelper = new ExcelHelper();
		}
		return excelHelper;
    }

	/**
	 * 获得excel WorkBook 默认使用GBK编码  通过输入流的形式
	 * 
	 * @param is
	 * @return
	 */
	public  Workbook getExcel(InputStream is) {
		Workbook wb = null;
		try {
			WorkbookSettings ws = new WorkbookSettings();
//			ws.setEncoding(ENCODE_WHEN_READING); // 关键代码，解决中文乱码
			wb = Workbook.getWorkbook(is, ws);
		} catch (Exception e) {
			log.error(e);
		}
		return wb;
	}
	

	/**
	 * 获得excel WorkBook 默认使用GBK编码  通过文件路径
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	public  Workbook getExcel(String path) throws FileNotFoundException {
	   File	file = new File(FileUtils.filterSpecialChart(path)) ;
		return getExcel(file);
	}

	/**
	 * 获得excel WorkBook 默认使用GBK编码  通过文件方式
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	public  Workbook getExcel(File file) throws FileNotFoundException {
		Workbook wb = null;
		try {
			FileUtils.makeDirectory(file);
			WorkbookSettings ws = new WorkbookSettings();
//			ws.setEncoding(ENCODE_WHEN_READING); // 关键代码，解决中文乱码
			wb = Workbook.getWorkbook(file, ws);
		} catch (Exception e) {
			log.error(e);
		}
		return wb;
	}
	
	/**
	 * 获得工作表的所有工作簿
	 * @param wb
	 * @return
	 */
	public  Sheet[] getEachSheet(Workbook wb) {
		if (null != wb) {
			return wb.getSheets();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param st
	 * @param strHeadColContents
	 * @return
	 */
	@SuppressWarnings("unused")
	public  int findColIndex(Sheet st, String strHeadColContents) {
		Assert.assertNull(st);
		Assert.assertNull(strHeadColContents);
		int intIndex = 0;
		if (st!=null) {
		for (int i = 0; i < st.getColumns(); i++) {
			String strCon = st.getCell(i, 0).getContents().toString().trim();
			if (strHeadColContents.equals(strCon)) {
				intIndex = i;
				break;
			}
		}
		}
		return intIndex;
	}

	/**
	 * 
	 * 获得excel 工作簿 工作表  某个单元格的值
	 * @param st   工作表 
	 * @param rowIndex row
	 * @param colIndex col
	 * @return
	 */
	public  String getValueAt(Sheet st, int rowIndex, int colIndex) {
//		Assert.assertNull("工作簿不能为空",st);
		String strValueAt = "";
		if (st.getCell(colIndex, rowIndex) != null) {
			Cell cellUnit = st.getCell(colIndex, rowIndex);
			if (cellUnit.getContents().trim() == null
					|| "".equals(cellUnit.getContents().trim())) {
				strValueAt = null;
			}
			if (cellUnit.getType() == CellType.NUMBER
					|| cellUnit.getType() == CellType.NUMBER_FORMULA) {// 
				NumberCell nc = (NumberCell) cellUnit;
				strValueAt = nc.getContents();
			} else if (cellUnit.getType() == CellType.DATE
					|| cellUnit.getType() == CellType.DATE_FORMULA) {// 
				DateCell dt = (DateCell) cellUnit;
				strValueAt = DateUtils.convertDateToString(dt.getDate());
			} else {
				try {
					strValueAt = cellUnit.getContents().trim();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		return strValueAt;
	}

	/**
	 * 获得excel 工作簿 工作表  某个行\列范围 内单元的内容
	 * @param st
	 * @param rowStart
	 * @param columnStart
	 * @param rowMinus
	 * @return
	 */
	public  String[][] getTableInSheet(Sheet st, int rowStart, int columnStart,
			int rowMinus) {
		String[][] strContents = null;

		int intActualRows = st.getRows() - rowStart - rowMinus;
		int intActualCols = st.getColumns() - columnStart;

		strContents = new String[intActualRows][intActualCols];
		for (int i = 0; i < intActualRows; i++) {
			for (int j = 0; j < intActualCols; j++) {
				strContents[i][j] = getValueAt(st, (i + rowStart),
						(j + columnStart));
			}
		}
		return strContents;
	}

	/**
	 * 创建一个以已有模版为基础的 可以进行写操作的excel文件
	 * createWorkbook:
	 * 适用:
	 * @param file
	 * @param wb
	 * @return
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
    public WritableWorkbook  createWritableWorkbook(File out,Workbook wb) throws IOException {
    	WorkbookSettings ws = new WorkbookSettings();
    	FileUtils.makeDirectory(out);
//		ws.setEncoding(ENCODE_WHEN_READING); // 关键代码，解决中文乱码
    	WritableWorkbook wwb = Workbook.createWorkbook(out, wb, ws);
    	return wwb;
    }
    
	
    /**
	 * 创建一个以已有模版为基础的 可以进行写操作的excel文件
     * createWorkbook:
     * 适用:
     * @param outPath 可进行操作文件路径
     * @param wb  模版excel 
     * @return
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public WritableWorkbook  createWritableWorkbook(String outPath,Workbook wb) throws IOException {
    	 File out = new File(FileUtils.filterSpecialChart(outPath));
    	 return createWritableWorkbook(out, wb);
    }
    /**
     * 创建一个根据已有模版为基础的 可以进行写操作的excel文件
     * createWorkbook:
     * 适用:
     * @param outPath  可进行操作文件的输出路径
     * @param templatePath  模版路径 
     * @return
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public WritableWorkbook  createWritableWorkbook(String outPath,String templatePath) throws IOException {
	 	 Workbook wb = getExcel(templatePath); 
	 	 WritableWorkbook wwb = createWritableWorkbook(outPath, wb);
	 	 wb.close();
    	 return wwb;
   }
    /**
     * 创建一个根据已有模版为基础的 可以进行写操作的excel文件
     * createWorkbook:
     * 适用:
     * @param outPath  可进行操作文件的输出路径
     * @param templatePath  模版路径 
     * @return
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public WritableWorkbook  createWritableWorkbook(File outPath,File templatePath) throws IOException {
    	 Workbook wb = getExcel(templatePath); 
    	 WritableWorkbook wwb = createWritableWorkbook(outPath, wb);
    	 wb.close();
    	 return wwb;
    }
    
	/**
	 * 创建一个以已有模版为基础的 可以进行写操作的excel文件
	 * createWorkbook:
	 * 适用:
	 * @param out  输出流操作
	 * @param wb
	 * @return
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
    public WritableWorkbook  createWritableWorkbook(OutputStream out,Workbook wb) throws IOException {
    	WorkbookSettings ws = new WorkbookSettings();
//		ws.setEncoding(ENCODE_WHEN_READING); // 关键代码，解决中文乱码
    	WritableWorkbook wwb = Workbook.createWorkbook(out, wb, ws);
    	return wwb;
    }
    

    /**
     * 创建一个根据已有模版为基础的 可以进行写操作的excel文件
     * createWorkbook:
     * 适用:
     * @param out  输出流
     * @param templatePath  模版路径 
     * @return
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public WritableWorkbook  createWritableWorkbook(OutputStream out,String templatePath) throws IOException {
	 	 Workbook wb = getExcel(templatePath); 
	 	 WritableWorkbook wwb = createWritableWorkbook(out, wb);
	 	 wb.close();
    	 return wwb;
   }
    
    /**
     * 创建一个根据已有模版为基础的 可以进行写操作的excel文件
     * createWorkbook:
     * 适用:
     * @param out  输出流
     * @param templatePath  模版路径 
     * @return
     * @throws IOException 
     * @exception 
     * @since  1.0.0
     */
    public WritableWorkbook  createWritableWorkbook(OutputStream out,File templatePath) throws IOException {
    	 Workbook wb = getExcel(templatePath); 
    	 WritableWorkbook wwb = createWritableWorkbook(out, wb);
    	 wb.close();
    	 return wwb;
    }
    
    /**
     * 给工作簿指定的行列添加内容 可以是数字 日期 或者字符
     * addCell:
     * 适用:
     * @param sheet
     * @param c
     * @param r
     * @param cont
     * @param st
     * @return
     * @throws Exception 
     * @exception 
     * @since  1.0.0
     */
    public WritableSheet addCell(WritableSheet sheet,int c, int r, Object cont, CellFormat st) throws Exception{
    	WritableCell cell  = null;
	  	  if(null == cont ){
			  cell =  new Blank(c, r, st);     
		  }
	  	  else if(cont instanceof Integer ) {
    		  cell = new jxl.write.Number(c, r,(Integer)cont , st); 
    	  }
    	  else if (cont instanceof Number ) 
    	  {
    		  cell = new jxl.write.Number(c, r,((Number)cont).doubleValue(), st); 
    	  }
    	  else if (cont instanceof Date ) 
    	  {
    		  String date = DateUtils.convertDateToString((Date)cont);
    		  cell =  new Label(c, r, date,st);     
    	  }
    	  else {
    		  cell  = new Label(c, r, cont.toString(), st);   
    	  }
    	  sheet.addCell(cell);
    	  return sheet;
    }
    
    /**
     * 给工作簿指定的行列添加内容 可以是数字 日期 或者字符
     * addCell:
     * 适用:
     * @param sheet
     * @param c
     * @param r
     * @param cont
     * @param st
     * @return
     * @throws Exception 
     * @exception 
     * @since  1.0.0
     */
    public WritableSheet addCellNuStyle(WritableSheet sheet,int c, int r, Object cont) throws Exception{
    	WritableCell cell  = null;
	  	  if(null == cont ){
			  cell =  new Blank(c, r);     
		  }
	  	  else if(cont instanceof Integer ) {
    		  cell = new jxl.write.Number(c, r,(Integer)cont ); 
    	  }
    	  else if (cont instanceof Number ) 
    	  {
    		  cell = new jxl.write.Number(c, r,((Number)cont).doubleValue()); 
    	  }
    	  else if (cont instanceof Date ) 
    	  {
    		  String date = DateUtils.convertDateToString((Date)cont);
    		  cell =  new Label(c, r, date);     
    	  }
    	  else {
    		  cell  = new Label(c, r, cont.toString());   
    	  }
    	  sheet.addCell(cell);
    	  return sheet;
    }
    
    /**
     * 合并单元格 并写入内容   带有样式 适合于表格头部--- 需要指定样式
     * addCell:
     * 适用:
     * @param sheet
     * @param c1
     * @param r1
     * @param c2
     * @param r2
     * @param cont
     * @param st
     * @return
     * @throws Exception 
     * @exception 
     * @since  1.0.0
     */
    public WritableSheet addCell(WritableSheet sheet,int c1, int r1, int c2, int r2, Object cont, CellFormat st) throws Exception{
    	sheet.mergeCells(c1, r1, c2, r2);
    	return addCell(sheet, c1, r1, cont, st);
    	
    }
    /**
     * 合并单元格 并写入内容   带有样式 适合于表格头部--使用默认样式
     * addCell:
     * 适用:
     * @param sheet
     * @param c1
     * @param r1
     * @param c2
     * @param r2
     * @param cont
     * @param st
     * @return
     * @throws Exception 
     * @exception 
     * @since  1.0.0
     */
    public WritableSheet addCell(WritableSheet sheet,int c1, int r1, int c2, int r2, Object cont) throws Exception{
    	sheet.mergeCells(c1, r1, c2, r2);
    	return addCell(sheet, c1, r1, cont);
    	
    }
    /**
     * 给工作簿指定的行列添加内容 可以是数字 日期 或者字符 使用默认样式
     * addCell:
     * 适用:
     * @param sheet  工作簿
     * @param c		col
     * @param r     row
     * @param cont  内容
     * @return
     * @throws Exception 
     * @exception 
     * @since  1.0.0
     */
    public WritableSheet addCell(WritableSheet sheet,int c, int r, Object cont) throws Exception{
	  	CellFormat cellFormat = getNormalCellFormat();
	  	return addCell(sheet, c, r, cont, cellFormat);
    }
    
    /**
     * 获得一个基本的写入字体样式 (即 宋体 11号,正常字体,居中显示 包含边界线)
     * getNormalCellFormat:
     * 适用:
     * @return
     * @throws WriteException 
     * @exception 
     * @since  1.0.0
     */
    public CellFormat getNormalCellFormat() throws WriteException{
    	
		WritableFont font= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat normalFormat = new WritableCellFormat(font);  
	
		normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		normalFormat.setAlignment(Alignment.CENTRE);
		normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);	
		return normalFormat; 
    }
    

    
    /**
     * 往Excel中插入图片  
     * @param sheet  待插入的工作表  
     * @param col 图片从该列开始  
     * @param row 图片从该行开始  
     * @param width 图片所占的列数  
     * @param height 图片所占的行数  
     * @param imageFile 要插入的图片文件  
     */  
    public WritableSheet addImage(WritableSheet sheet,int c, int r, int w, int h, File imageFile ) throws Exception{
    	WritableCell cell  = null;
	  	  if(null == imageFile || !imageFile.isFile()){
			  cell =  new Blank(c, r);    
			  sheet.addCell(cell);
			  return sheet;
		  }
	  	WritableImage ri = new WritableImage(c, r, w, h,  imageFile);        
	  	sheet.addImage(ri);       	 
    	return sheet;
    }
    
    /**
     * 往Excel中插入图片  
     * @param sheet  待插入的工作表  
     * @param col 图片从该列开始  
     * @param row 图片从该行开始  
     * @param width 图片所占的列数  
     * @param height 图片所占的行数  
     * @param imagePath 要插入的图片文件路径 
     */  
    public WritableSheet addImage(WritableSheet sheet,int c, int r, int w, int h,String imagePath ) throws Exception{
    	File imageFile = new File(FileUtils.filterSpecialChart(imagePath));
    	return addImage(sheet, c, r, w, h, imageFile);
    }
    
	
	/**
	 * 
	 * borderFormat:
	 * 适用:excel头部的样式
	 * @return 
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat borderFormat() throws Exception{
		WritableFont borderFont= new WritableFont(WritableFont.createFont("宋体"),21,WritableFont.BOLD);  //宋体
		WritableCellFormat borderFormat = new WritableCellFormat(borderFont);   
		borderFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
		borderFormat.setAlignment(Alignment.CENTRE);
		borderFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return borderFormat;
	}
	/**
	 * 
	 * doubleLineRightFormat:
	 * 适用:Excel单元格样式右边双线,上下左单线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineRightFormat() throws Exception{
		WritableFont doubleLineRightFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineRightFormat = new WritableCellFormat(doubleLineRightFont); 
		doubleLineRightFormat.setBorder(Border.RIGHT, BorderLineStyle.DOUBLE);
		doubleLineRightFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		doubleLineRightFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
		doubleLineRightFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
		doubleLineRightFormat.setAlignment(Alignment.CENTRE);
		doubleLineRightFormat.setVerticalAlignment(VerticalAlignment.CENTRE);	
		return doubleLineRightFormat;
	}
	/**
	 * 
	 * doubleLineLeftFormat:
	 * 适用:Excel单元格样式左边双线,上下左单线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineLeftFormat() throws Exception{
		WritableFont doubleLineLeftFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineLeftFormat = new WritableCellFormat(doubleLineLeftFont); 
		doubleLineLeftFormat.setBorder(Border.LEFT, BorderLineStyle.DOUBLE);
		doubleLineLeftFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		doubleLineLeftFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
		doubleLineLeftFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		doubleLineLeftFormat.setAlignment(Alignment.CENTRE);
		doubleLineLeftFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineLeftFormat;
	}

	/**
	 * 
	 * doubleLineBottomFormat:
	 * 适用:Excel单元格样式下边双线,右边单线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineBottomFormat() throws Exception{
		WritableFont doubleLineBottomFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineBottomFormat = new WritableCellFormat(doubleLineBottomFont); 
		doubleLineBottomFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
		doubleLineBottomFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		doubleLineBottomFormat.setAlignment(Alignment.CENTRE);
		doubleLineBottomFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineBottomFormat;
	}
	
	/**
	 * 
	 * doubleLineBottomAngleFormat:
	 * 适用:Excel单元格样式右边下角双线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineBottomAngleFormat() throws Exception{
		WritableFont doubleLineBottomAngleFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineBottomAngleFormat = new WritableCellFormat(doubleLineBottomAngleFont); 
		doubleLineBottomAngleFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
		doubleLineBottomAngleFormat.setBorder(Border.RIGHT, BorderLineStyle.DOUBLE);
		doubleLineBottomAngleFormat.setAlignment(Alignment.CENTRE);
		doubleLineBottomAngleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineBottomAngleFormat;
	}
	
	/**
	 * 
	 * doubleLineBottomLeftAngleFormat:
	 * 适用:Excel单元格样式左边下角双线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineBottomLeftAngleFormat() throws Exception{
		WritableFont doubleLineBottomLeftAngleFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineBottomLeftAngleFormat = new WritableCellFormat(doubleLineBottomLeftAngleFont); 
		doubleLineBottomLeftAngleFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
		doubleLineBottomLeftAngleFormat.setBorder(Border.LEFT, BorderLineStyle.DOUBLE);
		doubleLineBottomLeftAngleFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		doubleLineBottomLeftAngleFormat.setAlignment(Alignment.CENTRE);
		doubleLineBottomLeftAngleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineBottomLeftAngleFormat;
	}
	
	/**
	 * 
	 * doubleLineTopFormat:
	 * 适用:Excel单元格样式上右角边双线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineTopAngleFormat() throws Exception{
		WritableFont doubleLineTopAngleFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineTopAngleFormat = new WritableCellFormat(doubleLineTopAngleFont); 
		doubleLineTopAngleFormat.setBorder(Border.TOP, BorderLineStyle.DOUBLE);
		doubleLineTopAngleFormat.setBorder(Border.RIGHT, BorderLineStyle.DOUBLE);
		doubleLineTopAngleFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		doubleLineTopAngleFormat.setAlignment(Alignment.CENTRE);
		doubleLineTopAngleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineTopAngleFormat;
	}
	/**
	 * 
	 * doubleLineTopFormat:
	 * 适用:Excel单元格样式上边的双线
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static WritableCellFormat doubleLineTopFormat() throws Exception{
		WritableFont doubleLineTopFont= new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD);  //宋体
		WritableCellFormat doubleLineTopFormat = new WritableCellFormat(doubleLineTopFont); 
		doubleLineTopFormat.setBorder(Border.TOP, BorderLineStyle.DOUBLE);
		doubleLineTopFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		doubleLineTopFormat.setAlignment(Alignment.CENTRE);
		doubleLineTopFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		return doubleLineTopFormat;
	}
}
