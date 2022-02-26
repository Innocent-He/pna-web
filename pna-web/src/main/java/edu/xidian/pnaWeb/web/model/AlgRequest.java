package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgRequest {
	private Map params;
	private String algName;
	private PetriDTO petri;
	private String email;
	private Integer timeLevel;
}
