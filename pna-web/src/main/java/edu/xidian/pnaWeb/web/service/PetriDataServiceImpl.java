package edu.xidian.pnaWeb.web.service;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.model.*;
import edu.xidian.pnaWeb.web.service.api.PetriDataService;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author He
 * @Date 2021/10/5 17:34
 */
@Service
public class PetriDataServiceImpl implements PetriDataService {

	@Override
	public Invocation transformData(String jsonData, HttpSession session) {
		AdminContext adminContext =(AdminContext) session.getAttribute("adminContext");
		PetriDataDTO webData = JSON.parseObject(jsonData, PetriDataDTO.class);
		if (adminContext == null||webData.getEditNew()) {
			int maxPlaceId = webData.getAttr().getMaxPlaceId(), maxTranId = webData.getAttr().getMaxTranId();
			int[][] pre = new int[maxPlaceId][maxTranId];
			int[][] post = new int[maxPlaceId][maxTranId];
			int[] initToken=new int[maxPlaceId];

			for (NodeDTO nodeDTO : webData.getNodeList()) {
				String id = nodeDTO.getId();
				if (id.startsWith("p")&&nodeDTO.getToken()!=null){
					String[] idStr = id.split("-");
					initToken[Integer.parseInt(idStr[1])-1]=nodeDTO.getToken();
				}
			}
			for (LinkDTO linkDTO : webData.getLinkList()) {
				int weight = linkDTO.getWeight() == null ? 1 : linkDTO.getWeight();
				int placeNo;
				int tranNo;
				String sourceId = linkDTO.getSourceId();
				String targetId = linkDTO.getTargetId();
				if (sourceId.startsWith("p")) {
					placeNo = Integer.parseInt(sourceId.substring(2));
					tranNo = Integer.parseInt(targetId.substring(2));
					pre[placeNo-1][tranNo-1]=weight;
				} else {
					placeNo = Integer.parseInt(targetId.substring(2));
					tranNo = Integer.parseInt(sourceId.substring(2));
					post[placeNo-1][tranNo-1]=weight;
				}
			}
			PetriDO petriDO = PetriDO.builder()
					.postMatrix(post)
					.preMatrix(pre)
					.webJson(jsonData)
					.marking(initToken)
					.build();
			adminContext = new AdminContext();
			adminContext.setPetriDO(petriDO);
			session.setAttribute("adminContext",adminContext);
		}
		AdminContext.USER_INFO.set(adminContext);
		Invocation invocation = new Invocation();
		invocation.setParams(webData.getParams());
		invocation.setMethodName(webData.getMethodName());
		return invocation;
	}

}
