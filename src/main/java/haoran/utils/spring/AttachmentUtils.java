/**
 * <p>Copyright (c) 2015 深圳市鹏途信息技术有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 	深圳市道路检测管理系统        </p>
 * <p>创建者   :	ce 
 * 
 * <p>描   述  :   AttachmentUtils.java for com.pengtu.utils.spring    </p>
 * 
 * <p>最后修改 : $: 2015-8-13-下午04:44:20 v 1.0.0	 ce   $     </p>
 * 
*/

package haoran.utils.spring;

import java.util.List;

import haoran.entity.app.Attachment;
import haoran.service.system.SystemManageService;
import haoran.utils.StringUtils;

/**
 * 
 * AttachmentUtils
 * 
 * 2015-8-13 下午04:44:20
 * 
 * @version 1.0.0
 * 
 */
public class AttachmentUtils {
	
	/**
	 * 
	 * getAttachments:
	 * 适用: 根据 tableId tableField 获取相关的附件
	 * @param tableId
	 * @param tableField
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<Attachment> getAttachments(String tableId,String tableField){
		SystemManageService systemManageService =SpringContextHolder.getBean("systemManagerServiceImpl");
		List<Attachment> attachments =systemManageService.getAttachmentList(tableId, tableField);
		return attachments;
	}
	
	/**
	 * 
	 * checkFileType:
	 * 适用:根据标签 判断请求的文件是否为规章制度和其他类型文件
	 * @param tag tab标签
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean checkFileType(String tag) {
		if (StringUtils.isEmpty(tag)) {
			return false;
		} else if (tag.contains("_other") || tag.contains("_qtwj") || tag.contains("_gzzd")) {
			return true;
		} else {
			return false;
		}
	}

}
