package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 19:59
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AttrDTO implements Serializable {
	/**
	 * petriId
	 */
	private Long id;
	/**
	 * 创造者id(未登录用户为id地址)
	 */
	private String ownerId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 描述
	 */
	private String des;
	/**
	 * 最大的库所id
	 */
	private Integer maxPlaceId;
	/**
	 * 最大的变迁id
	 */
	private Integer maxTranId;
}
