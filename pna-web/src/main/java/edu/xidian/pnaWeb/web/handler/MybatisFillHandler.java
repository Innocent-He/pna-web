package edu.xidian.pnaWeb.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 18:59
 */
@Component
@Slf4j
public class MybatisFillHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		this.strictInsertFill(metaObject,
				"createTime",
				LocalDateTime.class,
				LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
		this.strictUpdateFill(metaObject,
				"updateTime",
				LocalDateTime.class,
				LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill ....");
		this.strictUpdateFill(metaObject,
				"updateTime",
				LocalDateTime.class,
				LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
	}
}
