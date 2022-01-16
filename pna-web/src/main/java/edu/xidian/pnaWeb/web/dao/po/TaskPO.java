package edu.xidian.pnaWeb.web.dao.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author He
 * @Date 2022/1/16 22:49
 */
@Builder
@Data
@TableName("p_task")
public class TaskPO {
	/**
	 * 主键
	 */
	@TableId(value = "id")
	private Long id;
	/**
	 * 提交者用户id
	 */
	private Long ownerId;
	/**
	 * 算法名称
	 */
	private String algName;
	/**
	 * 状态 0排队中 1执行中 2执行成功 3执行失败
	 */
	private Integer status;
	private String result;
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime updateTime;
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime createTime;
}
