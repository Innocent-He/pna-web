package edu.xidian.pnaWeb.web.model;

import edu.xidian.pnaWeb.petri.module.EventCircleInfo;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.SimphonInfo;
import lombok.Data;

/**
 * @Description
 * @Author He
 * @Date 2021/10/23 15:50
 */
@Data
public class AdminContext {
	public static final InheritableThreadLocal<AdminContext> USER_INFO=new InheritableThreadLocal<>();
	private PetriDO petriDO;
	private ReachGraphInfo reachGraphInfo;
	private SimphonInfo simphonInfo;
	private EventCircleInfo eventCircleInfo;
}
