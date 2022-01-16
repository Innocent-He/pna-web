package edu.xidian.pnaWeb.web.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2021/10/17 21:40
 */
@Data
@Builder
@ToString
@TableName("p_admin")
public class AdminPO {
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;
	private String email;
	private String userName;
	private String passWord;
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}
