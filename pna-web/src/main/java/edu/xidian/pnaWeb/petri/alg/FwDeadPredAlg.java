package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author He
 * @Date 2022/2/17 20:00
 */
@Component
public class FwDeadPredAlg implements AlgActuator{
	@Resource
	private EventCircleAlg eventCircleAlg;
	/**
	 * 可达图序号
	 */
	private int stateNo = 1;
	/**
	 * 存放每个状态序号及其详情的map
	 */
	private Map<Integer, StateNode> reachGraphMap;
	/**
	 * 存放每个状态下可以发射的变迁集合
	 */
	private List<List<Integer>> stateFire;
	/**
	 * 存放每个状态下不能发射的变迁集合
	 */
	private List<Set<Integer>> stateNotFire;
	/**
	 * 存放所有死区标识的字符串
	 */
	private List<String> deadStatesStr;
	/**
	 * 存放所有死区标识
	 */
	private List<StateNode> deadStates;
	/**
	 * 每个状态唯一标识key，及其状态详情
	 */
	private Map<ReachableGraphAlg.StateKey, StateNode> happened;
	/**
	 * 事件循环等待信息
	 */
	private EventCircleInfo eventCircleInfo;



	public DeadPredInfo generateReachWithPred(PetriDO petriDO) {
		this.reachGraphMap = new HashMap<Integer, StateNode>();
		this.stateFire = new ArrayList<List<Integer>>();
		this.stateNotFire = new ArrayList<Set<Integer>>();
		this.deadStatesStr = new ArrayList<String>();
		this.deadStates = new LinkedList<StateNode>();
		this.happened = new HashMap<>();
		this.eventCircleInfo = eventCircleAlg.generateEventCircle(petriDO);

		List<List<Integer>> allCircles = eventCircleInfo.getAllCircles();
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		Map<Integer, Set<Integer>> placePre = petriGraph.getPlacePreGraph();
		Map<Integer, Set<Integer>> tranPre = petriGraph.getTranPreGraph();

		for (List<Integer> circle : allCircles) {
			for (int i = 0; i < circle.size(); i++) {
				if (i % 2 == 0) {
					if (tranPre.get(circle.get(i)).size() > 1) {

					}
				}
			}
		}


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
		reach(petriDO, deque);
		return deadPredInfo;
	}

	/**
	 * @param petriDO petri网数据
	 * @param deque   当前状态跃迁的队列
	 * @return 如果ews重新有了活性则返回true
	 */
	private boolean reach(PetriDO petriDO, Deque<StateNode> deque) {
		int[][] postMatrix = petriDO.getPostMatrix();
		int[][] preMatrix = petriDO.getPreMatrix();
		int trans = postMatrix[0].length;
		List<Integer> tranIds = IntStream
				.range(0, trans)
				.collect(
				ArrayList::new,
				ArrayList::add,
				ArrayList::addAll);
		Set<ReachableGraphAlg.StateKey> view=new HashSet<>();
		while (!deque.isEmpty()) {
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
				methodFill(notFires,petriDO);

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
		return false;
	}



	private void methodFill(Set<Integer> notFires,PetriDO petriDO) {
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		Map<Integer, Set<Integer>> placePreGraph = petriGraph.getPlacePreGraph();
		Map<Integer, Set<Integer>> tranPreGraph = petriGraph.getTranPreGraph();
		List<List<Integer>> allInactiveEws = findAllInactiveEws(notFires, eventCircleInfo);

		for (List<Integer> inactiveEw : allInactiveEws) {
			Set<Integer> trans=Sets.newHashSet();
			Set<Integer> places=Sets.newHashSet();
			for (int i = 0; i < inactiveEw.size(); i++) {
				if (i % 2 == 0) {
					trans.add(inactiveEw.get(i));
				}else{
					places.add(inactiveEw.get(i));
				}
			}
			for (Integer tran : trans) {
				Set<Integer> prePlaces = tranPreGraph.get(tran);
				if (prePlaces != null && prePlaces.size() > 1) {
					Set<Integer> mustPlaces=prePlaces.stream().filter(p->{
						for (Integer place : places) {
							if (Objects.equals(p, place)) {
								return false;
							}
						}
						return true;
					}).collect(Collectors.toSet());
				}
			}
			for (Integer place : places) {
				Set<Integer> preTrans = placePreGraph.get(place);
				if (preTrans != null && preTrans.size() > 1) {
					Set<Integer> orTrans = preTrans.stream().filter(t -> {
						for (Integer tran : trans) {
							if (Objects.equals(t, tran)) {
								return false;
							}
						}
						return true;
					}).collect(Collectors.toSet());
					Set<Integer>[] dependFireTrans=dfs(placePreGraph,
							tranPreGraph,
							places,
							trans,
							new ArrayDeque<>(),
							orTrans,
							notFires,
							false);
				}
			}
		}
	}

	private Set<Integer>[] dfs(Map<Integer, Set<Integer>> placePreGraph,
							 Map<Integer, Set<Integer>> tranPreGraph,
							 Set<Integer> internalPlaces,
							 Set<Integer> internalTrans,
							 Deque<Integer> path,
							 Set<Integer> nodes,
							 Set<Integer> notFires,
							 boolean isPlace) {
		if (isPlace) {
			Set<Integer> allMustTrans = Sets.newHashSet();
			Set<Integer> allOrTrans = Sets.newHashSet();
			for (Integer place : nodes) {
				if (internalPlaces.contains(place)) return new HashSet[2];
				Set<Integer>[] dependTrans =dfs(placePreGraph,
						tranPreGraph,
						internalPlaces,
						internalTrans,
						path,
						placePreGraph.get(place),
						notFires,
						false);
				if (CollectionUtils.isEmpty(dependTrans[0])
						||CollectionUtils.isEmpty(dependTrans[1])) {
					return new HashSet[2];
				}

			}
		}else{
			for (Integer tran : nodes) {
				if (!notFires.contains(tran)) {

				}
			}
		}
		return null;
	}

	//	拿到当前状态下所有处于ew的环路
	private List<List<Integer>> findAllInactiveEws(Set<Integer> stateNotFire, EventCircleInfo eventCircleInfo) {
		List<List<Integer>> res = Lists.newArrayList();
		List<List<Integer>> allCircles = eventCircleInfo.getAllCircles();
		for (List<Integer> circle : allCircles) {
			List<Integer> ew=new ArrayList<>();
			for (int id = 0; id < circle.size(); id+=2) {
				ew.add(circle.get(id));
			}
			if (stateNotFire.containsAll(ew)) {
				res.add(circle);
			}
		}
		return res;
	}

	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), "deadPred");
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		return this.generateReachWithPred(algReqDO.getPetriDO());
	}
}
