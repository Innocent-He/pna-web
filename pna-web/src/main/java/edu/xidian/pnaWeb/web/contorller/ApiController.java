package edu.xidian.pnaWeb.web.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.model.*;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import edu.xidian.pnaWeb.web.transform.AlgTrans;
import edu.xidian.pnaWeb.web.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
	private TaskService taskService;

	@PostMapping("/algReq")
	public Response algorithmApi(@RequestBody AlgRequest algRequest) {
		taskService.submitTask(AlgTrans.transToDO(algRequest));
		return Response.success("算法提交成功！");
	}

	@PostMapping("/tasks")
	public PageResponse<TaskDTO> taskList(@RequestBody TaskQueryReq queryReq) {
		return PageResponse.success(taskService.queryTask(queryReq));
	}

	@GetMapping("/delete/task/{taskId}")
	public Response deleteTask(@PathVariable(value = "taskId") Long taskId) {
		taskService.removeById(taskId);
		return Response.success();
	}

	@GetMapping("/cancel/{taskId}")
	public Response cancelTask(@PathVariable(value = "taskId") Long taskId) {
		taskService.cancelTask(taskId);
		return Response.success();
	}



	@PostMapping("/generateNet")
	public Response generatePetriNet(@RequestBody GenerateRequest request) {

		return null;
	}




}
