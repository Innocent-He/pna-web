package edu.xidian.pnaWeb.web.exception;

import lombok.Data;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 20:03
 */
@Data
public class BaseException extends RuntimeException{
	private String message;
	private String code;
	public BaseException(String code,String msg) {
		this.code=code;
		this.message=msg;
	}
	public BaseException() {

	}
}
