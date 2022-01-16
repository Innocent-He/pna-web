package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 19:54
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO implements Serializable {
	/**
	 * 节点类型
	 */
	private String type;
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 类名称(用于前端css选择)
	 */
	private String className;
	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 库所token
	 */
	private Integer token;
	/**
	 * 节点高度
	 */
	private Integer height=50;
	/**
	 * 节点横坐标
	 */
	private Integer x;
	/**
	 * 节点纵坐标
	 */
	private Integer y;
}
