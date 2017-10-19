package haoran.service.system;

import java.util.List;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import haoran.dao.base.Page;
import haoran.dao.base.QueryFilter;
import haoran.entity.app.Attachment;
import haoran.entity.app.Code;
import haoran.entity.app.Organization;
import haoran.entity.check.CheckProject;

/**
 * @author zl
 * @version 创建时间：2017年4月5日 上午9:49:30 一些系统的manager 操作
 */
public interface SystemManageService {

	public Code getCode(String id);

	public String getFullPath(String partId);

	public String getSwfFullPath(String partId);
	
	public String getFullPathById(String id);
	
	public List<Code> getCodeList();
	
	public Page<Code> findCodes(final Page<Code> page, final QueryFilter queryFilter);
	
	public List<Code> findCodes(final QueryFilter queryFilter);
	
	public List<Code> getAvailableCodeList();
	
	public List<Code> getCodeListByCategory(Integer category);
	
	public Code getCodeByCategory(Integer category,String codeId);
	
	public Code getCodeNameByCategory(Integer category,String codeName);
	
	public void deleteCode(String id);
	
	public void deleteCodeAll();
	
	public Attachment getAttachment(String id);
	
	public void saveAttachment(Attachment attachment);
	
	public boolean deleteAttachment(String rootPath,String id);
	
	public boolean deleteAttachment(Attachment attachment);
	
	public List<Attachment> getAttachmentList(QueryFilter fileter);
	
	public List<Attachment> getCheckAttachments(CheckProject checkProject, String tag)throws Exception;
	
	public List<Attachment> getCheckAttachments(String startTime, String endTime, String tag) throws Exception;
	
	public List<Attachment> getCheckAttachments(String startTime, String endTime, String tag, String partId) throws Exception;
	
	public List<Attachment> getCheckAttachmentsByPartId(String startTime, String endTime, String tag, String partId) throws Exception;
	
	public List<Attachment> getAllCheckAttachments(String startTime, String endTime, String tag) throws Exception;
	
	public Attachment uploadFile(String rootPath,Attachment attachment,String tableId,String tableName,String tableField)throws Exception;
	 
	public Attachment uploadFile(String rootPath,Attachment attachment)throws Exception;
	
	public Page<Attachment> getAttachmentList(Page<Attachment> page,QueryFilter filters);
	
	public List<Attachment> getAttachmentList(String tableId, String tableField);
	
	public List<Attachment> getAttachmentListByTableId(String tableId, String partId);
	
	public List<Attachment> getAttachmentList(String tableId);

	public List<Attachment> getAllAttachment();
	
	public List<Attachment> getAttachmentBy(String propertyName, String value);
	
	public void delete(String tableId,String tableField);
	
	public int updateAttachmentTableId(String oldId,String newId);
	
	public String getExistAttachment(String tableId,String tableField);
	
	public List<Attachment> findAttachmensByTableId(String tableId);
	
	public List<Attachment> findAttachmentsByTableName(String tableName);
	
	public List<Attachment> findAttachmentsByPartId(String partId);
	
	public List<Organization> getAllJu();
	
	public void uploadFile(MultipartFile multipartFile, Attachment attachment) throws Exception;
	
	public List<Attachment> getAttachmentsByPartId(String partId, boolean all);
	
	public List<Attachment> getProjectAttachments(String partId, String tableId);
	
	public Map<String, Object> getOrgAndFile(String projectId);
	
	public List<Attachment> getCheckAttachmentByTag(String startTime, String endTime, String tag) throws Exception;
	
	public Integer getCheckYear(String endTime, String startTime, Integer temp);
	
//	public int findAttCount(String tableId);
//	
//	public List<Attachment> getAttachments();
	
	public List<String> getAttachmentYears();
	
	public List<Map<String, Object>> getCountJson(String year);
	
	public List<Attachment> getAttachmentsOfYear(String year);
	
	public ResponseEntity<byte[]> exportData(String year) throws Exception;

	public List<Attachment> getAllCheckAttachments(String tag) throws Exception;
	
	public List<Attachment> getAllCheckAttachmentsByParTag(String tag) throws Exception;
}
