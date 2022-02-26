package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2022/2/23 20:49
 */
@Builder
@Data
public class PetriGraph {
	/**
	 * 库所与它的的后置变迁集合
	 */
	Map<Integer, Set<Integer>> placePostGraph;
	/**
	 * 变迁与它的的后置库所集合
	 */
	Map<Integer, Set<Integer>> tranPostGraph;
	/**
	 * 库所与它的的前置变迁集合
	 */
	Map<Integer, Set<Integer>> placePreGraph;
	/**
	 * 变迁与它的的前置库所集合
	 */
	Map<Integer, Set<Integer>> tranPreGraph;
}
