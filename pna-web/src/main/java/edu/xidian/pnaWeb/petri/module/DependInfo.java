package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2022/2/23 22:40
 */
public class DependInfo {
	private List<DependTransInfo> dependInfos;
	@Builder
	static class DependTransInfo {
		private List<Set<Integer>> initialDependTrans;
		private List<Integer> ews;
		private Integer node;
		private Map<Integer, List<Set<Integer>>> dependTrans;
	}

	 static DependTransInfo appendDependTrans(List<Set<Integer>> initialDependTrans,
								  List<Integer> ews,
								  Integer node,
								  Map<Integer, List<Set<Integer>>> dependTrans) {
		return DependTransInfo.builder()
				.initialDependTrans(initialDependTrans)
				.ews(ews)
				.node(node)
				.dependTrans(dependTrans)
				.build();
	}
	static void appendDepends(List<Set<Integer>> initialDependTrans,
						 List<Integer> ews,
						 Integer node,
						 Map<Integer, List<Set<Integer>>> dependTrans) {
		DependInfo dependInfo = new DependInfo();
	}

}
