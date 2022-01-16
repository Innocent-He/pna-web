package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.PlaceNode;
import edu.xidian.pnaWeb.petri.module.TranNode;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2021/11/17 16:09
 */
@Service
@Slf4j
public class AcNetAlg extends PetriNetAlg {
	@Override
	protected boolean algPostProcess(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		for (PlaceNode placeNode : placeNodes) {
			if (CollectionUtils.isEmpty(placeNode.getPostTrans())) {
				TranNode tranNode = null;
				do {
					tranNode = tranNodes.get(RANDOM.nextInt(tranNodes.size()));
				} while (tranNode.containsPost(placeNode) || !this.dataCorrection(placeNode, tranNode));
			}
			if (CollectionUtils.isEmpty(placeNode.getPreTrans())) {
				TranNode tranNode = null;
				do {
					tranNode = tranNodes.get(RANDOM.nextInt(tranNodes.size()));
				} while (placeNode.containsPost(tranNode));
				tranNode.addPostPlace(placeNode);
				placeNode.addPreTran(tranNode);
			}
		}

		for (TranNode tranNode : tranNodes) {
			if (CollectionUtils.isEmpty(tranNode.getPostPlaces())) {
				PlaceNode placeNode = null;
				do {
					placeNode = placeNodes.get(RANDOM.nextInt(placeNodes.size()));
				} while (placeNode.containsPost(tranNode));
				tranNode.addPostPlace(placeNode);
				placeNode.addPreTran(tranNode);
			}
			if (CollectionUtils.isEmpty(tranNode.getPrePlaces())) {
				PlaceNode placeNode = null;
				do {
					placeNode = placeNodes.get(RANDOM.nextInt(placeNodes.size()));
				} while (tranNode.containsPost(placeNode) || !this.dataCorrection(placeNode, tranNode));
			}
		}
		return true;
	}

	public boolean dataCorrection(PlaceNode placeNode, TranNode tranNode) {
		Set<PlaceNode> prePlaces =new HashSet<>( tranNode.getPrePlaces());
		placeNode.addPostTran(tranNode);
		tranNode.addPrePlace(placeNode);
		if (prePlaces.size() == 0) {
			return true;
		}
		Set<TranNode> allJoinedTrans = new HashSet<>();
		for (PlaceNode prePlace : prePlaces) {
			Set<TranNode> joinedSet = getJoinedSet(placeNode.getPostTrans(), prePlace.getPostTrans());
			for (TranNode joinTran : joinedSet) {
				if (joinTran.containsPost(placeNode)) {
					placeNode.removePostTran(tranNode);
					tranNode.removePrePlace(placeNode);
					return false;
				}
			}
			allJoinedTrans.addAll(joinedSet);
		}
		for (TranNode joinedTran : allJoinedTrans) {
			placeNode.addPostTran(joinedTran);
			joinedTran.addPrePlace(placeNode);
		}
		return true;
	}

	@Override
	public boolean validPre(TranNode tranNode, PlaceNode selectedPlace) {
		if (tranNode.getPostPlaceCount() + tranNode.getPrePlaceCount() >= 3
				|| selectedPlace.getPreTranCount() + selectedPlace.getPostTranCount() >= 3
				|| tranNode.containsPre(selectedPlace)) {
			return false;
		}
//		log.info("==============Valid_Pre===========");
//
//		log.info(tranNode.toString());
//		log.info(selectedPlace.toString());
//		log.info(tranNode.getPrePlaces().toString());
//		log.info(selectedPlace.getPostTrans().toString());
//		log.info(tranNode.containsPre(selectedPlace).toString());
//		log.info("=========================");
		tranNode.addPostPlace(selectedPlace);
		selectedPlace.addPreTran(tranNode);
		return true;
	}
	@Override
	public void nodeConnect(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
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

	@Override
	public boolean validPost(PlaceNode placeNode, TranNode selectedTran) {
		if (placeNode.getPostTranCount() + placeNode.getPreTranCount() >= 3
				|| selectedTran.getPrePlaceCount() + selectedTran.getPostPlaceCount() >= 3
				|| selectedTran.containsPost(placeNode)) {
			return false;
		}
//		log.info("==============Valid_Post===========");
//
//		log.info(placeNode.toString());
//		log.info(selectedTran.toString());
//		log.info(placeNode.getPreTrans().toString());
//		log.info(selectedTran.getPostPlaces().toString());
//		log.info(placeNode.containsPre(selectedTran).toString());
//		log.info("=========================");

		return dataCorrection(placeNode, selectedTran);
	}

	/**
	 * 拿到当前库所为了符合ACNet定义所需要添加的所有后置变迁
	 *
	 * @param curTranSet
	 * @param otherTranSet
	 * @return
	 */
	private Set<TranNode> getJoinedSet(Set<TranNode> curTranSet, Set<TranNode> otherTranSet) {
		Set<TranNode> addTranSet = new HashSet<>();
		if (curTranSet.containsAll(otherTranSet) || otherTranSet.containsAll(curTranSet)) {
			return addTranSet;
		}
		for (TranNode tranNode : otherTranSet) {
			if (!curTranSet.contains(tranNode)) {
				addTranSet.add(tranNode);
			}
		}
		return addTranSet;
	}
}
