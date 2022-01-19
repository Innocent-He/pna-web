package edu.xidian.pnaWeb.web.transform;

import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.model.TaskDTO;

/**
 * @Description
 * @Author He
 * @Date 2022/1/19 18:30
 */
public class TaskTrans {
	public static TaskDTO transToDTO(TaskPO taskPO) {
		return TaskDTO.builder().id(taskPO.getId())
				.algName(taskPO.getAlgName())
				.ownerName(taskPO.getOwnerName())
				.result(taskPO.getResult())
				.createTime(taskPO.getCreateTime())
				.updateTime(taskPO.getUpdateTime())
				.status(taskPO.getStatus())
				.build();
	}
}
