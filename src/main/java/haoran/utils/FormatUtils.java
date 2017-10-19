package haoran.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.text.DecimalFormat;
import haoran.entity.base.LabelValue;

public class FormatUtils {
	
	
/**
 * @author qinnan
 * @param category
 * @param value
 * @return 根据码表的大类和传入值,返回传入值对应的编码
 */
	public  static String fromStringToCode(String category,String value){
		if(StringUtils.isEmpty(category)||StringUtils.isEmpty(value)){
			return "";
		}
		String code="";
		try {
		int categoryInt=Integer.parseInt(category);
		List<LabelValue> list=LabelHelper.getInstance().buildCodeNames(categoryInt);
		if(list!=null&&list.size()!=0){
			for (int i = 0; i < list.size(); i++) {
				LabelValue labelValue=list.get(i);
				String label=labelValue.getLabel();
				if(label.contains(value)||value.contains(label)){
					code=labelValue.getValue();
					break;
				}
			}
		}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * @author qinnan
	 * @param category
	 * @return 通过码表大类,获取所有该大类下的编码数组集合
	 */
	public static String[] getCodesByCategory (Integer category){
		List<LabelValue> list =LabelHelper.getInstance().buildCodeNames(category);
		if(list==null||list.size()==0){
			return new String[0];
		}
		String[] array=new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i]=list.get(i).getValue();
		}
        return array;		
	}
	/**
	 * @author qinnan
	 * @param category
	 * @return 通过码表大类,获取所有该大类下的名称数组集合
	 */
	public static String[] getNameByCategory (Integer category){
		List<LabelValue> list =LabelHelper.getInstance().buildCodeNames(category);
		if(list==null||list.size()==0){
			return new String[0];
		}
		String[] array=new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i]=list.get(i).getLabel();
		}
		return array;		
	}
	
	public static double keepTwoDecimal(double number){
		DecimalFormat d=new DecimalFormat("0.00");
		String numStr=d.format(number);
		return Double.parseDouble(numStr);
	}
}
