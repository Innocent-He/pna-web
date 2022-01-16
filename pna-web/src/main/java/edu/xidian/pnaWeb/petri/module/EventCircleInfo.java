package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/25 16:01
 */
@Builder
public class EventCircleInfo {
	private Map<Integer,List<List<Integer>>> stateEventCircle;
	private Map<Integer, Set<Integer>> deadTran;

	@Override
	public String toString() {
		StringBuilder res=new StringBuilder();
		for (int i = 0; i < stateEventCircle.size(); i++) {
			List<List<Integer>> circles = stateEventCircle.get(i);
			if (!CollectionUtils.isEmpty(circles)) {
				res.append("state:").append(i+1).append("\n").append("disable tran circle:");
				for (List<Integer> tran : circles) {
					List<Integer> circle = tran.stream().map(e -> e + 1).collect(Collectors.toList());
					res.append(circle.toString());
				}
				res.append("\n");
			}
		}

		for (Map.Entry<Integer, Set<Integer>> deadEntry : deadTran.entrySet()) {
			List<Integer> deadTrans = deadEntry.getValue().stream().map(e -> e + 1).collect(Collectors.toList());
			res.append("state:").append(deadEntry.getKey()+1).append("\n").append("deadTrans:").append(deadTrans).append("\n");
		}
		return res.toString();
	}
}
