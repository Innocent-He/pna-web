package edu.xidian.pnaWeb.web.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.xidian.pnaWeb.web.dao.po.AdminPO;
import edu.xidian.pnaWeb.web.model.AdminInfo;

/**
 * @Description
 * @Author He
 * @Date 2021/10/17 21:32
 */
public interface AdminService extends IService<AdminPO> {
	/**
	 * 登录服务
	 * @param adminInfo 用户登录信息
	 * @return 登陆是否成功
	 */
	AdminInfo login(AdminInfo adminInfo);

	/**
	 * 注册用户
	 * @param adminInfo
	 * @return
	 */
	void register(AdminInfo adminInfo);
}
