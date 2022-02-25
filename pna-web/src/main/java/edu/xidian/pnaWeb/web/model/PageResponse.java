package edu.xidian.pnaWeb.web.model;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2022/2/25 16:34
 */
@Data
public class PageResponse<T> {
	private boolean success;
	private List<T> content;
	private Long total;
	private String message;
	private String errorCode;

	public static PageResponse error(String errorCode, String msg) {
		PageResponse response = new PageResponse();
		response.setSuccess(false);
		response.setMessage(msg);
		response.setErrorCode(errorCode);
		return response;
	}


	public static Response success() {
		return Response.success(null);
	}

	public static <T> PageResponse<T> success(PageResult<T> pageResult) {
		return PageResponse.success(pageResult.getContent(),pageResult.getTotalCount(), null);
	}

	public static <T> PageResponse<T> success(List<T> data,Long total, String message) {
		PageResponse<T> response = new PageResponse();
		response.setSuccess(true);
		response.setTotal(total);
		response.setMessage(message);
		response.setContent(data);
		return response;
	}
}
