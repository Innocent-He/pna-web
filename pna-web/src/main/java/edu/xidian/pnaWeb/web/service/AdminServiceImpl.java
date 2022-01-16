package edu.xidian.pnaWeb.web.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import edu.xidian.pnaWeb.web.common.Constant;
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
		String encodePass = encodePass(adminInfo.getPassword());
		AdminPO adminPO = adminMapper.selectOne(new QueryWrapper<AdminPO>()
				.eq("user_name", adminInfo.getUsername())
				.eq("pass_word", encodePass));
		if (Objects.isNull(adminPO)) {
			throw new BizException(Constant.LOGIN_FAILED_CODE,Constant.LOGIN_FAILED_MESSAGE);
		}
		log.info(adminPO.toString());
		StpUtil.login(adminPO.getId());
		return AdminInfo.builder()
				.username(adminPO.getUserName())
				.email(adminPO.getEmail())
				.id(adminPO.getId()).build();
	}

	private String encodePass(String originPass) {
		return Hashing.md5().newHasher().putString(originPass, Charsets.UTF_8).hash().toString();
	}

	private void checkValid(AdminInfo adminInfo) {
		AdminPO adminPO = adminMapper.selectOne(Wrappers.lambdaQuery(AdminPO.class)
				.eq(AdminPO::getUserName, adminInfo.getUsername())
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
		AdminPO admin = AdminPO.builder().userName(adminInfo.getUsername())
				.email(adminInfo.getEmail())
				.passWord(encodePass(adminInfo.getPassword()))
				.build();
		log.info(admin.toString());
		adminMapper.insert(admin);
	}

}
