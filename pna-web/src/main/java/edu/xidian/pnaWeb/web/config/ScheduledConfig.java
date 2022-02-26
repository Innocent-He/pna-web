package edu.xidian.pnaWeb.web.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import edu.xidian.pnaWeb.web.service.api.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author He
 * @Date 2022/2/25 20:20
 */
@Component
@Slf4j
public class ScheduledConfig {
	@Resource
	private TaskService taskService;

	/**
	 * 每个月定期删除一个月之前的任务
	 * corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
	 * 每个月第一天凌晨2点执行任务
	 */
	@Scheduled(cron = "0 0 2 1 * ?")
	public void removeValidTask() {
		Date expiration = DateUtils.addMonths(new Date(), -1);
		boolean deleteSuccess = taskService.remove(new QueryWrapper<TaskPO>().le("create_time", expiration));
		if (deleteSuccess) log.info("定期删除任务成功");
	}
}
