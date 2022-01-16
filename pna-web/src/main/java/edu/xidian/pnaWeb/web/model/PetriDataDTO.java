package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class PetriDataDTO implements Serializable {
	private AttrDTO attr;
	private List<NodeDTO> nodeList;
	private List<LinkDTO> linkList;
	private String methodName;
	private Map params;
	private String status;
	private Boolean editNew;
}
