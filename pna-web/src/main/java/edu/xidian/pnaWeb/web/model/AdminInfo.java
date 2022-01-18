/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package edu.xidian.pnaWeb.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 用户登录信息
 * @Author He
 * @Date 2021/10/17 19:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfo implements Serializable {
    private Long id;
    private String userName;
    private String passWord;
    private String email;
}
