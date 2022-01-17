package edu.xidian.pnaWeb.petri.context;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.enums.TaskStatusEnum;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:57
 */
@Component
@Slf4j
public class TaskCenter implements InitializingBean {

	private Deque<AlgReqDO> taskDeque = new ArrayDeque<>();
	private Thread taskThread;
	@Resource
	private AlgContext algContext;
	@Resource
	private TaskService taskService;

	public void pushTask(AlgReqDO algReqDO) {
		TaskPO taskPO=TaskPO.builder().algName(algReqDO.getAlgName())
				.status(TaskStatusEnum.WAITING.code())
				.ownerId(algReqDO.getPetriDO().getOwnerId())
				.algName(algReqDO.getAlgName())
				.build();
		taskService.save(taskPO);
		algReqDO.setId(taskPO.getId());
		taskDeque.offerLast(algReqDO);
		// 唤醒任务线程执行
		LockSupport.unpark(taskThread);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		taskThread = new Thread(() -> {
			while (true) {
				if (taskDeque.isEmpty()) {
					// 当前队列无任务则阻塞
					LockSupport.park();
				}
				AlgReqDO algReqDO = taskDeque.pollFirst();
				TaskPO running = TaskPO.builder()
						.id(algReqDO.getId())
						.status(TaskStatusEnum.RUNNING.code())
						.build();
				// 更新状态
				taskService.updateById(running);
				try {
					String result = algContext.executeAlg(algReqDO);
					TaskPO success = TaskPO.builder()
							.id(algReqDO.getId())
							.status(TaskStatusEnum.SUCCESS.code())
							.result(result)
							.build();
					// 更新状态 结果
					taskService.updateById(success);
				} catch (Exception e) {
					log.info(e.getMessage());
					TaskPO failed = TaskPO.builder()
							.id(algReqDO.getId())
							.status(TaskStatusEnum.FAILED.code())
							.build();
					// 更新状态
					taskService.updateById(failed);
				}
			}
		}, "taskThread");
	}
}
