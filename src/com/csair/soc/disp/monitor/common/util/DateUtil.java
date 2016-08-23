package com.csair.soc.disp.monitor.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/** 
 * @Title:  时间处理类
 * @Description: 
 * @Company: CSN
 * @ClassName: DateUtiil.java
 * @Author: XiongPF
 * @CreateDate: 2013-4-19
 * @UpdateUser:  
 * @Version:0.1
 *    
 */
/**
 * @ClassName: DateUtil
 * @Description: TODO
 * @author: youna
 * @date: 2015年9月29日
 * 
 */
public abstract class DateUtil {

	/**
	 * 日志
	 */
	private static Logger logger = LogManager.getLogger(DateUtil.class);// 日志
	/**
	 * 一天的毫秒数
	 */
	public static final long DAY_OF_MILLISECOND = (long) 1440 * 60 * 1000;
	/**
	 * 一小时的毫秒数
	 */
	public static final long HOUR_OF_MILLISECOND = (long) 60 * 60 * 1000;
	/**
	 * 一分钟的毫秒数
	 */
	public static final long MINUTE_OF_MILLISECOND = (long) 60 * 1000;

	public static final long SECOND_OF_MILLISECOND = (long) 1 * 1000;
	/**
	 * 北京时与国际时时差 毫秒
	 */
	public static final long UTC_PEK_TIME_DIFF = (long) 480 * 60 * 1000;// 北京时，时差

	/**
	 * 默认日期格式，added by youna
	 */
	public final static String DEFAULT_FORMAT = "yyyyMMddHHmm";

	public final static String YYYYMMDDHH = "yyyyMMddHH";

	public final static String HH_MM = "HHmm";

	public final static String YYYY_MM_DD = "yyyyMMdd";
	
	public final static String DDMMHH = "ddHHmm";

	public final static String YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";

	public static final String START_HH_MM = "0000";

	public static final String END_HH_MM = "2359";

	public static final String COD_QUERY_DATE_FORMAT = "yyyy-MM-dd";

	public static final String ADSB_RCV_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DYNAMIC_DT_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String DYNAMIC_DOPST_FORMAT = "yyyy-MM-dd HHmm";

	/**
	 * 获取本世纪最后一刻的时间Date
	 * 
	 * @return
	 */

	public static Date get21thMaxTimestamp() {

		return parse("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前日期的yyyyMMdd格式
	 * 
	 * @return
	 */
	public static String getCurDateFormat() {
		return getNextDateFormat(0);
	}

	/**
	 * 以当前日期为准，获取第n天的yyyyMMdd格式日期 n可以为负值
	 * 
	 * @return
	 */
	public static String getNextDateFormat(int n) {
		Date date = addDate(new Date(), n);
		return format(date, YYYY_MM_DD);
	}
	
	
	/**
	 * 以当前日期为准，获取第n天的yyyyMMdd格式日期 n可以为负值,允许传入today参数
	 * 
	 * @return
	 */
	public static String getNextDateFormat(int n,Date today) {
		Date date = addDate(today, n);
		return format(date, YYYY_MM_DD);
	}


	/**
	 * 将FR_ADSB的时间由long转换为指定格式类型
	 * 
	 * @param longStr
	 * @return UTC yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateStrFromLongStr(String longStr) {
		Date date = new Date(1000 * Long.valueOf(longStr));
		return DateUtil.format(DateUtil.getUtcFromPek(date), ADSB_RCV_FORMAT);

	}

	/**
	 * 获得与数据库匹配的国际时当前时间
	 * 
	 * @return
	 */
	public static Date getCurGMTDateForDB() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}

	/**
	 * 时间戳查询出的航班动态时间统一格式
	 */
	/**
	 * 时间戳查询出的航班动态时间统一格式
	 * 
	 * @param str
	 * @return yyyy-MM-dd HH:mm(PEK)
	 */
	public static String changeDopstToFormat(String Dopst) {
		if (StringUtil.isEmpty(Dopst)) {
			return "";
		}
		return format(parse(Dopst, DYNAMIC_DOPST_FORMAT), DYNAMIC_DT_FORMAT);
	}

	/**
	 * ACARS 发送时间UTC
	 * 
	 * @param telexDt默认UTC
	 *            HHmm ex:1845
	 * @return String yyyyMMddHHmm
	 */
	public static String getAcarsTime(String texlexStr) {
		while (texlexStr.length() < 4) {
			texlexStr = "0" + texlexStr;
		}
		StringBuffer dtStr = new StringBuffer();

		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		int Day = cal.get(Calendar.DAY_OF_MONTH);

		if (Day < 10) {
			dtStr.append(0).append(Day).append(texlexStr);
		} else {
			dtStr.append(Day).append(texlexStr);
		}

		return getAcarsDt(dtStr.toString());
	}

	/**
	 * ACARS DSP转发时间UTC
	 * 
	 * @param dspStr
	 *            ddHHmm ex:301845
	 * @return String yyyyMMddHHmm
	 */

	/**
	 * ACARS DSP转发时间UTC
	 * 
	 * @param dspStr
	 *            ddHHmm ex:301845
	 * @return String yyyyMMddHHmm
	 */
	public static String getAcarsDt(String dspStr) {
		StringBuffer dtStr = new StringBuffer();
		LocalDate ld = LocalDate.now();
		int Month = ld.getMonth().getValue();
		int Year = ld.getYear();
		int Day = ld.getDayOfMonth();
		SimpleDateFormat sdf = new SimpleDateFormat("ddHHmm");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(dspStr));
			int dspDay = cal.get(Calendar.DAY_OF_MONTH);
			if (Day == 1 && Day != dspDay) {
				Month--;
			}
			if (Month < 10) {
				dtStr.append(Year).append(0).append(Month).append(dspStr);
			} else {
				dtStr.append(Year).append(Month).append(dspStr);
			}
		} catch (ParseException e) {
			logger.error("getAcarsDt ParseException");
		}

		return dtStr.toString();
	}

	/**
	 * 判断当前是否在有效时间内
	 * 
	 * @param effFrom
	 * @param effTill
	 * @return
	 */
	public static boolean isInEff(Date effFrom, Date effTill) {
		Date now = new Date();
		if (now.before(effTill) && now.after(effFrom)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断当前是否在有效时间内（使用国际时）
	 * 
	 * @param effFrom
	 * @param effTill
	 * @return
	 */
	public static boolean isUTCTimeInEff(Date effFrom, Date effTill) {
		Date now =getCurGMTDateForDB();
		if (now.before(effTill) && now.after(effFrom)) {
			return true;
		}
		return false;
	}

	/**
	 * 格式化时间 HHmm
	 */
	/**
	 * 两段时间之间是否有交集
	 * 
	 * @param strDt0
	 *            时间段0的起始时间
	 * @param endDt0
	 *            时间段0的终止时间
	 * @param strDt1
	 *            时间段1的起始时间
	 * @param endDt1
	 *            时间段1的终止时间
	 * @return true 时间段有交集 false 时间段无交集
	 */
	public static boolean isDateIntersect(Date strDt0, Date endDt0,
			Date strDt1, Date endDt1) {
		// 如果strDt1<=strDt0<=endDt1则返回true
		if (!strDt0.before(strDt1) && !strDt0.after(endDt1)) {
			return true;
		}
		// 如果strDt1<=endDt0<=endDt1则返回true
		if (!endDt0.before(strDt1) && !endDt0.after(endDt1)) {
			return true;
		}
		// 如果strDt0<=strDt1<=endDt0则返回true
		if (!strDt1.before(strDt0) && !strDt1.after(endDt0)) {
			return true;
		}
		// 如果strDt0<=endDt1<=endDt0则返回true
		if (!endDt1.before(strDt0) && !endDt1.after(endDt0)) {
			return true;
		}
		return false;
	}

	/**
	 * @method:Cal2String
	 * @Description:转换日期内部类型Calendar变成String
	 * @Company: CSN
	 * @ClassName: FileInfoProcess.java
	 * @Author: ZhongYu
	 * @CreateDate: 2012-12-10
	 * @UpdateUser:
	 * @Version:0.1
	 * 
	 */
	public static String cal2String(Calendar caDate, String formate) {
		DateFormat fmt = new SimpleDateFormat(formate, Locale.US);
		String Date = fmt.format(caDate.getTime());
		return Date;
	}

	/**
	 * @method:String2Cal
	 * @Description:转换日期内部类型String变成Calendar
	 * @Company: CSN
	 * @ClassName: FileInfoProcess.java
	 * @Author: ZhongYu
	 * @CreateDate: 2012-12-10
	 * @UpdateUser:
	 * @Version:0.1
	 */

	public static Calendar string2Cal(String date, String formate) {
		DateFormat fmt = new SimpleDateFormat(formate, Locale.US);
		Calendar caDate = Calendar.getInstance();
		try {
			Date daDate = fmt.parse(date);
			caDate.setTime(daDate);
		} catch (ParseException e) {
			logger.error("string2Cal error ", e);
		}
		return caDate;
	}

	/**
	 * 获取航班具体的班期（星期一、二.....)
	 * 
	 * @param calStr
	 *            航班日期
	 * @return 航班班期
	 */
	public static String getDow(Calendar calStr) {
		int dow = calStr.get(Calendar.DAY_OF_WEEK);
		int frequency = dow - 1;
		/**
		 * java使用美国的计算方式，计算班期需要减1，值0==7
		 */
		if (frequency == 0) {
			frequency = 7;
		}
		return String.valueOf(frequency);
	}

	/**
	 * 计算时间差 endDt - strDt 单位：分钟
	 * 
	 * @param strDt
	 *            格式：yyyyMMddHHmm
	 * @param endDt
	 *            格式：yyyyMMddHHmm
	 * @return 时间差值 单位：分钟
	 */
	public static Long calculateDuratime(String strDt, String endDt) {
		SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyyMMddHHmm",
				Locale.US);
		Long delayMin = Long.valueOf("0");
		try {
			Date strDate = SDF_DATE_TIME.parse(strDt);
			Date endDate = SDF_DATE_TIME.parse(endDt);
			delayMin = ((endDate.getTime() - strDate.getTime()) / MINUTE_OF_MILLISECOND);
		} catch (ParseException e) {
			logger.error("解析日期有错：strDt:" + strDt + "  endDt:" + endDt);
		}
		return delayMin;
	}

	/**
	 * 计算过夜天数 endDt - strDt 单位：天
	 * 
	 * @param strDt
	 *            开始时间
	 * @param endDt
	 *            结束时间
	 * @return 过夜天数 单位：天
	 */
	public static int calculateLayoverNight(String strDt, String endDt) {
		SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyyMMdd", Locale.US);
		Long overNightNumber = (long) 0;
		String str = strDt.substring(0, 7);
		String end = endDt.substring(0, 7);
		try {
			Date strDate = SDF_DATE.parse(str);
			Date endDate = SDF_DATE.parse(end);
			overNightNumber = (endDate.getTime() - strDate.getTime())
					/ DAY_OF_MILLISECOND;
		} catch (ParseException e) {
			logger.error("解析日期有错：strDt:" + strDt + "  endDt:" + endDt);
		}
		return overNightNumber.intValue();
	}

	// /**
	// * 次日零点
	// * @return 第二天零点date
	// */
	// public static Date getNextDay(){
	// Date nextDay = getNextNDay(1);
	// return nextDay;
	// }
	/**
	 * 第三天8点，动态系统运作时间
	 * 
	 * @return 第三天8点，动态系统运作时间
	 */
	public static Date getAiropsSplitTime() {
		Date AiropsSplitDay = getNextNDay(3);
		return AiropsSplitDay;
	}

	/**
	 * 第N天8点
	 * 
	 * @return 第N天零点date
	 */
	public static Date getNextNDay(int n) {
		Date date = new Date();
		Date nextNDay = new Date();
		SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyyMMdd", Locale.US);
		String dateStr = SDF_DATE.format(date);

		try {
			nextNDay = SDF_DATE.parse(dateStr);
		} catch (ParseException e) {
			logger.error("解析日期有错：" + dateStr);
		}
		nextNDay.setTime(nextNDay.getTime() + n * DAY_OF_MILLISECOND
				+ UTC_PEK_TIME_DIFF);
		return nextNDay;
	}

	/**
	 * 休眠
	 * 
	 * @param min
	 */
	public static void sleep(long min) {
		try {
			Thread.sleep(min * DateUtil.MINUTE_OF_MILLISECOND);
		} catch (InterruptedException e) {
			logger.error("sleep error", e);
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @author youna
	 * @param date
	 *            要格式化的日期
	 * @param formate
	 *            格式，如："yyyy-MM-dd HH:mm:ss"
	 * @return String
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat dateformat;
		if (null == date) {
			return null;
		}
		if (!StringUtil.isEmpty(format)) {
			dateformat = new SimpleDateFormat(format);
		} else {
			dateformat = new SimpleDateFormat(DEFAULT_FORMAT);
		}
		return dateformat.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @author youna
	 * @param date
	 *            要格式化的日期
	 * @param formate
	 *            格式，如："yyyy-MM-dd HH:mm:ss"
	 * @param locale
	 * @return String
	 */
	public static String format(Date date, String format, Locale locale) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat dateformat;
		if (!StringUtil.isEmpty(format)) {
			dateformat = new SimpleDateFormat(format, locale);
		} else {
			dateformat = new SimpleDateFormat(DEFAULT_FORMAT);
		}
		return dateformat.format(date);
	}

	/**
	 * 按格式严格解析出字符串对应的日期
	 * 
	 * @author youna 2015-8-13
	 * @param date
	 *            需要解析的日期
	 * @param formate
	 *            解析格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String format) {
	    return parse(date, format, false);
	}
	public static Date parse(String date, String format, boolean lenient){
	    if (StringUtil.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        // 严格解析，不只是按格式转换，例如 20150833这样的日期，需要解析抛异常
        dateformat.setLenient(lenient);
        try {
            return dateformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
	}
	/**
	 * 按格式严格解析出字符串对应的日期
	 * 
	 * @author youna 2015-8-13
	 * @param date
	 *            要解析的日期
	 * @param formate
	 *            解析格式
	 * @param locale
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String format, Locale locale)
			throws ParseException {
		SimpleDateFormat dateformat = new SimpleDateFormat(format, locale);
		dateformat.setLenient(false);
		return dateformat.parse(date);
	}

	/**
	 * 两时间相减的分钟数
	 * 
	 * @param date1
	 * @param date2
	 * @return date1 - date2 相隔分钟数
	 */
	public static int getDiffMins(Date date1, Date date2) {
		int minutes = 0;
		try {
			long time1 = date1.getTime();
			long time2 = date2.getTime();
			long aMinuteTime = 60 * 1000;
			minutes = (int) (time1 / aMinuteTime) - (int) (time2 / aMinuteTime);
		} catch (Exception e) {
			logger.error("getDiffMins error ", e);
		}

		return minutes;
	}
	
	/**
	 * 两时间相减的分钟数
	 * 
	 * @param date1
	 * @param date2
	 * @return date1 - date2 相隔分钟数
	 */
	public static int getDiffSeconds(Date date1, Date date2) {
		int seconds = 0;
		try {
			long time1 = date1.getTime();
			long time2 = date2.getTime();
			long aSecondTime = 1000;
			seconds = (int) (time1 / aSecondTime) - (int) (time2 / aSecondTime);
		} catch (Exception e) {
			logger.error("getDiffMins error ", e);
		}

		return seconds;
	}

	/**
	 * 两日期相减的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return 相隔天数
	 */
	public static int subtract(Date date1, Date date2) {

		int days = 0;
		try {
			long time1 = date1.getTime();
			long time2 = date2.getTime();
			long aDayTime = 24 * 60 * 60 * 1000;
			days = (int) (time1 / aDayTime) - (int) (time2 / aDayTime);
		} catch (Exception e) {
			logger.error("subtract error ", e);
		}

		return days;
	}

	/**
	 * 获取两个时间的间隔分钟,没有负值
	 * 
	 * @return
	 */
	public static int getIntervalOfSec(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new NullPointerException("Cann't input null date");
		}
		return (int) Math.abs((date1.getTime() - date2.getTime()) / 1000);
	}
	/**
     * 间隔小时数
     * 
     * @param date1
     * @param date2
     * @return date1 - date2
     */
    public static int intervalHours(Date date1, Date date2) {
        return intervalMinutes(date1, date2) / 60;
    }
    public static String getUtcTimeStr(int dayOffset) {  
        // 1、取得本地时间：  
        Calendar cal = Calendar.getInstance() ;  
        // 2、取得时间偏移量：  
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);  
        // 3、取得夏令时差：  
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);  
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：  
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));  
        cal.add(java.util.Calendar.DAY_OF_MONTH, dayOffset);
        int year = cal.get(Calendar.YEAR);  
        int month = cal.get(Calendar.MONTH)+1;  
        int day = cal.get(Calendar.DAY_OF_MONTH); 
        
        String UTCStr=year+"-"+month+"-"+day;
        return UTCStr ;  
    } 
    /**
     * 相隔分钟数
     * date1-date2
     * @param date1
     * @param date2
     * @return date1 - date2
     */
    public static int intervalMinutes(Date date1, Date date2) {
        return intervalSecond(date1, date2) / 60;
    }
	/**
     * 相隔秒数
     * 
     * @param date1
     * @param date2
     * @return date1 - date2
     */
    public static int intervalSecond(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            throw new IllegalArgumentException();
        int second = 0;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        second = (int) ((time1 - time2) / 1000);
        return second;
    }
	/**
	 * 判断两个日期之间跨了多少天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDays(Date date1, Date date2) {
		Date date1Str = getStrOfDay(date1);
		Date date2Str = getStrOfDay(date2);
		return subtract(date1Str, date2Str);
	}

	/**
	 * 北京时的时间转国际时的strdt
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLegUtcStrDtFromPek(Date date) {
		if (date != null) {
			Date utcDate = getUtcFromPek(date);
			Date utcStrDt = getStrOfDay(utcDate);
			return utcStrDt;
		}
		return null;
	}

	/**
	 * 获取指定日期的开始时间 ， 时间为0000
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStrOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		return c.getTime();
	}

	/**
	 * 获取指定日期的结束时间，时间为2359
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		return c.getTime();
	}

	/**
	 * 北京时转国际时
	 * 
	 * @param date
	 * @return
	 */
	public static Date getUtcFromPek(Date date) {
		return addMinute(date, -480);
	}

	/**
	 * 国际时转北京时
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPekFromUtc(Date date) {
		return addMinute(date, 480);
	}

	/**
	 * 把北京时间字符串改成UTC时间字符串
	 * 
	 * @param pek
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getUtcTimeFromPek(String pek) {
		if (StringUtil.isEmpty(pek)) {
			return "";
		}
		return format(getUtcFromPek(parse(pek, DYNAMIC_DT_FORMAT)),
				DYNAMIC_DT_FORMAT);
	}

	/**
	 * 把UTC时间字符串改成北京时时间字符串
	 * 
	 * @param pek
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getPekTimeFromUtc(String utc) {
		if (StringUtil.isEmpty(utc)) {
			return "";
		}
		return format(getPekFromUtc(parse(utc, DYNAMIC_DOPST_FORMAT)),
				DYNAMIC_DT_FORMAT);
	}

	/**
	 * 从国际时,到北京时的HHmm
	 * 
	 * @param date
	 * @return
	 */
	public static String getPekTimeFromUtc(Date date) {
		Date pekDate = addMinute(date, 480);
		String hhmm = format(pekDate, "HHmm");
		return hhmm;
	}

	/**
	 * 增加或减少秒数
	 * 
	 * @param date
	 * @param mins
	 *            为负数时，表示减少秒数； 为正数时，表示增加秒数
	 */
	public static Date addSecond(Date date, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * 增加或减少分钟
	 * 
	 * @param date
	 * @param mins
	 *            为负数时，表示减少分钟数； 为正数时，表示增加分钟数
	 */
	public static Date addMinute(Date date, int mins) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, mins);

		return cal.getTime();
	}

	/**
	 * 增加或减少日期
	 * 
	 * @param date
	 * @param days
	 *            为负数时，表示减少日期； 为正数时，表示增加日期
	 */
	public static Date addDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 判断是否同一天
	 * 
	 * @author youna 2013-9-12
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isTheSameDay(Date date1, Date date2) {
		boolean same = false;

		if (date1 == null || date2 == null) {
			return same;
		}

		if (format(date1, YYYY_MM_DD).equals(format(date2, YYYY_MM_DD))) {
			same = true;
		}

		return same;
	}

	/**
	 * 把date去掉时间，只保留日期
	 * 
	 * @author youna
	 * @param date
	 * @return
	 */
	public static Date getDateWithoutTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 计算输入和当前日期的时间差值  
	 * 返回相差几日 正值为输入时间大于当前时间
	 * 负值为输入时间小于当前时间
	 * @param recDt
	 * @param airTm
	 * @return
	 */
	public static int getDaysFromNow(Date date) {
		Date now=new Date();
		return (int) ((date.getTime()-now.getTime())/DAY_OF_MILLISECOND);
	}
	
	/**
	 * 获取HHmm的分钟数
	 * 
	 * @author youna 2015-9-29
	 * @param timeHHmm
	 * @return
	 */
	public static int getMinutesFromHHmm(int timeHHmm) {
		return 60 * (timeHHmm / 100) + timeHHmm % 100;
	}

	/**
	 * @description 获取两个时间之前的差值（分钟）
	 * @param recDt
	 * @param airTm
	 * @return
	 */
	public static Long getRecTimeFromAir(Date recDt, Date airTm) {
		if (recDt.before(airTm)) {
			return null;
		}
		long diff = recDt.getTime() - airTm.getTime();
		long diffDay = diff / DateUtil.DAY_OF_MILLISECOND;
		long diffHour = diff % DateUtil.DAY_OF_MILLISECOND
				/ DateUtil.HOUR_OF_MILLISECOND;
		long diffMin = diff % DateUtil.DAY_OF_MILLISECOND
				% DateUtil.HOUR_OF_MILLISECOND / DateUtil.MINUTE_OF_MILLISECOND
				+ diffHour * 60 + diffDay * 24 * 60;// 计算差多少分钟
		return diffMin;
	}
	
	/**
     * -> HH:mm
     * @return
     */
    public static String getHHmm(Date date){
        return format(date, "HH:mm");
    }
    
    /**
     * 
     * @param date
     * @return UTC时间
     */
    public static Date getNowUtcDate(){
      return getUtcFromPek(new Date());
    }
    /**
     * date1 是否小于或等于 date2
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isLessOrEquals(Date date1, Date date2) {
        if (date1 != null && date2 != null
                && (date1.before(date2) || date1.getTime() == date2.getTime())) {
            return true;
        }
        return false;
    }
    
    /**
     * date1 是否大于或等于 date2
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isGreaterOrEquals(Date date1, Date date2) {
        if (date1 != null && date2 != null
                && (date1.after(date2) || date1.getTime() == date2.getTime())) {
            return true;
        }
        return false;
    }
}
