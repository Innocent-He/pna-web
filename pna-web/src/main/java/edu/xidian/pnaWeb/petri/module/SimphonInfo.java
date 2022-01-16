package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2021/10/24 22:20
 */
@Data
@Builder
public class SimphonInfo {
	private List<List<Integer>> simphons;
	private List<List<Integer>> strictSimphons;

	@Override
	public String toString() {
		StringBuilder res=new StringBuilder();
		res.append("all simphons:").append("\n");

		for (List<Integer> simphon : simphons) {
			res.append(simphon).append("\n");
		}
		res.append("strict simphons:").append("\n");
		for (List<Integer> strictSimphon : strictSimphons) {
			res.append(strictSimphon).append("\n");
		}
		return res.toString();
	}
}
