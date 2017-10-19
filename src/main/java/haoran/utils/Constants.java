package haoran.utils;

import haoran.utils.FormatUtils;

/**
 * Constant values used throughout the application.
 * 
 * <p>
 * <a href="Constants.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author
 */
public class Constants {
	// ~ Static fields/initializers
	// =============================================

	/** double型运算时的误差范围为0.01 */
	public static final double ERRORRANGE = 0.01;// double型运算时的误差范围为0.01

	/** The name of the ResourceBundle used in this application */
	public static final String  BUNDLE_KEY = "messages";

	/** The encryption algorithm key to be used for passwords */
	public static final String ENC_ALGORITHM = "SHA";

	/** A flag to indicate if passwords should be encrypted */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";

	/** File separator from System properties */
	public static final String FILE_SEP = "\\";

	/** User home from System properties */
	public static final String USER_HOME = System.getProperty("user.home")
			+ FILE_SEP;

	public static final String TEMPPATH=System.getProperty("java.io.tmpdir");
	
	/** The name of the configuration hashmap stored in application scope. */

	public static final String CONFIG = "appConfig";

	public static final String APP_NAME = "cqps";

	// 以下是Ext json中的一些参数设置
	public static final String OPERATE_MODE = "json";

	public static final String SAVEMORE_JSON = "jsonStr";

	public static final String CODE_KEY_VALUE = "codeKeyValue";

	public static final Integer MAX_COUNT = Integer.MAX_VALUE;

	public static final String EXT_SUCCESS_JSON_STR = "{'success':true}";

	public static final String EXT_FAILURE_JSON_STR = "{'success':false}";

	public static final String EXT_LIST_JSON_STR = "{'success':true,'totalCounts':0,'results':[]}";

	public static final String EXT_DATA_JSON_STR = "{'success':true,'data':{}}";

	public static final String EXT_JSON_SUCCESS_STR = "success";
	public static final String EXT_JSON_TOTALCOUNTS_STR = "totalCounts";
	public static final String EXT_JSON_RESULTS_STR = "results";
	public static final String EXT_JSON_TOTALPAGE_STR = "totalPage";
	public static final String EXT_JSON_CURRENTPAGE_STR = "currentPage";
	public static final String EXT_JSON_DATA_STR = "data";
	public static final String USER_ADMIN = "admin";

	/**
	 * Session scope attribute that holds the locale set by the user. By setting
	 * this key to the same one that Struts uses, we get synchronization in
	 * Struts w/o having to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";

	public static final String ACTION = "1";

	public static final String UNACTION = "0";

	/** checkBox 选中 */
	public static final String CHECK_BOX_ON = "on";

	/** checkBox 未选中 */
	public static final String CHECK_BOX_UN = "un";

	public static final String MESSAGETYPE_URGENT = "1";

	public static final String MESSAGETYPE_COMMONLY = "0";

	/** 消息类型-通知 */
	public static final String MESSAGETYPE_NOTICE = "0";

	/** 消息类型-系统消息 */
	public static final String MESSAGETYPE_SYSTEM = "1";

	/** 当前记录有效 */
	public static final String RECFLAG_IN = "0";
	/** 当前记录无效 */
	public static final String RECFLAG_UN = "1";

	/** 当前路线是新建工程 */
	public static final String RECFLAG_IN_PROJECT = "-1";

	/** 当前记录未删除 */
	public static final String DELFLAG_IN = "0";

	/** 当前记录删除 */
	public static final String DELFLAG_UN = "1";

	/** 当前记录永久删除 */
	public static final Integer DELFLAG_OUT = -1;

	/** 标记可用 */
	public static final Boolean USEFLAG_ON = true;

	/** 标记不可用 */
	public static final Boolean USEFLAG_OFF = false;

	/** 标记可用 */
	public static final String ON = "1";

	/** 标记不可用 */
	public static final String OFF = "0";

	/** 每页分页大小 默认是每页10行 */
	public static final Integer PAGE_SIZE = 5;

	/** 分页时候 displaytag的table Id */
	public static final String LIST_ID_KEY = "listId";

	/** 分页时候 displaytag的table 默认id item */
	public static final String LIST_ID_DEFAULT = "item";

	/** 分页大小保存的参数名 */
	public static final String PAGE_SIZE_KEY = "pageSize";
	/** 跳转到哪页 */
	public static final String PAGE_NUMBER_KEY = "pageNumber";

	/** 总页大小 */
	public static final String RESULT_SIZE_KEY = "resultSize";

	/** Ext 当前从哪行开始 */
	public static final String PAGE_EXT_NUMBER_KEY = "start";
	/** Ext 分页 每页分页大小 */
	public static final String PAGE_EXT_SIZE_KEY = "limit";
	/** Ext 排序的字段 */
	public static final String PAGE_EXT_SORT_KEY = "sort";
	/** Ext 排序的顺序 */
	public static final String PAGE_EXT_DIR_KEY = "dir";

	/** 是否是一个新的查询 */
	public static final String NEW_SEARCH = "newSearch";

	/** 查询条件保存session 键值 */
	public static final String SEARCH_CONDITION = "searchCondition";
	/** 逻辑假、否 */
	public static final String LOGIC_FALSE = "0";
	/** 逻辑真、是 */
	public static final String LOGIC_TRUE = "1";
	/** 审核通过 */
	public static final String CHECK_TRUE = "1";
	/** 未审核 */
	public static final String CHECK_FALSE = "0";

	public static final String CONTAIN_ZERO = "1";

	public static final String CURRENT_USER = "_CurrentUser";

	public static final String CURRENT_USER_MSGCOUNT = "currentUsermsgCount";

	public static final String CURRENT_USER_MSG_SYS_COUNT = "currentUsermsgSysCount";

	public static final String CURRENT_USER_MSG_COMM_COUNT = "currentUsermsgCommCount";


	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	public static final String UPLOAD_PATH = "upload" + FILE_SEP;

	public static final String UPLOAD_ROOT_PATH = "upload" + FILE_SEP;

	public static final String UPLOAD_MSG_FILE_PATH = "通知文档" + FILE_SEP;

	public static final String TEMP_PATH = "temp" + FILE_SEP;

	public static final String CANNOT_FOUND_IMAGE_PATH = "resources" + FILE_SEP
			+ "cannotfoundimage.gif";
	
	public static final String NOT_UPLOAD_IMAGE_PATH = "resources" + "/"
			+ "notupload.gif";

	public static final String CANNOT_FOUND_FILE_PATH = "resources" + FILE_SEP
			+ "cannotfound.gif";

	public static final String NO_IMAGE_PATH = "resources" + FILE_SEP
			+ "nopic.gif";

	public static final String THUMBNAIL_PATH = "thumbnail" + FILE_SEP;
	//用户在线类型
	public static final String ONLINETYPE_WEB = "0";
	public static final String ONLINETYPE_HANDHELD = "1";
	
	public static final String RESPONSE_STATUS_SESSIONTIMEOUT = "9999";

	public static final String BACK_URL="BackURL";
	public static final String ENABLE_BACK_URL="szczm_enable_back_url";
	/**
	 * 标段绑定type 养护
	 */
	public static final String MANNING_TYPE_MAIN = "3";
	/**
	 * SpringSecurity中默认的角色/授权名前缀.
	 */
	public static final String AUTHORITY_PREFIX = "ROLE_";	
	
	/*
	 * MQI中各种指标所占的权重 
	 */
	public static final Double W_PQI = 0.70; //PQI在MQI中的权重
	public static final Double W_SCI = 0.08;
	public static final Double W_BCI = 0.12;
	public static final Double W_TCI = 0.10;
	
	/*
	 * 沥青路面
	 * 高速、一级公路指标权重
	 */
	public static final Double PITCH_HIGH_W_PCI = 0.35;
	public static final Double PITCH_HIGH_W_RQI = 0.40;
	public static final Double PITCH_HIGH_W_RDI = 0.15;
	public static final Double PITCH_HIGH_W_SRI = 0.10;
	/*
	 * 沥青路面
	 * 二、三、四级公路指标权重
	 */
	public static final Double PITCH_LOW_W_PCI = 0.60;
	public static final Double PITCH_LOW_W_RQI = 0.40;
	public static final Double PITCH_LOW_W_RDI = 0.00;
	public static final Double PITCH_LOW_W_SRI = 0.00;
	
	/*
	 * 水泥混凝土路面
	 * 高速、一级公路指标权重
	 */
	public static final Double CEMENT_HIGH_W_PCI = 0.50;
	public static final Double CEMENT_HIGH_W_RQI = 0.40;
	public static final Double CEMENT_HIGH_W_RDI = 0.00;
	public static final Double CEMENT_HIGH_W_SRI = 0.10;
	
	/*
	 * 水泥混凝土路面
	 * 二、三、四级公路指标权重
	 */
	public static final Double CEMENT_LOW_W_PCI = 0.60;
	public static final Double CEMENT_LOW_W_RQI = 0.40;
	public static final Double CEMENT_LOW_W_RDI = 0.00;
	public static final Double CEMENT_LOW_W_SRI = 0.00;
	
	/**
	 * @author qinnan
	 * 道路相关的数据代码 types道路类型,districts深圳的辖区局,techGrade1城市道路技术等级,techGrade2公路技术等级
	 */
    public static final String[] types=FormatUtils.getCodesByCategory(2011);
    public static final String[] districts=FormatUtils.getCodesByCategory(2051);
    public static final String[] techGrade1=new String[]{"111","212","313","414","515"};
    public static final String[] techGrade2=new String[]{"121","222","323","424","525"};
    public static final String[] tunnelkinds=FormatUtils.getCodesByCategory(2031);
    public static final String[] bridgeUses=FormatUtils.getCodesByCategory(2020);
    public static final String[] bridgeKinds=FormatUtils.getCodesByCategory(2021);
    
    //默认图片缩略图的比例
    public static final float DEFAULT_SCALE = 0.8f;
    
    public static final String ZIP_DEFAULT_IMAGE = "zip_default.png";
    public static final String HTML_DEFAULT_IMAGE = "html_default.png";
    public static final String EXE_DEFAULT_IMAGE = "exe_default.png";
    public static final String JSP_DEFAULT_IMAGE = "jsp_default.png";
    public static final String OTHER_DEFAULT_IMAGE = "other_default.png";
 
}
