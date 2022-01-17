package edu.xidian.pnaWeb.web.config;

import edu.xidian.pnaWeb.web.handler.RequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description 用于包装原生的servletRequest确保它可以被多次读取inputStream
 * @Author He
 * @Date 2022/1/17 22:42
 */
@Component
@WebFilter(urlPatterns = "/*",filterName = "enhanceRequest")
public class EnhanceReqFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if(servletRequest instanceof HttpServletRequest) {
			requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
		}
		if(requestWrapper == null) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			filterChain.doFilter(requestWrapper, servletResponse);
		}
	}

}
