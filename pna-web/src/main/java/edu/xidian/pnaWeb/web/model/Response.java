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

	public static Response error(String errorCode, String msg) {
		Response response = new Response();
		response.setSuccess(false);
		response.setMessage(msg);
		response.setErrorCode(errorCode);
		return response;
	}


	public static Response success() {
		return Response.success(null);
	}

	public static <T> Response<T> success(T data) {
		return Response.success(data, null);
	}


	public static <T> Response<T> success(T data, String message) {
		Response<T> response = new Response();
		response.setSuccess(true);
		response.setMessage(message);
		response.setData(data);
		return response;
	}
}
