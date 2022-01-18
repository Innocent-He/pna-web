package edu.xidian.pnaWeb.util;


import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.model.AdminContext;

import java.util.*;

/**
 * @Description
 * @Author He
 * @Date 2021/10/12 16:55
 */
public class PetriUtils {

	/**
	 * 根据后置矩阵以及当前图中的token分布，返回当前可以发射的变迁序号
	 *
	 * @param preMatrix 前置矩阵
	 * @param markings   库所token标识
	 * @return
	 */
	public static List<Integer> getCanFire(int[][] preMatrix, int[] markings) {
		List<Integer> res = new ArrayList<>();
		outer:
		for (int j = 0; j < preMatrix[0].length; j++) {
			for (int i = 0; i < preMatrix.length; i++) {
				if (markings[i] < preMatrix[i][j]) {
					continue outer;
				}
			}
			res.add(j);
		}
		return res;
	}

	/**
	 * 变迁发射，修改各个库所的token值
	 * @param postMatrix 后置变迁
	 * @param preMatrix  前置变迁
	 * @param fireTranId 发射的变迁id
	 * @param markings   库所token标识
	 */
	public static int[] fire(int[][] postMatrix, int[][] preMatrix, int fireTranId, int[] markings) {
		int[] res = Arrays.copyOf(markings, markings.length);
		for (int i = 0; i < postMatrix.length; i++) {
			res[i] += postMatrix[i][fireTranId];
		}
		for (int i = 0; i < preMatrix.length; i++) {
			res[i] -= preMatrix[i][fireTranId];
		}
		return res;
	}

	/**
	 * 获取某个库所所有的前置变迁
	 *
	 * @param placeId 库所序号
	 * @return 所有前置变迁
	 */
	public static List<Integer> getPreTran(PetriDO petriDO,int placeId) {
		List<Integer> trans = new ArrayList<>();
		int[][] preMatrix = petriDO.getPreMatrix();
		for (int i = 0; i < preMatrix[placeId].length; i++) {
			if (preMatrix[placeId][i] != 0) {
				trans.add(i);
			}
		}
		return trans;
	}

	/**
	 * 获取某个库所所有的后置变迁
	 *
	 * @param placeId 库所序号
	 * @return 所有后置变迁
	 */
	public static List<Integer> getPostTran(PetriDO petriDO,int placeId) {
		List<Integer> trans = new ArrayList<>();
		// todo 改为threadlocal
		int[][] postMatrix = petriDO.getPostMatrix();
		for (int i = 0; i < postMatrix[placeId].length; i++) {
			if (postMatrix[placeId][i] != 0) {
				trans.add(i);
			}
		}
		return trans;
	}

	/**
	 * 获取变迁的连通图
	 * @param petriDO
	 * @return
	 */
	public static Map buildTranGraph(PetriDO petriDO) {
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
	public static void findAllTranCircle(Map<Integer,Set<Integer>> postTranGraph) {
		List<List<Integer>> allCircles=new ArrayList<>();
		Deque<Integer> stack=new ArrayDeque<>();
		stack.push(0);
		dfsCircle(stack,allCircles,postTranGraph);
	}

	private static void dfsCircle(Deque<Integer> stack, List<List<Integer>> allCircles, Map<Integer,Set<Integer>> postTranGraph) {
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
