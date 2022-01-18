package edu.xidian.pnaWeb.petri.context.state;

import edu.xidian.pnaWeb.web.enums.Constant;

import java.util.Objects;

/**
 * @Author He
 * @Date 2022/1/18 14:10
 */
public enum Action {
	SUBMIT {
		@Override
		public void execute(TaskStateMachine taskStateMachine, TaskStatusEnum statusEnum) {
			if (!Objects.equals(statusEnum, TaskStatusEnum.WAITING)) {
				super.execute(taskStateMachine, statusEnum);
			}
			taskStateMachine.setStatus(TaskStatusEnum.RUNNING);
		}
	}, CANCEL {
		@Override
		public void execute(TaskStateMachine taskStateMachine, TaskStatusEnum statusEnum) {
			if (!Objects.equals(statusEnum, TaskStatusEnum.RUNNING)) {
				super.execute(taskStateMachine, statusEnum);
			}
			taskStateMachine.setStatus(TaskStatusEnum.CANCELED);

		}
	}, FINISHED {
		@Override
		public void execute(TaskStateMachine taskStateMachine, TaskStatusEnum statusEnum) {
			if (!Objects.equals(statusEnum, TaskStatusEnum.RUNNING)) {
				super.execute(taskStateMachine, statusEnum);
			}
			taskStateMachine.setStatus(TaskStatusEnum.SUCCESS);

		}
	}, ERROR {
		@Override
		public void execute(TaskStateMachine taskStateMachine, TaskStatusEnum statusEnum) {
			if (!Objects.equals(statusEnum, TaskStatusEnum.RUNNING)) {
				super.execute(taskStateMachine, statusEnum);
			}
			taskStateMachine.setStatus(TaskStatusEnum.FAILED);

		}
	};

	public void execute(TaskStateMachine taskStateMachine, TaskStatusEnum statusEnum) {
		throw new RuntimeException(Constant.STATE_TRAN_CODE_MESSAGE);
	}
}
