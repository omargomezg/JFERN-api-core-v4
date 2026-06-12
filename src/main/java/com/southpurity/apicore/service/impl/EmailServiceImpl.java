package com.southpurity.apicore.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.southpurity.apicore.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.service.EmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private static final String ENCODING = "UTF-8";
    private static final String PUREZA_DEL_SUR = "Pureza del Sur";
    private static final String CONTACT_CC_EMAIL = "caysensur@gmail.com";
    private static final String DEFAULT_SIGNATURE = "Atentamente,\nPureza del Sur";

    private static final String TEMPLATE_RESTORE_PASSWORD = "restore-password-template.flth";
    private static final String TEMPLATE_PURCHASE = "purchase-template.flth";
    private static final String TEMPLATE_CONTACT = "contact-template.flth";
    private static final String TEMPLATE_WELCOME = "welcome-template.flth";
    private static final String TEMPLATE_TEST = "test-template.flth";
    private static final String COMPANY_NAME = "companyName";
    private static final String SIGNATURE = "signature";

    private static final int PASSWORD_RESET_VALIDITY_MINUTES = 10;

    private final JavaMailSender javaMailSender;
    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;
    private final Configuration freemarkerConfiguration;
    private final ConfigurationRepository configurationRepository;

    @Value("${spring.mail.username}")
    private String purezaDelSurGmail;

    @Override
    public void sendRestorePasswordEmail(@NonNull UserDocument userDocument, @NonNull String code) {
        Objects.requireNonNull(code, "Verification code cannot be null");

        EmailRequest request = EmailRequest.builder()
                .to(userDocument.getEmail())
                .subject("Solicitud de cambio de contraseña")
                .templateName(TEMPLATE_RESTORE_PASSWORD)
                .model(Map.of(
                        "code", code,
                        "validityMinutes", PASSWORD_RESET_VALIDITY_MINUTES,
                        "userName", getUserName(userDocument),
                        COMPANY_NAME, PUREZA_DEL_SUR,
                        SIGNATURE, DEFAULT_SIGNATURE
                ))
                .build();

        sendTemplatedEmail(request);
        log.info("Recovery password code was send to {} with code {}", userDocument.getEmail(), code);
    }

    @Override
    public void sendWelcomeEmail(@NonNull UserDocument userDocument) {
        Objects.requireNonNull(userDocument.getEmail(), "User email cannot be null");

        EmailRequest request = EmailRequest.builder()
                .to(userDocument.getEmail())
                .subject("Bienvenido a " + PUREZA_DEL_SUR)
                .templateName(TEMPLATE_WELCOME)
                .model(Map.of(
                        "userName", getUserName(userDocument),
                        COMPANY_NAME, PUREZA_DEL_SUR,
                        SIGNATURE, DEFAULT_SIGNATURE
                ))
                .build();

        sendTemplatedEmail(request);
        log.info("Welcome mail was send to {}", userDocument.getEmail());
    }

    @Override
    public void sendPurchaseEmail(@NonNull String saleOrderId) {
        var model = new HashMap<String, Object>();
        model.put(COMPANY_NAME, PUREZA_DEL_SUR);
        model.put(SIGNATURE, DEFAULT_SIGNATURE);
        var saleOrder = saleOrderRepository.findById(saleOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Sale order not found: " + saleOrderId));
        model.put("clientName", getUserName(saleOrder.getClient()));
        model.put("keys", saleOrder.getKeys());
        EmailRequest request = EmailRequest.builder()
                .to(saleOrder.getClient().getEmail())
                .subject("Gracias por tu compra")
                .templateName(TEMPLATE_PURCHASE)
                .model(model)
                .build();

        sendTemplatedEmail(request);
    }

    @Override
    public void sendContactEmail(ContactRequest contactRequest) {
        Objects.requireNonNull(contactRequest, "Contact request cannot be null");
        Objects.requireNonNull(contactRequest.getModel(), "Contact request model cannot be null");

        Map<String, Object> model = new HashMap<>(contactRequest.getModel());
        model.put(COMPANY_NAME, PUREZA_DEL_SUR);
        model.put(SIGNATURE, DEFAULT_SIGNATURE);

        EmailRequest request = EmailRequest.builder()
                .to(purezaDelSurGmail)
                .subject("Contacto web")
                .templateName(TEMPLATE_CONTACT)
                .model(model)
                .build();

        MimeMessage mimeMessage = createMimeMessageWithCC(
                request.getTo(),
                request.getSubject(),
                processTemplate(request.getTemplateName(), request.getModel()),
                CONTACT_CC_EMAIL
        );

        sendEmail(mimeMessage);
    }

    @Override
    public void sendTestEmail(String email) {
        Objects.requireNonNull(email, "Email address cannot be null");

        EmailRequest request = EmailRequest.builder()
                .to(email)
                .subject("Email de prueba")
                .templateName(TEMPLATE_TEST)
                .model(Map.of(
                        COMPANY_NAME, PUREZA_DEL_SUR,
                        SIGNATURE, DEFAULT_SIGNATURE
                ))
                .build();

        sendTemplatedEmail(request);
    }

    @Override
    public void sendPasswordResetByAdmin(@NonNull String id,@NonNull String password) {
        var date = LocalDate.now();
        var formatCL = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var model = new HashMap<String, Object>();
        model.put(COMPANY_NAME, PUREZA_DEL_SUR);
        model.put(SIGNATURE, DEFAULT_SIGNATURE);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        model.put("userName", user.getFullName());
        model.put("email", user.getEmail());
        model.put("newPassword", password);
        model.put("date", date.format(formatCL));
        EmailRequest request = EmailRequest.builder()
                .to(user.getEmail())
                .subject("Tu contraseña ha cambiado")
                .templateName("password-updated-by-admin.flth")
                .model(model)
                .build();

        sendTemplatedEmail(request);
    }

    private void sendTemplatedEmail(EmailRequest request) {
        MimeMessage mimeMessage = createMimeMessage(
                request.getTo(),
                request.getSubject(),
                processTemplate(request.getTemplateName(), request.getModel())
        );
        sendEmail(mimeMessage);
    }

    private MimeMessage createMimeMessage(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(purezaDelSurGmail);
            helper.setText(htmlContent, true);
            return mimeMessage;
        } catch (MessagingException e) {
            log.error("Error creating MIME message: to={}, subject={}", to, subject);
            throw new EmailServiceException("Failed to create email message", e);
        }
    }

    private MimeMessage createMimeMessageWithCC(String to, String subject, String htmlContent, String cc) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(purezaDelSurGmail);
            helper.setCc(cc);
            helper.setText(htmlContent, true);
            return mimeMessage;
        } catch (MessagingException e) {
            log.error("Error creating MIME message with CC: to={}, cc={}, subject={}", to, cc, subject);
            throw new EmailServiceException("Failed to create email message with CC", e);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            log.error("Template not found or not accessible: {}", templateName);
            throw new EmailServiceException("Email template not found", e);
        } catch (TemplateException e) {
            log.error("Error processing template {}: {}", templateName, e.getMessage());
            throw new EmailServiceException("Failed to process email template", e);
        }
    }

    private void sendEmail(MimeMessage mimeMessage) {
        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new EmailServiceException("Failed to send email", e);
        }
    }

    private String getUserName(UserDocument userDocument) {
        return StringUtils.hasText(userDocument.getFullName()) ?
                userDocument.getFullName() : "Nuevo Usuario";
    }
}
