package haoran.entity.check;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.context.annotation.Lazy;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_CK_CHECKTABLE")
public class CheckTable extends IdEntity {

	/**
	 * serialVersionUID:TODO
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7712802937763528856L;
	private String roadType; //道路类型
	private String menuId; //菜单id
	private String checkContent; //检查内容
	private String scoringStandard; //评分标准
	private String scoringRubric; //评分细则
	private String implementation; //落实情况
	private String retrieval; //检索条件
	private CheckProject checkProject;
	
	private Double yearFull;
	private Double yearPart;
	
	private Double poitMark;
	private Double reserveMark;
	
	private Double firstStepScore;
	private Double secondStepScore;
	private Double thirdStepScore;
	private Double fourthStepScore;
	private Double fifthStepScore;
	
	private Double tableFull; //表单总分
	private Double perfection; //系统完善度
	private Double testFull; //检查得分
	//private String projId;	//工程项目Id
	private String itemProjectId; //工程项目Id，无关联其他外键
	private String secondString; //第二个String类型的备用字段
	
	public CheckTable() {
		
	}
	
	public CheckTable(Double testFull, Double tableFull, Double perfection) {
		this.testFull = testFull;
		this.tableFull = tableFull;
		this.perfection = perfection;
	}

	@Column(name = "ROADTYPE")
	public String getRoadType() {
		return roadType;
	}

	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}

	@Column(name = "MENUID")
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@Column(name = "CHECKCONTENT")
	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	@Column(name = "SCORINGSTANDARD")
	public String getScoringStandard() {
		return scoringStandard;
	}

	public void setScoringStandard(String scoringStandard) {
		this.scoringStandard = scoringStandard;
	}

	@Column(name = "SCORINGRUBRIC")
	public String getScoringRubric() {
		return scoringRubric;
	}

	public void setScoringRubric(String scoringRubric) {
		this.scoringRubric = scoringRubric;
	}

	@Column(name = "IMPLEMENTATION")
	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	@Column(name = "RETRIEVAL")
	public String getRetrieval() {
		return retrieval;
	}

	public void setRetrieval(String retrieval) {
		this.retrieval = retrieval;
	}

	@ManyToOne(cascade= {CascadeType.REFRESH,CascadeType.MERGE})
	@JoinColumn(name="PROJID")
	@Lazy(true)
	public CheckProject getCheckProject() {
		return checkProject;
	}

	public void setCheckProject(CheckProject checkProject) {
		this.checkProject = checkProject;
	}

	@Transient
	public Double getYearFull() {
		return yearFull;
	}

	public void setYearFull(Double yearFull) {
		this.yearFull = yearFull;
	}

	@Transient
	public Double getYearPart() {
		return yearPart;
	}

	public void setYearPart(Double yearPart) {
		this.yearPart = yearPart;
	}
	
	@Transient
	public Double getPoitMark() {
		return poitMark;
	}

	public void setPoitMark(Double poitMark) {
		this.poitMark = poitMark;
	}
	
	@Transient
	public Double getReserveMark() {
		return reserveMark;
	}

	public void setReserveMark(Double reserveMark) {
		this.reserveMark = reserveMark;
	}

	@Column(name = "TABLEFULL")
	public Double getTableFull() {
		return tableFull;
	}

	public void setTableFull(Double tableFull) {
		this.tableFull = tableFull;
	}

	@Column(name = "PERFECTION")
	public Double getPerfection() {
		return perfection;
	}

	public void setPerfection(Double perfection) {
		this.perfection = perfection;
	}

	@Column(name = "TESTFULL")
	public Double getTestFull() {
		return testFull;
	}

	public void setTestFull(Double testFull) {
		this.testFull = testFull;
	}

	@Transient
	public Double getFirstStepScore() {
		return firstStepScore;
	}

	public void setFirstStepScore(Double firstStepScore) {
		this.firstStepScore = firstStepScore;
	}
	@Transient
	public Double getSecondStepScore() {
		return secondStepScore;
	}

	public void setSecondStepScore(Double secondStepScore) {
		this.secondStepScore = secondStepScore;
	}
	@Transient
	public Double getThirdStepScore() {
		return thirdStepScore;
	}

	public void setThirdStepScore(Double thirdStepScore) {
		this.thirdStepScore = thirdStepScore;
	}

	@Transient
	public Double getFourthStepScore() {
		return fourthStepScore;
	}

	public void setFourthStepScore(Double fourthStepScore) {
		this.fourthStepScore = fourthStepScore;
	}

	@Transient
	public Double getFifthStepScore() {
		return fifthStepScore;
	}

	public void setFifthStepScore(Double fifthStepScore) {
		this.fifthStepScore = fifthStepScore;
	}

	@Transient
	public String getItemProjectId() {
		return itemProjectId;
	}

	public void setItemProjectId(String itemProjectId) {
		this.itemProjectId = itemProjectId;
	}
	
	@Transient
	public String getSecondString() {
		return secondString;
	}

	public void setSecondString(String secondString) {
		this.secondString = secondString;
	}

/*	@Transient
	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}*/
}
