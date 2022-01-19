package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2022/1/19 18:07
 */
@Data
@AllArgsConstructor
public class PageResult<T> {
	private List<T> content;
	private Long totalCount;
}
