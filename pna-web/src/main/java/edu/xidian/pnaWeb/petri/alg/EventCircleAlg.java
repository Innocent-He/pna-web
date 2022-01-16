package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.EventCircleInfo;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.StateNode;
import edu.xidian.pnaWeb.web.model.AdminContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @Author He
 * @Date 2021/10/25 16:00
 */
@Service
@Slf4j
public class EventCircleAlg {
	@Resource
	private ReachableGraphAlg reachableGraphAlg;

	@Resource
	private PetriDomain petriDomain;

	public EventCircleInfo generateEventCircle() {
		AdminContext adminContext = AdminContext.USER_INFO.get();
		ReachGraphInfo reachGraphInfo = adminContext.getReachGraphInfo();
		PetriDO petriDO = adminContext.getPetriDO();
		if (reachGraphInfo == null) {
			reachGraphInfo = reachableGraphAlg.generateReachGraph(petriDO);
		}
		Map<Integer, Set<Integer>> postTranGraph = petriDomain.buildTranGraph(petriDO);
		log.info(postTranGraph.toString());
		List<Set<Integer>> stateNotFires = reachGraphInfo.getStateNotFires();
		Map<Integer, List<List<Integer>>> eventWait = generateEventWait(stateNotFires, postTranGraph);
		Map<Integer, Set<Integer>> deadTran = getStateDeadTran(reachGraphInfo);
		return EventCircleInfo.builder().stateEventCircle(eventWait).deadTran(deadTran).build();
	}

	/**
	 * 获取每个可达状态下的ew结构
	 * @param stateNotFires 每个可达状态下disable的变迁
	 * @return 每个状态下的ew
	 */
	private Map<Integer, List<List<Integer>>> generateEventWait(List<Set<Integer>> stateNotFires, Map<Integer, Set<Integer>> postTranGraph) {
		Map<Integer, List<List<Integer>>> eventWaitCircle = new HashMap<>();
		for (int i = 0; i < stateNotFires.size(); i++) {
			List<List<Integer>> allCircle = new ArrayList<>();
			Set<Integer> viewedTran = new HashSet<>();
			Set<Integer> curNotFires = stateNotFires.get(i);
			for (Integer curNotFire : curNotFires) {
				dfsFindCircle(curNotFires, viewedTran, new ArrayDeque<>(), curNotFire, allCircle, postTranGraph);
			}
			allCircle = allCircle.stream().map(list -> {
				Collections.reverse(list);
				return list;
			}).collect(Collectors.toList());
			eventWaitCircle.put(i, allCircle);
		}
		return eventWaitCircle;
	}

	private void dfsFindCircle(Set<Integer> curNotFires, Set<Integer> viewedTran, Deque<Integer> stack, int curTran, List<List<Integer>> allCircle, Map<Integer, Set<Integer>> postTranGraph) {
		if (stack.contains(curTran)) {
			List<Integer> circle = new ArrayList<>();
			circle.add(curTran);
			for (Integer tran : stack) {
				circle.add(tran);
				if (tran.equals(curTran)) {
					break;
				}
			}
			allCircle.add(circle);
			return;
		}
		if (viewedTran.contains(curTran)) return;
		viewedTran.add(curTran);
		stack.push(curTran);
		for (Integer nextTran : postTranGraph.get(curTran)) {
			if (curNotFires.contains(nextTran)) {
				dfsFindCircle(curNotFires, viewedTran, stack, nextTran, allCircle, postTranGraph);
			}
		}
		stack.pop();
	}



	/**
	 * 获取每个可达状态下的deadT
	 *
	 * @param reachGraphInfo 可达图信息
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


//	private Set<Integer> dfs(StateNode curState, List<Set<Integer>> notFireTrans, Set<Integer> viewed) {
//		if (deadTrans.get(curState.getStateNo()) != null) {
//			return deadTrans.get(curState);
//		}
//		if (viewed.contains(curState.getStateNo())) {
//			Set<Integer> allNotFire = new HashSet<>();
//			for (int i = 0; i < tranGraph.size(); i++) {
//				allNotFire.add(i);
//			}
//			return allNotFire;
//		}
//
//		Set<Integer> curRes = new HashSet<>();
//		Set<Integer> curNotFires = notFireTrans.get(curState.getStateNo());
//		// 如果当前状态没有不可以发射的变迁，则返回空集合
//		// 如果当前状态出现过，则返回空集合
//		if (CollectionUtils.isEmpty(curNotFires)) {
//			return curRes;
//		}
//		viewed.add(curState.getStateNo());
//		Set<Integer> allChildDeadTrans = new HashSet<>();
//		List<StateNode> childNodes = curState.getChildNodes();
//		// 如果当前变迁没有可达状态，那么就将它当前所有的不可发射变迁返回
//		if (childNodes == null) {
//			return notFireTrans.get(curState.getStateNo());
//		}
//		// 拿到所有子状态的不可发射变迁列表
//		for (StateNode childNode : childNodes) {
//			Set<Integer> notFires = dfs(childNode, notFireTrans, viewed);
//			if (notFires != null) {
//				allChildDeadTrans.addAll(notFires);
//			}
//		}
//
//		for (Integer curNotFire : curNotFires) {
//			if (allChildDeadTrans.contains(curNotFire)) {
//				curRes.add(curNotFire);
//			}
//		}
//		deadTrans.put(curState.getStateNo(), curRes);
//		return curRes;
//	}
}
