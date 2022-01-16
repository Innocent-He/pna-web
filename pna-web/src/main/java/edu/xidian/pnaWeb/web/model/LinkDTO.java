package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 19:56
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO implements Serializable {
	/**
	 * 弧的id
	 */
	private String id;
	/**
	 * 前置节点id
	 */
	private String sourceId;
	/**
	 * 后置节点id
	 */
	private String targetId;
	/**
	 * 弧权值
	 */
	private Integer weight;

	public LinkDTO(Integer id,String sourceId,String targetId,Integer weight) {
		this.setId("link-"+id);
		this.setSourceId(sourceId);
		this.setTargetId(targetId);
		this.setWeight(weight);
	}

}
