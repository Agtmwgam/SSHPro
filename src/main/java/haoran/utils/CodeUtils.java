package haoran.utils;

public class CodeUtils {

	public static String codefound(String type) {
		if(type.contains("普通国道")){
			return "1";
		}else if(type.contains("普通省道")){
			return "2";
		}else if(type.contains("省高速")){
			return "3";
		}else if(type.contains("县道")){
			return "4";
		}else if(type.contains("乡道")){
			return "5";
		}else if(type.contains("一级公路")){
			return "222";
		}else if(type.contains("二级公路")){
			return "323";
		}else if(type.contains("三级公路")){
			return "424";
		}else if(type.contains("四级公路")){
			return "525";
		}else if(type.contains("高速公路")){
			return "121";
		}else if(type.contains("沥青混凝土")){
			return "11";
		}else if(type.contains("水泥混凝土")){
			return "12";
		}else if(type.contains("单车道")){
			return "1";
		}else if(type.contains("双车道")){
			return "2";
		}else if(type.contains("四车道")){
			return "4";
		}else if(type.contains("六车道")){
			return "6";
		}else if(type.contains("八车道")){
			return "8";
		}else if(type.contains("其它(混合车道)")){
			return "9";
		}else if(type.contains("宝安")){
			return "440306";
		}else if(type.contains("龙岗")){
			return "440307";
		}else if(type.contains("福田")){
			return "440308";
		}else if(type.contains("罗湖")){
			return "440309";
		}else if(type.contains("南山")){
			return "440310";
		}else if(type.contains("盐田")){
			return "440311";
		}else if(type.contains("坪山")){
			return "440312";
		}else if(type.contains("光明")){
			return "440313";
		}else if(type.contains("龙华")){
			return "440314";
		}else if(type.contains("大鹏")){
			return "440315";
		}else{
		return type;
		}
	}
}
