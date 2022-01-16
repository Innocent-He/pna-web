package edu.xidian.pnaWeb.web.model;

import lombok.Data;

/**
 * @Description
 * @Author He
 * @Date 2021/11/16 17:05
 */
@Data
public class GenerateRequest {
	private Integer placeCount;
	private Integer tranCount;
	private String netType;
}
