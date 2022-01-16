package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Description
 * @Author He
 * @Date 2021/10/16 15:33
 */
@Builder
@Data
public class ReachGraphInfo {
	private Map<Integer, StateNode> reachGraphMap;
	private List<List<Integer>> stateFires;
	private List<Set<Integer>> stateNotFires;
	private List<StateNode> deadStates;
	private List<String> deadStatesStr;

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < reachGraphMap.size(); i++) {
			StateNode stateNode = reachGraphMap.get(i);
			result.append("state").append(i + 1).append(":").append(Arrays.toString(stateNode.getState()));
			result.append("\n");
			List<Integer> canFireTrans = stateFires.get(i);
			int seq = 0;
			for (Integer canFireTran : canFireTrans) {
				if (!CollectionUtils.isEmpty(stateNode.getChildNodes())) {
					result.append("t")
							.append(canFireTran + 1)
							.append("-->state")
							.append(stateNode.getChildNodes().get(seq++).getStateNo() + 1)
							.append("\n");
				}
			}
			result.deleteCharAt(result.length() - 1);
			result.append("\n");
		}
		result.append("total dead state:").append("\n");
		for (StateNode deadState : deadStates) {
			result.append("state").append(deadState.getStateNo() + 1).append(":").append(Arrays.toString(deadState.getState()));
			result.append("\n");
		}
		return result.toString();
	}
}
