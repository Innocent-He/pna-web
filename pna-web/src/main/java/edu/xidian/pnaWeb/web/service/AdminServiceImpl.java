package edu.xidian.pnaWeb.web.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import edu.xidian.pnaWeb.web.enums.Constant;
import edu.xidian.pnaWeb.web.dao.AdminMapper;
import edu.xidian.pnaWeb.web.dao.po.AdminPO;
import edu.xidian.pnaWeb.web.exception.BizException;
import edu.xidian.pnaWeb.web.model.AdminInfo;
import edu.xidian.pnaWeb.web.service.api.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author He
 * @Date 2021/10/17 21:32
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminPO> implements AdminService {
	@Resource
	private AdminMapper adminMapper;

	@Override
	public AdminInfo login(AdminInfo adminInfo) {
		log.info(adminInfo.toString());
		// 如果是临时用户,通过userName是否为ip格式判断
		if (checkCasualUser(adminInfo)) {
			// 在登录时会默认创建一个token 用将token放入cookie中
			StpUtil.login(adminInfo.getUserName());
			SaSession session = StpUtil.getTokenSession();
			session.set("admin",adminInfo);
			return adminInfo;
		}

		String encodePass = encodePass(adminInfo.getPassWord());
		AdminPO adminPO = adminMapper.selectOne(new QueryWrapper<AdminPO>()
				.eq("user_name", adminInfo.getUserName())
				.eq("pass_word", encodePass));
		if (Objects.isNull(adminPO)) {
			throw new BizException(Constant.LOGIN_FAILED_CODE,Constant.LOGIN_FAILED_MESSAGE);
		}

		AdminInfo admin = AdminInfo.builder()
				.userName(adminPO.getUserName())
				.email(adminPO.getEmail())
				.id(adminPO.getId())
				.build();
		StpUtil.login(admin.getId());
		SaSession session = StpUtil.getTokenSession();
		session.set("admin",admin);
		return admin;
	}

	/**
	 * 判断是否为临时用户
	 * @param adminInfo
	 * @return
	 */
	private boolean checkCasualUser(AdminInfo adminInfo) {
		String ipRegex="((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
		if (Pattern.matches(ipRegex,adminInfo.getUserName())) {
			return true;
		}
		return false;
	}

	private String encodePass(String originPass) {
		return Hashing.md5().newHasher().putString(originPass, Charsets.UTF_8).hash().toString();
	}

	private void checkValid(AdminInfo adminInfo) {
		AdminPO adminPO = adminMapper.selectOne(Wrappers.lambdaQuery(AdminPO.class)
				.eq(AdminPO::getUserName, adminInfo.getUserName())
				.or()
				.eq(AdminPO::getEmail, adminInfo.getEmail()));
		if (adminPO != null) {
			if (StringUtils.equals(adminInfo.getEmail(), adminPO.getEmail())) {
				throw new BizException(Constant.EMAIL_REPEAT_CODE, Constant.EMAIL_REPEAT_MESSAGE);
			}
			throw new BizException(Constant.USER_REPEAT_CODE, Constant.USER_REPEAT_MESSAGE);
		}
	}

	@Override
	public void register(AdminInfo adminInfo) {
		checkValid(adminInfo);
		AdminPO admin = AdminPO.builder().userName(adminInfo.getUserName())
				.email(adminInfo.getEmail())
				.passWord(encodePass(adminInfo.getPassWord()))
				.build();
		log.info(admin.toString());
		adminMapper.insert(admin);
	}

}
