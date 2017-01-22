package com.longbei.appservice.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author Bear.Xiong
 */
@SuppressWarnings({"deprecation", "unused"})
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	public static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * 
	 * @param date
	 * @return
	 * 
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime1(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

    public static Date formatDate(String date, String format) throws ParseException {
    	return new SimpleDateFormat(format).parse(date);
    }      
	
	/**
	 * 得到日期时间字符串，转换格式（yyyyMMddHHmmss）
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime2(Date date) {
		return formatDate(date, "yyyyMMddHHmmss");
	}
	/**
	 * 得到日期时间字符串，转换格式（时间戳）
	 * 
	 * @param timestemp
	 * @return
	 */
	public static String formatDateTime3(long timestemp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(timestemp));
	}
	/**
	 * 得到当前时间字符串格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime2() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 得到当前年份字符串格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到指定日期字符串的当前月份（MM）
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonth(Date date) {
		return formatDate(date, "MM");
	}

	/**
	 * 得到指定日期字符串的当前月份（MM）
	 * 
	 * @param datestr
	 * @return
	 */
	public static String getMonth(String datestr) {
		return formatDate(parseDate(datestr), "MM");
	}

	/**
	 * 得到当天字符串格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 字符串转化为日期
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}



	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期当月天数的数组
	 * 
	 * @param datestr
	 * @return
	 */
	public static String[] getMonthDayArray(String datestr) {
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM");
		Calendar rightNow = Calendar.getInstance();
		try {
			rightNow.setTime(simpleDate.parse(datestr.substring(0, 4) + "-" + datestr.substring(4, 6)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int day = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);//根据年月获取月份天数
		String[] days = new String[day];
		for (int i = 0; i < day; i++) {
			if (i < 10) {
				if (i == 9) {
					days[i] = datestr.substring(4, 6) + "-" + String.valueOf(i + 1);
				} else {
					days[i] = datestr.substring(4, 6) + "-" + "0" + (i + 1);
				}
			} else {
				days[i] = datestr.substring(4, 6) + "-" + String.valueOf(i + 1);
			}
		}
		return days;
	}

	/**
	 * 获取指定日期当月天数
	 * 
	 * @param datestr
	 * @return
	 */
	public static int getMonthDays(String datestr) {
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM");
		Calendar rightNow = Calendar.getInstance();
		try {
			rightNow.setTime(simpleDate.parse(datestr.substring(0, 4) + "-" + datestr.substring(4, 6)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);//根据年月获取月份天数
	}

	public static Date getDateStart(Date date) {
		Date datetemp = date;
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			datetemp = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetemp;
	}

	public static Date getDateEnd(Date date) {
		Date datetemp = date;
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			datetemp = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetemp;
	}

	public static String getFullDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = sdf.parse(dateStr);
			return date.toLocaleString().substring(0, 9);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param before
	 * @param after
	 * @return int
	 * @throws ParseException
	 */
	public static int daysBetween(String before, String after) {
		Date beforeDate = new Date();
		Date afterDate = new Date();
		DateFormat df = DateFormat.getDateInstance();
		try {
			beforeDate = df.parse(before);
			afterDate = df.parse(after);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(beforeDate);
		caled.setTime(afterDate);
		// 设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		return ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
	}

	/**
	 * java计算两个时间相差（天、小时、分钟、秒）
	 * 
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @param str
	 * @return
	 */
	public static long dateDiff(String startTime, String endTime, String format, String str) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		// 获得两个时间的毫秒时间差异
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd;// 计算差多少天
			hour = diff % nd / nh + day * 24;// 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
			sec = diff % nd % nh % nm / ns;// 计算差多少秒
			// 输出结果
			//System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时" + (min - day * 24 * 60) + "分钟" + sec + "秒。");
			//System.out.println("hour=" + hour + ",min=" + min);
			if (str.equalsIgnoreCase("h")) {
				return hour;
			} else {
				return min;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (str.equalsIgnoreCase("h")) {
			return hour;
		} else {
			return min;
		}
	}

	/**
	 * 将形如yyyyMMddHHmmss的形式变成yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datestr
	 * @return
	 */
	public static String getFormatDateString(String datestr) {
		return datestr.substring(0, 4) + "-" + datestr.substring(4, 6) + "-" + datestr.substring(6, 8) + " " + datestr.substring(8, 10) + ":" + datestr.substring(10, 12) + ":" + datestr.substring(12, 14);
	}

	
	/**
	 * 日期比较
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static boolean compare(Date oldDate,Date newDate){
		if(oldDate.getTime() > newDate.getTime()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否同一天
	 * @param sDate1
	 * @param sDate2
	 * @return
	 */
	public static boolean isSameDay(String sDate1,String sDate2){
		sDate1 = sDate1.substring(0, 10);
		sDate2 = sDate2.substring(0, 10);
		return sDate1.equals(sDate2);
	}
	
	
}
