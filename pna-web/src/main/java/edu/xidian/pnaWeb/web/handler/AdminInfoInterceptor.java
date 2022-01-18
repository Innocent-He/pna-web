package edu.xidian.pnaWeb.web.handler;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.AdminInfo;
import edu.xidian.pnaWeb.web.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

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
			if (!StpUtil.isLogin()) {
				ServletOutputStream outputStream = httpServletResponse.getOutputStream();
				Response response = Response.error("304", "用户未登录");
				outputStream.write(JSON.toJSONBytes(response));
				outputStream.flush();
				return false;
			}
			String token = StpUtil.getTokenValue();
			SaSession sessionByToken = StpUtil.getTokenSessionByToken(token);
			AdminInfo admin= (AdminInfo) sessionByToken.get("admin");
			AdminContext adminContext = AdminContext.builder()
					.userId(admin.getId())
					.userName(admin.getUserName())
					.email(admin.getEmail())
					.build();
			AdminContext.USER_INFO.set(adminContext);
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
