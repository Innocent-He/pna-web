package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.PlaceNode;
import edu.xidian.pnaWeb.petri.module.TranNode;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2021/11/23 13:36
 */
@Service
public class FcNetAlg extends PetriNetAlg {
	@Override
	public boolean apply(AlgReqDO algReqDO) {
		String netType = (String) algReqDO.getParams().get("netType");
		if (StringUtils.equals(netType, "fc")) {
			return true;
		}
		return false;
	}

	@Override
	protected void nodeConnect(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		clearNodeInfo(placeNodes,tranNodes);
		NodeDTO initialNode = placeNodes.get(0), curNode = initialNode;
		int maxLimit = 1000;
		while (maxLimit > 0) {
			int nextX = curNode.getX() + RANDOM.nextInt(150), nextY = curNode.getY() + RANDOM.nextInt(150);
			int limit = placeNodes.size() * 2;
			if (curNode instanceof PlaceNode) {
				PlaceNode placeNode = (PlaceNode) curNode;
				TranNode selectedTran = null;
				do {
					limit--;
					selectedTran = tranNodes.get(RANDOM.nextInt(tranNodes.size()));
				} while (!validPost(placeNode, selectedTran) && limit > 0);
				curNode = selectedTran;
			} else {
				TranNode tranNode = (TranNode) curNode;
				PlaceNode selectedPlace = null;
				do {
					limit--;
					selectedPlace = placeNodes.get(RANDOM.nextInt(placeNodes.size()));
				} while (!validPre(tranNode, selectedPlace) && limit > 0);
				curNode = selectedPlace;
			}
			if (curNode.getX() == null) {
				curNode.setX(nextX);
				curNode.setY(nextY);
			}
			maxLimit--;
		}
	}

	private void clearNodeInfo(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		for (PlaceNode placeNode : placeNodes) {
			placeNode.clear();
		}
		for (TranNode tranNode : tranNodes) {
			tranNode.clear();
		}
	}

	@Override
	protected boolean algPostProcess(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		for (PlaceNode placeNode : placeNodes) {
			if (placeNode.getPostTranCount() == 0) {
				TranNode tranNode = null;
				int limit = placeNodes.size() * 3;
				do {
					limit--;
					tranNode = tranNodes.get(RANDOM.nextInt(tranNodes.size()));
				} while (this.validPost(placeNode, tranNode) && limit > 0);
				if (limit <= 0) {
					return false;
				}
				placeNode.addPostTran(tranNode);
				tranNode.addPrePlace(placeNode);
			}
			if (placeNode.getPreTranCount() == 0) {
				TranNode tranNode = null;
				do {
					tranNode = tranNodes.get(RANDOM.nextInt(tranNodes.size()));
				} while (placeNode.containsPost(tranNode));
				tranNode.addPostPlace(placeNode);
				placeNode.addPreTran(tranNode);
			}
		}

		for (TranNode tranNode : tranNodes) {
			if (tranNode.getPostPlaceCount() == 0) {
				PlaceNode placeNode = null;
				do {
					placeNode = placeNodes.get(RANDOM.nextInt(placeNodes.size()));
				} while (placeNode.containsPost(tranNode));
				tranNode.addPostPlace(placeNode);
				placeNode.addPreTran(tranNode);
			}
			if (tranNode.getPrePlaceCount() == 0) {
				PlaceNode placeNode = null;
				int limit = placeNodes.size() * 3;
				do {
					placeNode = placeNodes.get(RANDOM.nextInt(placeNodes.size()));
				} while (this.validPost(placeNode, tranNode) && limit-- > 0);
				if (limit <= 0) {
					return false;
				}
				placeNode.addPostTran(tranNode);
				tranNode.addPrePlace(placeNode);
			}
		}
		return true;
	}

	@Override
	protected boolean validPre(TranNode tranNode, PlaceNode selectedPlace) {
		if (tranNode.getPostPlaceCount() + tranNode.getPrePlaceCount() >= 3
				|| selectedPlace.getPreTranCount() + selectedPlace.getPostTranCount() >= 3
				|| tranNode.containsPre(selectedPlace)) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean validPost(PlaceNode placeNode, TranNode selectedTran) {
		if (placeNode.getPostTranCount() != 0 || selectedTran.getPrePlaceCount() != 0) {
			return false;
		}
		return true;
	}
}
