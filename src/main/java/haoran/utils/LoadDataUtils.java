package haoran.utils;

import java.util.List;

/*import com.pengtu.gsj.dao.base.QueryFilter;
import com.pengtu.gsj.dao.base.QueryFilter.MatchType;
import com.pengtu.gsj.entity.app.Organization;
import com.pengtu.gsj.entity.check.CheckProject;
import com.pengtu.gsj.entity.dl.BridgeBasicInfo;
import com.pengtu.gsj.entity.dl.TunnelInfo;
import com.pengtu.gsj.entity.gcjs.NCRoadConstruction;
import com.pengtu.gsj.entity.gcjs.StandardManagement;
import com.pengtu.gsj.entity.gcjs.XXRoadConstructionB;
import com.pengtu.gsj.entity.gcjs.YHRoadConstructionB;
import com.pengtu.gsj.entity.jhgl.MoneyOverView;
import com.pengtu.gsj.entity.jsbz.BasicInfoMent;
import com.pengtu.gsj.entity.jsbz.InfoManagement;
import com.pengtu.gsj.entity.jsbz.MainTenanceProject;
import com.pengtu.gsj.entity.jsbz.RoadCycle;
import com.pengtu.gsj.entity.jsbz.RoadMaterial;
import com.pengtu.gsj.entity.jsbz.TrafficAdjust;
import com.pengtu.gsj.entity.lzgl.GovernSpeed;
import com.pengtu.gsj.entity.lzgl.InPorMat;
import com.pengtu.gsj.entity.lzgl.LawInformOpen;
import com.pengtu.gsj.entity.lzgl.Budget;
import com.pengtu.gsj.entity.lzgl.ReportChannels;
import com.pengtu.gsj.entity.lzgl.RoadFileMan;
import com.pengtu.gsj.entity.lzgl.RoadSupInNote;
import com.pengtu.gsj.entity.yh.BuildingEffects;
import com.pengtu.gsj.entity.yh.MainBulletin;
import com.pengtu.gsj.entity.yh.MainTenance;
import com.pengtu.gsj.entity.yh.Reconstruction;
import com.pengtu.gsj.entity.zhgl.SiteCheck;
import com.pengtu.gsj.service.check.CheckProjectService;
import com.pengtu.gsj.service.check.gcjs.CouRoadConService;
import com.pengtu.gsj.service.check.gcjs.RoadHardenConService;
import com.pengtu.gsj.service.check.gcjs.RoadMatRecService;
import com.pengtu.gsj.service.check.gcjs.RoadMatRecUtilizeService;
import com.pengtu.gsj.service.check.gcjs.RuleManagerService;
import com.pengtu.gsj.service.check.gcjs.RuralRoadConService;
import com.pengtu.gsj.service.check.lwfw.MainTenanceWorkCheckService;
import com.pengtu.gsj.service.check.lzgl.basEduca.EnforFundService;
import com.pengtu.gsj.service.dl.BridgebasicinfoService;
import com.pengtu.gsj.service.dl.TunnelinfoService;
import com.pengtu.gsj.service.jhgl.MoneyOverViewService;
import com.pengtu.gsj.service.jsbz.BasicInfoMentService;
import com.pengtu.gsj.service.jsbz.InfoManagementService;
import com.pengtu.gsj.service.jsbz.MainTenanceProjectService;
import com.pengtu.gsj.service.jsbz.TrafficAdjustService;
import com.pengtu.gsj.service.lzgl.GovernSpeedService;
import com.pengtu.gsj.service.lzgl.InPorMatService;
import com.pengtu.gsj.service.lzgl.LawInformOpenService;
import com.pengtu.gsj.service.lzgl.ReportChannelsService;
import com.pengtu.gsj.service.lzgl.RoadFileManService;
import com.pengtu.gsj.service.lzgl.RoadSupInNoteService;
import com.pengtu.gsj.service.system.OrganizationService;
import com.pengtu.gsj.service.yh.BuildingEffectsService;
import com.pengtu.gsj.service.yh.MainBulletinService;
import com.pengtu.gsj.service.yh.MainTenanceService;
import com.pengtu.gsj.service.yh.ReconstructionService;
import com.pengtu.gsj.service.zhgl.SiteCheckService;
import com.pengtu.gsj.utils.spring.SpringContextHolder;*/

public class LoadDataUtils {

	/*private static OrganizationService organizationService = SpringContextHolder.getBean(OrganizationService.class);
	private static MainBulletinService mainBulletinService = SpringContextHolder.getBean(MainBulletinService.class);
	private static MoneyOverViewService moneyOverViewService = SpringContextHolder.getBean(MoneyOverViewService.class);
	private static InPorMatService inPorMatService = SpringContextHolder.getBean(InPorMatService.class);
	private static MainTenanceWorkCheckService mainTenanceWorkCheckService = SpringContextHolder.getBean(MainTenanceWorkCheckService.class);
	private static RoadFileManService roadFileManService = SpringContextHolder.getBean(RoadFileManService.class);
	private static EnforFundService enforFundService = SpringContextHolder.getBean(EnforFundService.class);
	private static RuleManagerService ruleManagerService = SpringContextHolder.getBean(RuleManagerService.class);
	private static RuralRoadConService ruralRoadConService = SpringContextHolder.getBean(RuralRoadConService.class);
	private static RoadMatRecService roadMatRecService = SpringContextHolder.getBean(RoadMatRecService.class);
	private static RoadMatRecUtilizeService roadMatRecUtilizeService = SpringContextHolder.getBean(RoadMatRecUtilizeService.class);
	private static RoadHardenConService roadHardenConService = SpringContextHolder.getBean(RoadHardenConService.class);
	private static CouRoadConService couRoadConService = SpringContextHolder.getBean(CouRoadConService.class);
	private static RoadSupInNoteService roadSupInNoteService = SpringContextHolder.getBean(RoadSupInNoteService.class);
	private static BasicInfoMentService basicInfoMentService = SpringContextHolder.getBean(BasicInfoMentService.class);
	private static TrafficAdjustService trafficAdjustService = SpringContextHolder.getBean(TrafficAdjustService.class);
	private static InfoManagementService infoManagementService = SpringContextHolder.getBean(InfoManagementService.class);
	private static MainTenanceService mainTenanceService = SpringContextHolder.getBean(MainTenanceService.class);
	private static CheckProjectService checkProjectService = SpringContextHolder.getBean(CheckProjectService.class);
	private static MainTenanceProjectService mainTenanceProjectService = SpringContextHolder.getBean(MainTenanceProjectService.class);
	private static SiteCheckService siteCheckService = SpringContextHolder.getBean(SiteCheckService.class);
	private static ReconstructionService reconstructionService = SpringContextHolder.getBean(ReconstructionService.class);
	private static BuildingEffectsService buildingEffectsService = SpringContextHolder.getBean(BuildingEffectsService.class);
	private static LawInformOpenService lawInformOpenService = SpringContextHolder.getBean(LawInformOpenService.class);
	private static ReportChannelsService reportChannelsService = SpringContextHolder.getBean(ReportChannelsService.class);
	private static GovernSpeedService governSpeedService = SpringContextHolder.getBean(GovernSpeedService.class);
	private static BridgebasicinfoService bridgebasicinfoService = SpringContextHolder.getBean(BridgebasicinfoService.class);
	private static TunnelinfoService tunnelinfoService = SpringContextHolder.getBean(TunnelinfoService.class);
	
	*//**
	 * 
	 * getMaintenanceOrg:
	 * 适用:得到所有从小修系统对接过来的养护单位   type：1 顶级单位  2 系统单位  3 对接的养护单位
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<Organization> getMaintenanceOrg(){
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.addFilter("type", "3", MatchType.EQ);
		return organizationService.findOrganizations(queryFilter);
	}
	
	*//**
	 * 
	 * getMainBulletinsGroupByManageOrg:
	 * 适用:获取小修监管中所有的养护检查记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<MainBulletin> getMainBulletinsGroupByManageOrg(){
		return mainBulletinService.getMainBulletinsGroupByManageOrg();
	}
	
	*//**
	 * getEnforFundYear:
	 * 适用:路政管理-执法经费页面中获取年份
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<Budget> getEnforFundYear(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return enforFundService.getEnforFundYear(queryFilter);
	}
	
	*//**
	 * geRoadSupInNoteSignOrg:
	 * 适用:路政管理-公路监督巡查页面中获取路政基层执法站所
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<RoadSupInNote> geRoadSupInNoteSignOrg(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return roadSupInNoteService.geRoadSupInNoteSignOrg(queryFilter);
	}
	
	*//**
	 * geRoadSupInNoteSignOrg:
	 * 适用:路政管理-路政档案管理页面中获取单位
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<RoadFileMan> getOrgName(String projectId) {
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return roadFileManService.getOrgName(queryFilter);
	}
	
	*//**
	 * getEnforFundYear:
	 * 适用:路政管理-涉路工程监督页面中获取监督记录
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<InPorMat> getInPorMat(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return inPorMatService.getInPorMat(queryFilter);
	}
	
	*//**
	 * getMainTenanceWorkCheckProject:
	 * 适用:路网服务-养护作业页面中获取工程项目
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<MainTenance> getMainTenanceWorkCheckProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("projectYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("projectYear", checkProject.getEndTime(), MatchType.LE);
		}
		return mainTenanceWorkCheckService.getMainTenanceWorkCheckProject(queryFilter);
	}
	
	*//**
	 * getRuleManagerProject:
	 * 适用:工程建设-规范管理页面中获取工程项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<StandardManagement> getRuleManagerProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime(), MatchType.LE);
		}
		return ruleManagerService.getRuleManagerProject(queryFilter);
	}
	
	*//**
	 * getgetConOpearAreaProject:
	 * 适用:工程建设-施工作业区页面中获取工程项目,调用规范管理的数据
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<StandardManagement> getConOpearAreaProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime(), MatchType.LE);
		}
		return ruleManagerService.getRuleManagerProject(queryFilter);
	}
	
	*//**
	 * getgetConOpearAreaProject:
	 * 适用:工程建设-交竣工验收页面中获取工程项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<StandardManagement> getComAcceptProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime(), MatchType.LE);
		}
		return ruleManagerService.getRuleManagerProject(queryFilter);
	}
	
	*//**
	 * getRuralRoadConProject:
	 * 适用:工程建设-农村公路建设页面中获取工程项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<NCRoadConstruction> getRuralRoadConProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return ruralRoadConService.getRuralRoadConProject(queryFilter);
	}
	
	*//**
	 * getRoadMatRecYear:
	 * 适用:工程建设-路面材料回收页面中获取年份
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<RoadMaterial> getRoadMatRecYear(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		String startTime = checkProject.getStartTime();
		String endTime = checkProject.getEndTime();
		return roadMatRecService.getRoadMatRecYear(startTime, endTime);
	}
	
	*//**
	 * getRoadMatRecUtilizeProject:
	 * 适用:工程建设-路面材料循环利用页面中获取工程项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<RoadCycle> getRoadMatRecUtilizeProject(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime(), MatchType.LE);
		}
		return roadMatRecUtilizeService.getRoadMatRecUtilizeProject(queryFilter);
	}
	
	*//**
	 * getRoadMatRecUtilizeYear:
	 * 适用:工程建设-路面材料循环利用页面中获取年份
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<RoadCycle> getRoadMatRecUtilizeYear(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		String startTime = checkProject.getStartTime();
		String endTime = checkProject.getEndTime();
		return roadMatRecUtilizeService.getRoadMatRecUtilizeYear(startTime, endTime);
	}
	
	*//**
	 * getCouRoadConPro:
	 * 适用:工程建设-县乡公路建设页面中获取项目列表
	 * @return 
	 * @exception 
	 * @since  1.0.0
	*//*
	public static List<XXRoadConstructionB> getCouRoadConPro(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return couRoadConService.getCouRoadConPro(queryFilter);
	}

	*//**
	 * getRoadHardenConBpros:
	 * 适用:工程建设-路面硬化建设的项目名称
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<YHRoadConstructionB> getRoadHardenConBpros(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return roadHardenConService.getRoadHardenConBpros(queryFilter);
	}
	
	*//**
	 * 
	 * getMoneyOverViewGroupByProjectName:
	 * 适用:获取养护资金中所有的资金项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<MoneyOverView> getMoneyOverViewGroupByProjectName(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return moneyOverViewService.getMoneyOverViewGroupByProjectName(queryFilter);
	}
	
	*//**
	 * 
	 * getBasicInfoMentGroupByFileName:
	 * 适用:获取技术保障中信息化管理模块的文件管理记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<BasicInfoMent> getBasicInfoMentGroupByFileName(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("reportTime", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("reportTime", checkProject.getEndTime(), MatchType.LE);
		}
		return basicInfoMentService.getBasicInfoMentGroupByFileName(queryFilter);
	}
	
	*//**
	 * 
	 * getTrafficAdjustByStationName:
	 * 适用:获取技术保障中信息化管理模块的交调管理记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<TrafficAdjust> getTrafficAdjustByStationName(){
		return trafficAdjustService.getTrafficAdjustByStationName();
	}
	
	*//**
	 * 获取技术保障中信息换管理模块下的信息系统管理的所有系统类型
	 * getDigitalGroupBySystemName:
	 * 适用:
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 * 5表示系统类型为综合类型的系统
	 *//*
	public static List<InfoManagement> getDigitalGroupBySystemName(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.addFilter("systemType", "5", MatchType.EQ);
		if (checkProject != null) {
			queryFilter.addFilter("systemDate", checkProject.getStartTime()+"-01", MatchType.GE);
			queryFilter.addFilter("systemDate", checkProject.getEndTime()+"-12", MatchType.LE);
		}
		return infoManagementService.getDigitalGroupBySystemName(queryFilter);
	}
	
	*//**
	 * 
	 * getMainTenanceProjectGroupByProjectName:
	 * 适用:获取技术保障中科学决策模块下的所有养护项目
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<MainTenanceProject> getMainTenanceProjectGroupByProjectName(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("year", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("year", checkProject.getEndTime(), MatchType.LE);
		}
		return mainTenanceProjectService.getMainTenanceProjectGroupByProjectName(queryFilter);
	}
	
	*//**
	 * 得到系统中所有的大中修项目
	 * getMainTenances:
	 * 适用:
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<MainTenance> getMainTenances(String projectId){
		System.out.println("projectId:"+projectId);
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("projectYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("projectYear", checkProject.getEndTime(), MatchType.LE);
		}
		return mainTenanceService.findMainTenances(queryFilter);
	}
	
	public static CheckProject getCheckProject(String projectId){
		if (StringUtils.isEmpty(projectId)) {
			return null;
		} else {
			return checkProjectService.getCheckProject(projectId);
		}
	}
	
	*//**
	 * 获取综合评价路容路貌模块现场检查的所有的线路信息
	 * getSiteCheckGroupByRoadName:
	 * 适用:
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<SiteCheck> getSiteCheckGroupByRoadName(String projectId){
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime()+"-01", MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime()+"-12", MatchType.LE);
		}
		return siteCheckService.getSiteCheckGroupByRoadName(queryFilter);
	}
	
	*//**
	 * getReconstruction:
	 * 适用:获取国省检时间段内项目的安保危桥灾害防治所有项目
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<Reconstruction> getReconstruction(String projectId) {
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("projectYear", checkProject.getStartTime(), MatchType.GE);
			queryFilter.addFilter("projectYear", checkProject.getEndTime(), MatchType.LE);
		}
		return reconstructionService.getReconstruction(queryFilter);
	}
	
	*//**
	 * getAllBuildingEffects:
	 * 适用:获取所有的示范道路信息
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<BuildingEffects> getAllBuildingEffects(){
		return buildingEffectsService.getAllBuildingEffects();
	}
	
	*//**
	 * getAllBridgeMaintaining:
	 * 适用:获取桥梁的所有记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<BridgeBasicInfo>getAllBridgeBasicInfo(){
		return bridgebasicinfoService.getAllBridgeBasicInfo();
	}
	
	*//**
	 * getAllBridgeMaintaining:
	 * 适用:获取隧道的所有记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<TunnelInfo>getAllTunnelInfo(){
		return tunnelinfoService.getAllTunnelInfo();
	}
	
	*//**
	 * getLawInformOpen:
	 * 适用 获取所有基层执法机构
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<LawInformOpen> getLawInformOpen(String projectrId){
		CheckProject checkProject = getCheckProject(projectrId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime()+"-01-01", MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime()+"-12-31", MatchType.LE);
		}
		queryFilter.addFilter("organizationType", "2", MatchType.EQ);
		return lawInformOpenService.getLawInformOpen(queryFilter);
	}
	
	*//**
	 * getReportChannels:
	 * 适用:获取全部的执法记录
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<ReportChannels> getReportChannels(){
		return reportChannelsService.getReportChannels();
	}
	
	*//**
	 * getReconstruction:
	 * 适用:获取国省检时间段内项目的治超管理记录
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<GovernSpeed> getGovernSpeed(String projectId) {
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getEndTime()+"-07-01", MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime()+"-08-31", MatchType.LE);
		}
		return governSpeedService.getGovernSpeeds(queryFilter);
	}
	
	
	*//**
	 * getReconstruction:
	 * 适用:获取所有的治超管理记录
	 * @param projectId
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 *//*
	public static List<GovernSpeed> getGovernSpeeds(String projectId) {
		CheckProject checkProject = getCheckProject(projectId);
		QueryFilter queryFilter = new QueryFilter();
		if (checkProject != null) {
			queryFilter.addFilter("testYear", checkProject.getStartTime()+"-01-01", MatchType.GE);
			queryFilter.addFilter("testYear", checkProject.getEndTime()+"-12-31", MatchType.LE);
		}
		return governSpeedService.getGovernSpeeds(queryFilter);
	}*/
}
