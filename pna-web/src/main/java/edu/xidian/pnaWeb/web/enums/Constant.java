package edu.xidian.pnaWeb.web.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author He
 * @Date 2021/10/24 18:18
 */
public class Constant {
	/**
	 * 算法名称
	 */
	public static final String REACH_GRAPH="reach";
	public static final String EVENT_CIRCLE="ew";



	/**
	 * 错误码
	 */
	public static final String LOGIN_USER="login_user";
	public static final String SYSTEM_ERROR_CODE="501";
	public static final String TIME_OUT_CODE="505";
	public static final String NO_METHOD_CODE="506";
	public static final String NET_GENERATE_CODE="507";
	public static final String ALG_RUN_CODE="508";
	public static final String USER_REPEAT_CODE="509";
	public static final String EMAIL_REPEAT_CODE="510";
	public static final String LOGIN_FAILED_CODE="511";
	public static final String NO_SUCH_ALG_CODE="512";


	/**
	 * 错误信息
	 */
	public static final String SYSTEM_ERROR_MESSAGE="服务器内部异常，请咨询管理员";
	public static final String TIME_OUT_MESSAGE="处理超时，请简化网络结构";
	public static final String NET_GENERATE_MESSAGE="生成超时，请减少参数重试";
	public static final String USER_REPEAT_MESSAGE="当前用户名已存在";
	public static final String EMAIL_REPEAT_MESSAGE="当前邮箱已存在";
	public static final String LOGIN_FAILED_MESSAGE="用户名或者密码错误，请检查后重试";
	public static final String NO_SUCH_ALG_MESSAGE="当前算法还没实现";



}
