package edu.xidian.pnaWeb.petri.alg;

import com.google.common.collect.Lists;
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
	@Resource
	private EventCircleAlg eventCircleAlg;


	// 事件循环等待信息
	private EventCircleInfo eventCircleInfo;

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
		this.eventCircleInfo = eventCircleAlg.generateEventCircle(petriDO);
		List<List<Integer>> allCircles = eventCircleInfo.getAllCircles();
		PetriGraph petriGraph = PetriUtils.buildPetriGraph(petriDO);
		Map<Integer, Set<Integer>> placePre = petriGraph.getPlacePreGraph();
		Map<Integer, Set<Integer>> tranPre = petriGraph.getTranPreGraph();
		List<DependInfo.EWSDependInfo> dependInfos= Lists.newArrayList();
		for (List<Integer> circle : allCircles) {
			List<Integer> ews = new ArrayList<>();
			for (int i = 0; i < circle.size(); i += 2) {
				ews.add(circle.get(i));
			}
			Map<Integer, List<Set<Integer>>> result = new HashMap<>();
			DependInfo.EWSDependInfo.Builder builder = DependInfo.EWSDependInfo.builder().ews(ews);

			for (int i = 0; i < circle.size(); i++) {
				List<Set<Integer>> initialDepend;
				Integer nodeId = circle.get(i);
				if (isTran(i)) {
					if (tranPre.get(nodeId).size() > 1) {
						findDepend(nodeId, Sets.newHashSet(), false, ews, result);
					}
					if (!CollectionUtils.isEmpty(result.get(nodeId))) {
						builder.addSyncDependTrans(result.get(nodeId),result);
					}
				} else {
					if (placePre.get(nodeId).size() > 1) {
						findDepend(nodeId, Sets.newHashSet(), true, ews, result);
					}
					Set<Integer> directDepend = placePre.get(nodeId)
							.stream()
							.filter(tran -> !ews.contains(tran))
							.collect(Collectors.toSet());
					initialDepend = new ArrayList<>();
					initialDepend.add(directDepend);
					if (!CollectionUtils.isEmpty(initialDepend)) {
						builder.addSelectDependTrans(initialDepend, result);
					}
				}
			}
			DependInfo.EWSDependInfo ewsDependInfo = builder.build();
			dependInfos.add(ewsDependInfo);
		}
		DependInfo dependInfo = new DependInfo();
		dependInfo.setDependInfos(dependInfos);
		return dependInfo;
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
		outer:for (Integer place : tranPreGraph.get(nodeId)) {
			Map<Integer, Set<Integer>> placePreGraph = petriGraph.getPlacePreGraph();
			for (Integer tran : ews) {
				if (placePreGraph.get(place).contains(tran)) continue outer;
			}
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
