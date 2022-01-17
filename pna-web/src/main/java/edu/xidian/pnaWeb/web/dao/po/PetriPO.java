package edu.xidian.pnaWeb.web.dao.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 13:25
 */
@Data
@Builder
public class PetriPO {
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;
	private String ownerName;
	private String desc;
	private String name;
	private String postJson;
	private String preJson;
	private String webJson;
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}
