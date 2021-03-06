package edu.xidian.pnaWeb.petri.context;

import edu.xidian.pnaWeb.petri.context.state.Action;
import edu.xidian.pnaWeb.petri.context.state.TaskStateMachine;
import edu.xidian.pnaWeb.petri.context.state.TaskStatusEnum;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.AlgResult;
import edu.xidian.pnaWeb.petri.module.TimeLevel;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.exception.BizException;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.AdminInfo;
import edu.xidian.pnaWeb.web.service.api.MessageService;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:57
 */
@Component
@Slf4j
public class TaskCenter implements InitializingBean {
	/**
	 * 任务阻塞队列
	 */
	private final Deque<AlgReqDO> taskDeque = new ArrayDeque<>();
	/**
	 * 任务执行线程池
	 */
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	/**
	 * 队列轮询线程
	 */
	private Thread taskThread;
	@Resource
	private AlgContext algContext;
	@Resource
	private TaskService taskService;
	@Resource
	private MessageService messageService;


	private Map<Long, TaskStateMachine> machineMap = new HashMap<>();

	public void pushTask(AlgReqDO algReqDO) {
		AdminInfo adminInfo = AdminContext.USER_INFO.get();
		TaskPO taskPO = TaskPO.builder().algName(algReqDO.getAlgName())
				.status(TaskStatusEnum.WAITING.code())
				.ownerName(adminInfo.getUserName())
				.algName(algReqDO.getAlgName())
				.build();
		taskService.save(taskPO);
		algReqDO.setId(taskPO.getId());
		machineMap.put(algReqDO.getId(), new TaskStateMachine());
		taskDeque.offerLast(algReqDO);
		// 唤醒任务线程执行
		LockSupport.unpark(taskThread);
	}

	@Override
	public void afterPropertiesSet() {
		taskThread = new Thread(() -> {
			while (true) {
				if (taskDeque.isEmpty()) {
					// 当前队列无任务则阻塞
					LockSupport.park();
				}
				AlgReqDO algReqDO;
				// 避免取消任务时造成影响
				synchronized (TaskCenter.class) {
					algReqDO = taskDeque.pollFirst();
				}
				beforeExecute(algReqDO);
				try {
					String result = obtainResult(algReqDO);
					afterRunning(algReqDO, result);
				} catch (TimeoutException e) {
					log.error("任务执行超时", algReqDO.getId());
					afterError(algReqDO);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.toString());
					afterError(algReqDO);
				} finally {
					afterComplete(algReqDO);
				}
			}
		}, "taskThread");
		taskThread.start();
	}

	private String obtainResult(AlgReqDO algReqDO) throws ExecutionException, InterruptedException, TimeoutException {
		Future<AlgResult> executeFuture = executorService.submit(() -> algContext.executeAlg(algReqDO));
		Integer level = algReqDO.getTimeLevel();
		TimeLevel time = TimeLevel.getTime(level);
		return executeFuture.get(time.getTime(), time.getTimeUnit()).display();
	}

	private void afterComplete(AlgReqDO algReqDO) {
		TaskStateMachine taskStateMachine = machineMap.get(algReqDO.getId());
		TaskStatusEnum status = taskStateMachine.getStatus();
		if (!StringUtils.isBlank(algReqDO.getEmail())) {
			noticeUser(algReqDO, status);
		}
		machineMap.remove(algReqDO.getId());
	}

	private void afterError(AlgReqDO algReqDO) {
		TaskStateMachine taskStateMachine = machineMap.get(algReqDO.getId());
		TaskStatusEnum status = taskStateMachine.execute(Action.ERROR);
		TaskPO failed = TaskPO.builder()
				.id(algReqDO.getId())
				.status(status.code())
				.build();
		// 更新状态
		taskService.updateById(failed);
	}

	private void afterRunning(AlgReqDO algReqDO, String result) {
		TaskStateMachine taskStateMachine = machineMap.get(algReqDO.getId());
		TaskStatusEnum status = taskStateMachine.execute(Action.FINISHED);
		TaskPO success = TaskPO.builder()
				.id(algReqDO.getId())
				.status(status.code())
				.result(result)
				.build();
		// 更新状态结果
		taskService.updateById(success);
	}

	private void beforeExecute(AlgReqDO algReqDO) {
		// 更新状态
		TaskStateMachine taskStateMachine = machineMap.get(algReqDO.getId());
		TaskStatusEnum status = taskStateMachine.execute(Action.SUBMIT);
		TaskPO running = TaskPO.builder()
				.id(algReqDO.getId())
				.status(status.code())
				.build();
		taskService.updateById(running);
	}

	private void noticeUser(AlgReqDO algReqDO, TaskStatusEnum statusEnum) {
		String email = algReqDO.getEmail();
		String content = "您在PNA-WEB站点提交的算法已结束，当前状态为:" + statusEnum.status();
		messageService.senMail(email, Constant.MAIL_SUBJECT_ALG, content);
	}

	public void cancelTask(Long taskId) {
		synchronized (TaskCenter.class) {
			for (AlgReqDO algReqDO : taskDeque) {
				if (Objects.equals(taskId, algReqDO.getId())) {
					algReqDO.setCancel(true);
					return;
				}
			}
			throw new BizException(Constant.TASK_CANCEL_FAILED_CODE, Constant.TASK_CANCEL_FAILED_MESSAGE);
		}
	}
}
