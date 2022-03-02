package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.AlgResult;

/**
 * @Description 算法模块接口，所有算法必须要实现改接口
 * @Author He
 * @Date 2022/1/17 12:00
 */
public interface AlgActuator {
	/**
	 * 当前模块可以拦截那些请求
	 * @param algReqDO
	 * @return
	 */
	public boolean apply(AlgReqDO algReqDO);

	/**
	 * 执行算法，返回结果
	 * @param algReqDO
	 * @return
	 */
	public AlgResult execute(AlgReqDO algReqDO);
}
