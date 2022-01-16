package edu.xidian.pnaWeb.web.model;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import lombok.Data;

import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2021/10/23 15:40
 */
@Data
public class Invocation {
	private String methodName;
	private Map params;
}
