package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description
 * @Author He
 * @Date 2021/10/5 17:36
 */
@Builder
@Data
public class PetriDO {
	private Long id;
	private String ownerName;
	private String desc;
	private String name;
	private int[][] preMatrix;
	private int[][] postMatrix;
	private int[] marking;
	private String webJson;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
