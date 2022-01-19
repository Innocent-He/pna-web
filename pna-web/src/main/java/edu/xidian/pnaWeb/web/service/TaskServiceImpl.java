package edu.xidian.pnaWeb.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xidian.pnaWeb.petri.context.TaskCenter;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.web.dao.TaskMapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.model.PageResult;
import edu.xidian.pnaWeb.web.model.TaskDTO;
import edu.xidian.pnaWeb.web.model.TaskQueryReq;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import edu.xidian.pnaWeb.web.transform.TaskTrans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 23:03
 */
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskPO> implements TaskService {
	@Resource
	private TaskCenter taskCenter;

	@Override
	public void submitTask(AlgReqDO algReqDO) {
		taskCenter.pushTask(algReqDO);
	}

	@Override
	public void cancelTask(Long taskId) {
		taskCenter.cancelTask(taskId);
	}

	@Override
	public PageResult<TaskDTO> queryTask(TaskQueryReq queryReq) {
		Page<TaskPO> taskPage = new Page(queryReq.getPageNo(), queryReq.getPageSize());
		String ownerName = queryReq.getOwnerName();
		QueryWrapper<TaskPO> taskPOQueryWrapper = new QueryWrapper<>();
		if (StringUtils.isBlank(ownerName)) {
			IPage<TaskPO> taskPOPage = baseMapper.selectPage(taskPage, taskPOQueryWrapper.orderByAsc("create_time"));
			List<TaskDTO> taskDTOS = taskPOPage.getRecords().stream().map(TaskTrans::transToDTO).collect(Collectors.toList());
			return new PageResult(taskDTOS, taskPage.getTotal());
		}
		IPage<TaskPO> taskPOPage = baseMapper.selectPage(taskPage, taskPOQueryWrapper
				.eq("owner_name", queryReq.getOwnerName())
				.orderByAsc("create_time"));
		List<TaskDTO> taskDTOS = taskPOPage.getRecords().stream().map(TaskTrans::transToDTO).collect(Collectors.toList());
		return new PageResult(taskDTOS, taskPage.getTotal());
	}

	public List<TaskPO> selectPage(String userName, Integer pageStart) {
		Page<TaskPO> taskPOPage = new Page(pageStart, 10);
		IPage<TaskPO> taskPage = this.baseMapper.selectPage(taskPOPage, new QueryWrapper<TaskPO>().eq("owner_name", userName).orderByDesc("create_time"));
		return taskPage.getRecords();
	}
}
