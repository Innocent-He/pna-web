package edu.xidian.pnaWeb.web.model;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.module.AlgResult;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 19:54
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetriDTO implements AlgResult,Serializable {
	/**
	 * petri网属性
	 */
	private AttrDTO attr;
	/**
	 * 节点列表
	 */
	private List<NodeDTO> nodeList;
	/**
	 * 连接弧列表
	 */
	private List<LinkDTO> linkList;
	public String display() {
		return JSON.toJSONString(this);
	}
}
