package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * State状态节点
 *
 * @author HanChun
 * @version 1.0 2016-5-18
 */
@Builder
@Data
public class StateNode implements Cloneable {

	private boolean isChange ;
	/**
	 * 距离死锁的距离
	 */
	private int toDeadDistance;
	/**
	 * 库所标识
	 */
	private int[] state;
	/**
	 * 状态编号
	 */
	private int stateNo;
	/**
	 * 是否为死区状态
	 */
	private boolean isDeadlock ;
	/**
	 * 父状态节点
	 */
	private List<StateNode> parentNodes;
	/**
	 * 子状态节点
	 */
	private List<StateNode> childNodes;
	/**
	 * 深度
	 */
	private int depth;
	/**
	 * 是否为强鲁棒
	 */
	private boolean stronglyRobust ;
	/**
	 * 是否为弱鲁棒
	 */
	private boolean weaklyRobust ;
	/**
	 * 是否为非鲁棒
	 */
	private boolean noRobust ;

	public boolean equals(Object o) {
		if (o instanceof StateNode) {
			StateNode contrast = (StateNode)o;
			return o == null ? false : Arrays.equals(this.state, contrast.state);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return Arrays.hashCode(this.state);
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		if (this.state != null && this.state.length != 0) {
			StringBuffer ab = new StringBuffer();

			for(int i = 0; i < this.state.length; ++i) {
				ab.append(this.state[i] + " ");
			}
			return ab.toString();
		} else {
			return null;
		}
	}

	public void addPathLength() {
		++this.toDeadDistance;
	}

	StateNode(final boolean isChange, final int toDeadDistance, final int[] state, final int stateNo, final boolean isDeadlock, final List<StateNode> parentNodes, final List<StateNode> childNodes, final int depth, final boolean stronglyRobust, final boolean weaklyRobust, final boolean non_Robust) {
		this.isChange = isChange;
		this.toDeadDistance = toDeadDistance;
		this.state = state;
		this.stateNo = stateNo;
		this.isDeadlock = isDeadlock;
		this.parentNodes = parentNodes;
		this.childNodes = childNodes;
		this.depth = depth;
		this.stronglyRobust = stronglyRobust;
		this.weaklyRobust = weaklyRobust;
		this.noRobust = non_Robust;
	}

	public static StateNode.StateNodeBuilder builder() {
		return new StateNode.StateNodeBuilder();
	}

	public boolean isChange() {
		return this.isChange;
	}

	public int getToDeadDistance() {
		return this.toDeadDistance;
	}

	public int[] getState() {
		return this.state;
	}

	public int getStateNo() {
		return this.stateNo;
	}

	public boolean isDeadlock() {
		return this.isDeadlock;
	}

	public List<StateNode> getParentNodes() {
		return this.parentNodes;
	}

	public List<StateNode> getChildNodes() {
		return this.childNodes;
	}

	public int getDepth() {
		return this.depth;
	}

	public boolean isStronglyRobust() {
		return this.stronglyRobust;
	}

	public boolean isWeaklyRobust() {
		return this.weaklyRobust;
	}

	public boolean isNon_Robust() {
		return this.noRobust;
	}

	public void setChange(final boolean isChange) {
		this.isChange = isChange;
	}

	public void setToDeadDistance(final int toDeadDistance) {
		this.toDeadDistance = toDeadDistance;
	}

	public void setState(final int[] state) {
		this.state = state;
	}

	public void setStateNo(final int stateNo) {
		this.stateNo = stateNo;
	}

	public void setDeadlock(final boolean isDeadlock) {
		this.isDeadlock = isDeadlock;
	}

	public void setParentNodes(final List<StateNode> parentNodes) {
		this.parentNodes = parentNodes;
	}

	public void setChildNodes(final List<StateNode> childNodes) {
		this.childNodes = childNodes;
	}

	public void setDepth(final int depth) {
		this.depth = depth;
	}

	public void setStronglyRobust(final boolean stronglyRobust) {
		this.stronglyRobust = stronglyRobust;
	}

	public void setWeaklyRobust(final boolean weaklyRobust) {
		this.weaklyRobust = weaklyRobust;
	}

	public void setNon_Robust(final boolean non_Robust) {
		this.noRobust = non_Robust;
	}

	public static class StateNodeBuilder {
		private boolean isChange;
		private int toDeadDistance;
		private int[] state;
		private int stateNo;
		private boolean isDeadlock;
		private List<StateNode> parentNodes=new ArrayList<>();
		private List<StateNode> childNodes=new ArrayList<>();
		private int depth;
		private boolean stronglyRobust;
		private boolean weaklyRobust;
		private boolean noRobust;

		StateNodeBuilder() {
		}

		public StateNode.StateNodeBuilder isChange(final boolean isChange) {
			this.isChange = isChange;
			return this;
		}

		public StateNode.StateNodeBuilder toDeadDistance(final int toDeadDistance) {
			this.toDeadDistance = toDeadDistance;
			return this;
		}

		public StateNode.StateNodeBuilder state(final int[] state) {
			this.state = state;
			return this;
		}

		public StateNode.StateNodeBuilder stateNo(final int stateNo) {
			this.stateNo = stateNo;
			return this;
		}

		public StateNode.StateNodeBuilder isDeadlock(final boolean isDeadlock) {
			this.isDeadlock = isDeadlock;
			return this;
		}

		public StateNode.StateNodeBuilder depth(final int depth) {
			this.depth = depth;
			return this;
		}

		public StateNode.StateNodeBuilder stronglyRobust(final boolean stronglyRobust) {
			this.stronglyRobust = stronglyRobust;
			return this;
		}

		public StateNode.StateNodeBuilder weaklyRobust(final boolean weaklyRobust) {
			this.weaklyRobust = weaklyRobust;
			return this;
		}

		public StateNode.StateNodeBuilder non_Robust(final boolean non_Robust) {
			this.noRobust = non_Robust;
			return this;
		}

		public StateNode build() {
			return new StateNode(this.isChange, this.toDeadDistance, this.state, this.stateNo, this.isDeadlock, this.parentNodes, this.childNodes, this.depth, this.stronglyRobust, this.weaklyRobust, this.noRobust);
		}

		public String toString() {
			return "StateNode.StateNodeBuilder(isChange=" + this.isChange + ", toDeadDistance=" + this.toDeadDistance + ", state=" + Arrays.toString(this.state) + ", stateNo=" + this.stateNo + ", isDeadlock=" + this.isDeadlock + ", parentNodes=" + this.parentNodes + ", childNodes=" + this.childNodes + ", depth=" + this.depth + ", stronglyRobust=" + this.stronglyRobust + ", weaklyRobust=" + this.weaklyRobust + ", noRobust=" + this.noRobust + ")";
		}
	}

}

