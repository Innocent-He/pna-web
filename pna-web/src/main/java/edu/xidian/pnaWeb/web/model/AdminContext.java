package edu.xidian.pnaWeb.web.model;

import edu.xidian.pnaWeb.petri.module.EventCircleInfo;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.SimphonInfo;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author He
 * @Date 2021/10/23 15:50
 */
@Data
@Builder
public class AdminContext {
	/**
	 * InheritableThreadLocal确保在子线程也能读取父线程数据
	 */
	public static final ThreadLocal<AdminContext> USER_INFO=new InheritableThreadLocal<>();
	private Long userId;
	private String userName;
	private String email;
}
