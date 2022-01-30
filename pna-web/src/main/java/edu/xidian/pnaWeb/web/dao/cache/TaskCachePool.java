package edu.xidian.pnaWeb.web.dao.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author He
 * @Date 2022/1/19 21:58
 */
public class TaskCachePool {
	static {
		Cache<Long, TaskPO> build = CacheBuilder.newBuilder()
				.concurrencyLevel(2)//并发添删改查的级别，等级越高创建的hash表越多
				.initialCapacity(16)//初始hash表槽位的总和
				.maximumSize(64)//缓冲池中元素最大数量
				.expireAfterWrite(1, TimeUnit.DAYS)
				.build();
	}
}
