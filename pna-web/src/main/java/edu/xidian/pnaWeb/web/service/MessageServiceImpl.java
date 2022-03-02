package edu.xidian.pnaWeb.web.service;

import edu.xidian.pnaWeb.web.service.api.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 21:49
 */
@Service
public class MessageServiceImpl implements MessageService {
	@Value("${spring.mail.username}")
	private String fromEmail;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void senMail(String targetEmail, String subject,String content) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromEmail);
		simpleMailMessage.setTo(targetEmail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		javaMailSender.send(simpleMailMessage);
	}
}
