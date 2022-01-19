package edu.xidian.pnaWeb.web.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.model.PageResult;
import edu.xidian.pnaWeb.web.model.TaskDTO;
import edu.xidian.pnaWeb.web.model.TaskQueryReq;

import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 23:03
 */
public interface TaskService extends IService<TaskPO> {
	void submitTask(AlgReqDO algReqDO);

	void cancelTask(Long taskId);

	PageResult<TaskDTO> queryTask(TaskQueryReq queryReq);
}
