package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.StateNode;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Author He
 * @Date 2021/10/12 16:37
 */
@Service
public class ReachableGraphAlg implements AlgActuator{

	public ReachGraphInfo generateReachGraph(PetriDO petriDO) {
		return doGenerateReachGraph(petriDO, 2000);
	}

	public ReachGraphInfo generateReachGraphWithStep(PetriDO petriDO, Integer step) {
		if (step == null) {
			return generateReachGraph(petriDO);
		}
		assert step >= 0;
		return doGenerateReachGraph(petriDO, step);
	}

	private ReachGraphInfo doGenerateReachGraph(PetriDO petriDO, int step) {
		Map<Integer, StateNode> reachGraphMap = new HashMap<Integer, StateNode>();

		List<List<Integer>> stateFire = new ArrayList<List<Integer>>();
		List<Set<Integer>> stateNotFire = new ArrayList<Set<Integer>>();

		List<String> deadStatesStr = new ArrayList<String>();
		List<StateNode> deadStates = new LinkedList<StateNode>();

		Map<StateKey, StateNode> happened = new HashMap<>();
		int[][] postMatrix = petriDO.getPostMatrix();
		int[][] preMatrix = petriDO.getPreMatrix();

		int trans = postMatrix[0].length;
		List<Integer> tranIds = new ArrayList<>();
		for (int i = 0; i < trans; i++) {
			tranIds.add(i);
		}
		Deque<StateNode> deque = new ArrayDeque<>();
		StateNode firstState = StateNode.builder()
				.state(petriDO.getMarking())
				.depth(0)
				.stateNo(0)
				.build();
		deque.offer(firstState);
		StateKey firstKey = StateKey.builder().marking(firstState.getState()).build();
		happened.put(firstKey, firstState);
		int depth = 1, stateNo = 1;
		while (!deque.isEmpty() && step-- > 0) {
			int size = deque.size();
			for (int i = 0; i < size; i++) {
				StateNode curState = deque.poll();
				reachGraphMap.put(curState.getStateNo(), curState);
				int[] markings = curState.getState();
				List<Integer> canFires = PetriUtils.getCanFire(preMatrix, markings);
				Set<Integer> notFires = new HashSet<>();
				notFires.addAll(tranIds);
				for (Integer canFire : canFires) {
					notFires.remove(canFire);
				}
				stateFire.add(canFires);
				stateNotFire.add(notFires);
				if (canFires.isEmpty()) {
					curState.setDeadlock(true);
					deadStatesStr.add(String.valueOf(curState.getStateNo()));
					deadStates.add(curState);
				}
				if (step != 0) {
					for (Integer tranId : canFires) {
						int[] nextMarking = PetriUtils.fire(postMatrix, preMatrix, tranId, markings);
						StateKey nextKey = StateKey.builder().marking(nextMarking).build();
						StateNode nextState = happened.get(nextKey);
						// 判断当前状态是否抵达过
						if (nextState == null) {
							nextState = StateNode.builder()
									.stateNo(stateNo++)
									.depth(depth)
									.state(nextMarking)
									.build();
							deque.offer(nextState);
							happened.put(nextKey, nextState);
						}
						curState.getChildNodes().add(nextState);
						nextState.getParentNodes().add(curState);
					}
				}

			}
			depth++;
		}
		ReachGraphInfo reachGraphInfo = ReachGraphInfo.builder()
				.reachGraphMap(reachGraphMap)
				.stateFires(stateFire)
				.stateNotFires(stateNotFire)
				.deadStatesStr(deadStatesStr)
				.deadStates(deadStates)
				.build();
		return reachGraphInfo;
	}

	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(),"reach");
	}

	@Override
	public String execute(AlgReqDO algReqDO) {
		ReachGraphInfo reachGraphInfo = this.generateReachGraph(algReqDO.getPetriDO());
		return reachGraphInfo.toString();
	}

	@Builder
	@Data
	public static class StateKey {
		private int[] marking;
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			StateKey stateKey = (StateKey) o;
			return Arrays.equals(marking, stateKey.marking);
		}
		@Override
		public int hashCode() {
			return Arrays.hashCode(marking);
		}
	}
}
