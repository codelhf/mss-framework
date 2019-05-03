package com.mss.framework.base.user.client.other.profile;

import com.mss.framework.base.user.client.other.definition.QQAttributesDefinition;
import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.OAuth20Profile;


/**
 * qq用户信息
 * @author zrk  
 * @date 2016年4月15日 下午5:43:50
 */
public class QQProfile extends OAuth20Profile implements ClientProfile{

	private static final long serialVersionUID = -7486869356444327781L;
	
	@Override
	protected AttributesDefinition getAttributesDefinition() {
		return new QQAttributesDefinition();
	}
	
	public String getOpenid() {
		return (String)getAttribute(QQAttributesDefinition.OPEN_ID);
	}
	
	public String getNickname() {
		return (String)getAttribute(QQAttributesDefinition.NICK_NAME);
	}
	public Integer getSex() {
		String sex = (String)getAttribute(QQAttributesDefinition.GENDER);
		if("男".equals(sex)) return 1;
		if("女".equals(sex)) return 0;
		return null;
	}
	public String getIcon(){
		String figureurl = (String)getAttribute(QQAttributesDefinition.FIGUREURL_QQ_2);	//100×100像素的QQ头像URL
		return figureurl == null || "".equals(figureurl)?(String)getAttribute(QQAttributesDefinition.FIGUREURL_QQ_1):figureurl;
	}
//	public String getProvince() {
//		return (String)getAttribute(QQAttributesDefinition.PROVINCE);
//	}
//	public String getCity() {
//		return (String)getAttribute(QQAttributesDefinition.CITY);
//	}
//	public String getFigureurl() {
//		return (String)getAttribute(QQAttributesDefinition.FIGUREURL);
//	}
//	public String getFigureurl_1() {
//		return (String)getAttribute(QQAttributesDefinition.FIGUREURL_1);
//	}
//	public String getFigureurl_2() {
//		return (String)getAttribute(QQAttributesDefinition.FIGUREURL_2);
//	}
//	public String getFigureurl_qq_1() {
//		return (String)getAttribute(QQAttributesDefinition.FIGUREURL_QQ_1);
//	}
//	public String getFigureurl_qq_2() {
//		return (String)getAttribute(QQAttributesDefinition.FIGUREURL_QQ_2);
//	}



}
