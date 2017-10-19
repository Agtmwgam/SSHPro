/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-09附件表    </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/
package haoran.entity.app;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_ATTACHMENT")
public class Attachment  extends IdEntity implements java.io.Serializable,Cloneable {
	
	private static final long serialVersionUID = 1L;
	private File upfile; // 文件
	private String upfileContentType ;      //上传文件格式
	private String upfileFileName ;        //文件原始名
	private String savePath ;        //文件存放路径
	private String fullPath ;        //文件的完整路径
	private String path;        //文件最后的路径
	private String type;	//文件类型
	private String size;	//文件大小
	private String uploadPerson;	//上传用户
	private String partId;	//对应工程/记录id
	private String tableId;	//对应表id
	private String tableName;	//对应表名称
	private String tableField;	//对应表中的属性
	private String fileName;	//文件名称
	private String fileNo;	//文件编号
	private String fileOrg;	//发文单位
	private Date fileTime;	//发文日期
	private String ownOrg;	//所属单位
	private String tag;	//标签
	private String parentTag;	// 父级标签
	private String reelNumber;	//卷号
	private String number;	//第x本
	private String fileDesc;	//文件描述
	private String imgPath;	//文件缩略图
	private String keyWords;	//关键字
	private String remark;  //备注
	private String fileMD5; //文件对应的md5码
	private String pdfPath; //文件转pdf文件后的地址
	private String scaleImagePath; //文件对应的缩略图地址
	private String swfPath;
	private String orgId;
	private String year; //上传文件年份
	private String parentTagIds; //标签的父id 便于后期统计
	

	public Attachment() {
		
	}
	
	public Attachment(String year) {
		this.year = year;
	}
	
	public Attachment(String tableId, String remark) {
		this.tableId = tableId;
		this.remark = remark;
	}
	@Column(name = "UPFILECONTENTTYPE")
	public String getUpfileContentType() {
		return upfileContentType;
	}
	public void setUpfileContentType(String upfileContentType) {
		this.upfileContentType = upfileContentType;
	}
	
	@Column(name = "UPFILEFILENAME")
	public String getUpfileFileName() {
		return upfileFileName;
	}
	public void setUpfileFileName(String upfileFileName) {
		this.upfileFileName = upfileFileName;
	}
	@Column(name = "SAVEPATH")
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	@Column(name = "FULLPATH")
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	
	@Column(name = "PATH")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "SIZE")
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	@Column(name = "UPLOADPERSON")
	public String getUploadPerson() {
		return uploadPerson;
	}
	public void setUploadPerson(String uploadPerson) {
		this.uploadPerson = uploadPerson;
	}
	
	@Column(name = "PARTID")
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	
	@Column(name = "TABLEID")
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	
	@Column(name = "TABLENAME")
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Column(name = "TABLEFIELD")
	public String getTableField() {
		return tableField;
	}
	public void setTableField(String tableField) {
		this.tableField = tableField;
	}
	
	@Column(name = "FILENAME")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "FILENO")
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	
	@Column(name = "FILEORG")
	public String getFileOrg() {
		return fileOrg;
	}
	public void setFileOrg(String fileOrg) {
		this.fileOrg = fileOrg;
	}
	

	@Temporal(TemporalType.DATE)
	@Column(name = "FILETIME", length = 10)
	public Date getFileTime() {
		return fileTime;
	}
	public void setFileTime(Date fileTime) {
		this.fileTime = fileTime;
	}
	
	@Column(name = "OWNORG")
	public String getOwnOrg() {
		return ownOrg;
	}
	
	public void setOwnOrg(String ownOrg) {
		this.ownOrg = ownOrg;
	}
	
	@Column(name = "TAG")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Column(name = "REELNUMBER")
	public String getReelNumber() {
		return reelNumber;
	}
	public void setReelNumber(String reelNumber) {
		this.reelNumber = reelNumber;
	}
	
	@Column(name = "NUMBER")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name = "FILEDESC")
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	
	@Column(name = "IMGPATH")
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	@Column(name = "KEYWORDS")
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	@Transient
	public File getUpfile() {
		return upfile;
	}
	public void setUpfile(File upfile) {
		this.upfile = upfile;
	}
	
	@Column(name="FILEMD5")
	public String getFileMD5() {
		return fileMD5;
	}
	public void setFileMD5(String fileMD5) {
		this.fileMD5 = fileMD5;
	}
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="PDFPATH")
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	
	@Column(name="SCALEIMAGEPATH")
	public String getScaleImagePath() {
		return scaleImagePath;
	}
	public void setScaleImagePath(String scaleImagePath) {
		this.scaleImagePath = scaleImagePath;
	}
	
	@Column(name="PARENTTAG")
	public String getParentTag() {
		return parentTag;
	}
	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}
	
	@Column(name = "SWFPATH")
	public String getSwfPath() {
		return swfPath;
	}
	public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}
	
	@Column(name = "ORGID")
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "YEAR")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "PARENTTAGIDS")
	public String getParentTagIds() {
		return parentTagIds;
	}

	public void setParentTagIds(String parentTagIds) {
		this.parentTagIds = parentTagIds;
	}

	@Transient
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullPath == null) ? 0 : fullPath.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Transient
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment other = (Attachment) obj;
		if (fullPath == null) {
			if (other.fullPath != null)
				return false;
		} else if (!fullPath.equals(other.fullPath))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("tableId", this.tableId).append("path", this.path)
				.append("fullPath", this.fullPath).append("type", this.type)
				.append("uploadUser", this.uploadPerson).toString();
	}
	
	@Transient
	public String getUrlPath() {
		return "/servlet/annex?id=" + this.getId();
	}
	
	@Transient
	public String getFileIcon() {
		 String type = this.getType();
		 if("bmp".equals(type)||"jpg".equals(type)||"jpeg".equals(type)||"tiff".equals(type)||"png".equals(type)||"gif".equals(type)) {
			 return "pic";
			 
		 }else if("rar".equals(type)||"zip".equals(type)) {
			 return "rar";
			 
		 }else if("xls".equals(type)||"xlsx".equals(type)) {
			 return "excel";
			 
		 } else if("doc".equals(type)||"docx".equals(type)) {
			 return "word";
			 
		 }else if("mp4".equals(type)||"flv".equals(type)||"avi".equals(type)||"mov".equals(type)||"wmv".equals(type)) {
			 return "video";
			 
		 }else if("text".equals(type)||"txt".equals(type)) {
			 return "text";
			 
		 }else if("ppt".equals(type)||"pptx".equals(type)) {
			 return "ppt";
			 
		 } else if("pdf".equals(type)) {
			 return "pdf";
			 
		 } else if("mp3".equals(type)||"wma".equals(type)) {
			 return "music";
		 } 
		 return "default";
	}
	
	@Transient
	public String getName() {
		return haoran.utils.StringUtils.leftFileName(this.getUpfileFileName(),15,this.getType());
	}
	
	@Transient
	public String getDelPath() {
		return "/attachment/delete.do?id=" + this.getId();
	}
}
