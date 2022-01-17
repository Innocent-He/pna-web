package edu.xidian.pnaWeb.web.transform;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.enums.TaskStatusEnum;
import edu.xidian.pnaWeb.web.model.AlgRequest;
import edu.xidian.pnaWeb.web.model.TaskDTO;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:17
 */
public class AlgTrans {
	public static AlgReqDO transToDO(AlgRequest algRequest) {
		PetriDO petriDO = PetriTrans.transToDO(algRequest.getPetriDTO());
		return AlgReqDO.builder()
				.algName(algRequest.getAlgName())
				.params(algRequest.getParams())
				.petriDO(petriDO)
				.build();
	}

	public static TaskDTO transToDTO(TaskPO taskPO) {
		return TaskDTO.builder()
				.id(taskPO.getId())
				.status(TaskStatusEnum.statusOfCode(taskPO.getStatus()))
				.ownerName(taskPO.getOwnerName())
				.result(taskPO.getResult())
				.createTime(taskPO.getCreateTime())
				.updateTime(taskPO.getUpdateTime())
				.build();
	}
}
