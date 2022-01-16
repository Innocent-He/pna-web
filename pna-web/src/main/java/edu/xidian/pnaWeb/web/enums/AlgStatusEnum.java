package edu.xidian.pnaWeb.web.enums;

/**
 * @Author He
 * @Date 2022/1/14 22:02
 */
public enum AlgStatusEnum {
	SUCCESS("运行成功"),FAILED("运行失败"),TIME_OUT("运行超时");
	private String msg;
	AlgStatusEnum(String msg) {
		this.msg = msg;
	}
}
