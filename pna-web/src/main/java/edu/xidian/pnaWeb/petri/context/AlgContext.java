package edu.xidian.pnaWeb.petri.context;

import com.google.common.collect.Lists;
import edu.xidian.pnaWeb.petri.alg.AlgActuator;
import edu.xidian.pnaWeb.petri.module.AlgReqDO;
import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.exception.BizException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 12:07
 */
@Service
public class AlgContext implements ApplicationContextAware {

	private List<AlgActuator> algActuators;

	public String executeAlg(AlgReqDO algReqDO) {
		for (AlgActuator algActuator : algActuators) {
			if (algActuator.apply(algReqDO)) {
				return algActuator.execute(algReqDO);
			}
		}
		throw new BizException(Constant.NO_SUCH_ALG_CODE,Constant.NO_SUCH_ALG_MESSAGE);
	}

	public String executeAlg(AlgReqDO algReqDO, String algName) {
		String originAlgName = algReqDO.getAlgName();
		algReqDO.setAlgName(algName);
		for (AlgActuator algActuator : algActuators) {
			if (algActuator.apply(algReqDO)) {
				algReqDO.setAlgName(originAlgName);
				return algActuator.execute(algReqDO);
			}
		}
		throw new BizException(Constant.NO_SUCH_ALG_CODE,Constant.NO_SUCH_ALG_MESSAGE);
	}

	/**
	 * 注入容器中所有算法处理器
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, AlgActuator> actuators = applicationContext.getBeansOfType(AlgActuator.class);
		algActuators= Lists.newArrayList(actuators.values());
	}
}
