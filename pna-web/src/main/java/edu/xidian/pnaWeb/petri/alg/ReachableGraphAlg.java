package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.exception.BizException;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/12 16:37
 */
@Service
public class ReachableGraphAlg implements AlgActuator {

	public ReachGraphInfo generateReachGraph(PetriDO petriDO) {
		return doGenerateReachGraph(petriDO, Integer.MAX_VALUE, null);
	}

	public ReachGraphInfo generateReachGraphWithStep(PetriDO petriDO, Integer step) {
		if (step == null) {
			return generateReachGraph(petriDO);
		}
		assert step >= 0;
		return doGenerateReachGraph(petriDO, step, null);
	}

	public ReachGraphInfo generateReachGraph(PetriDO petriDO, Integer step, Map<Integer, Set<Integer>> sync) {
		if (step == null) {
			return generateReachGraph(petriDO, sync);
		}
		assert step >= 0;
		return doGenerateReachGraph(petriDO, step, sync);
	}

	public ReachGraphInfo generateReachGraph(PetriDO petriDO, Map<Integer, Set<Integer>> sync) {
		return doGenerateReachGraph(petriDO, Integer.MAX_VALUE, sync);
	}

	private ReachGraphInfo doGenerateReachGraph(PetriDO petriDO, int step, Map<Integer, Set<Integer>> sync) {
		Map<Integer, StateNode> reachGraphMap = new HashMap<Integer, StateNode>();

		List<List<Integer>> stateFire = new ArrayList<>();
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
				List<Set<Integer>> canFires = PetriUtils.getCanFireWithSync(preMatrix, markings, sync);
				Set<Integer> notFires = new HashSet<>();
				notFires.addAll(tranIds);

				// 考虑同步变迁，判断当前发射变迁集是否能满足发射
				Set<Integer> allFires = new HashSet<>();
				for (Set<Integer> canFire : canFires) {
					if (!judgeRealCanFire(canFire, markings, preMatrix, postMatrix)) {
						canFire.clear();
					} else {
						notFires.removeAll(canFire);
					}
					allFires.addAll(canFire);
				}
				stateFire.add(Lists.newArrayList(allFires));
				stateNotFire.add(notFires);

				if (canFires.isEmpty() || canFires.stream().filter(fires -> fires.size() > 0).count() == 0) {
					curState.setDeadlock(true);
					deadStatesStr.add(String.valueOf(curState.getStateNo()));
					deadStates.add(curState);
				}

				if (step != 0) {
					for (Set<Integer> fireTranIds : canFires) {
						if (fireTranIds.size() == 1) {
							for (Integer tranId : fireTranIds) {
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
						} else {
							int[] curMarking = markings;
							for (Integer tranId : fireTranIds) {
								curMarking = PetriUtils.fire(postMatrix, preMatrix, tranId, curMarking);
							}
							StateKey nextKey = StateKey.builder().marking(curMarking).build();
							StateNode nextState = happened.get(nextKey);
							// 判断当前状态是否抵达过
							if (nextState == null) {
								nextState = StateNode.builder()
										.stateNo(stateNo++)
										.depth(depth)
										.state(curMarking)
										.build();
								deque.offer(nextState);
								happened.put(nextKey, nextState);
							}
							curState.getChildNodes().add(nextState);
							nextState.getParentNodes().add(curState);
						}

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

	private boolean judgeRealCanFire(Set<Integer> canFireTrans, int[] markings, int[][] preMatrix, int[][] postMatrix) {
		int[] cur = Arrays.copyOf(markings, markings.length);
		for (Integer tranId : canFireTrans) {
			boolean canFire = PetriUtils.judgeCanFire(preMatrix, tranId, cur);
			if (canFire) {
				cur = PetriUtils.fire(postMatrix, preMatrix, tranId, cur);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), "reach");
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		ReachParam reachParam = extractParams(algReqDO);
		Integer step = reachParam.getStep();
		Map<Integer, Set<Integer>> sync = reachParam.getSync();
		return this.generateReachGraph(algReqDO.getPetriDO(), step, sync);
	}

	private ReachParam extractParams(AlgReqDO algReqDO) {
		try {
			Integer step = (Integer) algReqDO.getParams().get("step");
			String sync = (String) algReqDO.getParams().get("sync");
			String[] split = sync.split("\\|");
			Map<Integer, Set<Integer>> syncMap = new HashMap<>();
			for (String s : split) {
				String[] syncTrans = s.split("-");
				Set<Integer> syncSet = new HashSet<>();
				for (String syncTran : syncTrans) {
					syncSet.add(Integer.parseInt(syncTran)-1);
				}
				for (Integer tranNo : syncSet) {
					syncMap.put(tranNo, syncSet.stream().filter(t -> !t.equals(tranNo)).collect(Collectors.toSet()));
				}
			}
			return ReachParam.builder().step(step).sync(syncMap).build();
		} catch (Exception e) {
			throw new BizException(Constant.REQUEST_PARAM_CODE, Constant.REQUEST_PARAM_MESSAGE);
		}

	}

	@Data
	@Builder
	static class ReachParam {
		private Integer step;
		private Map<Integer, Set<Integer>> sync;
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
