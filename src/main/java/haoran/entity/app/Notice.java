/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-12消息通知表    </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/
package haoran.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_NOTICE")
public class Notice  extends IdEntity {
	
	private static final long serialVersionUID = 1L;
	private String address ;     //收件人
	private String theme ;		//主题
	private String mainBody ;    //正文内容

	
	public Notice() {
		
	}
	@Column(name = "ADDRESS")	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "THEME")	
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	@Column(name = "MAINBODY")	
	public String getMainBody() {
		return mainBody;
	}
	public void setMainBody(String mainBody) {
		this.mainBody = mainBody;
	}		

	
}
