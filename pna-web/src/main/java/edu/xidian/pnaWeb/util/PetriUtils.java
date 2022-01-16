package edu.xidian.pnaWeb.util;


import edu.xidian.pnaWeb.web.model.AdminContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static List<Integer> getPreTran(int placeId) {
		List<Integer> trans = new ArrayList<>();
		AdminContext adminContext = AdminContext.USER_INFO.get();
		int[][] preMatrix = adminContext.getPetriDO().getPreMatrix();
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
	public static List<Integer> getPostTran(int placeId) {
		List<Integer> trans = new ArrayList<>();
		AdminContext adminContext = AdminContext.USER_INFO.get();
		int[][] postMatrix = adminContext.getPetriDO().getPostMatrix();
		for (int i = 0; i < postMatrix[placeId].length; i++) {
			if (postMatrix[placeId][i] != 0) {
				trans.add(i);
			}
		}
		return trans;
	}
}
