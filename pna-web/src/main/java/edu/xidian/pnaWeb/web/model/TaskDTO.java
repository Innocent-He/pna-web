package edu.xidian.pnaWeb.web.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 15:03
 */
@Data
@Builder
public class TaskDTO {
	private Long id;
	private String ownerName;
	private String status;
	private String result;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
