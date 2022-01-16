package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description
 * @Author He
 * @Date 2021/12/21 11:01
 */
@Component
public class PetriDomain {

	/**
	 * 获取变迁的连通图
	 * @param petriDO
	 * @return
	 */
	public Map buildTranGraph(PetriDO petriDO) {
		int[][] postMatrix = petriDO.getPostMatrix(), preMatrix = petriDO.getPreMatrix();
		Map<Integer, Set<Integer>> postGraph = new HashMap<>();
		for (int j = 0; j < postMatrix[0].length; j++) {
			List<Integer> curGraph = new ArrayList<>();
			for (int i = 0; i < postMatrix.length; i++) {
				if (postMatrix[i][j] != 0) {
					curGraph.add(i);
				}
			}
			for (Integer placeIdx : curGraph) {
				for (int tranIdx = 0; tranIdx < preMatrix[placeIdx].length; tranIdx++) {
					if (preMatrix[placeIdx][tranIdx] != 0) {
						postGraph.computeIfAbsent(j, (e) -> new HashSet<>()).add(tranIdx);
					}
				}
			}
		}
		return postGraph;
	}

	/**
	 * 找到Petri网中所有由变迁构成的环路
	 */
	public void findAllTranCircle(Map<Integer,Set<Integer>> postTranGraph) {
		List<List<Integer>> allCircles=new ArrayList<>();
		Deque<Integer> stack=new ArrayDeque<>();
		stack.push(0);
		dfsCircle(stack,allCircles,postTranGraph);
	}

	private void dfsCircle(Deque<Integer> stack, List<List<Integer>> allCircles, Map<Integer,Set<Integer>> postTranGraph) {
		Integer peek = stack.peek();
		for (Integer postTran : postTranGraph.get(peek)) {
			if (stack.contains(postTran)) {
				List<Integer> circle=new ArrayList<>();
				for (Integer tran : stack) {
					circle.add(tran);
					if (tran==postTran) break;
				}
				allCircles.add(circle);
			}else{
				stack.push(postTran);
				dfsCircle(stack,allCircles,postTranGraph);
				stack.pop();
			}
		}
	}
}
