package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 12:00
 */
public interface AlgActuator {
	public boolean apply(AlgReqDO algReqDO);
	public String execute(AlgReqDO algReqDO);
}
