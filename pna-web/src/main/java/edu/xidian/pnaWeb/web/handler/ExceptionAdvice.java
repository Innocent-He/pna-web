package edu.xidian.pnaWeb.web.handler;

import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.exception.BizException;
import edu.xidian.pnaWeb.web.exception.TimeOutException;
import edu.xidian.pnaWeb.web.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author He
 * @Date 2021/11/25 13:26
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionAdvice {

	@ExceptionHandler(value = TimeOutException.class)
	public Response timeOutHandler(TimeOutException e) {
		log.error(e.toString());
		return Response.error(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(value = BizException.class)
	public Response bizHandler(BizException e) {
		log.error(e.toString());
		return Response.error(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	public Response exceptionHandler(Exception e) {
		log.error(e.toString());
		return Response.error(Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR_MESSAGE);
	}
}
