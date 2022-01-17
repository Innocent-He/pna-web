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
	private Long id;
	private Map params;
	private PetriDO petriDO;
	private String algName;
	private boolean cancel;
	private boolean emailEnable;
}
