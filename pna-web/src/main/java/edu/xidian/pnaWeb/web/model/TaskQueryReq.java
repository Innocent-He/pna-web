package edu.xidian.pnaWeb.web.model;

import lombok.Data;
import lombok.ToString;

/**
 * @Description
 * @Author He
 * @Date 2022/1/19 18:11
 */
@Data
@ToString
public class TaskQueryReq {
	private Integer pageNo=1;
	private Integer pageSize=10;
	private Boolean queryAll;
}
