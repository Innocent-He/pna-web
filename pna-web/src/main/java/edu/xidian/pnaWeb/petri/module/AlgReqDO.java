package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:14
 */
@Data
@Builder
public class AlgReqDO {
	/**
	 * 当前算法唯一id
	 */
	private Long id;
	/**
	 * 当前请求参数
	 */
	private Map params;
	/**
	 * Petri网模型数据
	 */
	private PetriDO petriDO;
	/**
	 * 算法名称
	 */
	private String algName;
	/**
	 * 取消算法
	 */
	private boolean cancel;
	/**
	 * 用户邮箱，用于通知
	 */
	private String email;
	/**
	 * 算法最长等待时间
	 */
	private Integer timeLevel;
}
