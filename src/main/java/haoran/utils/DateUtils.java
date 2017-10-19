package haoran.utils;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import haoran.config.Constants;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 1.2 $ $Date: 2016/03/16 03:16:44 $
 */
public class DateUtils {
	// ~ Static fields/initializers
	// =============================================

	private static Log log = LogFactory.getLog(DateUtils.class);
	public static String defaultDatePattern = "yyyy-MM-dd";
	public static String yearPattern = "yyyy年";
	public static String monthPattern = "yyyy年MM月";
	public static String dayTimePattern = "yyyy-MM-dd HH:mm:ss";
	public static int day  = 90;
	// ~ Methods
	// ================================================================


	/**
	 * Return default datePattern (yyyy-MM-dd)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDefaultDatePattern() {
		try {
			defaultDatePattern = "yyyy-MM-dd";
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd";
		}

		return defaultDatePattern;
	}

	/**
	 * Return default datePattern (yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDefaultDateTimePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			dayTimePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY,
					locale).getString("datetime.format");
		} catch (MissingResourceException mse) {
			dayTimePattern = "yyyy-MM-dd HH:mm:ss";
		}
		return dayTimePattern;
	}


	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 核心String转化成date方法
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		if(StringUtils.isEmpty(strDate)){
			return (null);
		}
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	
	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 核心date转化成String方法
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String convertDateToString(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
			// log.error("aDate is null!");
			return "";
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		return returnValue;
	}
	
	/**
	 * 转化成系统默认的时间字符串类型 (yyyy-MM-dd)
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return convertDateToString(getDefaultDatePattern(), aDate);
	}
	
	/**
	 *  系统默认时间串(yyyy-MM-dd)转化成的时间类型
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;
		aDate = convertStringToDate(getDefaultDatePattern(), strDate);
		return aDate;
	}
	
	public static Date convertStringToDate2(String strDate)
			throws ParseException {
		Date aDate = null;
		aDate = convertStringToDate("dd-MM-yyyy HH:mm", strDate);
		return aDate;
	}
	
	
     /**
      * 获得当前的日期
      * getToday:
      * 适用:
      * @return
      * @throws ParseException 
      * @exception 
      * @see Calendar
      * @since  1.0.0
      */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = convertDateToString(getDefaultDatePattern(), today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));
		return cal;
	}
	
	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Date getTodayDate() throws ParseException {
		return getToday().getTime();
		
	}

	/**
	 * 
	 * yyyy-MM-dd HH:mm:ss";
	 * 
	 * @param strDate
	 *            the date to convert yyyy-MM-dd HH:mm:ss";
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDatetime(String strDate)
			throws ParseException {
		return convertStringToDate(getDefaultDateTimePattern(),strDate);
	}

	/**
	 * 
	 * yyyy-MM-dd HH:mm:ss;
	 * 
	 * @param dateTime
	 *             convert yyyy-MM-dd HH:mm:ss to ;
	 */
	public static String convertDatetimeToString(Date dateTime)
			throws ParseException {
			return convertDateToString(getDefaultDateTimePattern(),dateTime);
	}
	
	/**
	 * 转换成yyyy年MM月 形,字符串
	 * 
	 */
	public static String convertDateToMonthString(Date sDate) {
		return convertDateToString(monthPattern, sDate);

	}
	

	/**
	 * 转换成yyyy年 形,字符串
	 * 
	 */
	public static String convertDateToYearString(Date sDate) {
		return convertDateToString(yearPattern, sDate);

	}

	/**
	 * 转换成yyyy年 形,字符串字符串
	 */
	public static String convertDateToYearString(String s) {
		String sDate = null;
		if (s != null && s.length() >= 5) {
			sDate = s.substring(0, 5);
		}
		return sDate;

	}

	/**
	 * 转换成yyyy年MM月 形,字符串
	 */
	public static String convertDateToMonthString(String s) {
		String sDate = null;
		if (s != null && s.length() >= 8) {
			sDate = s.substring(0, 8);
		}
		return sDate;

	}
	/**
	 * 获得当前系统时间
	 * 
	 * @return Date
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());

	}
	
	/**
	 * 获得当前系统时间
	 * 
	 * @return String
	 * @throws 
	 */
	public static String getCurrentStrDate() {
		return convertDateToString(new Date(System.currentTimeMillis()));

	}

	/**
	 * 获得当前年份
	 *    631wj int
	 */
	public static int getCurrentYear() {
		return getYearofDate(getCurrentDate());
	}
	/**
	 * 获得当前月份
	 * 631wj int
	 */
	public static int getCurrentMonth() {
		return getMonthofDate(getCurrentDate());
	}
	
	/**
	 * 获得时间的年份
	 *  631wj int
	 */
	public static Integer getYearofDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	

	/**
	 * 获得日期的日
	 */
	public static int getDayofDate(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	/**
	 * 获得日期的月份
	 */
	public static int getMonthofDate(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}


	/**
	 * 获得当前月份的字符串类型 形如(yyyy年MM月)
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentMonthStrDate() {
		return convertDateToMonthString(new Date(System.currentTimeMillis()));

	}

	/**
	 * 将 yyyy、yyyyMM、yyyyMMdd 格式的字符串转换为日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date toDate(String str) {
		Date date = null;
		if (!StringUtils.isEmpty(str)) {
			try {
				if (str.length() == 12) {
					date = DateUtils.convertStringToDate("yyyyMMddHHmm",
							str.substring(0, 12));
				} else if (str.length() == 8) {
					date = DateUtils.convertStringToDate("yyyyMMdd",
							str.substring(0, 8));
				} else if (str.length() == 6) {
					date = DateUtils.convertStringToDate("yyyyMM",
							str.substring(0, 6));
				} else if (str.length() == 4) {
					date = DateUtils.convertStringToDate("yyyy",
							str.substring(0, 4));
				}
			} catch (ParseException e) {
				return date;
			}
		}
		return date;
	}

	/**
	 * 将 yyyy.、yyyy.MM、yyyy.MM.dd 格式的字符串转换为日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date toDotDate(String str) {
		Date date = null;
		if (!StringUtils.isEmpty(str)) {
			try {
				if (str.length() >= 10) {
					date = DateUtils.convertStringToDate("yyyy.MM.dd",
							str.substring(0, 10));
				} else if (str.length() == 7) {
					date = DateUtils.convertStringToDate("yyyy.MM",
							str.substring(0, 7));
				} else if (str.length() == 4) {
					date = DateUtils.convertStringToDate("yyyy",
							str.substring(0, 4));
				}
			} catch (ParseException e) {
				return date;
			}
		}
		return date;
	}

	/**
	 * 将 yyyy、yyyy-MM、yyyy-MM-dd 格式的字符串转换为日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date convertStringToStrigulaDate(String str) {
		Date date = null;
		if (!StringUtils.isEmpty(str)) {
			try {
				if (str.length() >= 10) {
					date = DateUtils.convertStringToDate("yyyy-MM-dd",
							str.substring(0, 10));
				} else if (str.length() == 7) {
					date = DateUtils.convertStringToDate("yyyy-MM",
							str.substring(0, 7));
				} else if (str.length() == 4) {
					date = DateUtils.convertStringToDate("yyyy",
							str.substring(0, 4));
				}
			} catch (ParseException e) {
				return date;
			}
		}
		return date;
	}

	/**
	 * 将 yyyy年、yyyy年MM月、yyyy年MM月dd日 格式的字符串转换为日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date convertStringToMoreDate(String str) {
		Date date = null;
		if (!StringUtils.isEmpty(str)) {
			try {
				if (str.length() > 11) {
					date = DateUtils.convertStringToDatetime(str);
				}
				if (str.length() == 11) {
					date = DateUtils.convertStringToDate("yyyy年MM月dd日", str);
				} else if (str.length() == 8) {
					date = DateUtils.convertStringToDate(monthPattern, str);
				} else if (str.length() == 5) {
					date = DateUtils.convertStringToDate(yearPattern, str);
				}
			} catch (ParseException e) {
				return date;
			}
		}
		return date;
	}


	/**
	 * date:2010.03.01 author:jw 日期加天数返回日期
	 */
	public static Date addDate(Date d, long day) throws ParseException {
		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return new Date(time);

	}

	/**
	 * 获得月份加减后的字符串
	 * 
	 * @param date
	 * @param num
	 *            (包括+,-)
	 * @param pattern
	 * @return
	 */
	public static String getMonthSubString(Date date, int num) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, num);
		SimpleDateFormat df = new SimpleDateFormat(monthPattern);
		return df.format(cal.getTime());
	}

	/**
	 * 获得日期加减
	 * 
	 * @param date
	 * @param num
	 *            (包括+,-)
	 * @param pattern
	 * @return
	 */
	public static Date getDayUpOrDown(Date date, int num) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);
		return cal.getTime();
	}


	/**
	 * 获得同年的两个月份之间的月份数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthSubNum(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (c2.get(1) - c1.get(1)) * 12 + (c2.get(2) - c1.get(2));
	}
	/**
	 * 获得获得时间的天数差值
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	 public static int betweenDays(Calendar beginDate, Calendar endDate) {
		      return (int)((beginDate.getTimeInMillis() - endDate.getTimeInMillis())/(1000*60*60*24));
		    
	 }
	/**
	 * 获得获得时间的天数差值
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	 public static int betweenDays(Date beginDate, Date endDate) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(beginDate);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(endDate);  
			return betweenDays(c1,c2);
	 }
	 public static String getYear(Date date) {
		 if(null==date){
			  return null;
		  }
		  Calendar calendar = GregorianCalendar.getInstance(); 
		  calendar.setTimeInMillis(date.getTime()); 
		  int m = calendar.get(Calendar.YEAR);
		  return String.valueOf(m)+"年度";
	    }
	  public static String getMonth(Date date) {
		  if(null==date){
			  return null;
		  }
		  Calendar calendar = GregorianCalendar.getInstance(); 
		  calendar.setTimeInMillis(date.getTime()); 
		  int m = calendar.get(Calendar.MONTH);
		  String[] str = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
		  return str[m];
	    }
	  public static String getQuarter(Date date) {
		  if(null==date){
			  return null;
		  }
		  Calendar calendar = GregorianCalendar.getInstance(); 
		  calendar.setTimeInMillis(date.getTime()); 
		  int m = calendar.get(Calendar.MONTH)+1;
		  int q = m/4;
		  String[] str = {"第一季度","第二季度","第三季度","第四季度"};
		  return str[q];
	  }
	  
	  /** 
	     * 将Date类转换为XMLGregorianCalendar 
	     * @param date 
	     * @return  
	     */  
	    public static XMLGregorianCalendar dateToXmlDate(Date date){  
	            Calendar cal = Calendar.getInstance();  
	            cal.setTime(date);  
	            DatatypeFactory dtf = null;  
	            XMLGregorianCalendar dateType = null;
	             try {  
	                dtf = DatatypeFactory.newInstance(); 
	                dateType = dtf.newXMLGregorianCalendar();  
		            dateType.setYear(cal.get(Calendar.YEAR));  
		            //由于Calendar.MONTH取值范围为0~11,需要加1  
		            dateType.setMonth(cal.get(Calendar.MONTH)+1);  
		            dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));  
		            dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));  
		            dateType.setMinute(cal.get(Calendar.MINUTE));  
		            dateType.setSecond(cal.get(Calendar.SECOND));  
	            } catch (DatatypeConfigurationException e) {  
	            }  
	            return dateType;  
	        }   
	    /** 
	     * 将XMLGregorianCalendar转换为Date 
	     * @param cal 
	     * @return  
	     */  
	    public static Date xmlDate2Date(XMLGregorianCalendar cal){  
	        return cal.toGregorianCalendar().getTime();  
	    }  

	/**
	 * 之排斥周末不排斥节假日
	 * @param startDate 开始时间
	 * @param workDay   工作日
	 * @return 结束时间
	 */
	public static Date getWorkDay(Date startDate, int workDay) {
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(startDate);
	    for (int i = 1; i <= workDay; i++) {
	    	c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 1);
	        if (c1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || c1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
	        	workDay = workDay + 1;
	            c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 1);
	            continue;
	        }
	    }
	    return c1.getTime();
	}
	
	/**
	 * 验证非空值
	 * @param startDate 需验证值
	 * @param planDate 默认值
	 * @return
	 */
	public static Date checkedNull(Date startDate,Date planDate){
		if(startDate==null){
			return planDate;
		}
		return startDate;
	}
	
	/**
	 * 验证是否还没有修改密码或者距离上次修改密码的时间超过了90天
	 * @param date
	 * @return
	 */
	public static  Boolean  checkChangePwdTime(Date date){
		if(date==null||betweenDays(new Date(), date)<=day){
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 添加一天
	 * @param date
	 * @return
	 */
	public static String addOneDay(String date){
		if(StringUtils.isNotBlank(date)){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long time = 24 * 60 * 60 * 1000;
				Date d = sdf.parse(date);
				date = sdf.format(new Date(d.getTime()+time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			}
		return date;
	}
	
	/**
	 * 
	 * isDate:
	 * 适用:判断变量是否符合年份格式
	 * @param date
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isDate(String date){
		String rexp = "^\\d{4}$";
		Pattern pattern = Pattern.compile(rexp);
		Matcher matcher = pattern.matcher(date);
		return matcher.matches();
	}
	
	/**
	 * 
	 * getHalfYear:
	 * 适用:得到两个时间，每隔半年的时间数据
	 * 例如 2011 2017  返回2011-01-01 2011-07-01 2012-01-01...
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @throws ParseException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String[] getHalfYear(String endTime, String satartTime) throws ParseException{
		int timeCount = (Integer.parseInt(endTime)+1-Integer.parseInt(satartTime))*2;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String[] timeData = new String[timeCount+1];
		calendar.setTime(DateUtils.convertStringToDate(satartTime+"-01-01"));
		timeData[0] = format.format(calendar.getTime());
		for (int i = 1; i <= timeCount; i++) {
			calendar.add(Calendar.MONTH, 6);
			timeData[i] = format.format(calendar.getTime());
		}
		return timeData;
	}
	
	/**
	 * 
	 * getDataStart:
	 * 适用:获取当前时间的起点时间
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getDataStart() throws Exception {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    Date start = calendar.getTime();
		return start;
	}
	
	/**
	 * 
	 * getDataEnd:
	 * 适用:获取当前时间的结束时间
	 * @return
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getDataEnd() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		
		Date end = calendar.getTime();
		return end;
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(isDate("2012-01-01"));
		getDataStart();
	}
	
}
