package edu.xidian.pnaWeb.petri.alg;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.petri.module.PlaceNode;
import edu.xidian.pnaWeb.petri.module.TranNode;
import edu.xidian.pnaWeb.web.model.AttrDTO;
import edu.xidian.pnaWeb.web.model.LinkDTO;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import edu.xidian.pnaWeb.web.model.PetriDTO;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @Author He
 * @Date 2021/11/16 14:54
 */
public abstract class PetriNetAlg implements AlgActuator{
	public static final Random RANDOM = new Random();

	public String execute(AlgReqDO algReqDO) {
		Map params = algReqDO.getParams();
		Integer placeCount = (Integer) params.get("placeCount");
		Integer tranCount = (Integer) params.get("tranCount");
		return generateNet(placeCount,tranCount);
	}

	public String generateNet(Integer placeCount, Integer tranCount) {
		List<PlaceNode> placeNodes = new ArrayList<>();
		List<TranNode> tranNodes = new ArrayList<>();

		nodePlacement(placeCount, tranCount, placeNodes, tranNodes);
		do {
			nodeConnect(placeNodes, tranNodes);
		} while (!this.algPostProcess(placeNodes, tranNodes));

		return JSON.toJSONString(buildPetriData(placeNodes, tranNodes));
	}

	private PetriDTO buildPetriData(List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		PetriDTO petriDTO = new PetriDTO();
		paddingLink(placeNodes, petriDTO);
		paddingAttr(petriDTO, placeNodes.size(), tranNodes.size());
		List<NodeDTO> allNodes = new ArrayList<>();
		allNodes.addAll(placeNodes);
		allNodes.addAll(tranNodes);
		petriDTO.setNodeList(allNodes);
		return petriDTO;
	}

	private void nodePlacement(Integer placeCount, Integer tranCount, List<PlaceNode> placeNodes, List<TranNode> tranNodes) {
		PlaceNode prePlace = null;
		for (Integer i = 1; i <= placeCount; i++) {
			PlaceNode placeNode = new PlaceNode(i);
			int x = prePlace == null ? 3000 : prePlace.getX();
			int y = prePlace == null ? 3000 : prePlace.getY();
			placeNode.setX(x + RANDOM.nextInt(150));
			placeNode.setY(y + RANDOM.nextInt(150));
			prePlace = placeNode;
			placeNodes.add(placeNode);
		}

		if (tranCount == null) {
			tranCount = placeCount + RANDOM.nextInt(placeCount);
		}
		for (Integer i = 1; i <= tranCount; i++) {
			TranNode tranNode = new TranNode(i);
			tranNodes.add(tranNode);
		}
	}



	private void paddingAttr(PetriDTO petriDTO, Integer placeCount, Integer tranCount) {
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		AttrDTO attrDTO = new AttrDTO(new Date().getTime(),"random","custom", nowDate, "ac网", placeCount + 1, tranCount + 1);
		petriDTO.setAttr(attrDTO);
	}


	private void paddingLink(List<PlaceNode> placeNodes, PetriDTO petriDTO) {
		int idx = 0;
		List<LinkDTO> allLink = new ArrayList<>();
		for (PlaceNode placeNode : placeNodes) {
			for (TranNode postTran : placeNode.getPostTrans()) {
				LinkDTO linkDTO = new LinkDTO(idx++, placeNode.getId(), postTran.getId(), null);
				allLink.add(linkDTO);
			}
			for (TranNode preTran : placeNode.getPreTrans()) {
				LinkDTO linkDTO = new LinkDTO(idx++, preTran.getId(), placeNode.getId(), null);
				allLink.add(linkDTO);
			}
		}
		petriDTO.setLinkList(allLink);
	}
	protected abstract void nodeConnect(List<PlaceNode> placeNodes, List<TranNode> tranNodes);

	protected abstract boolean validPre(TranNode tranNode, PlaceNode selectedPlace);

	protected abstract boolean validPost(PlaceNode placeNode, TranNode selectedTran);

	protected abstract boolean algPostProcess(List<PlaceNode> placeNodes, List<TranNode> tranNodes);

	public abstract boolean apply(AlgReqDO algReqDO) ;

}
