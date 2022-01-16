package edu.xidian.pnaWeb.web.model;

import lombok.Data;

/**
 * @Description 返回给前端的数据格式
 * @Author He
 * @Date 2021/10/17 21:25
 */
@Data
public class Response<T> {
	private boolean success;
	private T data;
	private String message;
	private String errorCode;

	public static Response error(String errorCode,String msg) {
		Response response = new Response();
		response.setSuccess(false);
		response.setMessage(msg);
		response.setErrorCode(errorCode);
		return response;
	}



	public static Response success() {
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}

	public static <T> Response success(T data) {
		Response<T> response = new Response();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}
}
