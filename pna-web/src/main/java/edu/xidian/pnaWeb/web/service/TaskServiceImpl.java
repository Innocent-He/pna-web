package edu.xidian.pnaWeb.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xidian.pnaWeb.web.dao.TaskMapper;
import edu.xidian.pnaWeb.web.dao.po.TaskPO;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 23:03
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper,TaskPO> implements IService<TaskPO> {
}
