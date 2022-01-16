package edu.xidian.pnaWeb.web.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author He
 * @Date 2022/1/16 22:54
 */

public enum TaskStatusEnum {
	WAITING(0,"等待中"),RUNNING(1,"执行中"),SUCCESS(2,"执行成功"),FAILED(3,"执行失败");
	private String status;
	private Integer code;
	private static Map<Integer,String> map=new HashMap<>();
	TaskStatusEnum(Integer code,String status) {
		this.status=status;
		this.code=code;
	}

	public static String getStatus(Integer code) {
		return map.get(code);
	}

	static {
		for (TaskStatusEnum status : TaskStatusEnum.values()) {
			map.put(status.code, status.status);
		}
	}

}
