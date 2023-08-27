package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SaleOrderRepository saleOrderRepository;

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

    @Override
    public void sendWelcomeEmail(UserDocument userDocument) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Bienvenido a Pureza del Sur");
        simpleMailMessage.setFrom("omar.fdo.gomez@gmail.com");
        simpleMailMessage.setTo(userDocument.getEmail());
        simpleMailMessage.setText("Bienvenido a Pureza del Sur");
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendPurchaseEmail(String saleOrderId) {
        var saleOrder = saleOrderRepository.findById(saleOrderId).orElseThrow();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject("Compra realizada");
            mimeMessageHelper.setFrom("omar.fdo.gomez@gmail.com");
            mimeMessageHelper.setTo(saleOrder.getClient().getEmail());
            mimeMessageHelper.setText(bodyPurchase(saleOrder), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @Override
    public void sendContactEmail(ContactRequest contactRequest) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Contacto");
        simpleMailMessage.setFrom("purezadelsur@gmail.com");
        simpleMailMessage.setTo("purezadelsur@gmail.com");
        simpleMailMessage.setText(contactRequest.getMessage());
        javaMailSender.send(simpleMailMessage);
    }

    private String bodyPurchase(SaleOrderDocument order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<thead><tr><th>Bidón</th><th>Clave</th></tr></thead>");
        order.getKeys().forEach(item -> sb.append("<tr><td>")
                .append(item.getKey())
                .append("</td><td>")
                .append(item.getValue())
                .append("</td></tr>"));
        sb.append("</table>");
        return String.format("<html>" +
                "<head></head>" +
                "<body style='background-color: #FAFAFA;'>" +
                "    <p>Hola %s,</p>" +
                "    <p>Tu compra se ha procesado con éxito!</p>" +
                "%s" +
                "    <p>Atentamente,</p>" +
                "    <p>Pureza del Sur</p>" +
                "</body>" +
                "</html>", order.getClient().getFullName(), sb);
    }

    private String getBody(String code) {
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
