package edu.xidian.pnaWeb.petri.module;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @Author He
 * @Date 2022/2/26 14:38
 */
@AllArgsConstructor
public enum TimeLevel {
	FIVE_MINUTE(5L,TimeUnit.MINUTES)
	,TEN_MINUTE(10L,TimeUnit.MINUTES)
	,HALF_HOUR(30L,TimeUnit.MINUTES)
	,HOUR(1L,TimeUnit.HOURS)
	,HALF_DAY(12L,TimeUnit.HOURS)
	,ONE_DAY(1L,TimeUnit.DAYS);
	private Long time;
	private TimeUnit timeUnit;
	public static TimeLevel getTime(Integer level) {
		switch (level) {
			case 0:return FIVE_MINUTE;
			case 1:return TEN_MINUTE;
			case 2:return HALF_HOUR;
			case 3:return HOUR;
			case 4:return HALF_DAY;
			case 5:return ONE_DAY;
			default:return null;
		}
	}

	public Long getTime() {
		return time;
	}
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
