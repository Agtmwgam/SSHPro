/**
 * <p>Copyright @ 2009 深圳市金绎科技发展有限公司</p>
 * <p>All right reserved. </p>
 * <p>项目名称				： 佛山市地方公路信息资源整合</p>
 * <p>JDK使用版本号		： jdk1.5 </P>
 * <p>描述				： </p>
 * @版本					： 1.0.0 
 * @author				： 谢庚才
 *
 * <p>修改历史 </p>
 *
 * <p>修改时间            修改人员    修改内容 </p>
 *  <p>2009-5-15			谢庚才	  新建    </p>
 */
package haoran.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 该工具类的计算方法皆忽略为 null 参数，即
 * sum(1,null,1)=2,sub(10,null,5)=2,mul(5,null,3)=15,div(10,null,5)=2
 * 
 * @author 谢庚才
 * 
 */
public final class MathsUtils {
	private MathsUtils() {
	}



	/**
	 * 以指定的舍入模式格式化双精度浮点型小数
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	public static Double formatDouble(Double d, int precision, int roundingMode) {
		if (d == null) {
			return null;
		}
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(precision, roundingMode).doubleValue();
	}

	/**
	 * 以指定的舍入模式格式化双精度 返回整型
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	public static Integer formatInteger(Double d, int precision, int roundingMode) {
		if (d == null) {
			return null;
		}
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(precision, roundingMode).intValue();
	}

	/**
	 * 以 四舍五入的舍入模式格式化双精度浮点型小数
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @return
	 */
	public static Double formatDouble(Double d, int precision) {
		return formatDouble(d, precision, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * 以 四舍五入的舍入模式格式化双精度浮点型小数 默认是四舍五入
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @return
	 */
	public static Double formatDoubleUp(Double d, int precision) {
		return formatDouble(d, precision, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 以指定的舍入模式格式化单精度浮点型小数
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	public static Float formatFloat(Float d, int precision, int roundingMode) {
		if (d == null) {
			return null;
		}
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(precision, roundingMode).floatValue();
	}

	/**
	 * 以 四舍五入的舍入模式格式化双精度浮点型小数
	 * 
	 * @param d
	 *            需格式化小数
	 * @param precision
	 *            保留小数位数
	 * @return
	 */
	public static Float formatFloat(Float d, int precision) {
		return formatFloat(d, precision, BigDecimal.ROUND_HALF_EVEN);
	}

	public static Number sum(Number... values) {
		if (values instanceof Double[]) {
			Double[] dvalues = (Double[]) values;
			Double result = new Double(0);
			for (Double value : dvalues) {
				if (value != null) {
					result += value;
				}
			}
			return result;

		} else if (values instanceof Integer[]) {
			Integer[] ivalues = (Integer[]) values;
			Integer result = new Integer(0);
			for (Integer value : ivalues) {
				if (value != null) {
					result += value;
				}
			}
			return result;
		} else if (values instanceof Float[]) {
			Float[] fvalues = (Float[]) values;
			Float result = new Float(0);
			for (Float value : fvalues) {
				if (value != null) {
					result += value;
				}
			}
			return result;
		} else if (values instanceof Long[]) {
			Long[] lvalues = (Long[]) values;
			Long result = new Long(0);
			for (Long value : lvalues) {
				if (value != null) {
					result += value;
				}
			}
			return result;
		}
		return 0;

	}

	/**
	 * 求和运算
	 * 
	 * @param ds
	 *            参数列表
	 * @return
	 */
	public static Double sum(Double... values) {

		Double result = new Double(0);
		for (Double value : values) {
			if (value != null) {
				result += value;
			}
		}
		return result;
	}

	public static Integer sum(Integer... values) {
		Integer result = new Integer(0);
		for (Integer value : values) {
			if (value != null) {
				result += value;
			}
		}
		return result;
	}

	public static Float sum(Float... values) {
		Float result = new Float(0);
		for (Float value : values) {
			if (value != null) {
				result += value;
			}
		}
		return result;
	}

	public static Long sum(Long... values) {
		Long result = new Long(0);
		for (Long value : values) {
			if (value != null) {
				result += value;
			}
		}
		return result;
	}

	/**
	 * 减法运算，取第一个参数为被减数
	 * 
	 * @param ds
	 * @return
	 */
	public static Double sub(Double... values) {
		Double result = 0d;
		if (values.length > 0) {
			if (values[0] != null) {
				result = values[0];
			}
		}
		for (int i = 1; i < values.length; i++) {
			if (values[i] != null) {
				result -= values[i];
			}
		}
		return result;
	}

	public static Float sub(Float... values) {
		Float result = 0f;
		if (values.length > 0) {
			if (values[0] != null) {
				result = values[0];
			}
		}
		for (int i = 1; i < values.length; i++) {
			if (values[i] != null) {
				result -= values[i];
			}
		}
		return result;
	}

	public static Integer sub(Integer... values) {
		Integer result = 0;
		if (values.length > 0) {
			if (values[0] != null) {
				result = values[0];
			}
		}
		for (int i = 1; i < values.length; i++) {
			if (values[i] != null) {
				result -= values[i];
			}
		}
		return result;
	}

	public static Long sub(Long... values) {
		Long result = 0l;
		if (values.length > 0) {
			if (values[0] != null) {
				result = values[0];
			}
		}
		for (int i = 1; i < values.length; i++) {
			if (values[i] != null) {
				result -= values[i];
			}
		}
		return result;
	}

	/**
	 * 乘法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”
	 * 
	 * @param ds
	 * @return
	 */
	public static Double mul(Number... numbers) {
		Double result = 0d;
		if (numbers.length > 0 && numbers[0] != null) {
			result = numbers[0].doubleValue();
			for (int i = 1; i < numbers.length; i++) {
				if (numbers[i] != null) {
					if (numbers[i].doubleValue() == 0) {
						result = 0d;
					} else {
						result *= numbers[i].doubleValue();
					}
				}
			}
		}
		return result;
	}

	public static Double mul(Double... ds) {
		Double result = 0d;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] == 0) {
						result = 0d;
					} else {
						result *= ds[i];
					}
				}
			}
		}

		return result;
	}

	public static Float mul(Float... ds) {
		Float result = 0f;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] == 0) {
						result = 0f;
					} else {
						result *= ds[i];
					}
				}
			}
		}
		return result;
	}

	public static Integer mul(Integer... ds) {
		Integer result = 0;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] == 0) {
						result = 0;
					} else {
						result *= ds[i];
					}
				}
			}
		}

		return result;
	}

	public static Long mul(Long... ds) {
		Long result = 0l;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] == 0) {
						result = 0l;
					} else {
						result *= ds[i];
					}
				}
			}
		}

		return result;
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @return
	 */
	public static Integer div(Integer... ds) {
		Integer result = 0;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] != 0) {
						result /= ds[i];
					} else {
						throw new ArithmeticException("除数不能为“0”");
					}
				}
			}
		}
		return result;
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @return
	 */
	public static Double div(Double... ds) {
		Double result = 0d;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] != 0) {
						result /= ds[i];
					} else {
						throw new ArithmeticException("除数不能为“0”");
					}
				}
			}
		}
		return result;
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @param precision
	 *            保留几位 使用整型写法
	 * @return 结果保留 precision 位小数 默认使用四舍五入的方式处理结果
	 */
	public static Double div(String precision, Double... ds) {
		Double result = 0d;
		result = div(ds);
		return formatDouble(result, Integer.valueOf(precision));
	}

	public static Float div(Float... ds) {
		Float result = 0f;
		if (ds.length > 0 && ds[0] != null) {
			result = ds[0];
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i] != 0) {
						result /= ds[i];
					} else {
						throw new ArithmeticException("除数不能为“0”");
					}
				}
			}
		}
		return result;
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @param precision
	 *            保留几位 使用整型写法
	 * @return 结果保留 precision 位小数 默认使用四舍五入的方式处理结果
	 */
	public static Float div(String precision, Float... ds) {
		Float result = 0f;
		result = div(ds);
		return formatFloat(result, Integer.valueOf(precision));
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @return
	 */
	public static Double div(Number... ds) {
		Double result = 0d;
		if (ds.length > 0 && ds[0] != null) {
			result = new Double(ds[0].doubleValue());
			for (int i = 1; i < ds.length; i++) {
				if (ds[i] != null) {
					if (ds[i].doubleValue() != 0) {
						result /= ds[i].doubleValue();
					} else {
						throw new ArithmeticException("除数不能为“0”");
					}
				}
			}
		}
		return result;
	}

	/**
	 * 除法运算，如果第一个参数为“0”，不管后面的参数怎样结果都为“0”； 若第一个参数不为“0”，后面的参数也不能为零，否则抛出除“0”异常
	 * 
	 * @param ds
	 * @param precision
	 *            保留几位 使用整型写法
	 * @return 结果保留 precision 位小数 默认使用四舍五入的方式处理结果
	 */
	public static Double div(String precision, Number... ds) {
		Double result = 0d;
		result = div(ds);
		return formatDouble(result, Integer.valueOf(precision));
	}

	/*
	 * $Id: MathsUtils.java,v 1.2 2016/03/16 03:16:44 zwm Exp $ Copyright (c)
	 * 2008-2010 ... Co. Ltd. All Rights Reserved Changelog: Li Guoliang - Jan
	 * 27, 2010: Initial version
	 */

	/**
	 * 为给定的分数列表计算对应的标准分, 返回对应的标准分列表.
	 * 
	 * @param rawScores
	 *            原始分列表
	 * @return [平均分, 方差, 原始分对应的标准分列表(顺序同原始分列表)]
	 */
	public static Object[] calculateStandardScore(Double... values) {
		List<Double> listStandardScore = new ArrayList<Double>();

		double scoreMean = calculateScoreMean(values); // 1. 获得平均分
		// 2. 计算标准差
		double standardDeviation = calculateSTD(scoreMean, values);
		for (int i = 0; i < values.length; i++) {
			double standardScore = (values[i] - scoreMean) / standardDeviation;
			listStandardScore.add(i, standardScore); // 将标准分按顺序放入列表中.
		}

		Object[] consolidateValues = new Object[3];
		consolidateValues[0] = scoreMean;
		consolidateValues[1] = standardDeviation;
		consolidateValues[2] = listStandardScore;

		return consolidateValues;
	}

	/**
	 * 获得平均分.
	 * 
	 * @param rawScores
	 * @return
	 */
	public static Double calculateScoreMean(Double... values) {
		double scoreAll = 0.0;
		int length =0;
		for (Double score : values) {
			if (score != null) {
				length ++; 
				scoreAll += score;
			}
		}
		if(length == 0) {
			return 0d;
		}
		return scoreAll /length;
	}

	/**
	 * 计算标准差
	 * 
	 * @param rawScores
	 * @param scoreMean
	 * @return
	 */
	public static double calculateSTD(double scoreMean, Double... values) {
		double allSquare = 0.0;
		int length =0;
		for (Double rawScore : values) {
			if (rawScore != null) {
				length ++; 
				allSquare += (rawScore - scoreMean) * (rawScore - scoreMean);
			}
		}
		// (xi - x(平均分)的平方 的和计算完毕
		double denominator = (length- 1);
		if(denominator == 0){
			return 0d;
		}
		return Math.sqrt(allSquare / denominator);
	}

	/**
	 * 计算矩形对角线长度
	 * 
	 * @param rawScores
	 * @param scoreMean
	 * @return
	 */
	public static Double calculateDiagonal(Double length, Double width) {
		return Math.sqrt(sum(mul(length, length), mul(width, width)));
	}
	
	public static void main(String[] args) {
		//DecimalFormat    df   = new DecimalFormat("######0.00");
	}
	
	public static boolean isBig0(Double d){
		if(d!=null && d>0){
			return true;
		}else{
			return false;
		}
		 
	}
	
	public static Long getRandom(){
		Long value = new SecureRandom().nextLong();
		return value<=0?-value:value;
	}
	
	/**
	 * 
	 * sumDouble:
	 * 适用:double类型数据相加
	 * @param d1
	 * @param d2
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static double sumDouble(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).doubleValue();
	}

	/**
	 * 
	 * subDouble:
	 * 适用:double类型数据相减
	 * @param d1
	 * @param d2
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static double subDouble(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.subtract(bd2).doubleValue();
	}
	/**
	 * 
	 * mulDouble:
	 * 适用:double类型数据相乘
	 * @param d1
	 * @param d2
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static double mulDouble(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.multiply(bd2).doubleValue(); 
    } 

	/**
	 * 
	 * divDouble:
	 * 适用:double类型数据相除
	 * @param d1
	 * @param d2
	 * @param scale 保留小数的位数
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static double divDouble(double d1, double d2, int scale) {
		if (d2 == 0) {
			return 0;
		}
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
