package edu.xidian.pnaWeb.web.exception;

import lombok.Data;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 18:41
 */
@Data
public class BizException extends BaseException{
	public BizException(String code,String msg) {
		super(code,msg);
	}
	public String getCode() {
		return super.getCode();
	}
	public String getMessage() {
		return super.getMessage();
	}
}
