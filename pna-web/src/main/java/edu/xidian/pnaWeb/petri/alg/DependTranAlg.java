package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Sets;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import lombok.Builder;
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
	@Resource
	private EventCircleAlg eventCircleAlg;


	// 事件循环等待信息
	private EventCircleInfo eventCircleInfo;

	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), "test");
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		method(algReqDO.getPetriDO());
		return null;
	}

	private PetriGraph petriGraph;

	public void method(PetriDO petriDO) {
		this.petriGraph = PetriUtils.buildPetriGraph(petriDO);
		this.eventCircleInfo = eventCircleAlg.generateEventCircle(petriDO);
		List<List<Integer>> allCircles = eventCircleInfo.getAllCircles();
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		Map<Integer, Set<Integer>> placePre = petriGraph.getPlacePreGraph();
		Map<Integer, Set<Integer>> tranPre = petriGraph.getTranPreGraph();
		for (List<Integer> circle : allCircles) {
			List<Integer> ews = new ArrayList<>();
			for (int i = 0; i < circle.size(); i += 2) {
				ews.add(circle.get(i));
			}
			for (int i = 0; i < circle.size(); i++) {
				Map<Integer, List<Set<Integer>>> result = new HashMap<>();
				List<Set<Integer>> initialDepend;
				if (isTran(i)) {
					if (tranPre.get(circle.get(i)).size() > 1) {
						findDepend(circle.get(i), Sets.newHashSet(), false, ews, result);
					}
					initialDepend = result.get(circle.get(i));
				} else {
					if (placePre.get(circle.get(i)).size() > 1) {
						findDepend(circle.get(i), Sets.newHashSet(), true, ews, result);
					}
					Set<Integer> directDepend = placePre.get(circle.get(i))
							.stream()
							.filter(tran -> !ews.contains(tran))
							.collect(Collectors.toSet());
					initialDepend = new ArrayList<>();
					initialDepend.add(directDepend);
				}
				if (!CollectionUtils.isEmpty(result)) {
					DependTransInfo.builder()
							.initialDependTrans(initialDepend)
							.dependTrans(result)
							.node(circle.get(i))
							.ews(ews);
				}
			}
		}
	}

	@Builder
	static class DependTransInfo {
		private List<Set<Integer>> initialDependTrans;
		private List<Integer> ews;
		private Integer node;
		private Map<Integer, List<Set<Integer>>> dependTrans;
	}

	public Set<Integer> findDepend(Integer nodeId, Set<Integer> path, boolean isPlace, List<Integer> ews, Map<Integer, List<Set<Integer>>> result) {
		if (isPlace) {
			Map<Integer, Set<Integer>> placePre = petriGraph.getPlacePreGraph();
			Set<Integer> trans = Sets.newHashSet();
			for (Integer tran : placePre.get(nodeId)) {
				if (path.contains(tran) || ews.contains(tran)) continue;
				trans.add(tran);
				path.add(tran);
				findDepend(tran, path, false, ews, result);
				path.remove(tran);
			}
			return trans;
		}
		List<Set<Integer>> curRes = new ArrayList<>();
		Map<Integer, Set<Integer>> tranPreGraph = petriGraph.getTranPreGraph();
		for (Integer place : tranPreGraph.get(nodeId)) {
			Set<Integer> dependTran = findDepend(place, path, true, ews, result);
			// 如果变迁的几个库所的前置前边存在“违规”则跳过
			if (CollectionUtils.isEmpty(dependTran)) return null;
			curRes.add(dependTran);
		}
		result.put(nodeId, curRes);
		return null;
	}

	private boolean isTran(int i) {
		return i % 2 == 0;
	}
}
