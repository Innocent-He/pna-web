package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.AlgResult;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.SimphonInfo;
import edu.xidian.pnaWeb.petri.util.PetriUtils;
import edu.xidian.pnaWeb.web.enums.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/24 21:32
 */
@Service
public class SimphonAlg implements AlgActuator{
	public SimphonInfo generateSimphon(PetriDO petriDO){
		List<List<Integer>> simphons=new ArrayList<>();
		List<List<Integer>> strictSimphons=new ArrayList<>();
		List<List<Integer>> subSets = generateSubset(petriDO.getMarking().length);
		for (List<Integer> subSet : subSets) {
			Set<Integer> preTran=new HashSet<>();
			Set<Integer> postTran=new HashSet<>();
			for (Integer placeId : subSet) {
				postTran.addAll(PetriUtils.getPostTran(petriDO,placeId));
				preTran.addAll(PetriUtils.getPreTran(petriDO,placeId));
			}
			if (postTran.containsAll(preTran)) {
				simphons.add(subSet);
				subSet=subSet.stream().map(e->e+1).collect(Collectors.toList());
				if (postTran.size() > preTran.size()) {
					strictSimphons.add(subSet);
				}
			}
		}
		SimphonInfo simphonInfo = SimphonInfo.builder().simphons(simphons).strictSimphons(strictSimphons).build();
		return simphonInfo;
	}

	private List<List<Integer>> generateSubset(int n) {
		List<List<Integer>> res=new ArrayList<>();
		dfs(res,0,n,new ArrayDeque<>());
		res.remove(0);
		return res;
	}

	private void dfs(List<List<Integer>> res, int curIdx, int n, Deque<Integer> queue) {
		if (curIdx == n) {
			res.add(new ArrayList<>(queue));
			return;
		}
		dfs(res,curIdx+1,n,queue);
		queue.push(curIdx);
		dfs(res,curIdx+1,n,queue);
		queue.pop();
	}



	@Override
	public boolean apply(AlgReqDO algReqDO) {
		return StringUtils.equals(algReqDO.getAlgName(), Constant.Siphon);
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {
		return this.generateSimphon(algReqDO.getPetriDO());
	}
}
