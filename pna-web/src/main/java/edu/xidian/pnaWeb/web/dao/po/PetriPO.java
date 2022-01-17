package edu.xidian.pnaWeb.web.dao.po;

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
	private Long id;
	private String ownerId;
	private String desc;
	private String name;
	private String postJson;
	private String preJson;
	private String webJson;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
