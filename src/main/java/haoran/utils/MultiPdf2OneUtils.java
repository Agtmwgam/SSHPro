/**
 * <p>Copyright (c) 2017 深圳市鹏途交通科技有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 深圳市国省检日常化监管系统</p>
 * <p>创建者   :	zl
 * 
 * <p>描   述  :   MultiPdf2OneUtils.java for com.pengtu.gsj.utils    </p>
 * 
 * <p>最后修改 : $: 2017年9月6日-上午10:32:12 v 1.0.0	 lei   $ </p>
 * 
*/

package haoran.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 将多个pdf文件合并成一个pdf文件
 * MultiPdf2OneUtils
 * 
 * 2017年9月6日 上午10:32:12
 * 
 * @version 1.0.0
 * 
 */
public class MultiPdf2OneUtils {

	/**
	 * 
	 * getFiles:
	 * 适用:得到文件夹下所有文件的文件名
	 * @param folder
	 * @return  返回字符串数组
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String[] getFiles(String folder) throws IOException {
		File _folder = new File(folder);
		String[] filesInFolder;
		
		if (_folder.isDirectory()) {
			filesInFolder = _folder.list();
			return filesInFolder;
		} else {
			throw new IOException("path is not a directory");
		}
	}
	
	/**
	 * 
	 * mergePdf:
	 * 适用:将多个pdf文件合并到一个文件中
	 * @param SourcePdfPaths  多个pdf路径组成的字符串数组
	 * @param destPdfPath  存放目的pdf文件的地址
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("deprecation")
	public static void mergePdf(String[] sourcePdfPaths, String destPdfPath) throws IOException {
		PDFMergerUtility mergerUtility = new PDFMergerUtility();
		for (String pdfPath : sourcePdfPaths) {
			mergerUtility.addSource(pdfPath);
		}
		mergerUtility.setDestinationFileName(destPdfPath);
		mergerUtility.mergeDocuments();
	}
	
	/**
	 * 
	 * getFilePage:
	 * 适用: 记录每个文件所对应页码数
	 * @param inputStreams  文件流集合
	 * @return 
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<Integer> getFilePages(List<InputStream> inputStreams) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		Iterator<InputStream> iterator = inputStreams.iterator();
		while (iterator.hasNext()) {
			InputStream inputStream = (InputStream) iterator.next();
			PdfReader pdfReader = new PdfReader(inputStream);
			list.add(pdfReader.getNumberOfPages());
		}
		return list;
	} 
	
	/**
	 * 
	 * getFilePage:
	 * 适用:获取单个文件流的页码数
	 * @param inputStream
	 * @return 
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getFilePage(InputStream inputStream) throws Exception {
		return new PdfReader(inputStream).getNumberOfPages();
	}
	
	
	/**
	 * 方法中将输入的Pdf文件流转换成一系列的PdfWriter对象，并记录每一个输入流对应pdf文件的总页数。
	 * 然后创建一个BaseFont对象，这个对象用来写页码。然后创建一个Output对象来写要合并的pdf。最后关闭所有的流
	 * concatPDFs:
	 * 适用:使用itext合并pdf
	 * @param streamOfPDFFiles
	 * @param outputStream
	 * @param paginate 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {
		Document document = new Document();
		
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();
			// Create Readers for the pdfs.
			int i =0;
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = (InputStream) iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				//前两个文档不用重新计算页码
				if (i > 1) {
					totalPages += pdfReader.getNumberOfPages();
				}
				i++;
			}
		
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			
			BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte pdfContentByte = writer.getDirectContent();
			
			PdfImportedPage page;
			int cunrrentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();
			
			i = 0;
			 // Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = (PdfReader) iteratorPDFReader.next();
				 // Create a new page in the target for each source page.
				//从第二个文档开始计算页码
				while(pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					cunrrentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					pdfContentByte.addTemplate(page, 0, 0);
					
					 // Code for pagination.
					if (paginate  && i > 1) {
						pdfContentByte.beginText();
						pdfContentByte.setFontAndSize(baseFont, 9);
						/*alignment:左、右、居中(ALIGN_CENTER, ALIGN_RIGHT or ALIGN_LEFT) text：要输出的文本  x:文本输入的X坐标  y:文本输入的Y坐标 rotation:文本的旋转角度*/
						pdfContentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, cunrrentPageNumber+" / "+totalPages,300, 40, 0);
						pdfContentByte.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
				if (i <= 1) {
					cunrrentPageNumber = 0;
				}
				i++;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen()) {
				document.close();
			}
			
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			}
	}
	
	
	public static void main(String[] args) throws IOException {
		String foler = "C:\\Users\\lei\\Desktop\\pdf";
		String destPdfPath = "C:\\Users\\lei\\Desktop\\merge.pdf";
		String destPdfPath1 = "C:\\Users\\lei\\Desktop\\merge1.pdf";
		String[] filesInFolder = getFiles(foler);
		List<InputStream> streamOfPDFFiles = new ArrayList<InputStream>();
//		for (String string : filesInFolder) {
//			string = foler + File.separator +string;
//			System.out.println("path:"+string);
//		}
		for (int i = 0; i < filesInFolder.length; i++) {
			filesInFolder[i] = foler + File.separator + filesInFolder[i];
			streamOfPDFFiles.add(new FileInputStream(filesInFolder[i]));
		}
		OutputStream outputStream = new FileOutputStream(destPdfPath1);
		mergePdf(filesInFolder, destPdfPath);
		concatPDFs(streamOfPDFFiles, outputStream, true);
		System.out.println("successs....");
	}
}
