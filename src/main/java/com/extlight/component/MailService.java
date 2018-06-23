package com.extlight.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
@EnableConfigurationProperties(MailProperties.class)
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    protected TemplateEngine templateEngine;
    @Autowired
    private CommonMap commonMap;

    @Async
    public void sendEmail(String nickname,String email,String subject, String content) throws Exception {
        Context con = new Context();
        con.setVariable("content", content);
        String emailtext = templateEngine.process("portal/mail.html", con);

        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        InternetAddress from = new InternetAddress();
        from.setAddress(this.mailProperties.getUsername());
        from.setPersonal(commonMap.get("blogName").toString(), "UTF-8");
        helper.setFrom(from);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(emailtext, true);
        this.javaMailSender.send(message);
    }
}
