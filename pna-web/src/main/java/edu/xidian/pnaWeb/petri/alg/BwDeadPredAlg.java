package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description 多步向后死锁预测算法
 * @Author He
 * @Date 2021/12/21 10:56
 */
@Component
@Slf4j
public class BwDeadPredAlg implements AlgActuator {
	@Resource
	private EventCircleAlg eventCircleAlg;
	//	可达图序号
	private int stateNo = 1;
	//存放每个状态序号及其详情的map
	private Map<Integer, StateNode> reachGraphMap;
	//存放每个状态下可以发射的变迁集合
	private List<List<Integer>> stateFire;
	//	存放每个状态下不能发射的变迁集合
	private List<Set<Integer>> stateNotFire;
	// 存放所有死区标识的字符串
	private List<String> deadStatesStr;
	// 存放所有死区标识
	private List<StateNode> deadStates;
	// 每个状态唯一标识key，及其状态详情
	private Map<ReachableGraphAlg.StateKey, StateNode> happened;
	// 事件循环等待信息
	private EventCircleInfo eventCircleInfo;

	public DeadPredInfo generateReachWithPred(PetriDO petriDO) {
		this.reachGraphMap = new HashMap<Integer, StateNode>();
		this.stateFire = new ArrayList<List<Integer>>();
		this.stateNotFire = new ArrayList<Set<Integer>>();
		this.deadStatesStr = new ArrayList<String>();
		this.deadStates = new LinkedList<StateNode>();
		this.happened = new HashMap<>();
		this.eventCircleInfo = eventCircleAlg.generateEventCircle(petriDO);
		Deque<StateNode> deque = new ArrayDeque<>();
		StateNode firstState = StateNode.builder()
				.state(petriDO.getMarking())
				.depth(0)
				.stateNo(0)
				.build();
		deque.offerFirst(firstState);
		ReachableGraphAlg.StateKey firstKey = ReachableGraphAlg.StateKey
				.builder()
				.marking(firstState.getState())
				.build();
		happened.put(firstKey, firstState);
		DeadPredInfo deadPredInfo = new DeadPredInfo();
		reach(petriDO, deque, null,deadPredInfo);
		return deadPredInfo;
	}

	/**
	 * @param petriDO petri网数据
	 * @param deque   当前状态跃迁的队列
	 * @param ews     当前正在判断的ews
	 * @return 如果ews重新有了活性则返回true
	 */
	private boolean reach(PetriDO petriDO, Deque<StateNode> deque, List<Integer> ews,DeadPredInfo deadPredInfo) {
		int[][] postMatrix = petriDO.getPostMatrix();
		int[][] preMatrix = petriDO.getPreMatrix();
		int trans = postMatrix[0].length;
		List<Integer> tranIds = IntStream.range(0, trans).collect(
				ArrayList::new,
				ArrayList::add,
				ArrayList::addAll);
		Set<ReachableGraphAlg.StateKey> view=new HashSet<>();
		reach:while (!deque.isEmpty()) {
			int size = deque.size();
			for (int i = 0; i < size; i++) {
				StateNode curState = deque.pollLast();

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
				if (Objects.isNull(ews)) {
					List<List<Integer>> allInactiveEws = findAllInactiveEws(notFires, eventCircleInfo);
					if (!CollectionUtils.isEmpty(allInactiveEws)) {
						for (List<Integer> inactiveEw : allInactiveEws) {
							Deque<StateNode> ewsDeque = new ArrayDeque<>();
							ewsDeque.offer(curState);
							boolean isActive = reach(petriDO, ewsDeque, new ArrayList<>(inactiveEw),null);
							if (!isActive) {
								List<DeadPredInfo.PredInfo> predInfos = deadPredInfo.getPredInfos();
								DeadPredInfo.PredInfo predInfo = DeadPredInfo.PredInfo.builder()
										.stateNode(curState)
										.ews(inactiveEw)
										.build();
								predInfos.add(predInfo);
								continue reach;
							}
						}
					}
				} else {
					// 后续可达状态下如果该ews又有了活性，则直接返回true
					ews.removeIf(canFires::contains);
					if (ews.isEmpty()) return true;
				}
				for (Integer tranId : canFires) {
					int[] nextMarking = PetriUtils.fire(postMatrix, preMatrix, tranId, markings);
					ReachableGraphAlg.StateKey nextKey = ReachableGraphAlg.StateKey
							.builder()
							.marking(nextMarking)
							.build();
					StateNode nextState = happened.get(nextKey);
					// 判断当前状态是否抵达过
					if (Objects.isNull(nextState)) {
						nextState = StateNode.builder()
								.stateNo(stateNo++)
								.state(nextMarking)
								.build();
						happened.put(nextKey,nextState);
					}
					if (!view.contains(nextKey)) {
						deque.offerFirst(nextState);
						view.add(nextKey);
					}
					curState.getChildNodes().add(nextState);
					nextState.getParentNodes().add(curState);
				}

			}
		}
		if (Objects.isNull(ews)) {
			reachGraphMap.forEach((key,value)->{
				log.info(key+"========>"+value+"\n");
			});
		}
		return false;
	}

	//	拿到当前状态下所有处于ew的极小ews
	private List<List<Integer>> findAllInactiveEws(Set<Integer> stateNotFire, EventCircleInfo eventCircleInfo) {
		List<List<Integer>> res = Lists.newArrayList();
		List<List<Integer>> allEws = eventCircleInfo.getAllEws();
		allEws.forEach(ew->{
			boolean flag = stateNotFire.containsAll(ew);
			if (flag) res.add(ew);
		});
		return res;
	}

	@Override
	public boolean apply(AlgReqDO algReqDO) {
		if (StringUtils.equals(algReqDO.getAlgName(), "bwDeadPred")) {
			return true;
		}
		return false;
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		return this.generateReachWithPred(algReqDO.getPetriDO());
	}
}
