package edu.xidian.pnaWeb.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xidian.pnaWeb.petri.context.TaskCenter;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.web.dao.TaskMapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 23:03
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper,TaskPO> implements TaskService {
	@Resource
	private TaskCenter taskCenter;
	@Override
	public void submitTask(AlgReqDO algReqDO) {
		taskCenter.pushTask(algReqDO);
	}
}
