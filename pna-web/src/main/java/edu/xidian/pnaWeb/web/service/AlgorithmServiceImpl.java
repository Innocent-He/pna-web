package edu.xidian.pnaWeb.web.service;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.alg.*;
import edu.xidian.pnaWeb.petri.module.EventCircleInfo;
import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.petri.module.ReachGraphInfo;
import edu.xidian.pnaWeb.petri.module.SimphonInfo;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.GenerateRequest;
import edu.xidian.pnaWeb.web.model.NodeDTO;
import edu.xidian.pnaWeb.web.model.PetriDataDTO;
import edu.xidian.pnaWeb.web.service.api.AlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2021/10/23 15:47
 */
@Service
@Slf4j
public class AlgorithmServiceImpl implements AlgorithmService {
	@Resource
	private ReachableGraphAlg reachableGraphAlg;
	@Resource
	private SimphonAlg simphonAlg;
	@Resource
	private EventCircleAlg eventCircleAlg;
	@Resource
	private AcNetAlg acNetAlg;
	@Resource
	private FcNetAlg fcNetAlg;
	@Resource
	private PythonAlg pythonAlg;

	@Override
	public String generateReachGraph(Map params) {
		Integer step = (Integer) params.get("step");
		AdminContext adminContext = AdminContext.USER_INFO.get();
		ReachGraphInfo reachGraphInfo=null;
		try {
			reachGraphInfo = reachableGraphAlg.generateReachGraphWithStep(adminContext.getPetriDO(), step);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		adminContext.setReachGraphInfo(reachGraphInfo);
		return reachGraphInfo.toString();
	}

	@Override
	public String generateSimphon(Map params) {
		SimphonInfo simphonInfo = simphonAlg.generateSimphon();
		AdminContext adminContext = AdminContext.USER_INFO.get();
		adminContext.setSimphonInfo(simphonInfo);
		return simphonInfo.toString();
	}

	@Override
	public String generateEventWait(Map params) {
		EventCircleInfo eventCircleInfo=null;
		try {
			eventCircleInfo = eventCircleAlg.generateEventCircle();
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		AdminContext adminContext = AdminContext.USER_INFO.get();
		adminContext.setEventCircleInfo(eventCircleInfo);
		return eventCircleInfo.toString();
	}
	@Override
	public String generatePetriNet(GenerateRequest request) {
		PetriDataDTO petriDataDTO=null;
		switch (request.getNetType()) {
			case "ac":
				petriDataDTO= acNetAlg.generateNet(request.getPlaceCount(),request.getTranCount());
				break;
			case "fc":
				petriDataDTO=fcNetAlg.generateNet(request.getPlaceCount(),request.getTranCount());
				break;
			default:
				break;
		}
		return JSON.toJSONString(petriDataDTO);
	}

	@Override
	public String executeByPython(Map params) {
		AdminContext adminContext = AdminContext.USER_INFO.get();
		PetriDO petriDO = adminContext.getPetriDO();
		String postMatrix = JSON.toJSONString(petriDO.getPostMatrix());
		String preMatrix= JSON.toJSONString(petriDO.getPreMatrix());
		String algType= (String) params.get("algType");
		switch (algType) {
			case "invariant":
				return pythonAlg.getInvariantP(postMatrix,preMatrix);
			default:
				return "noSuchAlg";
		}
	}
}
