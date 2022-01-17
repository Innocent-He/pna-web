package edu.xidian.pnaWeb.web.contorller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.exception.TimeOutException;
import edu.xidian.pnaWeb.web.model.*;
import edu.xidian.pnaWeb.web.service.api.AlgorithmService;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import edu.xidian.pnaWeb.web.transform.AlgTrans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 17:22
 */
@RestController
@Slf4j
public class ApiController {


	@Resource
	private AlgorithmService algorithmService;
	@Resource
	private TaskService taskService;

	@PostMapping("/algReq")
	public Response algorithmApi(@RequestBody AlgRequest algRequest) {
		taskService.submitTask(AlgTrans.transToDO(algRequest));
		return Response.success("算法提交成功！");
	}
	@GetMapping("/tasks/{ownerId}")
	public Response taskList(@PathVariable(value = "ownerId") String ownerId) {
		List<TaskDTO> taskDTOS=null;
		if (StringUtils.isBlank(ownerId)) {
			taskDTOS = taskService
					.list()
					.stream()
					.map((AlgTrans::transToDTO))
					.collect(Collectors.toList());
		} else {
			taskDTOS=taskService.list(new QueryWrapper<TaskPO>().eq("owner_id",ownerId))
					.stream()
					.map((AlgTrans::transToDTO))
					.collect(Collectors.toList());
		}
		return Response.success(taskDTOS);
	}

	@PostMapping("/generateNet")
	public Response generatePetriNet(@RequestBody String dataJson) {
		log.info(dataJson);
		Future<String> future = null;
		String result = null;
		try {
			GenerateRequest request = JSON.parseObject(dataJson, GenerateRequest.class);
			algorithmService.generatePetriNet(request);
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
