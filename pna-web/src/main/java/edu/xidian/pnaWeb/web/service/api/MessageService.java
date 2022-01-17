package edu.xidian.pnaWeb.web.service.api;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 21:48
 */
public interface MessageService {
	public void senMail(String targetEmail, String subject,String content);
}
