package edu.xidian.pnaWeb.web.handler;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.web.model.AdminContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 用户信息存储在ThreadLocal
 * @Author He
 * @Date 2022/1/17 22:09
 */
@Slf4j
public class AdminInfoInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		try {
			//todo 前端配置axios，确保每次请求都携带用户信息
			RequestWrapper requestWrapper = (RequestWrapper) httpServletRequest;
			String body = requestWrapper.getBody();
			AdminContext parse = JSON.parseObject(body, AdminContext.class);
			AdminContext.USER_INFO.set(parse);
			return true;
		} catch (Exception e) {
			log.error("权限判断出错", e);
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		// 防止内存泄漏
		AdminContext.USER_INFO.remove();
	}
}
