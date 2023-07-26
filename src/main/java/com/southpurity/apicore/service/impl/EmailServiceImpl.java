package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendRestorePasswordEmail(UserDocument userDocument, String code) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject("Solicitud de cambio de contraseña");
            mimeMessageHelper.setFrom("omar.fdo.gomez@gmail.com");
            mimeMessageHelper.setTo(userDocument.getEmail());
            mimeMessageHelper.setText(getBody(code), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private String getBody(String code){
        return String.format("<html>" +
                "<head></head>" +
                "<body style='background-color: #FAFAFA;'>" +
                "    <p>Hola!,</p>" +
                "    <p>Hemos recibido una solicitud para acceder a tu cuenta en <strong>Pureza del Sur</strong>, a través de tu dirección de correo. Tu código de verificación es:</p>" +
                "    <p style='font-size: 1.8em;'>%s</p>" +
                "    <p>El código tiene una validez de 10 minutos.</p>" +
                "    <p>Si no has solicitado este código, puede que alguien esté intentado acceder a la cuenta de Pureza del Sur.<strong>No reenvíes este correo electrónico ni des el código a nadie.</strong></p>" +
                "    <p>Atentamente,</p>" +
                "    <p>El equipo de cuentas de Pureza del Sur ;)</p>" +
                "</body>" +
                "</html>", code);
    }

}
