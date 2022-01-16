package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.EventCircleInfo;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.StateNode;
import edu.xidian.pnaWeb.util.PetriUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description 死锁预测
 * @Author He
 * @Date 2021/12/21 10:56
 */
@Component
public class DeadPredAlg {

	public EventCircleInfo generateReachWithPred(PetriDO petriDO,int step) {
		Map<Integer, StateNode> reachGraphMap = new HashMap<Integer, StateNode>();

		List<List<Integer>> stateFire = new ArrayList<List<Integer>>();
		List<Set<Integer>> stateNotFire = new ArrayList<Set<Integer>>();

		List<String> deadStatesStr = new ArrayList<String>();
		List<StateNode> deadStates = new LinkedList<StateNode>();

		Map<ReachableGraphAlg.StateKey, StateNode> happened = new HashMap<>();
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
		ReachableGraphAlg.StateKey firstKey = ReachableGraphAlg.StateKey.builder().marking(firstState.getState()).build();
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
						ReachableGraphAlg.StateKey nextKey = ReachableGraphAlg.StateKey.builder().marking(nextMarking).build();
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
		return null;
	}
}
