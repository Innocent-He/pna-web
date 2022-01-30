package edu.xidian.pnaWeb.petri.context.state;

/**
 * @Description
 * @Author He
 * @Date 2022/1/18 14:09
 */
public class TaskStateMachine {
	private TaskStatusEnum status=TaskStatusEnum.WAITING;
	public TaskStatusEnum execute(Action action) {
		action.execute(this,status);
		return this.status;
	}
	public TaskStatusEnum getStatus() {
		return this.status;
	}
	public void setStatus(TaskStatusEnum status) {
		this.status=status;
	}
}
