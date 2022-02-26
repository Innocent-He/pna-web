package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/25 16:01
 */
@Builder
@Data
public class EventCircleInfo implements AlgResult{
	private List<List<Integer>> allCircles;
	private List<List<Integer>> allEws;
	private List<List<Integer>> allMinEws;
	@Override
	public String display() {
		StringBuilder res=new StringBuilder();
		res.append("all EWS:\n");
		for (int i = 0; i < allEws.size(); i++) {
			res.append("No"+i+1+allEws.get(i).toString()+"\n");
		}
		res.append("all ews size:"+allEws.size()+"\n");

		res.append("all min EWS:\n");
		for (int i = 0; i < allMinEws.size(); i++) {
			res.append("No"+(i+1)+allMinEws.get(i).toString()+"\n");
		}
		res.append("all minEws size:"+allMinEws.size()+"\n");
		return res.toString();
	}
}
