package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2022/2/13 19:46
 */
@Data
public class DeadPredInfo {
	private List<PredInfo> predInfos=new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (PredInfo predInfo : predInfos) {
			StateNode stateNode = predInfo.getStateNode();
			List<StateNode> parentNodes = predInfo.stateNode.getParentNodes();
			if (!CollectionUtils.isEmpty(parentNodes)) {
				sb.append("parent state:");
				for (StateNode parentNode : parentNodes) {
					sb.append(parentNode.getStateNo()).append("===").append(Arrays.toString(parentNode.getState()));
				}
			}
			sb.append("No").append(stateNode.getStateNo()).append("\n");
			sb.append("marking:").append(Arrays.toString(stateNode.getState())).append("\n");
			sb.append("ew:").append(predInfo.getEws()).append("\n");

			sb.append("===========\n");
		}
		return sb.toString();
	}

	@Builder
	@Data
	public static class PredInfo{
		private List<Integer> ews;
		private StateNode stateNode;
	}
}
