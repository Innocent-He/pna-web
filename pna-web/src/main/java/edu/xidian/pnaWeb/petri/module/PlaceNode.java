package edu.xidian.pnaWeb.petri.module;

import com.alibaba.fastjson.annotation.JSONField;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import lombok.Data;

import java.beans.Transient;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2021/11/16 14:52
 */
public class PlaceNode extends NodeDTO {
	@JSONField(serialize =false)
	private Set<TranNode> preTrans=new HashSet<>();
	@JSONField(serialize =false)
	private Set<TranNode> postTrans=new HashSet<>();
	public PlaceNode(Integer id) {
		super();
		this.setId("p-"+id);
		this.setType("place");
		this.setClassName("place-node");
		this.setNodeName("库所");
	}

	public Set<TranNode> getPreTrans() {
		return preTrans;
	}

	public Set<TranNode> getPostTrans() {
		return postTrans;
	}
	@JSONField(serialize = false)
	public Integer getPostTranCount() {
		return postTrans.size();
	}
	@JSONField(serialize = false)
	public Integer getPreTranCount() {
		return preTrans.size();
	}

	public Boolean containsPost(TranNode tranNode) {
		return postTrans.contains(tranNode);
	}

	public Boolean containsPre(TranNode tranNode) {
		return preTrans.contains(tranNode);
	}

	public void addPreTran(TranNode... tranNodes) {
		this.preTrans.addAll(Arrays.asList(tranNodes));
	}

	public void addPostTran(TranNode... tranNodes) {
		this.postTrans.addAll(Arrays.asList(tranNodes));
	}

	public void removePostTran(TranNode tranNode) {
		this.postTrans.remove(tranNode);
	}

	public void addPostTran(Set<TranNode> tranNodes) {
		this.postTrans.addAll(tranNodes);
	}

	public void clear() {
		this.postTrans.clear();
		this.preTrans.clear();
	}
}
