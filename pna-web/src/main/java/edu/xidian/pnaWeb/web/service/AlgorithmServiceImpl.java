package edu.xidian.pnaWeb.web.service;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.petri.alg.*;
import edu.xidian.pnaWeb.petri.context.AlgContext;
import edu.xidian.pnaWeb.petri.module.*;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.GenerateRequest;
import edu.xidian.pnaWeb.web.model.PetriDTO;
import edu.xidian.pnaWeb.web.service.api.AlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
	private AcNetAlg acNetAlg;
	@Resource
	private FcNetAlg fcNetAlg;
	@Override
	public String generatePetriNet(GenerateRequest request) {
		PetriDTO petriDTO =null;
		switch (request.getNetType()) {
			case "ac":
				petriDTO = acNetAlg.generateNet(request.getPlaceCount(),request.getTranCount());
				break;
			case "fc":
				petriDTO =fcNetAlg.generateNet(request.getPlaceCount(),request.getTranCount());
				break;
			default:
				break;
		}
		return JSON.toJSONString(petriDTO);
	}

	@Override
	public String algRequest(AlgReqDO algReqDO) {

		return null;
	}
}
