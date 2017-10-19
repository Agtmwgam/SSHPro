package haoran.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EncodingException;

import com.google.common.collect.Maps;

public class ConfigUtils {
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 获取配置
	 * @throws IOException 
	 */
	public static String getConfig(String key) throws IOException {
		Properties props = PropertiesUtils.loadProperties("config.properties");
		String value = map.get(key);
		if (value == null){
			value = props.getProperty(key);
			map.put(key, value);
		}
		return value;
	}
	
	public static String encodeParamHTML(String key){
		return ESAPI.encoder().encodeForHTML(key);
	}
	public static String encodeParamHtmlAttribute(String key){
		return ESAPI.encoder().encodeForHTMLAttribute(key);
	}
	public static String encodeParamUrl(String key) throws EncodingException{
		return ESAPI.encoder().encodeForURL(key);
	}
	public static String encodeForJavaScript(String key){
		return ESAPI.encoder().encodeForJavaScript(key);
	}
	
	public static String encodeForSQL(String param){
		return StringEscapeUtils.escapeSql(param);
	}
	
	public static Boolean endStr(String sources, String content) {
		if (StringUtils.isEmpty(sources) || StringUtils.isEmpty(content)) {
			return false;
		} else {
			return sources.endsWith(content);
		}
	}
	
	
	public static String encodeParam(String value){
		if (value != null) {
			 value = ESAPI.encoder().canonicalize(value);
//            // 避免空字符
//            value = value.replaceAll("", "");
//            // 避免脚本标签之间的任何东西
//            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 避免任何A src=“http：/ / / /第www.yihaomen.com Java类型的e­xpression……”
//            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
//            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 删除任何单独的</script>标签
//            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 删除任何单独的 <script ...> tag
//            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 避免eval(...) 表达式
//            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 避免 e­xpression(...) 表达式
//            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // 避免 javascript:...表达式
//            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // Avoid vbscript:... e­xpressions
//            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
//            value = scriptPattern.matcher(value).replaceAll("");
//            // Avoid onload= e­xpressions
//            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
	}
	

	/**
	 * 
	 * @return
	 */
	public static String getJsVersion(){
		try {
			String dm = getConfig("devModel");
			if("true".equals(dm)){
				return String.valueOf(MathsUtils.getRandom());
			}else{
				return getConfig("version");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0.0.0";
	}
}
