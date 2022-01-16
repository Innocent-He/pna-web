package edu.xidian.pnaWeb.web.service.api;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import edu.xidian.pnaWeb.web.model.Invocation;
import edu.xidian.pnaWeb.web.model.PetriDataDTO;

import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author He
 * @Date 2021/10/5 17:35
 */
public interface PetriDataService {
	public Invocation transformData(String jsonData, HttpSession session);
}
