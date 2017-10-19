package haoran.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import haoran.entity.base.LabelValue;


/**
 *  要获得 T_APP_CODE 码表的统一管理  
 * 
 * LabelHelper
 * @see com.pengtu.web.taglib.CodeTag
 * 2011-9-20 下午02:48:06
 * 
 * @version 1.0.0
 *
 */
public class LabelHelper {
	private static Logger logger = LoggerFactory.getLogger(LabelHelper.class);
    private  Map<Integer,List<LabelValue>>  categoryMap ;
    private static LabelHelper labelHelper = new LabelHelper();
    
    public LabelHelper(){
    	
    }
    private LabelHelper(Map<Integer,List<LabelValue>>  categoryMap){
    	this.categoryMap = categoryMap;
    }
    public synchronized static LabelHelper buildLabelHelper(Map<Integer,List<LabelValue>>  categoryMap) {
		labelHelper = new LabelHelper(categoryMap);
		return labelHelper;
    }
    public synchronized static LabelHelper getInstance() {
		if (labelHelper == null) {
			logger.debug("没有设定categoryMap 无法获得值");
		}
		return labelHelper;
		
    	
    }
	/**
	 * 将显示该code编码对应某类别的编码名称,如果没有获得值 显示code
	 * 
	 * @param category
	 * @param code
	 * @return
	 */
	public  String buildCodeName(Integer category, String code) {
		return buildCodeName(category, code, false);  
	}
	
	/**
	 * 
	 * checkCode:
	 * 适用: 判断编码在数据字典中是否已存在
	 * @param category
	 * @param code
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean checkCode(Integer category, String code) {
		if (category == null || StringUtils.isEmpty(code)) {
			return false;
		}
		boolean flag = false;
		if (categoryMap != null) {
			List<LabelValue> labelValues = categoryMap.get(category);
			if (labelValues != null) {
				for (LabelValue labelValue : labelValues) {
					if (code.equals(labelValue.getValue())) {
						flag = true; 
						break;
					}
				}
			}
		}
		return flag;
	}
	

	/**
	 *  将显示该code编码对应某类别的编码名称,如果没有获得值 显示code
	 * @param category
	 * @param code
	 * @return
	 */
	public String buildCodeName(Integer category, String code,boolean both) {
		if (StringUtils.isEmpty(code)) {
			return "";
		}
		String codeName ="";
		if (categoryMap != null) {
			List<LabelValue> lavelValues = categoryMap.get(category);
			if (lavelValues != null)
				for (LabelValue labelValue : lavelValues) {
					if (code.equals(labelValue.getValue())) {
						if (both) {
							codeName = code + "." + labelValue.getLabel();
						} else {
							codeName= labelValue.getLabel();
						}
							break;
					}
				}
		} 
		return  StringUtils.isEmpty(codeName)?code:codeName;
	}
	/**
	 * 将显示一些列code编码对应某类别的编码名称,如果没有获得值 显示code 形如 1;3;4;5;6
	 * 
	 * @param category
	 * @param code
	 * @return
	 */
	public  String buildCodesName(Integer category, String codesStr) {
		if (StringUtils.isEmpty(codesStr)) {
			return "";
		}
		String[] codes = StringUtils.split(codesStr,";");
		String descStr = "";
		if (categoryMap != null) {
			List<LabelValue> lavelValues = categoryMap.get(category);
			if (lavelValues != null)
				for (int i = 0, length = codes.length; i < length; i++) {
					for (LabelValue labelValue : lavelValues) {
						if (codes[i].equals(labelValue.getValue())) {
							descStr = descStr + labelValue.getLabel();
							break;
						}
					}
					if (i < length - 1) {
						descStr = descStr + ";";
					}
				}
		}
		return descStr;
	}



	/**
	 * 获得类型下的值集合
	 * 
	 * @param category
	 * @param code
	 * @return
	 */
	public List<LabelValue> buildCodeNames(Integer category) {
		return buildCodeNames(category, false);
	}

	
	 /**
	  * 该 category类别的的一系列键值对 包含代码与对应的中文一体
	  * 例如 如果both 为true 则显示  C.村道
	  * 	 						G.国道
	  * @param category
	  * @return
	  */
	public List<LabelValue> buildCodeNames(Integer category,boolean both) {
		List<LabelValue> codes = new ArrayList<LabelValue>();
		if (categoryMap != null) {
			List<LabelValue> lavelValues = categoryMap.get(category);
			if (lavelValues != null) {
				if (both) {
					for (LabelValue labelValue : lavelValues) {
						codes
								.add(new LabelValue(labelValue.getValue() + "."
										+ labelValue.getLabel(), labelValue
										.getValue()));
					}
					return codes;
				} else {
					return lavelValues;
				}
			}
		}
			return codes;
	}
	public Map<Integer, List<LabelValue>> getCategoryMap() {
		return categoryMap;
	}
	
}
