package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description
 * @Author He
 * @Date 2021/10/5 17:36
 */
@Builder
@Data
public class PetriDO {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String ownerName;
	/**
	 * Petri网备注
	 */
	private String desc;
	/**
	 * Petri网名称
	 */
	private String name;
	/**
	 * 前置矩阵
	 */
	private int[][] preMatrix;
	/**
	 * 后置矩阵
	 */
	private int[][] postMatrix;
	/**
	 * 库所标识
	 */
	private int[] marking;
	/**
	 * web端页面数据
	 */
	private String webJson;
	/**
	 * 创造时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}
