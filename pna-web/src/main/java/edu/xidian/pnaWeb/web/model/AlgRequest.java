package edu.xidian.pnaWeb.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:00
 */
@Data
@Builder
public class AlgRequest {
	private Map params;
	private String algName;
	private PetriDTO petriDTO;
}
