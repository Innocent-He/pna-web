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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

/**
 * @Description 用户信息存储在ThreadLocal
 * @Author He
 * @Date 2022/1/17 22:09
 */
@Slf4j
public class AdminInfoInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
		try {
			if (!StpUtil.isLogin()) {
				ServletOutputStream outputStream = httpServletResponse.getOutputStream();
				httpServletResponse.setStatus(401);
				Response response = Response.error("401", "用户信息已过期");
				outputStream.write(JSON.toJSONBytes(response));
				outputStream.flush();
				outputStream.close();
				return false;
			}
			SaSession sessionByToken = StpUtil.getTokenSession();
			AdminInfo admin = (AdminInfo) sessionByToken.get("admin");
			AdminContext.USER_INFO.set(admin);
			return true;
		} catch (Exception e) {
			log.error("权限判断出错", e);
		}
		return false;
	}

	private void extendTimeout(HttpServletRequest httpServletRequest) {
		long tokenTimeout = StpUtil.getTokenTimeout();
		// saToken续签token时未延长cookie过期时间，因此在这里需要对cookie过期时间延长
		if (tokenTimeout < 60 * 60 * 24 * 15) {
			StpUtil.updateLastActivityToNow();
			Cookie[] cookies = httpServletRequest.getCookies();
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(cookie.getName(), "pna-token")) {
					cookie.setMaxAge(60 * 60 * 24 * 30);
					return;
				}
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		extendTimeout(httpServletRequest);
		// 防止内存泄漏
		AdminContext.USER_INFO.remove();
	}
}
