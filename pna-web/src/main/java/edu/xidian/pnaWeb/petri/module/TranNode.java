package edu.xidian.pnaWeb.petri.module;

import com.alibaba.fastjson.annotation.JSONField;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2021/11/16 14:51
 */
public class TranNode extends NodeDTO {
	@JSONField(serialize =false)
	private Set<PlaceNode> prePlaces = new HashSet<>();
	@JSONField(serialize =false)
	private Set<PlaceNode> postPlaces = new HashSet<>();

	public TranNode(Integer id) {
		super();
		this.setId("t-"+id);
		this.setType("transaction");
		this.setClassName("tran-node");
		this.setNodeName("变迁");
	}

	public Set<PlaceNode> getPrePlaces() {
		return prePlaces;
	}

	public Set<PlaceNode> getPostPlaces() {
		return postPlaces;
	}

	public Boolean containsPost(PlaceNode placeNode) {
		return postPlaces.contains(placeNode);
	}

	public Boolean containsPre(PlaceNode placeNode) {
		return prePlaces.contains(placeNode);
	}
	@JSONField(serialize = false)
	public Integer getPostPlaceCount() {
		return postPlaces.size();
	}
	@JSONField(serialize = false)
	public Integer getPrePlaceCount() {
		return prePlaces.size();
	}

	public void addPostPlace(PlaceNode... placeNodes) {
		this.postPlaces.addAll(Arrays.asList(placeNodes));
	}

	public void addPrePlace(PlaceNode... placeNodes) {
		this.prePlaces.addAll(Arrays.asList(placeNodes));
	}
	public void removePrePlace(PlaceNode placeNode) {
		this.prePlaces.remove(placeNode);
	}
	public void clear() {
		this.prePlaces.clear();
		this.postPlaces.clear();
	}
}
