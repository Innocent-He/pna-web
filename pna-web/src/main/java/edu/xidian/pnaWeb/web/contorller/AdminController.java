package edu.xidian.pnaWeb.web.contorller;

import cn.dev33.satoken.stp.StpUtil;
import edu.xidian.pnaWeb.web.model.AdminContext;
import edu.xidian.pnaWeb.web.model.AdminInfo;
import edu.xidian.pnaWeb.web.model.Response;
import edu.xidian.pnaWeb.web.service.api.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author He
 * @Date 2021/10/17 15:56
 */
@RestController
public class AdminController {
	@Resource
	private AdminService adminService;
	/**
	 * 登录
	 */
	@PostMapping("/login")
	public Response<AdminInfo> login(@RequestBody AdminInfo info) {
		return Response.success(adminService.login(info));
	}

	@PostMapping("/register")
	public Response register(@RequestBody AdminInfo info) {
		adminService.register(info);
		return Response.success();
	}
	/**
	 * 退出
	 */
	@GetMapping("/logout")
	public Response logout() {
		StpUtil.logoutByTokenValue(StpUtil.getTokenValue());
		return Response.success();
	}

	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	@GetMapping("/userInfo")
	public Response<AdminInfo> userInfo() {
		return Response.success(AdminContext.USER_INFO.get());
	}
}

