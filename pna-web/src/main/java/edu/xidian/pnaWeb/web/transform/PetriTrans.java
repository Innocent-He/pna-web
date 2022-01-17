package edu.xidian.pnaWeb.web.transform;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.dao.po.PetriPO;
import edu.xidian.pnaWeb.web.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:18
 */
public class PetriTrans {
	public static PetriDO transToDO(PetriDTO petriDTO) {
		AttrDTO attr = petriDTO.getAttr();
		int maxPlaceId = attr.getMaxPlaceId(), maxTranId = attr.getMaxTranId();
		int[][] pre = new int[maxPlaceId][maxTranId];
		int[][] post = new int[maxPlaceId][maxTranId];
		int[] initToken = new int[maxPlaceId];
		for (NodeDTO nodeDTO : petriDTO.getNodeList()) {
			String id = nodeDTO.getId();
			if (id.startsWith("p") && nodeDTO.getToken() != null) {
				String[] idStr = id.split("-");
				initToken[Integer.parseInt(idStr[1]) - 1] = nodeDTO.getToken();
			}
		}
		for (LinkDTO linkDTO : petriDTO.getLinkList()) {
			int weight = linkDTO.getWeight() == null ? 1 : linkDTO.getWeight();
			int placeNo;
			int tranNo;
			String sourceId = linkDTO.getSourceId();
			String targetId = linkDTO.getTargetId();
			if (sourceId.startsWith("p")) {
				placeNo = Integer.parseInt(sourceId.substring(2));
				tranNo = Integer.parseInt(targetId.substring(2));
				pre[placeNo - 1][tranNo - 1] = weight;
			} else {
				placeNo = Integer.parseInt(targetId.substring(2));
				tranNo = Integer.parseInt(sourceId.substring(2));
				post[placeNo - 1][tranNo - 1] = weight;
			}
		}
		PetriDO petriDO = PetriDO.builder()
				.id(attr.getId())
				.ownerId(attr.getOwnerId())
				.desc(attr.getDes())
				.name(attr.getName())
				.createTime(LocalDateTime.parse(attr.getCreateTime()))
				.postMatrix(post)
				.preMatrix(pre)
				.marking(initToken)
				.webJson(JSON.toJSONString(petriDTO))
				.build();
		return petriDO;
	}
	public static PetriDTO transToDTO(PetriDO petriDO) {
		return JSON.parseObject(petriDO.getWebJson(), PetriDTO.class);
	}
	public static PetriPO transToPO(PetriDO petriDO) {
		String postJon=JSON.toJSONString(petriDO.getPostMatrix());
		String preJon=JSON.toJSONString(petriDO.getPreMatrix());
		return PetriPO.builder()
				.id(petriDO.getId())
				.ownerId(petriDO.getOwnerId())
				.desc(petriDO.getDesc())
				.name(petriDO.getName())
				.createTime(petriDO.getCreateTime())
				.updateTime(petriDO.getUpdateTime())
				.postJson(postJon)
				.preJson(preJon)
				.webJson(petriDO.getWebJson())
				.build();
	}
	public static PetriDO transToDO(PetriPO petriPO) {
		int[][] postMatrix = JSON.parseObject(petriPO.getPostJson(), int[][].class);
		int[][] preMatrix = JSON.parseObject(petriPO.getPreJson(), int[][].class);
		return PetriDO.builder()
				.id(petriPO.getId())
				.ownerId(petriPO.getOwnerId())
				.name(petriPO.getName())
				.desc(petriPO.getDesc())
				.createTime(petriPO.getCreateTime())
				.updateTime(petriPO.getUpdateTime())
				.postMatrix(postMatrix)
				.preMatrix(preMatrix)
				.webJson(petriPO.getWebJson())
				.build();
	}
}
