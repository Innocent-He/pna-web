package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.SimphonInfo;
import edu.xidian.pnaWeb.util.PetriUtils;
import edu.xidian.pnaWeb.web.model.AdminContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/24 21:32
 */
@Service
public class SimphonAlg {
	public SimphonInfo generateSimphon(){
		List<List<Integer>> simphons=new ArrayList<>();
		List<List<Integer>> strictSimphons=new ArrayList<>();
		PetriDO petriDO = AdminContext.USER_INFO.get().getPetriDO();
		List<List<Integer>> subSets = generateSubset(petriDO.getMarking().length);
		for (List<Integer> subSet : subSets) {
			Set<Integer> preTran=new HashSet<>();
			Set<Integer> postTran=new HashSet<>();
			for (Integer placeId : subSet) {
				postTran.addAll(PetriUtils.getPostTran(placeId));
				preTran.addAll(PetriUtils.getPreTran(placeId));
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
}
