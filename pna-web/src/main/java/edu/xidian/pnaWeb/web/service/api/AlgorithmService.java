package edu.xidian.pnaWeb.web.service.api;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.model.GenerateRequest;

import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2021/10/23 15:46
 */
public interface AlgorithmService {
	String generateReachGraph(Map params);

	String generateSimphon(Map params);

	String generateEventWait(Map params);

	String executeByPython(Map params);

	String generatePetriNet(GenerateRequest request);
}
