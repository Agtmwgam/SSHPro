/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-06banner基础设置信息表   </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/
package haoran.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_BANNERBASE")
public class Bannerbase  extends IdEntity {
	
	private static final long serialVersionUID = 1L;
	private Double timeout; //时间间隔
	private Double moveTime; //动画时间
	private String autoSlider; //是否自动轮播
	private String moveStyle; //动画效果
	private String addTag; //是否创建圆点标记
	private Double tagSize; //圆点的尺寸
	private String tagDefaultBg; //圆点标记的默认背景颜色
	private String tagActiveBg; //圆点标记选中背景颜色
	private String btnShow; //是否显示左右按钮
	private Double btnW; //按钮的宽度
	private Double btnH; //按钮的高度
	private String btnBg; //按钮的背景颜色
	
	
	public Bannerbase() {
		
	}
	@Column(name = "TIMEOUT")
	public Double getTimeout() {
		return timeout;
	}
	public void setTimeout(Double timeout) {
		this.timeout = timeout;
	}
	
	@Column(name = "MOVETIME")
	public Double getMoveTime() {
		return moveTime;
	}
	public void setMoveTime(Double moveTime) {
		this.moveTime = moveTime;
	}
	
	@Column(name = "AUTOSLIDER")
	public String getAutoSlider() {
		return autoSlider;
	}
	public void setAutoSlider(String autoSlider) {
		this.autoSlider = autoSlider;
	}
	
	@Column(name = "MOVESTYLE")
	public String getMoveStyle() {
		return moveStyle;
	}
	public void setMoveStyle(String moveStyle) {
		this.moveStyle = moveStyle;
	}
	
	@Column(name = "ADDTAG")
	public String getAddTag() {
		return addTag;
	}
	public void setAddTag(String addTag) {
		this.addTag = addTag;
	}
	
	@Column(name = "TAGSIZE")
	public Double getTagSize() {
		return tagSize;
	}
	public void setTagSize(Double tagSize) {
		this.tagSize = tagSize;
	}
	
	@Column(name = "TAGDEFAULTBG")
	public String getTagDefaultBg() {
		return tagDefaultBg;
	}
	public void setTagDefaultBg(String tagDefaultBg) {
		this.tagDefaultBg = tagDefaultBg;
	}
	
	@Column(name = "TAGACTIVEBG")
	public String getTagActiveBg() {
		return tagActiveBg;
	}
	public void setTagActiveBg(String tagActiveBg) {
		this.tagActiveBg = tagActiveBg;
	}
	
	@Column(name = "BTNSHOW")
	public String getBtnShow() {
		return btnShow;
	}
	public void setBtnShow(String btnShow) {
		this.btnShow = btnShow;
	}
	
	@Column(name = "BTNW")
	public Double getBtnW() {
		return btnW;
	}
	public void setBtnW(Double btnW) {
		this.btnW = btnW;
	}
	
	@Column(name = "BTNH")
	public Double getBtnH() {
		return btnH;
	}
	public void setBtnH(Double btnH) {
		this.btnH = btnH;
	}
	
	@Column(name = "BTNBG")
	public String getBtnBg() {
		return btnBg;
	}
	public void setBtnBg(String btnBg) {
		this.btnBg = btnBg;
	}
	
}
