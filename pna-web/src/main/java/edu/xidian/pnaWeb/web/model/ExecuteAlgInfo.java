package edu.xidian.pnaWeb.web.model;

import edu.xidian.pnaWeb.web.enums.AlgStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author He
 * @Date 2022/1/14 21:56
 */
@Data
public class ExecuteAlgInfo {
	/**
	 * 算法执行者
	 */
	private String owner;
	/**
	 * 执行算法名称
	 */
	private String algName;
	/**
	 * 提交时间
	 */
	private LocalDateTime subTime;
	/**
	 * 算法运行状态
	 */
	private AlgStatusEnum algStatus;
}
