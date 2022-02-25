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
	Map<Integer, Set<Integer>> placePostGraph;
	Map<Integer, Set<Integer>> tranPostGraph;
	Map<Integer, Set<Integer>> placePreGraph;
	Map<Integer, Set<Integer>> tranPreGraph;
}
