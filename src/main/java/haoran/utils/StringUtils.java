package haoran.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * String Utility Class This is used to encode passwords programmatically
 * 
 * <p>
 * <a h ref="StringUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StringUtils {
	// ~ Static fields/initializers
	// =============================================

	private final static Log log = LogFactory.getLog(StringUtils.class);
	private final static Map<String, Object> colorByid = new HashMap<String, Object>();

	// ~ Methods
	// ================================================================

	/**
	 * Encode a string using algorithm specified in web.xml and return the
	 * resulting encrypted password. If exception, the plain credentials string
	 * is returned
	 * 
	 * @param password
	 *            Password or other credentials to use in authenticating this
	 *            username
	 * @param algorithm
	 *            Algorithm used to do the digest
	 * 
	 * @return encypted password based on the algorithm.
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			log.error("Exception: " + e);

			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}
	//SHA,MD5加盐加密
	 public static String passwordEncoderSalt(String password, Object salt, String algorithm){ //algorithm为MD5,SHA
		String result = null;
		try {
	      MessageDigest md = MessageDigest.getInstance(algorithm);
		  if (password == null) {
	            result = "";
	        }
		  if((salt == null) || "".equals(salt)) {
	            result = password;
	       }else {
	            result =  password + "{" + salt.toString() + "}";
	        }
	      //加密后的字符串  
	      result = byteArrayToHexString(md.digest(result.getBytes("utf-8")));
	    }catch (Exception ex) {
	    }
	    return result;
	 }
	 /**
	  * 转换字节数组为16进制字串
	  * @param b 字节数组
	  * @return 16进制字串
	  */
	 public static String byteArrayToHexString(byte[] b) {
	  StringBuffer resultSb = new StringBuffer();
	  for (int i = 0; i < b.length; i++) {
	   resultSb.append(byteToHexString(b[i]));
	  }
	  return resultSb.toString();
	 }

	 public static String byteToHexString(byte b) {
	   String[] hexDigits = { "0", "1", "2", "3", "4", "5",
	   "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	  int n = b;
	  if (n < 0)
	   n = 256 + n;
	  int d1 = n / 16;
	  int d2 = n % 16;
	  return hexDigits[d1] + hexDigits[d2];
	 }

	    
	/**
	 * 判断字符串是email
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern pattern = Pattern
				.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;

	}

	/**
	 * 判断字符串是否有中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasChinese(String str) {
		if (str == null) {
			return false;
		}
		if (str.getBytes().length != str.length()) {
			return true;
		}
		return false;

	}
	
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isError(String str) {
    	return str == null || str.length() == 0 || str.toLowerCase().equals("null") || str.equals("/");
    }
    
	/**
	 * @author zl
	 * @param value
	 * @param flag 是否和100比较大小的标志
	 * @return
	 * @描述  判断字符串是否满足double类型
	 */
	public static boolean checkDouble(String value,boolean flag){
		if (isEmpty(value)) {
			return false;
		}
		 value = replaceString(value);
		 if (flag) {
			 return value.matches("^[0-9]+([.]{1}[0-9]+){0,1}$")&&Double.parseDouble(value)<=100;
		}else {
			 return value.matches("^[0-9]+([.]{1}[0-9]+){0,1}$");
		}
	}
	
	/**
	 * 
	 * checkTime:
	 * 适用:检查时间格式是否为2015/06格式
	 * @param value
	 * @return  
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean checkTime(String value) {
		if (isEmpty(value)) {
			return false;
		} else {
			 value = replaceString(value);
			 String reg = "^[1-9][0-9]{3}[/|-][0-9]{1,2}$";
			 return value.matches(reg);
		}
		 
	}

	/**
	 * 字符串分割 默认按照";" 分割
	 * 
	 * @param selItem
	 * @return
	 */
	public static String[] getItemsForSplit(String selItem) {
		return getItemsForSplit(selItem, ";");
	}


	/**
	 * 字符串分割
	 * getItemsForSplit:
	 * 适用:
	 * @param selItem
	 * @param split 分隔符号
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String[] getItemsForSplit(String selItem, String split) {
		if (selItem == null) {
			return new String[0];
		}
		return selItem.split(split);
	}

	/**
	 * 格式化数字，若为 null 返回 ""，否则返回其数值
	 * 
	 * @author 谢庚才
	 * @param number
	 * @return
	 */
	public static String formatNumber(Number number) {
		if (number != null) {
			return number + "";
		} else {
			return "";
		}
	}

	/**
	 * @author zl
	 * @param value
	 * @return
	 * @描述  判断字符串是否K999+333格式
	 */
	public static boolean checkPegNum(String value){
		if (isEmpty(value)) {
			return false;
		}
		 value = replaceString(value);
		 return value.matches("^(K|k)[0-9]+\\+[0-9]{1,3}$");
	}
	
	/**
	 * @author zl
	 * @param value
	 * @return
	 * @描述 去掉字符串中的所有空格和换行符
	 */
	public static String replaceString(String value){
		//去全角空格
		value= value.replace((char) 12288, ' ');
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(value);
        value = m.replaceAll("");
		return value;
	}
	
	/**
	 * 格式化桩号 将Double类型的桩号 转化为 形如K999+333类型的桩号写法 犹如0.0转化为 K0+0 犹如333转化为 K333+0
	 * 犹如3.33 转化成3K330米
	 * 
	 * @param pegNum
	 * @return
	 */
	public static String formatPegNum(Number pegNum) {
		if (pegNum == null) {
			return "";
		}

		Double doubleValue = new Double(pegNum.doubleValue());
		doubleValue = doubleValue * 1000;

		Double kilo = (doubleValue / 1000);
		int kiloPart = kilo.intValue();
		Double meterPart = doubleValue - kiloPart * 1000;
		return "K" + kiloPart + "+" + meterPart.intValue();

	}

	/**
	 * 格式化桩号 将Double类型的桩号 转化为 形如K999+333类型的桩号写法 犹如0.0转化为 K0+0 犹如333转化为 K333+0
	 * 
	 * @param pegNum
	 * @return
	 */
	public static String formatPegNum(String pegNum) {
		if (pegNum == null) {
			return "";
		}
		return formatPegNum(Double.parseDouble(pegNum));
	}
	/**
	 * 形如K999+333类型的桩号写法 转换成 999.333
	 * 
	 * @param pegNum
	 * @return
	 */
	public static Double formatStringtoPegNum(String pegNum) {
		if (pegNum == null) {
			return 0.0;
		}
		pegNum =pegNum.replace("K", "");
		pegNum =pegNum.replace("k", "");
		pegNum =pegNum.replace("+", ".");
		if(isNumber(pegNum)){
			return Double.valueOf(pegNum);
		}
		return  0.0;
	}
	
	public static boolean isNumber(String value){
		return value.matches("^[0-9]+([.]{1}[0-9]+){0,1}$");
	}


	/** 随机获取颜色样式 */
	public static String randomColor() {
		String t = "#";
		String str = "0123456789abcdef";
		for (int j = 0; j < 6; j++) {
			t = t + str.charAt((int) (new SecureRandom().nextFloat()* str.length()));
		}
		return t;
	}

	/** 根据id获取对应的颜色 */
	public static String colorMapById(String id) {
		String color = null;
		if (colorByid.containsKey(id)) {
			color = (String) colorByid.get(id);
		} else {
			color = randomColor();
			colorByid.put(id, color);
		}
		return color;
	}

	/**
	 * 根据字符串找到 < >之间的字符串
	 * 
	 * @param str
	 * @return list
	 */
	public static List<String> findString(String str) {
		// String str
		// ="管理员<admin>;陈惠芬<chenhf>;康从宇<kangcy>;黄惠初<huanghc>;周小浪<zhouxl>";
		List<String> list = new ArrayList<String>();
		if (str == null) {
			return list;
		}
		for (int i = 0; i < str.length(); i++) {

			int start = str.indexOf("<");
			int end = str.indexOf(">");
			if (start != -1 && end != -1) {
				String findStr = str.substring(start + 1, end);
				list.add(findStr);
				// 如果找到 findStr 后 ,findStr还有字符
				if (!str.substring(start + 1).equals(
						str.substring(start + 1, end + 1))) {
					str = str.substring(end + 1);
				} else {
					break;
				}
			} else {
				break;
			}

		}

		return list;
	}
	
	/**   
     * 获得一个UUID   
     * @return String UUID   
     */    
    public static String getUUID(){    
        String s = UUID.randomUUID().toString();    
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);    
    }    
    
    /**   
     * 获得指定数目的UUID   
     * @param number int 需要获得的UUID数量   
     * @return String[] UUID数组   
     */    
    public static String[] getUUID(int number){    
        if(number < 1){    
            return null;    
        }    
        String[] ss = new String[number];    
        for(int i=0;i<number;i++){    
            ss[i] = getUUID();    
        }    
        return ss;    
    }
    
    /**
     * 将字符串按指定符号分割为数组
     * resolveString:
     * 适用:
     * @return 
     * @exception 
     * @since  1.0.0
     */
    public static String[] resolveString(String value , String  mark){
    	String [] strarray  = null;
    	 if(value != null) {
    		strarray = value.split(mark);
    	 }
    	return  strarray;
    }
    
    /**
     * 将数组拼接为字符串
     * appendString:
     * 适用:
     * @param array
     * @return 
     * @exception 
     * @since  1.0.0
     */
    public static String appendString(String [] array ){
    	StringBuffer appenStr = new StringBuffer();
    	for (int i = 0;i<array.length;i++) {
			if(i==array.length-1){
				appenStr.append(array[i]);
			}else{
				appenStr.append(array[i]+",");
			}
		}
    	return appenStr.toString();
    }
    
    /**
     * 
     * changeHTML:
     * 适用:转化html代码
     * @param value
     * @return 
     * @exception 
     * @since  1.0.0
     */
    public static String changeHTML(String value){
		value=value.replace("&","&amp;");
		value=value.replace(" ","&nbsp;");
		value=value.replace("<","&lt;");
		value=value.replace(">","&gt;");
		value=value.replace("\r\n","<br>");
		return value;
	}
	/**
	 * 判断是否为数字  是数字为真
	 * @param str
	 * @return
	 */
	  public static boolean isNumeric(String str)
      {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if( !isNum.matches() )
            {
                  return false;
            }
            return true;
      }

 
	  /**
	   * 
	   * isListin:
	   * 适用:str是否在strs中，存在着为true
	   * @param strs
	   * @param str
	   * @return 
	   * @exception 
	   * @since  1.0.0
	   */
	  public static boolean isListin(List<String> strs, String str){
		  for(String var :strs){
			  if(var.equals(str)){
				  return true;
			  }
		  }
		  return false;
	  }
		private final static String[] CN_Digits = { "零", "壹", "貳", "叁", "肆", "伍","陆", "柒", "捌", "玖", };
		/** 
		 * 将数字型货币转换为中文型货币 <br/> 
		 * 作者：钟声明　时间：2009-4-10　下午09:59:26<br/> 
		 * 博客：http://blog.csdn.net/wallimn<br/> 
		 * 参数：<br/> 
		 *  
		 * @param moneyValue 
		 *    字符串形式的金额，小数部分，将多于3位部分舍去，不做四舍五入 
		 * @return 
		 */  
		public static String CNValueOf(String moneyValue) {  
		    //使用正则表达式，去除前面的零及数字中的逗号  
		    String value = moneyValue.replaceFirst("^0+", "");  
		    value = value.replaceAll(",", "");  
		    //分割小数部分与整数部分  
		    int dot_pos = value.indexOf('.');  
		    String int_value;  
		    String fraction_value;  
		    if (dot_pos == -1) {  
		        int_value = value;  
		        fraction_value = "00";  
		    } else {  
		        int_value = value.substring(0, dot_pos);  
		        fraction_value = value.substring(dot_pos + 1, value.length())  
		                + "00".substring(0, 2);//也加两个0，便于后面统一处理  
		    }  

		    int len = int_value.length();  
		    if (len>16) return "值过大";  
		    StringBuffer cn_currency = new StringBuffer();  
		    String[] CN_Carry = new String[] { "", "万", "亿", "万" };  
		    //数字分组处理，计数组数  
		    int cnt = len/4+(len%4==0?0:1);  
		    //左边第一组的长度  
		    int partLen = len-(cnt-1)*4;  
		    String partValue=null;  
		    boolean bZero=false;//有过零  
		    String curCN=null;  
		    for(int i =0; i<cnt; i++){  
		        partValue = int_value.substring(0,partLen);  
		        int_value=int_value.substring(partLen);  
		        curCN = Part2CN(partValue,i!=0&&!"零".equals(curCN));  
		        //若上次为零，这次不为零，则加入零            
		        if(bZero && !"零".equals(curCN)){  
		            cn_currency.append("零");  
		            bZero=false;  
		        }  
		        if("零".equals(curCN))bZero=true;  
		        //若数字不是零，加入中文数字及单位  
		        if(!"零".equals(curCN)){  
		            cn_currency.append(curCN);  
		            cn_currency.append(CN_Carry[cnt-1-i]);  
		        }  
		        //除最左边一组长度不定外，其它长度都为4  
		        partLen=4;  
		        partValue=null;  
		    }  
		    cn_currency.append("元");  
		    //处理小数部分  
		    int fv1 = Integer.parseInt(fraction_value.substring(0,1));  
		    int fv2 = Integer.parseInt(fraction_value.substring(1,2));  
		    if(fv1+fv2==0){  
		        cn_currency.append("整");  
		    }  
		    else{  
		        cn_currency.append(CN_Digits[fv1]).append("角");  
		        cn_currency.append(CN_Digits[fv2]).append("分");  
		    }  
		    return cn_currency.toString();  
		} 
		/** 
		 * 将一组数字（不多于四个）转化成中文表示 <br/> 
		 * 作者：钟声明　时间：2009-4-11　下午07:41:25<br/> 
		 * 博客：http://wallimn.iteye.com<br/> 
		 * 参数：<br/> 
		 *  
		 * @param partValue 字符串形式的数字 
		 * @param bInsertZero 是否在前面添加零 
		 * @return 
		 */  
		private static String Part2CN(String partValue,boolean bInsertZero) {  
		    //使用正则表达式，去除前面的0  
		    partValue = partValue.replaceFirst("^0+", "");  
		    int len = partValue.length();  
		    if (len == 0)  
		        return "零";  
		    StringBuffer sbResult = new StringBuffer();  
		    int digit;  
		    String[] CN_Carry = new String[] { "", "拾", "佰", "仟" };  
		    for (int i = 0; i < len; i++) {  
		        digit = Integer.parseInt(partValue.substring(i, i + 1));  
		        if (digit != 0) {  
		            sbResult.append(CN_Digits[digit]);  
		            sbResult.append(CN_Carry[len - 1 - i]);  
		        } else {  
		            // 若不是最后一位，且下不位不为零，追加零  
		            if (i != len - 1  
		                    && Integer.parseInt(partValue.substring(i + 1, i + 2)) != 0)  
		                sbResult.append("零");  
		        }  
		    }  
		    if(bInsertZero && len!=4)sbResult.insert(0, "零");  
		    return sbResult.toString();  
		}  
		

	  public static void main(String[] args){
		  /**
	        String  str="G107京深线";  
	        String s = "\\d+.\\d+|\\w+";
	        Pattern  pattern=Pattern.compile(s);  
	        Matcher  ma=pattern.matcher(str);  
	        while(ma.find()){  
	        }**/
	  }
	  /**
	   * 
	   * fullPathFormat:
	   * 适用:将附件表中的图片格式化为单独的服务地址：/image/+fullpath
	   * @param fullPath
	   * @return 
	   * @exception 
	   * @since  1.0.0
	   */
	  public static String fullPathFormat(String fullPath){
		  if(org.apache.commons.lang.StringUtils.isEmpty(fullPath)){
			  return null;
		  }
		  String fp = "/image/"+fullPath.replace("\\", "/");
		  return fp;
	  }
		/**
		 * 缩略字符串（不区分中英文字符）
		 * @param str 目标字符串
		 * @param length 截取长度
		 * @return
		 */
		public static String abbr(String str, int length) {
			if (str == null) {
				return "";
			}
			try {
				StringBuilder sb = new StringBuilder();
				int currentLength = 0;
				for (char c : str.toCharArray()) {
					currentLength += String.valueOf(c).getBytes("GBK").length;
					if (currentLength <= length - 3) {
						sb.append(c);
					} else {
						sb.append("...");
						break;
					}
				}
				return sb.toString();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "";
		}
		
	    public static String leftFileName(String str, int len,String type) {
	        if (str == null) {
	            return null;
	        }
	        if (len < 0) {
	            return "";
	        }
	        if (str.length() <= len) {
	            return str;
	        }
	        return str.substring(0, len)+"..."+type;
	    }
	@SuppressWarnings("unchecked")
	public static  String parseGeoJson(String geoJson,JSONObject object){
		    JSONObject obj = (JSONObject)JSONValue.parse(geoJson);
		    JSONArray array = (JSONArray) obj.get("features");
		    JSONObject	obj1 = null;
		    if(array.size()>0){
	    	for(int i=0;i<array.size();i++){
	    		obj1=(JSONObject) array.get(i);
		    if(obj1==null||obj1.isEmpty()){
		    	return geoJson;
		    }
		    obj1.put("properties", object);
		    array.set(0, obj1);
	    	 }
		    }
			obj.put("features", array);
			return obj.toString();
	    }
	public static String ClobToString(Object clob) throws SQLException, IOException {
     if(clob instanceof Clob){
		String reString = "";
		Reader is = ((Clob)clob).getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
		sb.append(s);
		s = br.readLine();
		}
		reString = sb.toString();
		return reString;
		}
	return null;
	}
	
	public static String getKeyWords(String words){
		System.out.println("words:"+words);
		StringBuffer keywods = new StringBuffer();
		if (org.apache.commons.lang.StringUtils.isEmpty(words)) {
			return "";
		}else {
			String[] str = words.split("\\\\");
			for (int i = 1; i < str.length; i++) {
				keywods.append(str[i]);
				if (i != str.length-1) {
					keywods.append(",");
				}
			}
			return keywods.toString();
		}
	}
	
	public static String[] getLastSeason(){
		  String[] sts = new String[2];
			Calendar calendar = Calendar.getInstance();
			int month =calendar.get(Calendar.MONTH)+1;
			int year = calendar.get(Calendar.YEAR);
			String start = "";
			String end = "";
			if(month==1||month==2||month==3){
				start = (year-1)+"-10-01";
				end = (year)+"-01-01";
			}
			if(month==4||month==5||month==6){
				start = (year)+"-01-01";
				end = (year)+"-04-01";
			}
			if(month==7||month==8||month==9){
				start = (year)+"-04-01";
				end = (year)+"-07-01";
			}
			if(month==10||month==11||month==12){
				start = (year)+"-07-01";
		        end = (year)+"-10-01";
			}
			sts[0]=start;
			sts[1]=end;
			return sts;
	  }
}
