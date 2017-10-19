package haoran.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import haoran.entity.afgc.LifeCherish;
import haoran.utils.web.SpringMvcUtils;

/**
 * 
 * 
 * ExcelImportUtils
 * excel导入处理数据
 * 2017年6月14日 下午5:53:37
 * @author lei
 * @version 1.0.0
 *
 */
public class ExcelImportUtils {
	
	//总行数
	private static int totalRows = 0;
	//总条数
	private static int totalCells = 0;
	
	private static final String ERRORNULL = "值不能为空";
	private static final String ERRORCHANGE = "值不在指定范围内";
	private static final String ERRORRIGHT = "内容不能为空或者内容不正确";
	
	private static List<Map<String, Object>> list;
	
	public ExcelImportUtils() {
		
	}

	/**
	 * 
	 * validateExcel:
	 * 适用:验证excel文件
	 * @param filePath
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean validateExcel(String filePath){
		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "导入失败：文件格式不是excel格式");
			list.add(map);
			return false;
		}
		return true;
	}
	
	
	public static List<Map<String, Object>>  getExcelContent(MultipartFile multipartFile, String type) throws Exception {
		list = new ArrayList<Map<String,Object>>();
		//将文件存在临时目录中
		String tempPath = System.getProperty("java.io.tmpdir")+"gsjTemp\\";
		String fileType = FileUtils.getTypePart(multipartFile.getOriginalFilename());
		String tempFileName = "导入文件"+MathsUtils.getRandom()+"."+fileType;
		File file = new File(tempPath, tempFileName);
		FileUtils.makeDirectory(file);
		multipartFile.transferTo(file);
		
		//初始化输入流
		InputStream isInputStream = null;
		
		try {
			//验证文件名是否合格
			if (!validateExcel(multipartFile.getOriginalFilename())) {
				return list;
			}
			
			//根据文件名判断文件是2003版本还是2007版本
			boolean isExcel2003 = true;
			if (isExcel2007(multipartFile.getOriginalFilename())) {
				isExcel2003 = false;
			}
			
			isInputStream = new FileInputStream(file);
			//根据excel内的内容读取客户信息
			getExcelInfo(isInputStream, file, isExcel2003, type);
			isInputStream.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (isInputStream != null) {
				isInputStream.close();
			}
		}
		return list;
	}
	
	public static void getExcelInfo(InputStream inputStream, File file, boolean isExcel2003, String type) throws IOException {
		//创建输出流
		FileOutputStream outputStream = null;
		try {
			/*根据版本选择创建workbook的方式*/
			Workbook workbook = null;
			if (isExcel2003) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}
			
			//读取表格里面的数据
			if (type.equals("1")) {
				readExcelValue(workbook, file.getName(), isExcel2003);
			} else if (type.equals("2")) {
				readThirdValue(workbook, file.getName(), isExcel2003);
			}
			
			
			//将修改的内容写入到原文件中  一定要在整个workbook操作结束之后再进行写入  关闭流
			outputStream = new FileOutputStream(file);
			outputStream.flush();
			workbook.write(outputStream);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	public static void readExcelValue(Workbook workbook, String fileName, boolean isExcel2003){
		//得到第一个shell
		Sheet sheet = workbook.getSheetAt(0);
		//得到excel的行数
		totalRows = sheet.getPhysicalNumberOfRows();
		//得到excel的列数（前提是有行数）
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		List<LifeCherish> lifeCherishs = new ArrayList<LifeCherish>();
		LifeCherish lifeCherish;
		Map<String, Object> map;
		boolean flag = true;
		try {
			//判断导入的excel是否与模板相同
			if (!validataExcelAndTemp(sheet)) {
				map = new HashMap<String, Object>();
				map.put("error", "导入失败：该文件未按模板来定义");
				list.add(map);
				return ;
			}
			//循环excel行数，从第六行开始
			for (int i = 5; i < totalRows; i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				lifeCherish = new LifeCherish();
				for (int j = 0; j < totalCells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						/** 
			             * 为了处理：Excel异常Cannot get a text value from a numeric cell 
			             * 将所有列中的内容都设置成String类型格式 
			             */  
						cell.setCellType(Cell.CELL_TYPE_STRING);
						if (j == 1) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							}else {
								lifeCherish.setProvince(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell); //删除之前的注释信息
							}
						} else if (j == 2) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							}else {
								lifeCherish.setCity(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 3) {
							if (StringUtils.isError(cell.getStringCellValue()) || getDistrictCode(cell.getStringCellValue()).equals("false")) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							} else {
								lifeCherish.setCounty(getDistrictCode(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 4) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							}else {
								lifeCherish.setRoadCode(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 5) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false; 
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							}else if (!LabelHelper.getInstance().checkCode(6000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setTechnicalLevel(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 6) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false; 
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							}else if (!LabelHelper.getInstance().checkCode(6001, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setDirection(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 7) {
							if (StringUtils.isEmpty(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (StringUtils.checkDouble(cell.getStringCellValue(), flag)) { //如果桩号为2.25这样的小数形式
								lifeCherish.setStartPegNum(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							} else if (!StringUtils.checkPegNum(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "起点桩号不符合K999+222格式");
							} else {
								lifeCherish.setStartPegNum(String.valueOf(StringUtils.formatStringtoPegNum(cell.getStringCellValue())));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 8) {
							if (StringUtils.isEmpty(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (StringUtils.checkDouble(cell.getStringCellValue(), flag)) { //如果桩号为2.25这样的小数形式
								lifeCherish.setEndPegNum(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							} else if (!StringUtils.checkPegNum(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "终点桩号不符合K999+222格式");
							} else {
								lifeCherish.setEndPegNum(String.valueOf(StringUtils.formatStringtoPegNum(cell.getStringCellValue())));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 9) {
							if (StringUtils.isError(cell.getStringCellValue())) { 
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!StringUtils.checkTime(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "排查时间格式不符合2015/06、2015/6、2015-06、2015-6格式");
							} else {
								lifeCherish.setInvestigationTime(cell.getStringCellValue());
								lifeCherish.setYear(StringUtils.replaceString(cell.getStringCellValue()).substring(0, 4));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 10) {
							if (StringUtils.isEmpty(cell.getRichStringCellValue().toString())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(6002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadType(cell.getRichStringCellValue().toString());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 11) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if ( !LabelHelper.getInstance().checkCode(6003, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInvestigationMethod(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 12) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(6004, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setAccidentArea(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 13) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(6003, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setConditionInvestigation(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 14) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (StringUtils.isEmpty(LabelHelper.getInstance().buildCodeName(6004, String.valueOf(cell.getStringCellValue())))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setHighRiskSection(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 15) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRadiusCurve(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 16) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setContinuousDescent(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 17) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setSteepSlope(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 18) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setPoorSight(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 19) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadTest(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 20) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setComplexEnvironment(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 21) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setStandard(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						}  else if (j == 22) {
							if (StringUtils.isEmpty(cell.getStringCellValue())) {
								lifeCherish.setTraffic(0L);
								removeCommentAndColor(workbook, cell);
							}else if (!StringUtils.checkDouble(cell.getStringCellValue(), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "值不能转换成数字");
							} else {
								lifeCherish.setTraffic(Long.parseLong(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 23) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setSchoolBus(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 24) {
							if (!StringUtils.checkDouble(String.valueOf(cell.getStringCellValue()), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "值不能转换成数字");
							} else {
								lifeCherish.setDesignSpeed(Double.parseDouble(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 25) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setGuardrail(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 26) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setMarkMarking(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 27) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setGuidanceFacility(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 28) {
								lifeCherish.setOtherInfo(cell.getStringCellValue());
						} else if (j == 29) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInvolvingProject(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 30) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadEnvironment(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 31) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setMarkLineDisposal(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 32) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setComprehensive(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 33) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInstallingGuardrail(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 34) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInducedDisposal(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 35) {
							lifeCherish.setOtherMeasures(cell.getStringCellValue());
						} else if (j == 36) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								lifeCherish.setInvestmentEstimation(0.0);
								removeCommentAndColor(workbook, cell);
							}else if (!StringUtils.checkDouble(String.valueOf(cell.getStringCellValue()), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInvestmentEstimation(Double.parseDouble(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 37) {
							if (!StringUtils.checkDouble(cell.getStringCellValue(), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setProjectYear(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						}
					} 
				}
				
				if (flag) {
					lifeCherish.setDelStatus("0");
					lifeCherish.setCurrentStatus("0");
					lifeCherish.setRefuseStatus("0");
					lifeCherishs.add(lifeCherish);
				}
			}
			
			map = new HashMap<String, Object>();
			map.put("result", flag);
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("filePath", fileName); //将错误文件地址带到前端供用户下载
			list.add(map);
			if (flag) {
				map = new HashMap<String, Object>();
				map.put("projectInfo", lifeCherishs);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void readThirdValue(Workbook workbook, String fileName, boolean isExcel2003){
		//得到第一个shell
		Sheet sheet = workbook.getSheetAt(0);
		//得到excel的行数
		totalRows = sheet.getPhysicalNumberOfRows();
		//得到excel的列数（前提是有行数）
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		List<LifeCherish> lifeCherishs = new ArrayList<LifeCherish>();
		LifeCherish lifeCherish;
		Map<String, Object> map;
		boolean flag = true;
		try {
			//判断导入的excel是否与模板相同
			if (!validataExcelAndTemp(sheet)) {
				map = new HashMap<String, Object>();
				map.put("error", "导入失败：该文件未按模板来定义");
				list.add(map);
				return ;
			}
			//循环excel行数，从第六行开始
			for (int i = 5; i < totalRows; i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				lifeCherish = new LifeCherish();
				for (int j = 0; j < totalCells; j++) {
					Cell cell = row.getCell(j);
					
					if (cell != null) {
						/** 
			             * 为了处理：Excel异常Cannot get a text value from a numeric cell 
			             * 将所有列中的内容都设置成String类型格式 
			             */  
						cell.setCellType(Cell.CELL_TYPE_STRING);
						if (j == 1) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							}else {
								lifeCherish.setProvince(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell); //删除之前的注释信息
							}
						} else if (j == 2) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							}else {
								lifeCherish.setCity(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 3) {
							if (StringUtils.isError(cell.getStringCellValue()) || getDistrictCode(cell.getStringCellValue()).equals("false")) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORRIGHT);
							} else {
								lifeCherish.setCounty(getDistrictCode(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 4) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							}else {
								lifeCherish.setRoadCode(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 5) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false; 
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							}else if (!LabelHelper.getInstance().checkCode(6000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setTechnicalLevel(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 6) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false; 
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							}else if (!LabelHelper.getInstance().checkCode(6001, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setDirection(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 7) {
							if (StringUtils.isEmpty(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (StringUtils.checkDouble(cell.getStringCellValue(), flag)) { //如果桩号为2.25这样的小数形式
								lifeCherish.setStartPegNum(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							} else if (!StringUtils.checkPegNum(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "起点桩号不符合K999+222格式");
							} else {
								lifeCherish.setStartPegNum(String.valueOf(StringUtils.formatStringtoPegNum(cell.getStringCellValue())));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 8) {
							if (StringUtils.isEmpty(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (StringUtils.checkDouble(cell.getStringCellValue(), flag)) { //如果桩号为2.25这样的小数形式
								lifeCherish.setEndPegNum(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							} else if (!StringUtils.checkPegNum(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "终点桩号不符合K999+222格式");
							} else {
								lifeCherish.setEndPegNum(String.valueOf(StringUtils.formatStringtoPegNum(cell.getStringCellValue())));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 9) {
							if (StringUtils.isError(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!StringUtils.checkTime(cell.getStringCellValue())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "排查时间格式不符合2015/06、2015/6、2015-06、2015-6格式");
							} else {
								lifeCherish.setInvestigationTime(cell.getStringCellValue());
								lifeCherish.setYear(StringUtils.replaceString(cell.getStringCellValue()).substring(0, 4));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 10) {
							if (StringUtils.isEmpty(cell.getRichStringCellValue().toString())) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(6007, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadType(cell.getRichStringCellValue().toString());
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 11) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setCoincidenceAccident(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 12) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setSingleSharp(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 13) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setContinuousBends(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 14) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setContinuousDescent(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 15) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setSteepSlope(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 16) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setPoorSight(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 17) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadSurvey(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 18) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setAccordEnvironment(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						}else if (j == 19) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setMeetTraffic(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 20) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1000, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setSchoolBus(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 21) {
							if (!StringUtils.checkDouble(String.valueOf(cell.getStringCellValue()), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, "值不能转换成数字");
							} else {
								lifeCherish.setDesignSpeed(Double.parseDouble(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 22) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setGuardrail(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 23) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setMarkMarking(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 24) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setGuidanceFacility(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 25) {
								lifeCherish.setOtherInfo(cell.getStringCellValue());
						} else if (j == 26) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInvolvingProject(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 27) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setRoadEnvironment(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 28) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setMarkLineDisposal(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 29) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setComprehensive(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 30) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInstallingGuardrail(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 31) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORNULL);
							} else if (!LabelHelper.getInstance().checkCode(1002, String.valueOf(cell.getStringCellValue()))) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInducedDisposal(String.valueOf(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 32) {
							lifeCherish.setOtherMeasures(cell.getStringCellValue());
						} else if (j == 33) {
							if (StringUtils.isEmpty(String.valueOf(cell.getStringCellValue()))) {
								lifeCherish.setInvestmentEstimation(0.0);
								removeCommentAndColor(workbook, cell);
							}else if (!StringUtils.checkDouble(String.valueOf(cell.getStringCellValue()), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setInvestmentEstimation(Double.parseDouble(cell.getStringCellValue()));
								removeCommentAndColor(workbook, cell);
							}
						} else if (j == 34) {
							if (!StringUtils.checkDouble(cell.getStringCellValue(), false)) {
								flag = false;
								addComment(workbook, cell, isExcel2003, ERRORCHANGE);
							} else {
								lifeCherish.setProjectYear(cell.getStringCellValue());
								removeCommentAndColor(workbook, cell);
							}
						}
					} 
				}
				
				if (flag) {
					lifeCherish.setDelStatus("0");
					lifeCherish.setCurrentStatus("0");
					lifeCherish.setRefuseStatus("0");
					lifeCherishs.add(lifeCherish);
				}
			}
			
			map = new HashMap<String, Object>();
			map.put("result", flag);
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("filePath", fileName); //将错误文件地址带到前端供用户下载
			list.add(map);
			if (flag) {
				map = new HashMap<String, Object>();
				map.put("projectInfo", lifeCherishs);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void addComment(Workbook workbook, Cell cell, boolean isExcel2003, String content) throws IOException {
		if (isExcel2003) { //如果是2003版本  则利用HSSF方式添加注解
			addHSSFComment(workbook, cell, content);
			//将修改的内容写入到原文件中
		} else { //如果是2007之后的版本 则利用XSSF方式添加注解
			addXSSFComment(workbook, cell, content);
		}
		//设置单元格背景颜色
		setCellColor(workbook, cell);
	}

	/**
	 * 
	 * addComment:
	 * 适用: HSSF方式设置单元格注释
	 * @param patriarch
	 * @param cell
	 * @param content 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void addHSSFComment(Workbook workbook, Cell cell, String content) {
		HSSFPatriarch patriarch = (HSSFPatriarch) workbook.getSheetAt(0).createDrawingPatriarch();
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)1, 2, (short)4, 4));
		comment.setString(new HSSFRichTextString(content));
		cell.setCellComment(comment);
	}
	
	/**
	 * 
	 * addXSSFComment:
	 * 适用:XSSF方式设置单元格注释
	 * @param workbook
	 * @param cell
	 * @param content 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void addXSSFComment(Workbook workbook, Cell cell, String content){
		System.out.println("XSSF:"+content);
		//得到一个poi的工具类
		CreationHelper factory = workbook.getCreationHelper();
		//创建一个工作表
		XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
		//得到一个换图的对象
		Drawing drawing = sheet.createDrawingPatriarch();
		//ClientAnchor是附属在WorkSheet上的一个对象，  其固定在一个单元格的左上角和右下角
		ClientAnchor anchor = factory.createClientAnchor();
		Comment comment = drawing.createCellComment(anchor);
		RichTextString richTextString = factory.createRichTextString(content);
		comment.setString(richTextString);
		comment.setAuthor("pt-zl");
		cell.setCellComment(comment);
	}
	
	/**
	 * 
	 * setCellColor:
	 * 适用:设置单元格的背景色
	 * @param workbook
	 * @param cell 
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void setCellColor(Workbook workbook, Cell cell) throws IOException {
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.ROSE.getIndex()); //设置前景色
		style.setFillPattern(CellStyle.SOLID_FOREGROUND); //填充模式
		cell.setCellStyle(style); 
	}
	
	/**
	 * 
	 * removeCommentAndColor:
	 * 适用:  当单元格的内容修改正确之后，移除单元格上的注释信息及背景颜色
	 * @param workbook
	 * @param cell 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void removeCommentAndColor(Workbook workbook, Cell cell){
		cell.removeCellComment();;
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex()); 
		style.setFillPattern(CellStyle.NO_FILL); //无填充颜色
		cell.setCellStyle(style); 
	}
	
	public static boolean validataExcelAndTemp(Sheet sheet){
		String[] temp = {"序号","省","市","县"};
		if (sheet.getPhysicalNumberOfRows()<2) {
			return false;
		} else if (sheet.getRow(1).getPhysicalNumberOfCells()<4) {
			return false;
		} else {
			for (int i = 0; i < 4; i++) {
				Cell cell = sheet.getRow(1).getCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (StringUtils.isEmpty(cell.getStringCellValue()) || !cell.getStringCellValue().equals(temp[i])) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static String getDistrictCode(String districtName){
		if (StringUtils.isError(districtName)) {
			return "false";
		} else {
			if (districtName.contains("宝安")) {
				return "440306";
			} else if (districtName.contains("龙岗")) {
				return "440307";
			} else if (districtName.contains("光明")) {
				return "440309";
			} else if (districtName.contains("坪山")) {
				return "440310";
			} else if (districtName.contains("龙华")) {
				return "440311";
			} else if (districtName.contains("大鹏")) {
				return "440312";
			} else if (districtName.contains("西部")) {
				return "440313";
			} else if (districtName.contains("东部")) {
				return "440314";
			} else if (districtName.contains("罗湖")) {
				return "440303";
			} else if (districtName.contains("福田")) {
				return "440304";
			} else if (districtName.contains("南山")) {
				return "440305";
			} else if (districtName.contains("盐田")) {
				return "440308";
			} else {
				return "false";
			}
		}
	}
	/**
	 * 
	 * isExcel2003:
	 * 适用: 判断文件是否是2003的excel 
	 * @param filePath 文件路径
	 * @return 返回true 表示文件是2003
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}
	
	/**
	 * 
	 * isExcel2007:
	 * 适用:判断文件是否是2007的excel
	 * @param filePath 文件路径
	 * @return 返回true表示是2007
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
	
	public int getTotalRows() {
		return totalRows;
	}

	public int getTotalCells() {
		return totalCells;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}
	
	
	/**
	 * 
	 * isertDataToExcel:
	 * 适用:将数据填充到模板文件中
	 * @param tempName  模板文件名称
	 * @param list 数据集合
	 * @param downFileName  下载文件名称
	 * @return 
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static ResponseEntity<byte[]> insertDataToExcel(String tempName, List<List<String>> list, String downFileName) throws Exception {
		if (StringUtils.isEmpty(tempName) || StringUtils.isEmpty(downFileName)) {
			return null;
		}
		//得到模板文件
 		String filePath = SpringMvcUtils.getServletContext().getRealPath("/resources/templet/statistics/"+tempName);
 		String tempPath = System.getProperty("java.io.tmpdir")+"statistics\\tempFile"+StringUtils.getUUID()+".xls"; //临时目录
 		InputStream isInputStream = new FileInputStream(filePath);
 		 //读取excel模板  
        HSSFWorkbook wb = new HSSFWorkbook(isInputStream);  
        //读取了模板内所有sheet内容  
        HSSFSheet sheet = wb.getSheetAt(0);
        for (int i = 0; i < list.size() ; i++) {
        	List<String> tableData = list.get(i);
			for (int j = 0; j < tableData.size(); j++) {
				 HSSFCell cell = sheet.getRow(i+2).getCell(j);
			     cell.setCellValue(tableData.get(j));  
			}
		}
         File tempFile = new File(tempPath);
        FileUtils.makeDirectory(tempFile);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.flush();
        wb.write(outputStream);
        outputStream.close();
        isInputStream.close();
        return FileUtils.downFile(tempPath, downFileName);
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\lei\\Desktop\\一二级安防工程导入模板.xlsx";
		File file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		System.out.println("rows:"+rows);
		System.out.println("cells:"+cells);
		System.out.println(sheet.getRow(1).getCell(0).getStringCellValue());
		System.out.println(sheet.getRow(1).getCell(1).getStringCellValue());
		System.out.println(sheet.getRow(1).getCell(2).getStringCellValue());
		System.out.println(sheet.getRow(1).getCell(3).getStringCellValue());
		
//		CellStyle style = workbook.createCellStyle();
//		style.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
//		style.setFillPattern(CellStyle.NO_FILL);
//		cell.setCellStyle(style);
//		for(int i =0; i<rows;i++){
//			for (int j = 0; j < cells; j++) {
//				Cell cell = sheet.getRow(i).getCell(j);
//				cell.setCellValue("44444");
//				cell.setCellStyle(style);
////				ExcelImportUtils.addXSSFComment(workbook, cell, "错误消息");
//			}
//		}
		
//		String outPath = "C:\\Users\\lei\\Desktop\\test.xlsx";
//		FileOutputStream outputStream = new FileOutputStream(new File(outPath));
//		outputStream.flush();
//		workbook.write(outputStream);
//		inputStream.close();
//		outputStream.close();
	}
}
