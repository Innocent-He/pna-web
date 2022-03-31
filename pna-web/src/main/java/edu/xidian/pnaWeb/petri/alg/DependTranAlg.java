package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2022/2/23 20:44
 */
@Component
public class DependTranAlg implements AlgActuator {
	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), "fwDeadPred");
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		return method(algReqDO.getPetriDO());
	}

	private PetriGraph petriGraph;

	public AlgResult method(PetriDO petriDO) {
		this.petriGraph = PetriUtils.buildPetriGraph(petriDO);
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		Map<Integer, Set<Integer>> tranPre = petriGraph.getTranPreGraph();
		List<DependInfo.EWSDependInfo> dependInfos = Lists.newArrayList();
		Set<Integer> multiPreTran = Sets.newHashSet();
		tranPre.forEach((key, places) -> {
			if (places.size() > 1) multiPreTran.add(key);
		});
		Map<Integer, List<Set<Integer>>> directDepends = new HashMap<>();
		for (Integer tranId : multiPreTran) {
			findDepend(tranId, false, directDepends, Maps.newHashMap());
		}
		directDepends.forEach((key, value) -> {
			System.out.println(key);
			for (Set<Integer> trans : value) {
				System.out.println(trans);
			}
			System.out.println("=================");
		});
		Map<Integer, Set<Integer>> tranPreGraph = petriGraph.getTranPreGraph();
		tranPreGraph.forEach((tranId, prePlaces) -> {
			if (prePlaces.size() > 1) {
				Set<Integer> view = Sets.newHashSet();
				view.add(tranId);
				findDepends(Lists.newArrayList(tranId), directDepends, view);
			}
		});

		DependInfo dependInfo = new DependInfo();
		dependInfo.setDependInfos(dependInfos);
		return dependInfo;
	}

	private void findDepends(List<Integer> tranIds, Map<Integer, List<Set<Integer>>> directDepends, Set<Integer> path) {
		List<List<Set<Integer>>> allDependTrans = Lists.newArrayList();
		List<List<List<Set<Integer>>>> realDependTrans = Lists.newArrayList();
		combineReplace(directDepends, tranIds, 0, allDependTrans, realDependTrans);
		for (List<List<Set<Integer>>> realDependTran : realDependTrans) {
			List<List<Integer>> result = Lists.newArrayList();
			findCombineResult(realDependTran, path, result, 0, Sets.newHashSet());
			result=rmDuplicate(result);
			for (List<Integer> trans : result) {
				System.out.println(trans);
				path.addAll(trans);
				findDepends(trans, directDepends, path);
				path.removeAll(trans);
			}
		}
	}

	private void combineReplace(Map<Integer, List<Set<Integer>>> directDepends, List<Integer> tranIds, int idx, List<List<Set<Integer>>> allDependTrans, List<List<List<Set<Integer>>>> realDependTrans) {
		if (idx == tranIds.size()) {
			realDependTrans.add(Lists.newArrayList(allDependTrans));
			return;
		}
		Integer tranId = tranIds.get(idx);
		List<Set<Integer>> dependTrans = directDepends.get(tranId);
		List<Set<Integer>> replaceResult = Lists.newArrayList();
		replaceTrans(dependTrans, 0, replaceResult, Sets.newHashSet());
		int i = 0;
		while (i < 2) {
			if (i++ == 0) {
				List<Set<Integer>> temp = Lists.newArrayList();
				temp.add(Sets.newHashSet(tranId));
				allDependTrans.add(temp);
				combineReplace(directDepends, tranIds, idx + 1, allDependTrans, realDependTrans);
				allDependTrans.remove(temp);
			} else {
				allDependTrans.add(replaceResult);
				combineReplace(directDepends, tranIds, idx + 1, allDependTrans, realDependTrans);
				allDependTrans.remove(replaceResult);
			}
		}

	}

	/**
	 * 去除重复列表
	 * @param result
	 * @return
	 */
	private List<List<Integer>> rmDuplicate(List<List<Integer>> result) {
		List<List<Integer>> all = Lists.newArrayList();
		return result.stream()
				.map(el -> el.stream().sorted().collect(Collectors.toList()))
				.filter(el -> {
					boolean exist = all.stream().anyMatch(com -> com.equals(el));
					if (!exist) {
						all.add(el);
						return true;
					}
					return false;
				})
				.collect(Collectors.toList());
	}

	/**
	 * @param allDependTrans 所有变迁的直接依赖事件集列表
	 * @param view           已经替换过的变迁集
	 * @param result         经过替换之后的依赖变迁集列表
	 * @param idx            当前对第几个变迁进行依赖事件集替换
	 * @param preResult      目前的替换结果
	 * @description 对当前事件列表进行依赖事件集的替换
	 */
	private void findCombineResult(List<List<Set<Integer>>> allDependTrans, Set<Integer> view, List<List<Integer>> result, Integer idx, Set<Integer> preResult) {
		if (idx == allDependTrans.size() && !preResult.isEmpty()) {
			result.add(Lists.newArrayList(preResult));
			return;
		}
		for (Set<Integer> trans : allDependTrans.get(idx)) {
			if (Sets.intersection(trans, view).isEmpty()) {
				if (Sets.intersection(trans, preResult).isEmpty()) {
					preResult.addAll(trans);
					findCombineResult(allDependTrans, view, result, idx + 1, preResult);
					preResult.removeAll(trans);
				}
			}
		}
	}


	private void replaceTrans(List<Set<Integer>> dependTrans, Integer tranIdx, List<Set<Integer>> result, Set<Integer> path) {
		if (tranIdx == dependTrans.size()) {
			result.add(Sets.newHashSet(path));
			return;
		}
		for (Integer dependTran : dependTrans.get(tranIdx)) {
			if (path.contains(dependTran)) continue;
			path.add(dependTran);
			replaceTrans(dependTrans, tranIdx + 1, result, path);
			path.remove(dependTran);
		}
	}


	public Set<Integer> findDepend(Integer nodeId, boolean isPlace, Map<Integer, List<Set<Integer>>> result, Map<Integer, Set<Integer>> placeViewed) {
		if (isPlace) {
			Map<Integer, Set<Integer>> placePre = petriGraph.getPlacePreGraph();
			Set<Integer> trans = Sets.newHashSet();
			placeViewed.put(nodeId, trans);
			for (Integer tran : placePre.get(nodeId)) {
				trans.add(tran);
				if (!result.containsKey(tran)) {
					findDepend(tran, false, result, placeViewed);
				}
			}
			return trans;
		}
		List<Set<Integer>> curRes = Lists.newArrayList();
		result.put(nodeId, curRes);
		Map<Integer, Set<Integer>> tranPreGraph = petriGraph.getTranPreGraph();
		for (Integer place : tranPreGraph.get(nodeId)) {
			Set<Integer> dependTran = null;
			if (placeViewed.containsKey(place)) {
				dependTran = placeViewed.get(place);
			} else {
				dependTran = findDepend(place, true, result, placeViewed);
			}
			if (!CollectionUtils.isEmpty(dependTran)) {
				curRes.add(dependTran);
			}
		}
		return null;
	}


}
