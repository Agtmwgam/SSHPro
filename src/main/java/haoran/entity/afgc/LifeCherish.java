package haoran.entity.afgc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import haoran.entity.app.Attachment;
import haoran.entity.base.IdEntity;

/**
 * 
 * 
 * LifeProtection
 * 安全生命防护工程
 * 2017年6月13日 下午4:19:51
 * @author lei
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "T_AF_LIFEPROJECT")
public class LifeCherish extends IdEntity{
	
	private static final long serialVersionUID = 8624256187106038076L;

	private String province;   //省
	private String city;   //市
	private String county;   // 县
	private String roadCode;   // 公路编号
	private String technicalLevel;   // 技术等级
	private String direction;   // 方向
	private String startPegNum;   // 桩号起点
	private String endPegNum;   // 桩号终点
	private String investigationTime;  //  排查时间
	private String roadType;   // 路段分类
	private String investigationMethod;   // 交通事故排查方法
	private String accidentArea;   // 是否是事故多发点段
	private String conditionInvestigation;  // 公路条件排查方法
	private String highRiskSection;   // 是否是高公路风险路段
	private String coincidenceAccident;   // 符合事故判别指标
	private String singleSharp;   // 单个急弯
	private String continuousBends;   // 连续急弯
	private String continuousDescent;   // 连续下坡
	private String steepSlope;   // 陡坡
	private String poorSight;   // 视距不良
	private String roadSurvey;   // 符合公路路测判别指标
	private String accordEnvironment;   // 符合公路环境判别指标
	private String meetTraffic;   // 符合交通量判别指标
	private String schoolBus;   // 符合同行校车判别指标
	private Double designSpeed;   // 运行车速或设计速度
	private String radiusCurve;   // 小半径圆曲线
	private String roadTest;   // 路测险要
	private String complexEnvironment;   // 环境复杂
	private String standard;   // 交叉口不规范
	private Long traffic;   // 交通量
	private String guardrail;   // 护栏
	private String markMarking;   // 标志标线
	private String guidanceFacility;   // 警示和视线诱导设施
	private String otherInfo;   // 路段设施其他信息
	private String involvingProject;   // 涉及路线参数调整的土建工程
	private String roadEnvironment;   // 边坡、边沟或路域环境整治
	private String markLineDisposal;   // 标志线处置
	private String comprehensive;   // 交叉口综合处置
	private String installingGuardrail;   // 加装护栏
	private String inducedDisposal;   // 警示诱导设施处置
	private String otherMeasures;   // 其他处置措施
	private Double investmentEstimation;   // 处置投资估算
	private String projectYear;   // 计划实施安防工程年份
	private String remark;   // 备注
	private String status;   // 状态标识位
	private String content;// 交流内容
	private String delStatus;// 处理状态
	private String currentStatus; //工程状态
	private String refuseStatus;
	private String year;
	private String operation; //操作记录
	private long count;
	private List<Attachment> attachments;
	public LifeCherish() {
		
	}
	
	public LifeCherish(String year) {
		this.year = year;
	}
	
	public LifeCherish(String county, String technicalLevel, long count) {
		this.county = county;
		this.technicalLevel = technicalLevel;
		this.count = count;
	}
	public LifeCherish(String county, long count, String delStatus) {
		this.county = county;
		this.delStatus = delStatus;
		this.count = count;
	}
	public LifeCherish(String county, String technicalLevel, String delStatus, long count) {
		this.county = county;
		this.technicalLevel = technicalLevel;
		this.delStatus = delStatus;
		this.count = count;
	}
	
	public LifeCherish(String county, long count) {
		this.county = county;
		this.count = count;
	}
	
	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name = "COUNTY")
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	
	@Column(name = "ROADCODE")
	public String getRoadCode() {
		return roadCode;
	}
	public void setRoadCode(String roadCode) {
		this.roadCode = roadCode;
	}
	
	@Column(name = "TECHNICALLEVEL")
	public String getTechnicalLevel() {
		return technicalLevel;
	}
	public void setTechnicalLevel(String technicalLevel) {
		this.technicalLevel = technicalLevel;
	}
	
	@Column(name = "DIRECTION")
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	@Column(name = "STARTPEGNUM")
	public String getStartPegNum() {
		return startPegNum;
	}
	public void setStartPegNum(String startPegNum) {
		this.startPegNum = startPegNum;
	}
	
	@Column(name = "ENDPEGNUM")
	public String getEndPegNum() {
		return endPegNum;
	}
	public void setEndPegNum(String endPegNum) {
		this.endPegNum = endPegNum;
	}
	
	@Column(name = "INVESTIGATIONTIME")
	public String getInvestigationTime() {
		return investigationTime;
	}
	public void setInvestigationTime(String investigationTime) {
		this.investigationTime = investigationTime;
	}
	
	@Column(name = "ROADTYPE")
	public String getRoadType() {
		return roadType;
	}
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	
	@Column(name = "INVESTIGATIONMETHOD")
	public String getInvestigationMethod() {
		return investigationMethod;
	}
	public void setInvestigationMethod(String investigationMethod) {
		this.investigationMethod = investigationMethod;
	}
	
	@Column(name = "ACCIDENTAREA")
	public String getAccidentArea() {
		return accidentArea;
	}
	public void setAccidentArea(String accidentArea) {
		this.accidentArea = accidentArea;
	}
	
	@Column(name = "CONDITIONINVESTIGATION")
	public String getConditionInvestigation() {
		return conditionInvestigation;
	}
	public void setConditionInvestigation(String conditionInvestigation) {
		this.conditionInvestigation = conditionInvestigation;
	}
	
	@Column(name = "HIGHRISKSECTION")
	public String getHighRiskSection() {
		return highRiskSection;
	}
	public void setHighRiskSection(String highRiskSection) {
		this.highRiskSection = highRiskSection;
	}
	
	@Column(name = "COINCIDENCEACCIDENT")
	public String getCoincidenceAccident() {
		return coincidenceAccident;
	}
	public void setCoincidenceAccident(String coincidenceAccident) {
		this.coincidenceAccident = coincidenceAccident;
	}
	
	@Column(name = "SINGLESHARP")
	public String getSingleSharp() {
		return singleSharp;
	}
	public void setSingleSharp(String singleSharp) {
		this.singleSharp = singleSharp;
	}
	
	@Column(name = "CONTINUOUSBENDS")
	public String getContinuousBends() {
		return continuousBends;
	}
	public void setContinuousBends(String continuousBends) {
		this.continuousBends = continuousBends;
	}
	
	@Column(name = "CONTINUOUSDESCENT")
	public String getContinuousDescent() {
		return continuousDescent;
	}
	public void setContinuousDescent(String continuousDescent) {
		this.continuousDescent = continuousDescent;
	}
	
	@Column(name = "STEEPSLOPE")
	public String getSteepSlope() {
		return steepSlope;
	}
	public void setSteepSlope(String steepSlope) {
		this.steepSlope = steepSlope;
	}
	
	@Column(name = "POORSIGHT")
	public String getPoorSight() {
		return poorSight;
	}
	public void setPoorSight(String poorSight) {
		this.poorSight = poorSight;
	}
	
	@Column(name = "ROADSURVEY")
	public String getRoadSurvey() {
		return roadSurvey;
	}
	public void setRoadSurvey(String roadSurvey) {
		this.roadSurvey = roadSurvey;
	}
	
	@Column(name = "ACCORDENVIRONMENT")
	public String getAccordEnvironment() {
		return accordEnvironment;
	}
	public void setAccordEnvironment(String accordEnvironment) {
		this.accordEnvironment = accordEnvironment;
	}
	
	@Column(name = "MEETTRAFFIC")
	public String getMeetTraffic() {
		return meetTraffic;
	}
	public void setMeetTraffic(String meetTraffic) {
		this.meetTraffic = meetTraffic;
	}
	
	@Column(name = "SCHOOLBUS")
	public String getSchoolBus() {
		return schoolBus;
	}
	public void setSchoolBus(String schoolBus) {
		this.schoolBus = schoolBus;
	}
	
	@Column(name = "DESIGNSPEED")
	public Double getDesignSpeed() {
		return designSpeed;
	}
	public void setDesignSpeed(Double designSpeed) {
		this.designSpeed = designSpeed;
	}
	
	@Column(name = "RADIUSCURVE")
	public String getRadiusCurve() {
		return radiusCurve;
	}
	public void setRadiusCurve(String radiusCurve) {
		this.radiusCurve = radiusCurve;
	}
	
	@Column(name = "ROADTEST")
	public String getRoadTest() {
		return roadTest;
	}
	public void setRoadTest(String roadTest) {
		this.roadTest = roadTest;
	}
	
	@Column(name = "COMPLEXENVIRONMENT")
	public String getComplexEnvironment() {
		return complexEnvironment;
	}
	public void setComplexEnvironment(String complexEnvironment) {
		this.complexEnvironment = complexEnvironment;
	}
	
	@Column(name = "STANDARD")
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	@Column(name = "TRAFFIC")
	public Long getTraffic() {
		return traffic;
	}
	public void setTraffic(Long traffic) {
		this.traffic = traffic;
	}
	
	@Column(name = "GUARDRAIL")
	public String getGuardrail() {
		return guardrail;
	}
	public void setGuardrail(String guardrail) {
		this.guardrail = guardrail;
	}
	
	@Column(name = "MARKMARKING")
	public String getMarkMarking() {
		return markMarking;
	}
	public void setMarkMarking(String markMarking) {
		this.markMarking = markMarking;
	}
	
	@Column(name = "GUIDANCEFACILITY")
	public String getGuidanceFacility() {
		return guidanceFacility;
	}
	public void setGuidanceFacility(String guidanceFacility) {
		this.guidanceFacility = guidanceFacility;
	}
	
	@Column(name = "OTHERINFO")
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	
	@Column(name = "INVOLVINGPROJECT")
	public String getInvolvingProject() {
		return involvingProject;
	}
	public void setInvolvingProject(String involvingProject) {
		this.involvingProject = involvingProject;
	}
	
	@Column(name = "ROADENVIRONMENT")
	public String getRoadEnvironment() {
		return roadEnvironment;
	}
	public void setRoadEnvironment(String roadEnvironment) {
		this.roadEnvironment = roadEnvironment;
	}
	
	@Column(name = "MARKLINEDISPOSAL")
	public String getMarkLineDisposal() {
		return markLineDisposal;
	}
	public void setMarkLineDisposal(String markLineDisposal) {
		this.markLineDisposal = markLineDisposal;
	}
	
	@Column(name = "COMPREHENSIVE")
	public String getComprehensive() {
		return comprehensive;
	}
	public void setComprehensive(String comprehensive) {
		this.comprehensive = comprehensive;
	}
	
	@Column(name = "INSTALLINGGUARDRAIL")
	public String getInstallingGuardrail() {
		return installingGuardrail;
	}
	public void setInstallingGuardrail(String installingGuardrail) {
		this.installingGuardrail = installingGuardrail;
	}
	
	@Column(name = "INDUCEDDISPOSAL")
	public String getInducedDisposal() {
		return inducedDisposal;
	}
	public void setInducedDisposal(String inducedDisposal) {
		this.inducedDisposal = inducedDisposal;
	}
	
	@Column(name = "OTHERMEASURES")
	public String getOtherMeasures() {
		return otherMeasures;
	}
	public void setOtherMeasures(String otherMeasures) {
		this.otherMeasures = otherMeasures;
	}
	
	@Column(name = "INVESTMENTESTIMATION")
	public Double getInvestmentEstimation() {
		return investmentEstimation;
	}
	public void setInvestmentEstimation(Double investmentEstimation) {
		this.investmentEstimation = investmentEstimation;
	}
	
	@Column(name = "PROJECTYEAR")
	public String getProjectYear() {
		return projectYear;
	}
	public void setProjectYear(String projectYear) {
		this.projectYear = projectYear;
	}
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "DELSTATUS")
	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	@Column(name = "CURRENTSTATUS")
	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Column(name = "REFUSESTATUS")
	public String getRefuseStatus() {
		return refuseStatus;
	}

	public void setRefuseStatus(String refuseStatus) {
		this.refuseStatus = refuseStatus;
	}

	@Column(name = "YEAR")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "OPERATION")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Transient
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	} 

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLEID")
	@JsonIgnore
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	
}
