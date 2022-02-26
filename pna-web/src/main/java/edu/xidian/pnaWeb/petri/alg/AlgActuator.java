package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.AlgResult;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 12:00
 */
public interface AlgActuator {
	public boolean apply(AlgReqDO algReqDO);
	public AlgResult execute(AlgReqDO algReqDO);
}
