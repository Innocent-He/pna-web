package edu.xidian.pnaWeb.web.contorller;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.web.common.Constant;
import edu.xidian.pnaWeb.web.exception.TimeOutException;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.GenerateRequest;
import edu.xidian.pnaWeb.web.model.Invocation;
import edu.xidian.pnaWeb.web.model.Response;
import edu.xidian.pnaWeb.web.service.AlgorithmServiceImpl;
import edu.xidian.pnaWeb.web.service.api.AlgorithmService;
import edu.xidian.pnaWeb.web.service.api.PetriDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 17:22
 */
@RestController
@Slf4j
public class ApiController {
	@Resource
	private PetriDataService petriDataService;
	@Resource
	private AlgorithmService algorithmService;

	public static final ThreadPoolExecutor threadPoolExecutor;

	static {
		int nThreads = Runtime.getRuntime().availableProcessors();
		threadPoolExecutor = new ThreadPoolExecutor(nThreads, nThreads + 1, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(128));
	}

	@PostMapping("/getResult")
	public Response algorithmApi(@RequestBody String dataJson, HttpSession session) {
		log.info(dataJson);
		Object result = null;
		Future<Object> future = null;
		try {
			Invocation invocation = petriDataService.transformData(dataJson, session);
			Method declaredMethod = AlgorithmServiceImpl.class.getDeclaredMethod(invocation.getMethodName(), Map.class);
			future = threadPoolExecutor.submit(() -> {
				return declaredMethod.invoke(algorithmService, invocation.getParams());
			});
			result = future.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error(e.toString());
		} catch (ExecutionException e) {
			log.error(e.toString());
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new TimeOutException(Constant.TIME_OUT_CODE,Constant.TIME_OUT_MESSAGE);
		} catch (NoSuchMethodException e) {
			log.error(e.toString());
		} finally {
			AdminContext.USER_INFO.remove();
		}
		return Response.success(result.toString());
	}

	@PostMapping("/generateNet")
	public Response generatePetriNet(@RequestBody String dataJson) {
		log.info(dataJson);
		Future<String> future = null;
		String result = null;
		try {
			GenerateRequest request = JSON.parseObject(dataJson, GenerateRequest.class);
			future = threadPoolExecutor.submit(() -> {
				return algorithmService.generatePetriNet(request);
			});
			result = future.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error(e.toString());
		} catch (ExecutionException e) {
			log.error(e.toString());
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new TimeOutException(Constant.TIME_OUT_CODE,Constant.TIME_OUT_MESSAGE);
		} finally {
			AdminContext.USER_INFO.remove();
		}
		return Response.success(result);
	}
}
