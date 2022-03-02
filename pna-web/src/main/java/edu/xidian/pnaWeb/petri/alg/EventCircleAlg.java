package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import edu.xidian.pnaWeb.web.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 获取到所有极小EWS
 * @Author He
 * @Date 2021/10/25 16:00
 */
@Service
@Slf4j
public class EventCircleAlg implements AlgActuator {
	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), Constant.EVENT_CIRCLE);
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		return this.generateEventCircle(algReqDO.getPetriDO());
	}

	public EventCircleInfo generateEventCircle(PetriDO petriDO) {
		Map<Integer, Set<Integer>> postTranGraph = PetriUtils.buildTranGraph(petriDO);
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		List<List<Integer>> allCircles = findAllCircle(petriGraph);
		List<List<Integer>> allEwsList = findAllEws(postTranGraph);
		List<List<Integer>> allMinEwsList = findAllMinEws(allEwsList);
		return EventCircleInfo.builder()
				.allCircles(allCircles)
				.allEws(allEwsList)
				.allMinEws(allMinEwsList)
				.build();
	}

	private List<List<Integer>> findAllCircle(PetriGraph petriGraph) {
		Set<Integer> viewed = new HashSet<>();
		List<List<Integer>> allEws = new ArrayList<>();
		for (int tranId = 0; tranId < petriGraph.getTranPreGraph().size(); tranId++) {
			if (!viewed.contains(tranId)) {
				dfsFindCircle(viewed, new ArrayDeque<>(), tranId, allEws, petriGraph, true);
			}
		}
		return allEws;
	}

	private List<List<Integer>> findAllMinEws(List<List<Integer>> allEwsList) {
		List<List<Integer>> minEws = new ArrayList<>(allEwsList);
		minEws.sort((a, b) -> b.size() - a.size());
		Iterator<List<Integer>> iterator = minEws.iterator();
		while (iterator.hasNext()) {
			List<Integer> curEws = iterator.next();
			// 如果存在一个EWS和当前EWS的元素存在交集，则将当前ews删除
			boolean flag = minEws.stream().anyMatch(ews -> {
				if (ews != curEws) {
					return ews.stream().anyMatch(curEws::contains);
				}
				return false;
			});
			if (flag) iterator.remove();
		}
		return minEws;
	}

	private List<List<Integer>> findAllEws(Map<Integer, Set<Integer>> postTranGraph) {
		Set<Integer> viewed = new HashSet<>();
		List<List<Integer>> allEws = new ArrayList<>();
		for (Integer tranId : postTranGraph.keySet()) {
			if (!viewed.contains(tranId)) {
				dfsFindCircle(viewed, new ArrayDeque<>(), tranId, allEws, postTranGraph);
			}
		}
		return allEws;
	}


	private void dfsFindCircle(Set<Integer> viewedTran, Deque<Integer> stack, int curTran, List<List<Integer>> allCircle, Map<Integer, Set<Integer>> postTranGraph) {
		viewedTran.add(curTran);
		if (stack.contains(curTran)) {
			stack.push(curTran);
			List<Integer> circle = new ArrayList<>(stack);
			dealCircle(circle);
			allCircle.add(circle);
			stack.pop();
			return;
		}
		stack.push(curTran);
		for (Integer nextTran : postTranGraph.get(curTran)) {
			dfsFindCircle(viewedTran, stack, nextTran, allCircle, postTranGraph);
		}
		stack.pop();
	}

	private void dfsFindCircle(Set<Integer> viewed, Deque<Integer> stack, int curId, List<List<Integer>> allCircle, PetriGraph petriGraph, boolean isTran) {
		if (isTran) viewed.add(curId);
		if (stack.contains(curId)) {
			stack.push(curId);
			List<Integer> circle = new ArrayList<>(stack);
			if (isTran) {
				dealCircle(circle);

			}else{
				dealCircle(circle);
				circle.remove(0);
				circle.add(circle.get(0));
			}
			List<Integer> result = circle.stream().map(id -> {
				if (id >= petriGraph.getTranPostGraph().size()) {
					return id - petriGraph.getTranPostGraph().size();
				}
				return id;
			}).collect(Collectors.toList());
			allCircle.add(result);
			stack.pop();
			return;
		}
		stack.push(curId);
		Map<Integer, Set<Integer>> postGraph;
		if (isTran) {
			postGraph = petriGraph.getTranPostGraph();
			for (Integer nextPlace : postGraph.get(curId)) {
				dfsFindCircle(viewed, stack, nextPlace + postGraph.size(), allCircle, petriGraph, false);
			}
		} else {
			postGraph = petriGraph.getPlacePostGraph();
			for (Integer nextTran : postGraph.get(curId - petriGraph.getTranPostGraph().size())) {
				dfsFindCircle(viewed, stack, nextTran, allCircle, petriGraph, true);
			}
		}
		stack.pop();
	}

	private void dealCircle(List<Integer> circle) {
		int left = 0, right = circle.size() - 1;
		while (!Objects.equals(circle.get(left), circle.get(right))) {
			circle.remove(right--);
		}
		Collections.reverse(circle);
	}


	/**
	 * 获取每个可达状态下的deadT
	 *
	 * @param reachGraphInfo 可达图
	 * @return 可达状态序号与它所有的deadT列表映射
	 */
	private Map<Integer, Set<Integer>> getStateDeadTran(ReachGraphInfo reachGraphInfo) {
		Map<Integer, StateNode> reachGraphMap = reachGraphInfo.getReachGraphMap();
		List<Set<Integer>> notFireTrans = reachGraphInfo.getStateNotFires();
		//每个状态下的deadTran
		Map<Integer, Set<Integer>> deadTrans = new HashMap<>();

		for (int i = 0; i < notFireTrans.size(); i++) {
			Set<Integer> viewed = new HashSet<>();
			Set<Integer> curDeadTrans = new HashSet<>();
			curDeadTrans.addAll(notFireTrans.get(i));
			Deque<StateNode> stack = new ArrayDeque<>();
			stack.push(reachGraphMap.get(i));
			while (!stack.isEmpty()) {
				if (curDeadTrans.isEmpty()) break;
				StateNode childState = stack.pop();
				int childNo = childState.getStateNo();
				if (!viewed.contains(childNo)) {
					viewed.add(childNo);
					Set<Integer> childNotFires = notFireTrans.get(childNo);
					Set<Integer> removeTrans = new HashSet<>();
					for (Integer deadTran : curDeadTrans) {
						if (!childNotFires.contains(deadTran)) {
							removeTrans.add(deadTran);
						}
					}
					curDeadTrans.removeAll(removeTrans);
					if (childState.getChildNodes() != null) {
						for (StateNode childNode : childState.getChildNodes()) {
							stack.push(childNode);
						}
					}
				}

			}
			if (!curDeadTrans.isEmpty()) {
				deadTrans.put(i, curDeadTrans);
			}
		}
		return deadTrans;
	}


}
