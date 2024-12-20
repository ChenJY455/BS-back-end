package Server.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String toEmail, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("1678538142@qq.com", "springdoc");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(content, false);
			mailSender.send(message);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		mailSender.send(message);
	}
}
