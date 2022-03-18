package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.AlgResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description
 * @Author He
 * @Date 2022/3/18 20:15
 */
public class DeadAlg implements AlgActuator{
	@Override
	public boolean apply(AlgReqDO algReqDO) {

		return StringUtils.equals(algReqDO.getAlgName(),"dead");
	}

	@Override
	public AlgResult execute(AlgReqDO algReqDO) {

		return null;
	}
}
